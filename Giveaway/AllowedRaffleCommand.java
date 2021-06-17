package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;

public class AllowedRaffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage("No users specified!!!").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            ctx.getChannel().sendMessage("You don't have Kick Member Permissions").queue();
            return;
        }

        if (!RaffleData.allowed.containsKey(ctx.getMessage().getMentionedMembers().get(0))) RaffleData.allowed.put(ctx.getMessage().getMentionedMembers().get(0), false);

        if (RaffleData.allowed.get(ctx.getMessage().getMentionedMembers().get(0))) {
            ctx.getChannel().sendMessage("User already allowed").queue();
            return;
        }

        RaffleData.allowed.put(ctx.getMessage().getMentionedMembers().get(0), true);
        ctx.getChannel().sendMessage(ctx.getMessage().getMentionedUsers().get(0).getAsTag() + " has been allowed to join the raffle by " + ctx.getMember().getAsMention()).queue();
    }

    @Override
    public String getName() {
        return "allow";
    }

    @Override
    public String getHelp(String prefix) {
        return "Allows the user to join the raffle.\n" +
                "Usage: `" + prefix + "allow [mention]";
    }
}
