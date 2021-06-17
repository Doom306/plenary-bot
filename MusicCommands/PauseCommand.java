package com.general_hello.commands.commands.MusicCommands;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import com.general_hello.commands.commands.Pro.ProData;
import com.general_hello.commands.lavaplayer.GuildMusicManager;
import com.general_hello.commands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.IOException;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (ProData.isPro.containsKey(ctx.getAuthor())) {
            if (ProData.isPro.get(ctx.getAuthor())) {
                final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
                final AudioPlayer audioPlayer = musicManager.audioPlayer;
                audioPlayer.setPaused(true);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Success").setColor(Color.GREEN);
                final long guildID = ctx.getGuild().getIdLong();
                String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
                embedBuilder.setDescription("The music is now set to paused!").setFooter("To continue playing the music type `" + prefix + "play`");
                ctx.getChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                ctx.getChannel().sendMessage("Sorry this but this command is a **Premium users** only!\n" +
                        "You can get ***Premium*** by typing **+premium**").queue();
                return;
            }
        } else {
            ProData.isPro.put(ctx.getAuthor(), false);
        }
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp(String prefix) {
        return "Pauses the current music playing.\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
