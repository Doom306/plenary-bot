package com.general_hello.commands.commands.Money;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Pro.ProData;

import java.text.DecimalFormat;

public class BankCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (!MoneyData.money.containsKey(ctx.getAuthor())) {
            ctx.getChannel().sendMessage("You have no money at all!!!").queue();
            return;
        }

        if (!MoneyData.moneyGoalProgress.containsKey(ctx.getAuthor())) {
            MoneyData.moneyGoalProgress.put(ctx.getAuthor(), 0d);
        }
        if (MoneyData.money.get(ctx.getAuthor()).intValue() <= 0) {
            ctx.getChannel().sendMessage("You have no money at all!!!").queue();
            return;
        }

        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("You did not mention how much at all!!!").queue();
            return;
        }

        if (!ctx.getArgs().get(0).equals("all")) {
            if (Integer.parseInt(ctx.getArgs().get(0)) > 10_000_000) {
                ctx.getChannel().sendMessage("You can only deposit \uD83E\uDE99 10,000,000 at a time!!!").queue();
                return;
            }
        }

        Double money = MoneyData.bank.get(ctx.getAuthor());
        Double wallet = MoneyData.money.get(ctx.getAuthor());

        double lol;

        try {
            lol = Double.parseDouble(ctx.getArgs().get(0));
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Error! You didn't sent a number!").queue();
            return;
        }

        System.out.println(money);
        System.out.println(lol);
        System.out.println(money + lol);

        double sum = money + lol;

        MoneyData.bank.remove(ctx.getAuthor());
        MoneyData.bank.put(ctx.getAuthor(), sum);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " \uD83E\uDE99 " +formatter.format(lol) + " has been deposited").queue();
        MoneyData.money.put(ctx.getAuthor(), wallet - lol);
        Double progress = MoneyData.moneyGoalProgress.get(ctx.getAuthor());
        MoneyData.moneyGoalProgress.put(ctx.getAuthor(), progress + lol);


        while (true) {
            if (MoneyData.moneyGoalProgress.get(ctx.getAuthor()) >= MoneyData.goal.get(ctx.getAuthor())) {
                if (ProData.isPro.get(ctx.getAuthor())) {
                    final Double goal = MoneyData.goal.get(ctx.getAuthor());
                    MoneyData.bank.put(ctx.getAuthor(), 2000*(goal/1000) + sum);
                    ctx.getAuthor().openPrivateChannel().complete().sendMessage(ctx.getMember().getEffectiveName() + " " + "\uD83E\uDE99 " + (2000*(goal/1000)) +" coins has been deposited for reaching \uD83E\uDE99 " + formatter.format(goal) + " in your bank.").queue();
                    MoneyData.goal.put(ctx.getAuthor(), goal * 2);
                } else {
                    final Double goal = MoneyData.goal.get(ctx.getAuthor());
                    MoneyData.bank.put(ctx.getAuthor(), 1000*(goal/1000) + sum);
                    ctx.getAuthor().openPrivateChannel().complete().sendMessage(ctx.getMember().getEffectiveName() + " " + "\uD83E\uDE99 " + (1000*(goal/1000)) + " coins has been deposited for reaching \uD83E\uDE99 " + formatter.format(goal) + " in your bank.").queue();
                    MoneyData.goal.put(ctx.getAuthor(), goal * 2);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "dep";
    }

    @Override
    public String getHelp(String prefx) {
        return "Deposits money at your bank\n" +
                "Usage: `" + prefx + getName() + " [money]`";
    }
}
