package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Others.BadDesign;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StartGiveawayCommand implements ICommand {
    @Override
    public void handle(CommandContext event) throws InterruptedException {
        String prefix = BadDesign.PREFIXES.computeIfAbsent(event.getGuild().getIdLong(), DatabaseManager.INSTANCE::getPrefix);

        if (event.getArgs().isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Error!").setColor(Color.RED).setFooter("Custom discord bot needed? DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 now!").setDescription(getHelp(prefix));
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        try {
            if (event.getArgs().get(1).isEmpty()) {
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Error!").setColor(Color.RED).setFooter("Custom discord bot needed? DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 now!").setDescription(getHelp(prefix));
                event.getChannel().sendMessage(embedBuilder.build()).queue();
                return;
            }
        } catch (Exception e) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Error!").setColor(Color.RED).setFooter("Custom discord bot needed? DM \uD835\uDD0A\uD835\uDD22\uD835\uDD2B\uD835\uDD22\uD835\uDD2F\uD835\uDD1E\uD835\uDD29 ℌ\uD835\uDD22\uD835\uDD29\uD835\uDD29\uD835\uDD2C#3153 now!").setDescription(getHelp(prefix));
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        if(!PermissionUtil.checkPermission(event.getChannel(), event.getMember(), Permission.MANAGE_SERVER))
        {
            event.getChannel().sendMessage("You must have Manage Server perms to use this!").queue();
            return;
        }
        String str = event.getMessage().toString().substring(7).trim();
        String[] parts = str.split("\\s+",2);
        String lol;
        Role role;

        try{
            TextChannel channel;
            int sec = Integer.parseInt(event.getArgs().get(0));
            String message = event.getArgs().toString();
            message = message.replaceAll("\\[","");
            message = message.replaceAll("]","");
            message = message.replaceAll(",","");
            message = message.replaceFirst(event.getArgs().get(0), "");
            try {
                if (event.getArgs().get(2).isEmpty()) {
                    channel = event.getChannel();
                    lol = "none";
                    role = null;
                } else {
                    channel = event.getMessage().getMentionedChannels().get(0);
                    message = message.replaceFirst(event.getMessage().getMentionedChannels().get(0).getAsMention(), "");
                    lol = event.getMessage().getMentionedRoles().get(0).getAsMention();
                    message = message.replace(lol, "");
                    role = event.getMessage().getMentionedRoles().get(0);
                }
            } catch (Exception e) {
                channel = event.getChannel();
                lol = "none";
                role = null;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("\uD83C\uDF89  **GIVEAWAY!**  \uD83C\uDF89");
            embedBuilder.setFooter("Made by Wow #3153");
            embedBuilder.addField((parts.length>1 ? "\u25AB*`"+ message +"`*\u25AB\n" : ""), "", false);
            embedBuilder.addField("Requirements", lol, false);
            embedBuilder.addField("React with \uD83C\uDF89 to enter!", "", true);
            embedBuilder.setColor(Color.cyan);
            String finalMessage = message;
            Role finalRole = role;
            channel.sendMessage(embedBuilder.build()).queue(m -> {
                Giveaway.messageHashMap.put(m.getIdLong(), m);
                ArrayList<Member> members = new ArrayList<>();
                Giveaway.membersArrayListHashMap.put(m, members);
                Giveaway.requirements.put(m, finalRole);
                m.addReaction("\uD83C\uDF89").queue();
                new Giveaway(sec,m, finalMessage, finalRole).start();
            });
            event.getMessage().delete().queue();
            event.getChannel().sendMessage("Giveaway for " + message + " starting in " + channel.getAsMention()).queue();
        } catch(NumberFormatException ex) {
            event.getChannel().sendMessage("Could not parse seconds from `"+parts[0]+"`").queue();
        }

    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("create");
        aliases.add("begin");
        return aliases;
    }

    @Override
    public String getHelp(String prefix) {
        return "Start a giveaway\n" +
                "Usage: `" + prefix + "start <seconds> [<item>] [channel] [req]` - starts a giveway. \n" +
                "Ex: `" + prefix + "start 180` for a 3 minute giveaway\n" +
                "Don't include <> nor []; <> means required, [] means optional";
    }
}
