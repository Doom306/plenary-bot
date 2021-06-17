package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class AvatarCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();

        if (message.getMentionedMembers().isEmpty()) {
            final Member target = message.getMember();
            final String avatarUrl = target.getUser().getAvatarUrl();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(target.getNickname());
            embedBuilder.setImage(avatarUrl);
            embedBuilder.setColor(Color.cyan);
            embedBuilder.setFooter("Need a bot? DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 now!");

            channel.sendMessage(embedBuilder.build()).queue();
        } else {
            final Member target = message.getMentionedMembers().get(0);
            final String avatarUrl = target.getUser().getAvatarUrl();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(target.getNickname());
            embedBuilder.setImage(avatarUrl);
            embedBuilder.setColor(Color.cyan);
            embedBuilder.setFooter("Need a bot? DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 now!");

            channel.sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the avatar of a specific user. \n" +
                "Usage: `" + prefix + "avatar <@user>`";
    }
}