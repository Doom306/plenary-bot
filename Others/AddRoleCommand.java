package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class AddRoleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();

        if (message.getMentionedMembers().isEmpty() || message.getMentionedRoles().isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);
        final Role mentionedRoles = message.getMentionedRoles().get(0);

        if (!member.canInteract(target) || !member.hasPermission(Permission.MANAGE_ROLES)) {
            channel.sendMessage("You or I are missing permissions to add roles to this member").queue();
            return;
        }

        ctx.getGuild()
                .addRoleToMember(target,mentionedRoles)
                .queue(
                        (__) -> channel.sendMessage("Roles was added successfully").queue(),
                        (error) -> channel.sendMessageFormat("Could not add roles to %s", error.getMessage()).queue()
                );
    }

    @Override
    public String getName() {
        return "role";
    }

    @Override
    public String getHelp(String prefix) {
        return "Add a role to a member in the server. \n" +
                "Usage: `" + prefix + "role <@user> <@role>`";
    }
}

