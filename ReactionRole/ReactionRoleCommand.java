package com.general_hello.commands.commands.ReactionRole;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;

public class ReactionRoleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getMessage().getMentionedRoles().isEmpty()) {
            ctx.getChannel().sendMessage("Kindly mention a role!!").queue();
            return;
        }

        if (ctx.getMessage().getMentionedChannels().isEmpty()) {
            ctx.getChannel().sendMessage("Kindly mention a channel!!").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            ctx.getChannel().sendMessage("Unsuccessful!!! You do not has the `Manage Roles` permission.").queue();
            return;
        }

        String mention = ctx.getMessage().getMentionedRoles().get(0).getName();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Roles");
        embedBuilder.setColor(Color.cyan);
        embedBuilder.setDescription("React here to get the role of " + mention);
        embedBuilder.setFooter("Created by Wow#3153");

        ctx.getMessage().getMentionedChannels().get(0).sendMessage(embedBuilder.build()).queue((message -> {
            ReactionRoleData.messages.add(message.getIdLong());
            ReactionRoleData.roles.put(message.getIdLong(), ctx.getMessage().getMentionedRoles().get(0));
            message.addReaction("⚡").queue();
        }));

        ctx.getAuthor().openPrivateChannel().queue(
                (PrivateChannel) -> PrivateChannel.sendMessage("Successful!!! The user will now receive the role of " + mention + " once they react to the message.").queue()
        );
    }

    @Override
    public String getName() {
        return "reactrole";
    }

    @Override
    public String getHelp(String prefix) {
        return "Make a reaction role message\n" +
                "Usage: `" + prefix + "reactrole [@mentioned role] [mentioned channel]`";
    }
}
