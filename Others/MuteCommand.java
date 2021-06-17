package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MuteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();


        if (args.size() < 3 || message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("You are missing permissions to mute this member").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I am missing permissions to mute that member").queue();
            return;
        }

         String reason = String.join(" ", args.subList(1, args.size()));
        reason = reason.replace(ctx.getArgs().get(1), "");
        int length = Integer.parseInt(ctx.getArgs().get(1));
        Role roleById;
        try {
            roleById = ctx.getGuild().getRolesByName("muted", true).get(0);
            ctx.getGuild().addRoleToMember(target, roleById).queue();
        } catch (Exception e) {
            roleById = ctx.getGuild().getRolesByName("mute", true).get(0);
            ctx.getGuild().addRoleToMember(target, roleById).queue();
        }

        ctx.getGuild().removeRoleFromMember(target, roleById).queueAfter(length, TimeUnit.MINUTES);

        ctx.getChannel().sendMessage(target.getAsMention() + " was muted successfully!\n" +
                "Reason: " + reason + "\n" +
                "Length: " + length + " minutes.").queue();
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getHelp(String prefix) {
        return "Mutes the user!\n" +
                "`" + prefix + "mute [@user] [length in minutes] [reason]`";
    }
}
