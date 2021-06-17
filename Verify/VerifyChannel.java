package com.general_hello.commands.commands.Verify;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;

public class VerifyChannel implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (ctx.getMessage().getMentionedChannels().isEmpty()) {
            ctx.getChannel().sendMessage("You didn't state what the channel will be!").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            ctx.getChannel().sendMessage("You lack the `Manage Server` permission!").queue();
            return;
        }

        VerifyData.channel.put(ctx.getGuild(), ctx.getMessage().getMentionedChannels().get(0));
        ctx.getChannel().sendMessage("The channel that the users will be verified for this server has been successfully set to `" + ctx.getMessage().getMentionedChannels().get(0).getAsMention() + "`!").queue();
    }

    @Override
    public String getName() {
        return "verifychannel";
    }

    @Override
    public String getHelp(String prefix) {
        return "Set the channel for the users that will be verified!\n" +
                "Usage: `" + prefix + "verifychannel [#channel]`";
    }
}

