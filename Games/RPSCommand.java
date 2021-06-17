package com.general_hello.commands.commands.Games;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.Utils.UtilNum;

public class RPSCommand implements ICommand {

    private String emoji2 = "";

    @Override
    public void handle(CommandContext ctx) {
        if(ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("Usage: `/rps`").queue();
            return;
        }

        if(!ctx.getArgs().isEmpty()) {
            String hand = "", emoji = "";
            String hand2 = getHand();
            if("rock".equals(ctx.getArgs().get(0)) || "rocks".equals(ctx.getArgs().get(0)) || "r".equals(ctx.getArgs().get(0)) || "stone".equals(ctx.getArgs().get(0)))
            {
                emoji = "✊🏻";
                hand = "rock";
            }
            else if("paper".equals(ctx.getArgs().get(0)) || "papers".equals(ctx.getArgs().get(0)) || "p".equals(ctx.getArgs().get(0)))
            {
                emoji = "✋🏻";
                hand = "paper";
            }
            else if("scissor".equals(ctx.getArgs().get(0)) || "scissors".equals(ctx.getArgs().get(0)) || "s".equals(ctx.getArgs().get(0)))
            {
                emoji = "✌🏻";
                hand = "scissors";
            }
            else
            {
                ctx.getChannel().sendMessage("⛔ Please enter a valid choice.").queue();
                return;
            }

            String output = compare(hand, hand2, ctx);

            ctx.getChannel().sendMessage(output + "\n You: " + emoji + " Me: " + emoji2).queue();
        }
    }


    public String getHand()
    {
        String hand = "";
        int choice = UtilNum.randomNum(1, 3);
        switch(choice)
        {
            case 1: hand = "rock";
                emoji2 = "✊🏻";
                break;
            case 2: hand = "paper";
                emoji2 = "🤚🏻";
                break;
            case 3: hand = "scissors";
                emoji2 = "✌";
                break;
            default: hand = "no hand";
                break;
        }
        return hand;
    }

    public String compare(String hand, String hand2, CommandContext ctx)
    {
        String result = "";
        if(hand.equals(hand2))
            result = "🏁 It's a tie!";
        else if(hand.equals("rock"))
        {
            if(hand2.equals("paper"))
                result = "I won!";
            if(hand2.equals("scissors")) {
                result = "You won! \uD83E\uDE99 200 was added to your account";
                final Double aDouble = MoneyData.money.get(ctx.getAuthor());
                MoneyData.money.put(ctx.getAuthor(), aDouble + 200);
            }
        }
        else if(hand.equals("paper"))
        {
            if(hand2.equals("scissors"))
                result = "I won!";
            if(hand2.equals("rock")) {
                result = "You won! \uD83E\uDE99 200 was added to your account";
                final Double aDouble = MoneyData.money.get(ctx.getAuthor());
                MoneyData.money.put(ctx.getAuthor(), aDouble + 200);
            }
        }
        else if(hand.equals("scissors"))
        {
            if(hand2.equals("rock"))
                result = "I won!";
            if(hand2.equals("paper")) {
                result = "You won! \uD83E\uDE99 200 was added to your account";
                final Double aDouble = MoneyData.money.get(ctx.getAuthor());
                MoneyData.money.put(ctx.getAuthor(), aDouble + 200);
            }
        }

        return result;
    }

    @Override
    public String getName() {
        return "rps";
    }

    @Override
    public String getHelp(String prefix) {
        return "Plays rock paper scissors\n" +
                "Usage: `" +prefix + "rps`\n" +
                "Parameter: `rock | paper | scissors | null`";
    }
}
