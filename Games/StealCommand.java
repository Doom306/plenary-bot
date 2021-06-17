package com.general_hello.commands.commands.Games;


import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.Pro.ProData;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class StealCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (!MoneyData.money.containsKey(ctx.getAuthor())) {
            ctx.getChannel().sendMessage("You have no money at all!!!\n" +
                    "You also think that you could steal a fellow").queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().get(0).getUser().isBot()) {
            ctx.getChannel().sendMessage("Why are you robbing my own kind! Go home kid!").queue();
            return;
        }

        if (ctx.getMessage().getMentionedUsers().get(0).equals(ctx.getJDA().getUserById(Config.get("owner_id")))) {
            ctx.getChannel().sendMessage("Why are you robbing my owner? Go ***HOME***").queue();
            return;
        }

        if (!MoneyData.money.containsKey(ctx.getMessage().getMentionedUsers().get(0))) {
            MoneyData.goal.put(ctx.getMessage().getMentionedUsers().get(0), 5000d);
            MoneyData.money.put(ctx.getMessage().getMentionedUsers().get(0), 500d);
            MoneyData.moneyGoalProgress.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
            MoneyData.bank.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
            MoneyData.robGoalProgress.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
            MoneyData.robGoal.put(ctx.getMessage().getMentionedUsers().get(0), 1000d);
            ProData.isPro.put(ctx.getMessage().getMentionedUsers().get(0), false);
            ctx.getChannel().sendMessage("Error 102!!! Please try again later!!!").queue();
            return;
        }

        if (MoneyData.money.get(ctx.getAuthor()).intValue() == 0) {
            ctx.getChannel().sendMessage("You have no money at all!!!\n" +
                    "You also think that you could steal a fellow").queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage("You did not mention who to steal at all!!!").queue();
            return;
        }

        if (!MoneyData.money.containsKey(ctx.getMessage().getMentionedUsers().get(0))) {
            ctx.getChannel().sendMessage("That person has no money at all!!!\n" +
                    "You think that you could steal that poor fellow").queue();
            return;
        }

        if (MoneyData.money.get(ctx.getMessage().getMentionedUsers().get(0)).intValue() < 1000) {
            ctx.getChannel().sendMessage("That person has no money at all!!!\n" +
                    "You think that you could steal that poor fellow").queue();
            return;
        }

        if (!MoneyData.timeRob.containsKey(ctx.getAuthor())) {
            MoneyData.timeRob.put(ctx.getAuthor(), LocalDateTime.now());
            ctx.getChannel().sendMessage("Error 354!!! Please try again later").queue();
            return;
        }

        if (ProData.isPro.get(ctx.getAuthor())) {
            if (!LocalDateTime.now().minusSeconds(8).isAfter(MoneyData.timeRob.get(ctx.getAuthor()))) {
                LocalDateTime till = MoneyData.timeRob.get(ctx.getAuthor()).plusSeconds(8);
                LocalDateTime temp = LocalDateTime.now();
                long seconds = temp.until(till, ChronoUnit.SECONDS);
                ctx.getChannel().sendMessage(String.format("You need to wait %d second%s before you can rob someone else!", seconds, seconds == 1 ? "" : "s")).queue();
                return;
            }
        } else {
            if (!LocalDateTime.now().minusSeconds(30).isAfter(MoneyData.timeRob.get(ctx.getAuthor()))) {
                LocalDateTime till = MoneyData.timeRob.get(ctx.getAuthor()).plusSeconds(30);
                LocalDateTime temp = LocalDateTime.now();
                long seconds = temp.until(till, ChronoUnit.SECONDS);
                ctx.getChannel().sendMessage(String.format("You need to wait %d second%s before you can rob someone else! `Pro users only wait 8 seconds`", seconds, seconds == 1 ? "" : "s")).queue();
                return;
            }
        }

        final double random = Math.random();
        final User target = ctx.getMessage().getMentionedUsers().get(0);
        final Double targetMoney = MoneyData.money.get(target);
        final Double robber = MoneyData.money.get(ctx.getAuthor());
        Double robProgress = MoneyData.robGoalProgress.get(ctx.getAuthor());

        MoneyData.timeRob.put(ctx.getAuthor(), LocalDateTime.now());

        int multiplier = 1;

        if (targetMoney > 100_000) {
            multiplier = 10;
        }

        if (targetMoney > 1_000_000) {
            multiplier = 100;
        }

        if (random > 0.4) {
            int finalEarned;

            if (random * 10000 * multiplier >= targetMoney) {
                finalEarned = targetMoney.intValue();
                MoneyData.money.put(target, targetMoney - finalEarned);
            } else {
                finalEarned = (int) (random * 10000 * multiplier);
                MoneyData.money.put(target, targetMoney - finalEarned);
            }

            if (ProData.isPro.get(ctx.getAuthor())) {
                MoneyData.money.put(ctx.getAuthor(), robber + finalEarned + 200);
                MoneyData.robGoalProgress.put(ctx.getAuthor(), robProgress + finalEarned);
            } else {
                MoneyData.money.put(ctx.getAuthor(), finalEarned + robber);
                MoneyData.robGoalProgress.put(ctx.getAuthor(), robProgress + finalEarned);
            }

            ctx.getChannel().sendMessage("You successfully robbed someone " + finalEarned).queue();

            Double robGoal = MoneyData.robGoal.get(ctx.getAuthor());
            final Double money = MoneyData.money.get(ctx.getAuthor());

            while (true) {
                if (MoneyData.robGoal.get(ctx.getAuthor()) <= robProgress) {
                    if (ProData.isPro.get(ctx.getAuthor())) {
                        MoneyData.bank.put(ctx.getAuthor(), 2000 + money);
                        ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " " + "\uD83E\uDE99 2000 coins has been deposited for robbing someone \uD83E\uDE99 " + robGoal + ".").queue();
                    } else {
                        MoneyData.bank.put(ctx.getAuthor(), 1000 + money);
                        ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " " + "\uD83E\uDE99 1000 coins has been deposited for robbing someone \uD83E\uDE99 " + robGoal + "").queue();
                    }
                    MoneyData.robGoal.put(ctx.getAuthor(), MoneyData.robGoal.get(ctx.getAuthor()) * 2);
                    robGoal = MoneyData.robGoal.get(ctx.getAuthor());
                } else {
                    break;
                }
            }

        } else {
            ctx.getChannel().sendMessage("You failed to robbed someone and lost 1000 coins").queue();
            final Double thecash = MoneyData.money.get(ctx.getAuthor());
            MoneyData.money.put(ctx.getAuthor(), thecash - 1000);
        }
    }

    @Override
    public String getName() {
        return "rob";
    }

    @Override
    public String getHelp(String prefix) {
        return "Robs a user\n" +
                "Usage: `" + prefix + getName() + " [mention user]`";
    }
}
