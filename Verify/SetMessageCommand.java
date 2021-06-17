package com.general_hello.commands.commands.Verify;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;

public class SetMessageCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("You didn't state what the message will be!").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            ctx.getChannel().sendMessage("You lack the `Manage Server` permission!").queue();
            return;
        }

        String message = ctx.getArgs().toString();
        message = message.replaceAll("\\[","");
        message = message.replaceAll("]","");
        message = message.replaceAll(",","");
        VerifyData.message.put(ctx.getGuild(), message);
        ctx.getChannel().sendMessage("The verify message for this server has been successfully set to `" + message + "`!").queue();
    }

    @Override
    public String getName() {
        return "verifymessage";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the verify message.\n" +
                "Usage: `" + prefix + "verifymessage [message]`\n" +
                "Possible variables: \n" +
                "To mention a user: {mention} " +
                "To send the server name: {servername}";
    }
}
