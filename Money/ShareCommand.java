package com.general_hello.commands.commands.Money;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Pro.ProData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.text.DecimalFormat;

public class ShareCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (!MoneyData.money.containsKey(ctx.getAuthor())) {
            ctx.getChannel().sendMessage("You have no money at all!!!\n" +
                    "You also think that you could share nothing to a fellow").queue();
            return;
        }

        if (MoneyData.money.get(ctx.getAuthor()) == 0) {
            ctx.getChannel().sendMessage("You have no money at all!!!\n" +
                    "You also think that you could share something to a fellow").queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage("You did not mention who to give at all!!!").queue();
            return;
        }

        if (Integer.parseInt(ctx.getArgs().get(0)) > 10_000_000) {
            ctx.getChannel().sendMessage("You can only share \uD83E\uDE99 10,000,000 at a time!!!").queue();
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
        }

        if (MoneyData.money.get(ctx.getMessage().getMentionedUsers().get(0)) < 500) {
            ctx.getChannel().sendMessage("That person has no money at all!!!\n" +
                    "You think that you could help that fellow\n" +
                    "He needs to rise by himself.\n" +
                    "You can give that fellow cash when he got \uD83E\uDE99 500").queue();
            return;
        }

        if (ctx.getArgs().get(0).isEmpty()) {
            ctx.getChannel().sendMessage("You didn't state how much am I to give at all!!!\n" +
                    "What am I supposed to give nothing?!?!").queue();
            return;
        }

        if (MoneyData.money.get(ctx.getAuthor()) < Integer.parseInt(ctx.getArgs().get(0))) {
            ctx.getChannel().sendMessage("You don't have enough money").queue();
            return;
        }

        if (Integer.parseInt(ctx.getArgs().get(0)) > 10_000_000) {
            EmbedBuilder em = new EmbedBuilder().setTitle("Error").setColor(Color.RED).setDescription("You can only share \uD83E\uDE99 10,000,000 at a time!!!");
            ctx.getChannel().sendMessage(em.build()).queue();
            return;
        }


        final Double moneyWallet = MoneyData.money.get(ctx.getAuthor());
        final User toBeGivenTo = ctx.getMessage().getMentionedUsers().get(0);
        final Double moneyOfReciever = MoneyData.money.get(toBeGivenTo);

        DecimalFormat formatter = new DecimalFormat("#,###.00");

        MoneyData.money.put(ctx.getAuthor(), moneyWallet - Integer.parseInt(ctx.getArgs().get(0)));
        MoneyData.money.put(toBeGivenTo, moneyOfReciever + Integer.parseInt(ctx.getArgs().get(0)));
        EmbedBuilder em = new EmbedBuilder().setTitle("Success!").setColor(Color.GREEN).setDescription("Successfully sent \uD83E\uDE99 " + formatter.format(Double.parseDouble(ctx.getArgs().get(0))) + " to " + toBeGivenTo.getAsMention());
        ctx.getChannel().sendMessage(em.build()).queue();
    }

    @Override
    public String getName() {
        return "share";
    }

    @Override
    public String getHelp(String prefix) {
        return "Share an amount of money to your friend\n" +
                "Usage: `" + prefix + "share [money] [mention]`";
    }
}
