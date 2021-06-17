package com.general_hello.commands.commands.Games;

import com.general_hello.commands.Listener;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.Pro.ProData;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HangMan implements Game{

    private static GuildMessageReceivedEvent e;
    private static EmbedBuilder embedstart = new EmbedBuilder();
    private static EmbedBuilder embedend = new EmbedBuilder();
    private static EmbedBuilder embedgame = new EmbedBuilder();
    public static User starter;

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private static String letter;
    private static ArrayList<String> word = new ArrayList<String>();
    private static ArrayList<String> guessed = new ArrayList<String>(); //All guesses
    private static ArrayList<String> missed = new ArrayList<String>(); //Wrong guesses
    private static ArrayList<String> right = new ArrayList<String>(); //Right guesses

    private static final int limit = 7;

    public static String hangman = "```_____________   \n"
            + "|           |   \n"
            + "|          " + "😲" + "  \n"
            + "|           |   \n"
            + "|          /|\\  \n"
            + "|          / \\  \n"
            + "|               \n"
            + "___             ```\n";

    public HangMan(GuildMessageReceivedEvent event)
    {
        e = event;

        starter = e.getAuthor();

        startGame();
    }

    @Override
    public void startGame()
    {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("hangman.txt"));

            String ranword;
            long random = UtilNum.randomNum(1, 58109);
            long count = 0;

            while((ranword = reader.readLine() ) != null)
            {
                count++;
                if(random == count) break;
            }
            reader.close();
            System.out.println(ranword);

            //Clear Last GAME's data
            clear();

            //Initialize
            for(int i = 0; i < ranword.length(); i ++)
            {
                word.add(ranword.substring(i,i+1));
                right.add("_");
            }
            ranword = "";

        } catch (IOException ioe) {
            LOGGER.info(this.getClass().getName(), "BufferReader at startGame()");
        }

        embedstart.setColor(Color.green);
        embedstart.addField("🎮 Hang Man: Game Started!",
                "Starter: " + starter.getAsMention()
                        + "\nWord length: " + word.size()
                        + "\n" + hangman, true);
        MessageEmbed me = embedstart.build();
        e.getChannel().sendMessage(me).queue();
        embedstart.clearFields();

        printRightLetter();
    }

    @Override
    public void endGame() { //End the GAME
        embedend.setColor(Color.green);
        embedend.setTitle( "🎮 Hang Man: Game Ended!", null);
        embedend.setFooter(e.getAuthor().getName() + " ended the game.", null);
        MessageEmbed me = embedend.build();
        e.getChannel().sendMessage(me).queue();
        embedend.clearFields();

        String aword = "";
        for(String w : word)
        {
            aword += w + " ";
        }
        e.getChannel().sendMessage("The word was : `" + aword +"`").queue();
        clear();
    }

    @Override
    public void sendInput(List<String> in, GuildMessageReceivedEvent event) { //Get input
        e = event;
        if(in.size() > 1 || in.get(0).length() != 1)
            e.getChannel().sendMessage("🛑 One letter at a time!").queue();

        else if(!Character.isLetter(in.get(0).charAt(0)))
            e.getChannel().sendMessage("🛑 Please enter a valid letter.").queue();

        else if(word.size() == 0)
            e.getChannel().sendMessage("🛑 game haven't started yet!").queue();

        else
        {
            letter = in.get(0);
            if(!guessed.contains(letter))
                guessed.add(letter);
            else
            {
                e.getChannel().sendMessage("🛑 This letter has been guessed before.").queue();
                return;
            }

            int countmiss = 0;
            for(int i = 0; i < word.size(); i ++)
            {
                if(letter.equals(word.get(i)))
                {
                    right.set(i, letter);
                }
                else
                {
                    countmiss++;
                }
            }

            if(countmiss == word.size())
            {
                missed.add(letter);

                if(missed.size() >= limit)
                {
                    endGame();
                    return;
                }
            }

            boolean end = checkWin();
            if(end == false) print();
        }
    }

    public boolean checkWin() //Check for winner
    {
        for(int i = 0; i < word.size(); i++)
        {
            if(!right.get(i).equals(word.get(i)))
                return false;
        }

        int rewardbonus = 0;
        if (ProData.isPro.get(e.getAuthor())) {
            rewardbonus += 400;
        }
        embedend.setColor(Color.green);
        embedend.setTitle("\uD83C\uDFAE Hang Man: Game finished!", null);
        embedend.addField("\uD83E\uDE99 " + (800 + rewardbonus) + " more if you're Pro was added to your account", null, false);
        embedend.setFooter(e.getAuthor().getName() + " is the winner!", null);
        MessageEmbed me = embedend.build();
        e.getChannel().sendMessage(me).queue();

        final Double aDouble = MoneyData.money.get(e.getAuthor());
        MoneyData.money.put(e.getAuthor(), aDouble + 800);
        embedend.clearFields();

        String aword = "";
        for(String w : word)
        {
            aword += w + " ";
        }
        e.getChannel().sendMessage("The word was : `" + aword +"`").queue();
        return true;
    }

    public void clear() //Clear all arraylist.
    {
        guessed.clear();
        missed.clear();
        word.clear();
        right.clear();
    }

    public void print() //Print out the result
    {
        String missedletter = "";

        for(String s : missed)
        {
            missedletter += s + ", ";
        }

        if("".equals(missedletter))
            missedletter = "Missed Letters: None";
        else
            missedletter = "Missed Letters: " + missedletter.substring(0,missedletter.length()-2).toUpperCase();

        embedgame.setColor(Color.green);
        embedgame.setTitle("\uD83C\uDFAE Current Man (Hanged!?)", null);
        embedgame.setDescription(missedletter + "\n" + printHangMan());
        embedgame.setFooter("Guessed by " + e.getAuthor().getName(), e.getAuthor().getAvatarUrl());

        MessageEmbed me = embedgame.build();
        e.getChannel().sendMessage(me).queue();
        embedgame.clearFields();

        printRightLetter();

    }

    public void printRightLetter()
    {
        String rightletter = "`";
        for(String s : right)
        {
            rightletter += s + " ";
        }
        rightletter += "`";
        e.getChannel().sendMessage(rightletter).queue();
    }

    public String printHangMan()
    {
        String hangman =  "";
        switch(missed.size())
        {
            case 0:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|               \n"
                        + "|               \n"
                        + "|                \n"
                        + "|                \n"
                        + "|                \n"
                        + "___              ```\n";
            case 1:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|          " + "😲" + "  \n"
                        + "|               \n"
                        + "|                \n"
                        + "|                \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            case 2:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|           " + "😲" + "  \n"
                        + "|           |   \n"
                        + "|                \n"
                        + "|                \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            case 3:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|           " + "😲" + "  \n"
                        + "|           |   \n"
                        + "|           |    \n"
                        + "|                \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            case 4:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|           " + "😲" + "  \n"
                        + "|           |  \n"
                        + "|          /|    \n"
                        + "|                \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            case 5:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|           " + "😲" + "  \n"
                        + "|           |   \n"
                        + "|          /|\\   \n"
                        + "|                \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            case 6:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|           " + "😲" + "  \n"
                        + "|           |   \n"
                        + "|          /|\\   \n"
                        + "|          /     \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            case 7:
                hangman =   "```_____________   \n"
                        + "|           |   \n"
                        + "|           " + "😲" + "  \n"
                        + "|           |   \n"
                        + "|          /|\\   \n"
                        + "|          / \\   \n"
                        + "|                \n"
                        + "___              ```\n";
                break;
            default:
                hangman = " ";
        }
        return hangman;
    }

}