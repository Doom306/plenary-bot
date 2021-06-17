package com.general_hello.commands.commands.Blackjack;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Money.MoneyData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.text.DecimalFormat;

public class BlackjackCommand implements ICommand {
    private GameHandler gameHandler;

    public BlackjackCommand(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }


    @Override
    public void handle(CommandContext e) throws InterruptedException {
        User author = e.getAuthor();
        long playerId = author.getIdLong();
        int bet = e.getArgs().size() == 0 ? 0 : getInt(e.getArgs().get(0));
        if (e.getArgs().isEmpty()) {
            e.getChannel().sendMessage("Are you gonna bet on nothing!!!!").queue();
            return;
        }
        if (e.getArgs().size() != 0 && e.getArgs().get(0).matches("(?i)all(-?in)?")) {
            bet = (int) Math.round(MoneyData.money.get(author));
        }
        if (bet >= 10) {
            if (MoneyData.money.get(author) - bet >= 0) {
                BlackjackGame objg = GameHandler.getBlackJackGame(playerId);
                if (objg == null) {
                    BlackjackGame bjg = new BlackjackGame(bet);
                    EmbedBuilder eb = bjg.buildEmbed(author.getName(), e.getGuild());
                    if (!bjg.hasEnded()) {
                        GameHandler.putBlackJackGame(playerId, bjg);
                    } else {
                        Double d = (double) bjg.getWonCreds();
                        final Double money = MoneyData.money.get(author);
                        MoneyData.money.put(author, money + d);
                        int credits = (int) Math.round(MoneyData.money.get(author));
                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        eb.addField("Credits", String.format("You now have %s credits", formatter.format(credits)), false);
                    }
                    e.getChannel().sendMessage(eb.build()).queue(m -> {
                        if (!bjg.hasEnded()) bjg.setMessageId(m.getIdLong());
                    });
                } else {
                    e.getChannel().sendMessage("You're already playing a game").queue();
                }
            } else {
                e.getChannel().sendMessage(String.format("You don't have enough credits to make a %d credits bet", bet)).queue();
            }
        } else {
            e.getChannel().sendMessage("You need to place a bet for at least 10 credits").queue();
        }
    }

    @Override
    public String getName() {
        return "bj";
    }

    @Override
    public String getHelp(String p) {
        return "Start a blackjack game!\n" +
                "Usage: `" + p + "bj [amount of money]`";
    }

    public static int getInt(String s) {
        if (s.matches("(?i)[0-9]*k?m?")) {
            s = s.replaceAll("m", "000000");
            s = s.replaceAll("k", "000");
        }
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            i = -1;
        }
        return i;
    }
}
