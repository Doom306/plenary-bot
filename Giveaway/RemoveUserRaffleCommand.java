package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.ArrayList;

public class RemoveUserRaffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (!ctx.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            ctx.getChannel().sendMessage("You don't have `Kick Member Permissions`").queue();
            return;
        }

        if (!RaffleData.allowed.containsKey(ctx.getMember())) {
            RaffleData.allowed.put(ctx.getMember(), false);
        }

        ctx.getMessage().delete().queue();

        ArrayList<Integer> id = new ArrayList<>();

        ctx.getMessage().delete().queue();

        final int lol = RaffleData.count.size();
        int lastID = RaffleData.count.get(lol-1);

        if (lastID == 0) {
            lastID++;
        }

        ArrayList<Integer> integers = RaffleData.id.get(ctx.getMember());
        integers.remove(lastID);
        RaffleData.count.add(lastID-1);
            RaffleData.id.remove(ctx.getMember());
            RaffleData.allowed.put(ctx.getMember(), false);

        if (RaffleData.memberHashMap.containsKey(ctx.getGuild())) {
            if (RaffleData.memberHashMap.get(ctx.getGuild()).contains(ctx.getMember())) {
                ArrayList<Member> members = RaffleData.memberHashMap.get(ctx.getGuild());
                members.remove(ctx.getMember());
                RaffleData.memberHashMap.remove(ctx.getGuild(), members);
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder().setThumbnail(ctx.getAuthor().getAvatarUrl()).setTitle("Raffle user removed!!!").setColor(Color.cyan);
        embedBuilder.addField(ctx.getAuthor().getName() + " just got removed from the raffle!!! ", "Id is " + lastID,false);
        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getHelp(String prefix) {
        return "Removes the user from the raffle\n" +
                "Usage: `" + prefix + "remove [mentioned user]";
    }
}
