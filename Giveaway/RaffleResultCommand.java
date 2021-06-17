package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RaffleResultCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        StringBuilder message = new StringBuilder();

        final int size = RaffleData.memberHashMap.size();
        final ArrayList<Member> members = RaffleData.memberHashMap.get(ctx.getGuild());
        int x = 0;

        if (members.isEmpty()) {
            ctx.getChannel().sendMessage("No one joined the raffle!!!").queue();
            return;
        }

        ctx.getMessage().delete().queue();

        while (x <= size) {
            String id = RaffleData.id.get(members.get(x)).toString();
            System.out.println(id);
            id = id.replaceAll("\\[", "");
            id = id.replaceAll("]", "");
            message.append("Raffles for ").append(members.get(x).getUser().getName()).append(" #").append(members.get(x).getUser().getDiscriminator()).append(" ID is ").append(id).append("\n");
            x++;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder().setThumbnail(ctx.getSelfUser().getAvatarUrl()).setColor(Color.cyan).setTitle("Raffle Summary");
        embedBuilder.addField(message.toString(), "", false);
        embedBuilder.setFooter(LocalDateTime.now().getDayOfWeek().toString(), ctx.getAuthor().getAvatarUrl());

        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "result";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows a summarized report of the raffle\n" +
                "Usage: `" + prefix + "result";
    }
}
