package com.general_hello.commands.commands.Verify;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;

public class SetRoleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (ctx.getMessage().getMentionedRoles().isEmpty()) {
            ctx.getChannel().sendMessage("You didn't state what the role will be!").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            ctx.getChannel().sendMessage("You lack the `Manage Server` permission!").queue();
            return;
        }

        VerifyData.guilds.add(ctx.getGuild());

        VerifyData.role.put(ctx.getGuild(), ctx.getMessage().getMentionedRoles().get(0));
        ctx.getChannel().sendMessage("The role that the users will be given to for this server has been successfully set to `" + ctx.getMessage().getMentionedRoles().get(0).getName() + "`!").queue();
    }

    @Override
    public String getName() {
        return "verifyrole";
    }

    @Override
    public String getHelp(String prefix) {
        return "Set the role to be given to the users that will be verified!\n" +
                "Usage: `" + prefix + "verifyrole [@role]`";
    }
}
