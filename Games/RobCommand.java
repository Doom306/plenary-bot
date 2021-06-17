package com.general_hello.commands.commands.Games;


import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.Pro.ProData;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RobCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (!MoneyData.money.containsKey(ctx.getAuthor())) {
            ctx.getChannel().sendMessage("You have no money at all!!!\n" +
                    "You also think that you could bank rob a fellow").queue();
            return;
        }

        if (MoneyData.money.get(ctx.getAuthor()).intValue() == 0) {
            ctx.getChannel().sendMessage("You have no money at all!!!\n" +
                    "You also think that you could bank rob a fellow").queue();
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

        if (!MoneyData.bank.containsKey(ctx.getMessage().getMentionedUsers().get(0))) {
            MoneyData.goal.put(ctx.getMessage().getMentionedUsers().get(0), 5000d);
            MoneyData.money.put(ctx.getMessage().getMentionedUsers().get(0), 500d);
            MoneyData.moneyGoalProgress.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
            MoneyData.bank.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
            MoneyData.robGoalProgress.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
            MoneyData.robGoal.put(ctx.getMessage().getMentionedUsers().get(0), 1000d);
            ProData.isPro.put(ctx.getMessage().getMentionedUsers().get(0), false);
            ctx.getChannel().sendMessage("Error 101!!! Please try again later!").queue();
            return;
        }

        if (MoneyData.money.get(ctx.getAuthor()).intValue() < 5000) {
            ctx.getChannel().sendMessage("You don't have 4000 in your wallet. SO GET SOME NOW!!!").queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage("You did not mention who to bank rob at all!!!").queue();
            return;
        }

        if (!MoneyData.bank.containsKey(ctx.getMessage().getMentionedUsers().get(0))) {
            ctx.getChannel().sendMessage("That person has no money at all!!!\n" +
                    "You think that you could bank rob that poor fellow").queue();
            return;
        }

        if (MoneyData.bank.get(ctx.getMessage().getMentionedUsers().get(0)).intValue() < 4000) {
            ctx.getChannel().sendMessage("That person has no money at all!!!\n" +
                    "You think that you could bank rob that poor fellow").queue();
            return;
        }

        if (!MoneyData.timeBankRob.containsKey(ctx.getAuthor())) {
            MoneyData.timeBankRob.put(ctx.getAuthor(), LocalDateTime.now());
            ctx.getChannel().sendMessage("Error 354!!! Please try again later").queue();
            return;
        }

        if (ProData.isPro.get(ctx.getAuthor())) {
            if (!LocalDateTime.now().minusSeconds(60).isAfter(MoneyData.timeBankRob.get(ctx.getAuthor()))) {
                LocalDateTime till = MoneyData.timeBankRob.get(ctx.getAuthor()).plusSeconds(60);
                LocalDateTime temp = LocalDateTime.now();
                long seconds = temp.until(till, ChronoUnit.SECONDS);
                ctx.getChannel().sendMessage(String.format("You need to wait %d second%s before you can rob someone else!", seconds, seconds == 1 ? "" : "s")).queue();
                return;
            }
        } else {
            if (!LocalDateTime.now().minusSeconds(300).isAfter(MoneyData.timeBankRob.get(ctx.getAuthor()))) {
                LocalDateTime till = MoneyData.timeBankRob.get(ctx.getAuthor()).plusSeconds(300);
                LocalDateTime temp = LocalDateTime.now();
                long seconds = temp.until(till, ChronoUnit.SECONDS);
                ctx.getChannel().sendMessage(String.format("You need to wait %d second%s before you can rob someone else! `Pro users only wait 60 seconds`", seconds, seconds == 1 ? "" : "s")).queue();
                return;
            }
        }

        final double random = Math.random();
        final User target = ctx.getMessage().getMentionedUsers().get(0);
        final Double targetMoney = MoneyData.bank.get(target);
        final Double robber = MoneyData.money.get(ctx.getAuthor());

        MoneyData.timeBankRob.put(ctx.getAuthor(), LocalDateTime.now());

        MoneyData.money.put(ctx.getAuthor(), robber - 4000);

        int multiplier = 1;

        if (targetMoney > 100_000) {
            multiplier = 10;
        }

        if (targetMoney > 1_000_000) {
            multiplier = 100;
        }

        if (random > 0.5) {
            int finalEarned;

            if (random * 100000 * multiplier >= targetMoney) {
                finalEarned = targetMoney.intValue();
                MoneyData.money.put(target, targetMoney - finalEarned);
            } else {
                finalEarned = (int) (random * 100000 * multiplier);
                MoneyData.money.put(target, targetMoney - finalEarned);
            }

            if (ProData.isPro.get(ctx.getAuthor())) {
                MoneyData.money.put(ctx.getAuthor(), robber + finalEarned + 3000);
                ctx.getChannel().sendMessage("You earned a 3000 bonus for being a PRO user.").queue();
            } else {
                MoneyData.money.put(ctx.getAuthor(), finalEarned + robber);
            }
            ctx.getChannel().sendMessage("You successfully robbed someone " + finalEarned).queue();
        } else {
            ctx.getChannel().sendMessage("You failed to  bank rob someone and lost 4000 more coins").queue();
            final Double theCash = MoneyData.money.get(ctx.getAuthor());
            MoneyData.money.put(ctx.getAuthor(), theCash - 4000);
        }
    }

    @Override
    public String getName() {
        return "bankrob";
    }

    @Override
    public String getHelp(String prefix) {
        return "Bank Robs a user\n" +
                "Entry Fee: 4000\n" +
                "Amount of money needed is 5000\n" +
                "1000 will be lost for a failed robbery\n" +
                "Usage: `" + prefix + "bankrob [mention]`";
    }
}
