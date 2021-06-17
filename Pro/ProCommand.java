package com.general_hello.commands.commands.Pro;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class ProCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ProData.isPro.get(ctx.getAuthor())) {
            ctx.getChannel().sendMessage("Well done! You already have Premium!!!").queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.CYAN).setTitle("Buying Pro for Plenary bot").setFooter("Buy pro now!");
        embedBuilder.setDescription("Get **Premium commands** now by going to this [`link`](https://www.patreon.com/plenary) and buy any type of plan!\n" +
                "Once you are done, you will have access to **Pro commands** within 48 hours!");
        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "premium";
    }

    @Override
    public String getHelp(String prefix) {
        return "Buys Premium\n" +
                "Premium gives more rewards and commands.\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
