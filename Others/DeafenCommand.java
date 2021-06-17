package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeafenCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();

        if (message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing mentioned member").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        if (!member.canInteract(target) || !member.hasPermission(Permission.VOICE_DEAF_OTHERS)) {
            channel.sendMessage("You are missing permissions to deafen this member").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.VOICE_DEAF_OTHERS)) {
            channel.sendMessage("I am missing permissions to mute that member").queue();
            return;
        }

        ctx.getGuild()
                .deafen(target, true)
                .queue(
                        (__) -> channel.sendMessage("Deafen was successful").queue(),
                        (error) -> channel.sendMessageFormat("Could not deafen %s", error.getMessage()).queue()
                );
    }

    @Override
    public String getName() {
        return "deafen";
    }

    @Override
    public String getHelp(String prefix) {
        return "Deafen a member in a voice channel at the server. \n" +
                "Usage: `" + prefix + "deafen <@user>`";
    }
}
