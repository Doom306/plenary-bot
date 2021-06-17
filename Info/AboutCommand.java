package com.general_hello.commands.commands.Info;


import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class AboutCommand implements ICommand {
    String[] featureses = new String[4];
    public boolean IS_AUTHOR = true;
    public String REPLACEMENT_ICON = "+";
    public Color color;
    public String description;
    public Permission[] perms;
    public String oauthLink;
    public final String[] features;

    public AboutCommand(Color color, String description, Permission[] perms) {
        this.color = color;
        this.description = description;
        this.perms = perms;
        featureses[0] = "Kick people";
        featureses[1] = "Make giveaways";
        featureses[2] = "Show live basketball scores!";
        featureses[3] = "Currency!";
        this.features = featureses;
    }

    @Override
    public void handle(CommandContext event) throws InterruptedException {

        final long guildID = event.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, (id) -> Config.get("prefix"));

        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, perms) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Could not generate invite link ", e);
                oauthLink = "";
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.cyan);
        builder.setAuthor("All about " + event.getSelfUser().getName() + "!", null, event.getSelfUser().getAvatarUrl());
        event.getChannel().createInvite().complete().getUrl();
        boolean join = !event.getChannel().retrieveInvites().complete().isEmpty();
        boolean inv = !oauthLink.isEmpty();
        String invline = "\n" + (join ? "Join my server [here](https://discord.gg/UeQ7NJcpNd)" : (inv ? "Please " : ""))
                + (inv ? (join ? ", or " : "") + "[invite](" + oauthLink + ") me to your server" : "") + "!";
        String author = event.getJDA().getUserById(Config.get("owner_id"))==null ? "<@" + event.getJDA().getUserById(Config.get("owner_id"))+">"
                : event.getJDA().getUserById(Config.get("owner_id")).getName();
        StringBuilder descr = new StringBuilder().append("Hello! I am **").append(event.getSelfUser().getName()).append("**, ")
                .append(description).append("\nI ").append(IS_AUTHOR ? "was written in Java" : "am owned").append(" by **")
                .append(author).append("** using " + JDAUtilitiesInfo.AUTHOR + "'s [Commands Extension](" + JDAUtilitiesInfo.GITHUB + ") (")
                .append(JDAUtilitiesInfo.VERSION).append(") and the [JDA library](https://github.com/DV8FromTheWorld/JDA) (")
                .append(JDAInfo.VERSION).append(")\nType `").append(prefix).append("help")
                .append("` to see my commands!").append(join || inv ? invline : "").append("\n\nSome of my features include: ```css");
        for (String feature : features)
            descr.append("\n").append(REPLACEMENT_ICON).append(" ").append(feature);
        descr.append(" ```");
        builder.setDescription(descr);
        event.getJDA().getShardInfo();
        builder.addField("Stats", (event.getJDA().getGuilds().size() + " Servers\nShard " + (event.getJDA().getShardInfo().getShardId() + 1)
                + "/" + event.getJDA().getShardInfo().getShardTotal()), true);
        builder.addField("This shard", event.getJDA().getUsers().size() + " Users\n" + event.getJDA().getGuilds().size() + " Servers", true);
        builder.addField("", event.getJDA().getTextChannels().size() + " Text Channels\n" + event.getJDA().getVoiceChannels().size() + " Voice Channels", true);
        builder.setFooter("Support me on patreon with https://www.patreon.com/plenary", null);
        event.getMessage().reply(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "about";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows information about the bot!\n" +
                "Usage: `" + prefix + "about`";
    }
}
