package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.Instant;
import java.util.Random;

public class WarnCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        if (!ctx.getMember().hasPermission(Permission.KICK_MEMBERS) && !ctx.getMember().canInteract(ctx.getMessage().getMentionedMembers().get(0))) {
            ctx.getChannel().sendMessage("What are you Donald Trump?!?! You can't warn that peace lover! (You don't have kick members permission or he has a higher role than you)").queue();
            return;
        }
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("You didn't state who is the user to warn!!!").queue();
            return;
        }


        String reason = "No reason specified!!!";

        try {
            if (!ctx.getArgs().get(1).isEmpty()) {
                reason = ctx.getArgs().toString();
            }
        } catch (IndexOutOfBoundsException ignored) {

        }


        Member member;

        try {
            member = ctx.getMessage().getMentionedMembers().get(0);
        } catch (IndexOutOfBoundsException e) {
            ctx.getChannel().sendMessage("That user doesn't seem to be here!!!").queue();
            return;
        }

        try {
            if (ctx.getGuild().getRoles().contains(ctx.getGuild().getRolesByName("Muted", false).get(0))) {
                ctx.getGuild().addRoleToMember(ctx.getMessage().getMentionedMembers().get(0), ctx.getGuild().getRolesByName("mute", false).get(0)).queue();
            }
        } catch (IndexOutOfBoundsException ignored) {}

        EmbedBuilder embedBuilder = new EmbedBuilder().setThumbnail(ctx.getMessage().getMentionedUsers().get(0).getAvatarUrl()).setTimestamp(Instant.now());

        embedBuilder.setTitle("Warning!!!", ctx.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(randomColor());
        reason = reason.replaceAll(ctx.getMessage().getMentionedMembers().get(0).getAsMention(), "");
        reason = reason.replaceAll(",","");
        reason = reason.replaceAll("\\[","");
        reason = reason.replaceAll("]","");
        embedBuilder.addField(ctx.getMessage().getMentionedMembers().get(0).getEffectiveName() + " has been warned!!!", "**Reason:** " + reason, false);
        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
        ctx.getMessage().delete().queue();
    }

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getHelp(String prefix) {
        return "Warns the user of what they did! `[] = optional`\n" +
                "Usage: `" + prefix + "warn @mention [reason]";
    }

    public static Color randomColor() {
        Random colorpicker = new Random();
        int red = colorpicker.nextInt(255) + 1;
        int green = colorpicker.nextInt(255) + 1;
        int blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }
}
