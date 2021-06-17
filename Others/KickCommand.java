package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("You are missing permissions to kick this member").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I am missing permissions to kick that member").queue();
            return;
        }

        final String reason = String.join(" ", args.subList(1, args.size()));

        try {
            ctx.getGuild().kick(target, reason).reason(reason).queue();
            target.getUser().openPrivateChannel().complete().sendMessage("You were kicked from **" + ctx.getGuild().getName() + "** with the reason of ***" + reason + "***").queue();
            channel.sendMessage("Kick was successful").queue();
        } catch (Exception e) {
            channel.sendMessageFormat("Could not kick %s", target.getAsMention()).queue();
        }
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getHelp(String prefix) {
        return "Kick a member off the server. \n" +
                "Usage: `" + prefix + "kick <@user> <reason>`";
    }
}
