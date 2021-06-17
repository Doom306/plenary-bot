package com.general_hello.commands.commands.MusicCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.lavaplayer.GuildMusicManager;
import com.general_hello.commands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        final Member self = ctx.getMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I need to be in a voice channel to play music").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("There is no track playing currently").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        EmbedBuilder em = new EmbedBuilder().setTitle("Music").setColor(Color.YELLOW);
        em.setDescription("Skipped the current track");
        channel.sendMessage(em.build()).queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp(String prefix) {
        return "Skips the current track\n" +
                "Usage: `" + prefix + "skip`";
    }
}