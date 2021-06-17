package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;

public class SetRaffleCount implements ICommand
{
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("No last id specified").queue();
            return;
        }

        if (!ctx.getAuthor().getId().equals(Config.get("owner_id"))) return;

        if (RaffleData.count.isEmpty()) {
            RaffleData.count.add(0);
        }

        final int setid = Integer.parseInt(ctx.getArgs().get(0));

        RaffleData.count.add(setid);
        ctx.getChannel().sendMessage("Last id set to " + setid).queue();
    }

    @Override
    public String getName() {
        return "rafflecount";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the last raffle id!!!\n" +
                "Usage: `" + prefix + "rafflecount`";
    }
}
