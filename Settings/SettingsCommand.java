package com.general_hello.commands.commands.Settings;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        final long guildID = ctx.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);

        if (!SettingsData.antiRobServer.containsKey(ctx.getGuild())) SettingsData.antiRobServer.put(ctx.getGuild(), false);

        Boolean pingPrefix = SettingsData.pingForPrefix.get(ctx.getAuthor());
        Boolean antiRob = SettingsData.antiRobServer.get(ctx.getGuild());

        String onOrOff = "✅ **Enabled**";
        String onOrOff1 = "✅ **Enabled**";

        if (!SettingsData.guilds.contains(ctx.getGuild())) SettingsData.guilds.add(ctx.getGuild());

        if (!antiRob) onOrOff = "❎ **Disabled**";
        if (!pingPrefix) onOrOff1 = "❎ **Disabled**";

        if (ctx.getArgs().isEmpty()) {

            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(ctx.getAuthor().getName() + "'s settings for " + ctx.getSelfUser().getName()).setFooter("Your settings ↔ Page 1 of 1").setColor(Color.ORANGE);
            embedBuilder.setDescription("You can use `" + prefix + " settings [setting] [value]` to change the value of a specific setting: For example, `" + prefix + " settings antiRob true`\n" +
                    "If the setting can only be `True` or `False`, providing no value will simply toggle to the other option.\n" +
                    "\n**Your settings**\n" +
                    " Anti-Server rob `(Mod only)` - ***antiRob*** - " + onOrOff + "\n" +
                    "When enabled, the server will be protected against robbing.\n" +
                    "\n" +
                    "Ping for prefix - ***prefixDm*** - " + onOrOff1 + "\n" +
                    "Toggles whether or not you'll get a DM of the prefix once you ping me.\n");
            ctx.getChannel().sendMessage(embedBuilder.build()).queue();
        }

        if (ctx.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            if (ctx.getArgs().get(0).equalsIgnoreCase("antirob")) {
                try {
                    SettingsData.antiRobServer.put(ctx.getGuild(), Boolean.valueOf(ctx.getArgs().get(1).toLowerCase()));
                    antiRob = SettingsData.antiRobServer.get(ctx.getGuild());
                    onOrOff = "✅ **Enabled**";
                    onOrOff1 = "now";

                    if (!antiRob) onOrOff = "❎ **Disabled**";
                    if (!antiRob) onOrOff1 = "not";

                    ctx.getChannel().sendMessage("**Ping for prefix** successfully changed to " + onOrOff + "\n" +
                            "The server is " + onOrOff1 + " protected against robbing.").queue();
                } catch (Exception e) {
                    ctx.getChannel().sendMessage("Kindly place either ***true*** or ***false***").queue();
                }
            }
        }

        if (ctx.getArgs().get(0).equalsIgnoreCase("prefixdm")) {
            try {
                SettingsData.pingForPrefix.put(ctx.getAuthor(), Boolean.valueOf(ctx.getArgs().get(1).toLowerCase()));
                pingPrefix = SettingsData.pingForPrefix.get(ctx.getAuthor());
                onOrOff = "✅ **Enabled**";
                onOrOff1 = "";

                if (!pingPrefix) onOrOff = "❎ **Disabled**";
                if (!pingPrefix) onOrOff1 = "not";

                ctx.getChannel().sendMessage("**Ping for prefix** successfully changed to " + onOrOff + "\n" +
                        "You will " + onOrOff1 + "get the bot's prefix in your DM's if you ping me.").queue();
            } catch (Exception e) {
                ctx.getChannel().sendMessage("Kindly place either ***true*** or ***false***").queue();
            }
        }
    }

    @Override
    public String getName() {
        return "setting";
    }

    @Override
    public List<String> getAliases() {
        List<String> list = new ArrayList<>();
        list.add("settings");
        list.add("serversettings");
        return list;
    }

    @Override
    public String getHelp(String prefix) {
        return "Changes the setting of the bot to the user or to the server\n" +
                "Usage: `" + prefix + getName() + "`";
    }
}
