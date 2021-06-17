package com.general_hello.commands.commands.MusicCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

@SuppressWarnings("ConstantConditions")

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I'm already in a voice channel").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if (!selfMember.hasPermission(Permission.VOICE_CONNECT)) {
            channel.sendMessage("I am not allowed to join that voice channel").queue();
            return;
        }

        audioManager.openAudioConnection(memberChannel);
        audioManager.setSelfDeafened(true);
        EmbedBuilder em = new EmbedBuilder().setTitle("Music!").setColor(Color.GREEN);
        em.setDescription("Successfully connected to `\uD83D\uDD0A " + memberChannel.getName() + "`");

        channel.sendMessage(em.build()).queue();
    }

    @Override
    public String getName() {
        return "connect";
    }

    @Override
    public String getHelp(String prefix) {
        return "Makes the bot join your voice channel" +
                "Usage: `" + prefix + "connect`";
    }
}