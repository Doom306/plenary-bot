package com.general_hello.commands.commands.Games;

import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.Pro.ProData;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class GuessNumber implements Game {

    public boolean isEnded = false;
    private int number = 0;
    private int count = 8;
    private GuildMessageReceivedEvent e;

    public GuessNumber(GuildMessageReceivedEvent event)
    {
        e = event;

        startGame();
    }

    @Override
    public void startGame() {
        number = UtilNum.randomNum(0, 100);

        e.getChannel().sendMessage("1️⃣ Guess a number between 0 and 100! You have " + count  + " chances.").queue();
    }

    @Override
    public void endGame() {
        number = 0;
        count = 8;
        isEnded = true;
    }

    @Override
    public void sendInput(List<String> in, GuildMessageReceivedEvent event) {
        int innum = Integer.parseInt(in.get(0));

        if(isEnded)
        {
            e.getChannel().sendMessage("🛑 game haven't started yet!").queue();
            return;
        }

        count--;
        if(innum == number)
        {

            int rewardbonus = 0;
            if (ProData.isPro.get(e.getAuthor())) {
                rewardbonus += 100;
            }

            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " won! The number was " + number + ".\n" +
                    "\uD83E\uDE99 " + (200 + rewardbonus) + " was added to your account").queue();
            final Double aDouble = MoneyData.money.get(e.getAuthor());
            MoneyData.money.put(e.getAuthor(), aDouble + 200);
            endGame();
            return;
        }

        else if(count == 0)
        {
            e.getChannel().sendMessage("⏰ Time's up! The number was " + number + ".").queue();
            endGame();
            return;
        }

        else if(innum < number)
        {
            e.getChannel().sendMessage("Higher! ⬆" +
                    "\nYou got " + count + " chances left.").queue();
        }

        else if(innum > number)
        {
            e.getChannel().sendMessage("Lower! ⬇ " +
                    "\nYou got " + count + " chances left.").queue();
        }
    }

    public int getNumber() {
        return number;
    }
}
