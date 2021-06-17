package com.general_hello.commands.commands.Money;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;

import java.io.IOException;


public class CodeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        String id = ctx.getAuthor().getId();

        if (ctx.getArgs().get(0).equalsIgnoreCase("PL-" + id + "-" + Config.get("code1"))) {
            Double money = MoneyData.money.get(ctx.getAuthor());
            System.out.println(money);
            MoneyData.money.put(ctx.getAuthor(), money + 100_000);
            ctx.getChannel().sendMessage("Successfully claimed the code entered!\n" +
                    "You got 100,000 credits!").queue();
            return;
        }

        if (ctx.getArgs().get(0).equalsIgnoreCase("PL-" + id + "-" + Config.get("code2"))) {
            Double money = MoneyData.money.get(ctx.getAuthor());
            MoneyData.money.put(ctx.getAuthor(), money + 10_000);
            ctx.getChannel().sendMessage("Successfully claimed the code entered!\n" +
                    "You got 10,000 credits!").queue();
            return;
        }

        if (ctx.getArgs().get(0).equalsIgnoreCase("PL-" + id + "-" + Config.get("code3"))) {
            Double money = MoneyData.money.get(ctx.getAuthor());
            MoneyData.money.put(ctx.getAuthor(), money + 1_000_000);
            ctx.getChannel().sendMessage("Successfully claimed the code entered!\n" +
                    "You got 1,000,000 credits!").queue();
            return;
        }

        ctx.getChannel().sendMessage("Invalid code placed!").queue();
    }

    @Override
    public String getName() {
        return "code";
    }

    @Override
    public String getHelp(String prefix) {
        return "Redeeming money/items with codes given by the creators of Plenary bot!\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
