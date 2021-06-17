package com.general_hello.commands.commands.MusicCommands;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import com.general_hello.commands.lavaplayer.GuildMusicManager;
import com.general_hello.commands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        final long guildID = ctx.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        if (ctx.getArgs().isEmpty()) {
            try {
                final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
                final AudioPlayer audioPlayer = musicManager.audioPlayer;
                audioPlayer.setPaused(false);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Success").setColor(Color.GREEN);
                embedBuilder.setDescription("The music has now continued playing! Enjoy listening!");
                ctx.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            } catch (Exception e) {
                channel.sendMessage("Correct usage is : `" + prefix + "play <youtube link>` **or** `" + prefix + "play [song name]").queue();
                return;
            }
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final Member member = ctx.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
            audioManager.setSelfDeafened(true);
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work!\n" +
                    "If you are kindly retry the ***command***").queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getINSTANCE()
                .loadAndPlay(channel, link);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp(String prefix) {
        return "Plays a song\n" +
                "Usage: `" + prefix + "play <youtube link> | [song name]`\n" +
                "Result: Plays the music specified!\n\n" +
                "Another usage: `" + prefix + getName() + "`\n" +
                "Result: It will resume playing the music!";
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}