package com.general_hello.commands.commands.MusicCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.lavaplayer.GuildMusicManager;
import com.general_hello.commands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NowPlayingCommand implements ICommand {

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
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (playingTrack == null) {
            channel.sendMessage("There is no track playing currently").queue();
            return;
        }

        final AudioTrackInfo info = playingTrack.getInfo();

        EmbedBuilder em = new EmbedBuilder().setTitle("Music!").setColor(Color.YELLOW);
        em.setDescription("Now playing `" + info.title + "` (" + info.length + ") by `" + info.author + "` (Link: <" + info.uri + ">)");
        channel.sendMessage(em.build()).queue();
    }

    @Override
    public String getName() {
        return "playinginfo";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("np");
        aliases.add("nowplaying");
        aliases.add("info");
        aliases.add("song");
        return aliases;
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the current playing song\n" +
                "Usage: `" + prefix + "playinginfo`";
    }
}