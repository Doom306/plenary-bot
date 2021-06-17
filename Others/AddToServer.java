package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;

public class AddToServer implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("Invite our bot now for exclusive rewards!\n" +
                "<https://discord.com/oauth2/authorize?client_id=761072632958681099&scope=bot&permissions=819014710>").queue();
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp(String prefix) {
        return "Sends a link to invite the bot to your server";
    }
}
