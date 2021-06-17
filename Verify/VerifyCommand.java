package com.general_hello.commands.commands.Verify;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class VerifyCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        ctx.getMessage().delete().queueAfter(4, TimeUnit.SECONDS);
        TextChannel channel;

        if (!VerifyData.role.containsKey(ctx.getGuild())) {
            ctx.getChannel().sendMessage("Kindly ask the admins to set the role to be given for the users to be verified!").queue();
            return;
        }

        if (!VerifyData.channel.containsKey(ctx.getGuild())) {
            ctx.getChannel().sendMessage("Kindly ask the admins add the channel where the user could use the verify command!").queue();
            return;
        }

        channel = VerifyData.channel.get(ctx.getGuild());

        if (!ctx.getChannel().equals(channel)) {
            return;
        }

        Role role = VerifyData.role.get(ctx.getGuild());

        ctx.getGuild().addRoleToMember(ctx.getMember(), role).queueAfter(10, TimeUnit.SECONDS);

        if (VerifyData.message.containsKey(ctx.getGuild())) {
            String message = VerifyData.message.get(ctx.getGuild());
            message = message.replaceAll("\\{mention}", ctx.getMember().getAsMention());
            message = message.replaceAll("\\{servername}", ctx.getGuild().getName());
            ctx.getChannel().sendMessage(message).complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }

        VerifyData.message.put(ctx.getGuild(), ctx.getMember().getAsMention() + ". You are successfully verified!\n\nYou are now officially a member of \n" + ctx.getGuild().getName());

        if (ctx.getGuild().equals(ctx.getJDA().getGuildById(798428155907801089L))) {
            ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + ". You are successfully verified!\n" +
                    "Enjoy the server!").complete().delete().queueAfter(7, TimeUnit.SECONDS);
        } else {
            ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + ". You are successfully verified!\n" +
                    "You are now officially a member of " + ctx.getGuild().getName()).complete().delete().queueAfter(7, TimeUnit.SECONDS);
        }
    }

    @Override
    public String getName() {
        return "verify";
    }

    @Override
    public String getHelp(String prefix) {
        return "Verifies the user\n" +
                "Usage: `" + prefix + "verify`";
    }
}
