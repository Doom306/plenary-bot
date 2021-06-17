package com.general_hello.commands.commands.Money;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Pro.ProData;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class BalanceCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (!MoneyData.money.containsKey(ctx.getAuthor())) {
            MoneyData.goal.put(ctx.getMessage().getAuthor(), 5000d);
            MoneyData.money.put(ctx.getMessage().getAuthor(), 500d);
            MoneyData.moneyGoalProgress.put(ctx.getMessage().getAuthor(), 0d);
            MoneyData.bank.put(ctx.getMessage().getAuthor(), 0d);
            MoneyData.robGoalProgress.put(ctx.getMessage().getAuthor(), 0d);
            MoneyData.robGoal.put(ctx.getMessage().getAuthor(), 1000d);
            ProData.isPro.put(ctx.getMessage().getAuthor(), false);
            MoneyData.timeRob.put(ctx.getAuthor(), LocalDateTime.now());
            MoneyData.timeBankRob.put(ctx.getAuthor(), LocalDateTime.now());
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {

            DecimalFormat formatter = new DecimalFormat("#,###.00");

            double amount = MoneyData.money.get(ctx.getAuthor());
            double amount1 = MoneyData.bank.get(ctx.getAuthor());

            EmbedBuilder embedBuilder;

            if (amount == 0) {
                embedBuilder = new EmbedBuilder().setTitle(ctx.getMember().getEffectiveName() + "'s Balance").setColor(Color.cyan).setDescription("**Wallet**: \uD83E\uDE99 0.00" + "\n" +
                        "**Bank:** \uD83E\uDE99 " + formatter.format(amount1)).setFooter("Nice balance you got there XD");
            } else if (amount1 == 0) {
                embedBuilder = new EmbedBuilder().setTitle(ctx.getMember().getEffectiveName() + "'s Balance").setColor(Color.cyan).setDescription("**Wallet**: \uD83E\uDE99 " + formatter.format(amount) + "\n" +
                        "**Bank:** \uD83E\uDE99 0.00").setFooter("Nice balance you got there XD");
            } else {
                embedBuilder = new EmbedBuilder().setTitle(ctx.getMember().getEffectiveName() + "'s Balance").setColor(Color.cyan).setDescription("**Wallet**: \uD83E\uDE99 " + formatter.format(amount) + "\n" +
                        "**Bank:** \uD83E\uDE99 " + formatter.format(amount1)).setFooter("Nice balance you got there XD");
            }

            ctx.getChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            if (!MoneyData.money.containsKey(ctx.getMessage().getMentionedUsers().get(0))) {
                MoneyData.goal.put(ctx.getMessage().getMentionedUsers().get(0), 5000d);
                MoneyData.money.put(ctx.getMessage().getMentionedUsers().get(0), 500d);
                MoneyData.moneyGoalProgress.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
                MoneyData.bank.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
                MoneyData.robGoalProgress.put(ctx.getMessage().getMentionedUsers().get(0), 0d);
                MoneyData.robGoal.put(ctx.getMessage().getMentionedUsers().get(0), 1000d);
                ProData.isPro.put(ctx.getMessage().getMentionedUsers().get(0), false);
                MoneyData.timeRob.put(ctx.getMessage().getMentionedUsers().get(0), LocalDateTime.now());
                MoneyData.timeBankRob.put(ctx.getMessage().getMentionedUsers().get(0), LocalDateTime.now());
            }
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            double amount = MoneyData.money.get(ctx.getMessage().getMentionedUsers().get(0));
            double amount1 = MoneyData.bank.get(ctx.getMessage().getMentionedUsers().get(0));

            EmbedBuilder embedBuilder;

            if (amount == 0) {
                embedBuilder = new EmbedBuilder().setTitle(ctx.getMessage().getMentionedMembers().get(0).getEffectiveName() + "'s Balance").setColor(Color.cyan).setDescription("**Wallet**: \uD83E\uDE99 0.00" + "\n" +
                        "**Bank:** \uD83E\uDE99 " + formatter.format(amount1)).setFooter("Nice balance you got there XD");
            } else if (amount1 == 0) {
                embedBuilder = new EmbedBuilder().setTitle(ctx.getMessage().getMentionedMembers().get(0).getEffectiveName() + "'s Balance").setColor(Color.cyan).setDescription("**Wallet**: \uD83E\uDE99 " + formatter.format(amount) + "\n" +
                        "**Bank:** \uD83E\uDE99 0.00").setFooter("Nice balance you got there XD");
            } else {
                embedBuilder = new EmbedBuilder().setTitle(ctx.getMessage().getMentionedMembers().get(0).getEffectiveName() + "'s Balance").setColor(Color.cyan).setDescription("**Wallet**: \uD83E\uDE99 " + formatter.format(amount) + "\n" +
                        "**Bank:** \uD83E\uDE99 " + formatter.format(amount1)).setFooter("Nice balance you got there XD");
            }
            ctx.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "bal";
    }

    @Override
    public String getHelp(String p) {
        return "Checks the wallet balance\n" +
                "Usage: `" + p + getName() + " [@mention]`";
    }
}
