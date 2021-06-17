package com.general_hello.commands.commands.Others;

import com.general_hello.commands.Config;
import com.general_hello.commands.Listener;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SoftBanCommand implements ICommand {
    private final int delDays = 7;
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext e) {
        final long guildID = e.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, (id) -> Config.get("prefix"));

        if(e.getArgs().isEmpty()) {
            e.getChannel().sendMessage(getHelp(prefix)).queue();
            return;
        }

        if(e.getArgs().size() == 0) {
            e.getChannel().sendMessage("🛑 You need to mention 1 or more members to soft ban!").queue();
        }

        else {
            Guild guild = e.getGuild();
            Member selfMember = guild.getSelfMember();

            //Check if the bot have permission to kick.
            if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                e.getChannel().sendMessage("🛑 I need to have **Ban Members** Permission to soft ban members.").queue();
                return;
            } else if (!e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                e.getChannel().sendMessage("🛑 You need to have **Ban Members** Permission to soft ban members.").queue();
                return;
            }

            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            LOGGER.info(this.getClass().getName(), "Called to soft ban " + mentionedUsers.size() + " users.");

            for (User user : mentionedUsers) {
                Member member = guild.getMember(user);

                guild.ban(member, delDays).queue(
                        success -> {
                            guild.unban(user).queue();
                            e.getChannel().sendMessage("✅ Successfully Soft Banned `" + user.getName() + "`").queue();
                        },
                        error -> {
                            e.getChannel().sendMessage("🛑 An error occurred!\n```" + error.getMessage() + "```").queue();
                        }
                );
            }
        }
    }

    @Override
    public String getName() {
        return "softban";
    }

    @Override
    public String getHelp(String prefix) {
        return "Soft ban will ban and immediately unban a member to kick and clean up the member's message.\n"
                + "Command Usage: `" + prefix + "softban`\n"
                + "Parameter: `@Member(s)`";
    }
}
