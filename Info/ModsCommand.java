package com.general_hello.commands.commands.Info;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.Emoji.Emoji;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Utils.UtilBot;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.stream.Collectors;

public class ModsCommand implements ICommand {
    @Override
    public void handle(CommandContext e) {
        if (e.getArgs().size() >= 1 && ("pin".equals(e.getArgs().get(0)))) {
            String message = "";
            for(int i = 0; i < e.getArgs().size(); i++) { message += i==0 ? "" : e.getArgs().get(i) + " "; }

            if (e.getGuild().getMembers().size() > 150) {     // filter large servers
                e.getChannel().sendMessage(Emoji.ERROR + " The server is too big. I'm afraid I'll get banned by pinning mod...").queue();
            } else {
                List<Member> onlineMod = getMods(e.getGuild()).stream()
                        .filter(mod -> mod.getOnlineStatus().equals(OnlineStatus.ONLINE)).collect(Collectors.toList());

                onlineMod.addAll(getMods(e.getGuild()).stream()
                        .filter(mod -> mod.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)).collect(Collectors.toList()));

                onlineMod.addAll(getMods(e.getGuild()).stream()
                        .filter(mod -> mod.getOnlineStatus().equals(OnlineStatus.IDLE)).collect(Collectors.toList()));

                if (onlineMod.isEmpty()) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Sorry, no mod is available.").queue();
                } else {
                    Member randomMod = onlineMod.get(UtilNum.randomNum(0, onlineMod.size()-1));
                    String finalMessage = message;
                    randomMod.getUser().openPrivateChannel().queue((PrivateChannel) -> {
                        PrivateChannel.sendMessage(randomMod.getAsMention() + ", **" + e.getMember().getEffectiveName() + "#" + e.getAuthor().getDiscriminator() +
                                "** said: " + finalMessage).queue();
                    });

                    e.getChannel().sendMessage("Request sent to " + randomMod.getEffectiveName()).queue();
                }
            }

        } else {
            List<Member> mods = getMods(e.getGuild());
            StringBuilder output = new StringBuilder("Mods in **").append(e.getGuild().getName()).append("**\n");

            /* Iterate through mod list */
            for (Member mem : mods) {
                output.append(UtilBot.getStatusEmoji(mem.getOnlineStatus()))
                        .append(" ")
                        .append(mem.getUser().getName()).append("#").append(mem.getUser().getDiscriminator())
                        .append(mem.isOwner() ? " (Owner)" : "")
                        .append(mem.hasPermission(Permission.BAN_MEMBERS) && !mem.isOwner() ? " (Admin)" : "").append("\n");
            }

            e.getChannel().sendMessage(output.toString()).queue();
        }
    }

    private List<Member> getMods(Guild guild) {
        return guild.getMembers().stream()
                .filter(member -> !member.getUser().isBot())
                .filter(member -> (member.getPermissions().contains(Permission.MANAGE_PERMISSIONS)))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "mods";
    }

    @Override
    public String getHelp(String prefix) {
        return "This command is for getting a list of mods in this server."
                + "Command Usage: `" + prefix + "mods`\n"
                + "Parameter: `-h | pin [message] | nothing`\n"
                + "pin [message]: Automatically pin a mod, and leave a message. For example, `/pin Someone is spamming`.\n"
                + "[nothing] : Get a list of mods.";
    }
}
