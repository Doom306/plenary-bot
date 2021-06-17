package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.Objects;

public class ServerListCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();
        List<Guild> list = jda.getGuilds();
        int i = 0;


        while (i < (list.size())) {
            ctx.getChannel().sendMessage("This bot is in `" + list.get(i).getName() + "` Main Channel is `" + Objects.requireNonNull(list.get(i).getDefaultChannel()).getName() + "`").queue();
            i++;
        }
    }

    @Override
    public String getName() {
        return "serverlist";
    }

    @Override
    public String getHelp(String prefix) {
        return "Usage: `" + prefix + "serverlist`\n" +
                "Displays the name of servers the bot is in.";
    }
}
