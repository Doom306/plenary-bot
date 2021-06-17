package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class ServerCountCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();
        List<Guild> list = jda.getGuilds();
        ctx.getChannel().sendMessage("This bot is in " + (list.size()) + " servers").queue();
    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getHelp(String prefix) {
        return "Usage: `" + prefix + "server`\n" +
                "Returns the count of servers the bot is in";
    }
}
