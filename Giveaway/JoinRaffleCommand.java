package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.ArrayList;

public class JoinRaffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (!RaffleData.allowed.containsKey(ctx.getMember())) {
            RaffleData.allowed.put(ctx.getMember(), false);
        }

        if (!RaffleData.allowed.get(ctx.getMember())) {
            ctx.getChannel().sendMessage("Kindly check for any missing requirements!!! Or ask the mods to approve you!!!").queue();
            return;
        }

        ArrayList<Integer> id = new ArrayList<>();

        ctx.getMessage().delete().queue();

        final int lol = RaffleData.count.size();
        int lastID = RaffleData.count.get(lol-1);

        if (lastID == 0) {
            lastID++;
        }

        if (RaffleData.id.containsKey(ctx.getMember())) {
            ArrayList<Integer> integers = RaffleData.id.get(ctx.getMember());
            integers.add(lastID);
            RaffleData.count.add(lastID+1);
            RaffleData.id.put(ctx.getMember(), integers);
            RaffleData.allowed.put(ctx.getMember(), false);
        } else {
            id.add(lastID);
            RaffleData.count.add(lastID+1);
            RaffleData.id.put(ctx.getMember(), id);
            RaffleData.allowed.put(ctx.getMember(), false);
        }

        if (RaffleData.memberHashMap.containsKey(ctx.getGuild())) {
            if (!RaffleData.memberHashMap.get(ctx.getGuild()).contains(ctx.getMember())) {
                ArrayList<Member> members = RaffleData.memberHashMap.get(ctx.getGuild());
                members.add(ctx.getMember());
                RaffleData.memberHashMap.put(ctx.getGuild(), members);
            }
        } else {
            ArrayList<Member> members = new ArrayList<>();
            members.add(ctx.getMember());
            RaffleData.memberHashMap.put(ctx.getGuild(), members);
        }

        EmbedBuilder embedBuilder = new EmbedBuilder().setThumbnail(ctx.getAuthor().getAvatarUrl()).setTitle("Raffle joined!!!").setColor(Color.cyan);
        embedBuilder.addField(ctx.getAuthor().getName() + " joined the raffle!!! ", "Id is " + lastID,false);
        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp(String prefix) {
        return "Joins the raffle!\n" +
                "Usage: `" + prefix + "join`";
    }
}
