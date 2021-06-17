package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.util.List;

public class LockChannelCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        TextChannel channel = ctx.getChannel();
        TextChannel guildChannelById = ctx.getGuild().getTextChannelById("839684246661627914");
        TextChannel guildChannelById1 = ctx.getGuild().getTextChannelById("798697170604654623");

        List<Role> roles = channel.getGuild().getRoles();

        int x = 0;

        while (x < roles.size()) {
            if (!roles.get(x).hasPermission(Permission.KICK_MEMBERS) && !ctx.getSelfMember().getRoles().contains(roles.get(x))) {
                try {
                    channel.getManager().clearOverridesAdded().getChannel().putPermissionOverride(roles.get(x)).setDeny(Permission.MESSAGE_WRITE).complete();
                } catch (Exception ignored) {}
            }

            System.out.println("ook");
            x++;
        }

        try {
            guildChannelById.sendMessage(ctx.getMember().getAsMention() + " locked " + channel.getAsMention()).queue();
        } catch (Exception ignored) {}

        try {
            guildChannelById1.sendMessage(ctx.getMember().getAsMention() + " locked " + channel.getAsMention()).queue();
        } catch (Exception ignored) {}

        ctx.getChannel().sendMessage("Success in locking the channel").complete();
    }

    @Override
    public String getName() {
        return "lock";
    }

    @Override
    public String getHelp(String prefix) {
        return "Locks the channel\n" +
                "`" + prefix + "lock`";
    }
}
