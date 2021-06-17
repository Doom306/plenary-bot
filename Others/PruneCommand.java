package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PruneCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel guildChannelById = ctx.getGuild().getTextChannelById("839684246661627914");
        TextChannel guildChannelById1 = ctx.getGuild().getTextChannelById("798697170604654623");

        TextChannel tc = ctx.getChannel();


        if (!ctx.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            ctx.getChannel().sendMessage("Insufficient rights. You have don't have Kick Permission rights.\n").queue();
            return;
        }
        if (ctx.getArgs().isEmpty()) {

            deleteMemberMessage(tc, ctx.getJDA().getSelfUser());
        } else {
            if (!ctx.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                tc.sendMessage("🛑 I do not have the `Manage Message` and `Message History` Permission!").queue();
                return;
            } else if (!ctx.getMember().hasPermission(ctx.getChannel(), Permission.MESSAGE_MANAGE)) {
                tc.sendMessage( "🛑 You need to have the `Manage Message` and `Message History` Permission!").queue();
                return;
            }

            try {
                if ("bot".equals(ctx.getArgs().get(0))) {
                    deleteBotMessage(tc);
                } else {
                    List<User> users = ctx.getMessage().getMentionedUsers();
                    deleteMemberMessage(tc, users.toArray(new User[users.size()]));

                    List<Role> roles = ctx.getMessage().getMentionedRoles();
                    deleteRoleMessage(tc, roles.toArray(new Role[roles.size()]));
                }
            } catch (IllegalArgumentException lae) {
                return;
            }
            ctx.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
        }

        try {
            guildChannelById.sendMessage(ctx.getMember().getAsMention() + " used the delete command in " + ctx.getChannel().getAsMention()).queue();
        } catch (Exception ignored) {}

        try {
            guildChannelById1.sendMessage(ctx.getMember().getAsMention() + " used the delete command in " + ctx.getChannel().getAsMention()).queue();
        } catch (Exception ignored) {}
    }

    private void deleteMemberMessage(TextChannel tc, User... users)
    {
        List<Message> messages = new ArrayList<>();
        for (User user : users) {
            List<Message> newMsg = tc.getIterableHistory()
                    .cache(false)
                    .stream()
                    .limit(1000)
                    .filter(message -> message.getAuthor().getId().equals(user.getId()))
                    .filter(message -> ChronoUnit.WEEKS.between(message.getTimeCreated(), ZonedDateTime.now()) < 2)
                    .collect(Collectors.toList());
            messages.addAll(newMsg);
        }
        messages = messages.size() > 100 ? messages.subList(0, 100) : messages;
        int size = messages.size();

        tc.deleteMessages(messages).queue(
                success -> tc.sendMessage("✔ Cleaned up " + size + " message(s) from " + users.length + " member(s).").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS))
        );
    }

    private void deleteRoleMessage(TextChannel tc, Role... roles)
    {
        List<Message> messages;
        messages = new ArrayList<>();
        for (Role role : roles) {
            List<Message> newMsg = tc.getIterableHistory()
                    .cache(false)
                    .stream()
                    .limit(1000)
                    .filter(message -> !message.getMember().getRoles().isEmpty() && !message.getMember().getRoles().contains(role))
                    .filter(message -> ChronoUnit.WEEKS.between(message.getTimeCreated(), ZonedDateTime.now()) < 2)
                    .collect(Collectors.toList());
            messages.addAll(newMsg);
        }

        messages = messages.size() > 100 ? messages.subList(0, 100) : messages;
        int size = messages.size();
        tc.deleteMessages(messages).queue(
                success -> tc.sendMessage("✔ Cleaned up " + size + " message(s) from " + roles.length + " role(s).").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS))
        );
    }

    private void deleteBotMessage(TextChannel tc)
    {
        List<Message> messages = tc.getIterableHistory()
                .cache(false)
                .stream()
                .limit(1000)
                .filter(message -> message.getAuthor().isBot())
                .filter(message -> ChronoUnit.WEEKS.between(message.getTimeCreated(), ZonedDateTime.now())<2)
                .collect(Collectors.toList());
        messages = messages.size() > 100 ? messages.subList(0, 100) : messages;
        int size = messages.size();

        tc.deleteMessages(messages).queue(
                success -> tc.sendMessage("✔ Cleaned up "+size+" message(s) from bots.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS))
        );
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getHelp(String prefix) {
        return "Deletes messages depending on what you imputed\n" +
                "Usage: `" + prefix + "delete`\n" +
                "Parameters: `bot` | `@Member(s)` | `@Role(s)`";
    }
}
