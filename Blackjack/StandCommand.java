package com.general_hello.commands.commands.Blackjack;


import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class StandCommand implements ICommand {
    @Override
    public void handle(CommandContext e) throws InterruptedException {
        final long guildID = e.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);

        if (e.getArgs().isEmpty()) {
            BlackjackGame bjg = GameHandler.getBlackJackGame(e.getAuthor().getIdLong());
            if (bjg != null) {
                try {
                    e.getMessage().delete().queueAfter(4, TimeUnit.SECONDS);
                } catch (Exception ea) {
                    e.getChannel().sendMessage("Error 210!!! Correcting error now.....").queue(
                            (message) -> message.editMessage("Error fixed!!!!").queueAfter(6, TimeUnit.SECONDS)
                    );
                }
                bjg.stand();
                e.getChannel().retrieveMessageById(bjg.getMessageId()).queue(m -> {
                    EmbedBuilder eb = bjg.buildEmbed(e.getAuthor().getName(), e.getGuild());
                    if (bjg.hasEnded()) {
                        int won_lose = bjg.getWonCreds();
                        final Double money = MoneyData.money.get(e.getAuthor());
                        MoneyData.money.put(e.getAuthor(), money + won_lose);
                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        eb.addField("Credits", String.format("You now have %s credits", formatter.format(((int) Math.round(MoneyData.money.get(e.getAuthor()))))), false);
                        GameHandler.removeBlackJackGame(e.getAuthor().getIdLong());
                    }
                    m.editMessage(eb.build()).queue();
                });
            } else e.getChannel().sendMessage("No game has been started! Type `" +prefix + "bj` to start one!").queue();
        }
    }

    @Override
    public String getName() {
        return "stand";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows if you win or lose and not to get a card anymore.\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
