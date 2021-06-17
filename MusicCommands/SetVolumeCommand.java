package com.general_hello.commands.commands.MusicCommands;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Pro.ProData;
import com.general_hello.commands.lavaplayer.GuildMusicManager;
import com.general_hello.commands.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetVolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("Kindly include the volume to be placed!").queue();
            return;
        }

        if (ProData.isPro.containsKey(ctx.getAuthor())) {
            if (ProData.isPro.get(ctx.getAuthor())) {
                int volume = Integer.parseInt(ctx.getArgs().get(0));

                if (volume > 100) {
                    volume = 100;
                }

                if (volume < 0) {
                    volume = 0;
                }

                final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
                final AudioPlayer audioPlayer = musicManager.audioPlayer;
                audioPlayer.setVolume(volume);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Success").setColor(Color.GREEN);
                embedBuilder.setDescription("The volume is now adjusted to " + volume + "%! Enjoy listening!");
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
        return "setvolume";
    }

    @Override
    public List<String> getAliases() {
        List<String> lol = new ArrayList<>();
        lol.add("sv");
        lol.add("volume");
        return lol;
    }

    @Override
    public String getHelp(String prefix) {
        return "Sets the volume of the music playing.\n" +
                "Usage: `" + prefix + getName() + " [volume]`\n" +
                "Example: `" + prefix + getName() + " 100`";
    }
}
