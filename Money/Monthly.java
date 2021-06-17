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

public class Monthly implements ICommand {
    @Override
    public void handle(CommandContext e) throws InterruptedException {
        final List<String> args = e.getArgs();
        final long guildID = e.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);

        if (!MoneyData.timeMonthly.containsKey(e.getAuthor())) MoneyData.timeMonthly.put(e.getAuthor(), LocalDateTime.now());
        if (args.size() == 0) {
            LocalDateTime latestcollect = MoneyData.timeMonthly.get(e.getAuthor());
            if (latestcollect == null || LocalDateTime.now().minusDays(1).isAfter(latestcollect)) {
                final Double money = MoneyData.money.get(e.getAuthor());
                int cash = 200_000;
                if (ProData.isPro.get(e.getAuthor())) {
                    cash = cash * 2;
                }
                MoneyData.money.put(e.getAuthor(), money + cash);
                int creds = (int) Math.round(MoneyData.money.get(e.getAuthor()));
                MoneyData.timeMonthly.put(e.getAuthor(), LocalDateTime.now());
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                e.getChannel().sendMessage(String.format("You collected your monthly **" + formatter.format(cash) + " credits** \nYour new balance is now **%s credits**", formatter.format(creds))).queue();
            } else {
                LocalDateTime till = latestcollect.plusMonths(1);
                LocalDateTime temp = LocalDateTime.now();
                long hours = temp.until(till, ChronoUnit.HOURS);
                e.getChannel().sendMessage(String.format("You need to wait %d hour%s before you can collect your next credits", hours, hours == 1 ? "" : "s")).queue();
            }

        } else {
            e.getChannel().sendMessage(this.getHelp(prefix)).queue();
        }
    }

    @Override
    public String getName() {
        return "monthly";
    }

    @Override
    public String getHelp(String p) {
        return "Claims ye monthly credits!!!\n" +
                "Usage: `" + p + getName() + "`";
    }
}
