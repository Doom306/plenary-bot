package com.general_hello.commands.commands.Money;


import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import com.general_hello.commands.commands.Pro.ProData;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HourlyCommand implements ICommand {
    @Override
    public void handle(CommandContext e) throws InterruptedException {
        final List<String> args = e.getArgs();
        final long guildID = e.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);

        if (!MoneyData.time.containsKey(e.getAuthor())) MoneyData.time.put(e.getAuthor(), LocalDateTime.now());
        if (args.size() == 0) {
            LocalDateTime latestcollect = MoneyData.time.get(e.getAuthor());
            if (latestcollect == null || LocalDateTime.now().minusMinutes(60).isAfter(latestcollect)) {
                final Double money = MoneyData.money.get(e.getAuthor());
                int cash = 1000;
                if (ProData.isPro.get(e.getAuthor())) {
                    cash = cash * 2;
                }
                MoneyData.money.put(e.getAuthor(), money + cash);
                int creds = (int) Math.round(MoneyData.money.get(e.getAuthor()));
                MoneyData.time.put(e.getAuthor(), LocalDateTime.now());
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                e.getChannel().sendMessage(String.format("You collected your hourly **" + formatter.format(cash) + " credits** \nYour new balance is now **%s credits**", formatter.format(creds))).queue();
            } else {
                LocalDateTime till = latestcollect.plusHours(1);
                LocalDateTime temp = LocalDateTime.now();
                long minutes = temp.until(till, ChronoUnit.MINUTES);
                e.getChannel().sendMessage(String.format("You need to wait %d minute%s before you can collect your next credits", minutes, minutes == 1 ? "" : "s")).queue();
            }

        } else {
            e.getChannel().sendMessage(this.getHelp(prefix)).queue();
        }
    }

    @Override
    public String getName() {
        return "hourly";
    }

    @Override
    public String getHelp(String p) {
        return "Claims ye hourly credits!!!\n" +
                "Usage: `" + p + getName() + "`";
    }
}
