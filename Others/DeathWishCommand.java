package com.general_hello.commands.commands.Others;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class DeathWishCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final long guildID = ctx.getGuild().getIdLong();
        String prefix = BadDesign.PREFIXES.computeIfAbsent(ctx.getGuild().getIdLong(), DatabaseManager.INSTANCE::getPrefix);

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage(getHelp(prefix)).queue();
            return;
        }
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage(getHelp(prefix)).queue();
        } else {
            /*state = state.replace("\\[", "");
            state = state.replace("]", "");
            state = state.replace(ctx.getMessage().getMentionedMembers().get(0).getAsMention(), "");
            state = state.replace(",", "");*/

            final String state = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size()));

            String stuff = String.format(("%s died due to %s"), ctx.getMessage().getMentionedMembers().get(0).getAsMention(), state);
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setThumbnail("https://img1.looper.com/img/gallery/ryuk-in-netflixs-death-note-had-to-be-played-by-two-actors/intro-1503169693.jpg")
                    .setTitle("DEATH NOTE")
                    .setDescription(stuff)
                    .setFooter("Custom Discord bot needed? DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153")
                    .setImage("https://media.comicbook.com/2017/03/screen-shot-2017-03-23-at-2-46-03-pm-240311.png");
            ctx.getChannel().sendMessage(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "death";
    }

    @Override
    public String getHelp(String prefix) {
        return "The user whose name is written in this note shall have a death wish.\n" +
                "Usage: `" + prefix + "death [mention] [how you will kill him/her]`\n" +
                "Made by ***Overkill#9150***";
    }
}
