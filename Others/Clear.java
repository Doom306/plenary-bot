package com.general_hello.commands.commands.Others;

import com.general_hello.commands.Listener;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Clear implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) {
        if(ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("\uD83D\uDED1 You must add a number after Clear command to delete an amount of messages.\n").queue();
        }

        else
        {
            TextChannel chan = ctx.getChannel();
            if (!ctx.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                chan.sendMessage("🛑 I do not have the `Manage Message` and `Message History` Permission!").queue();
                return;
            } else if(!ctx.getMember().hasPermission(ctx.getChannel(), Permission.MESSAGE_MANAGE)) {
                chan.sendMessage("\uD83D\uDED1 You need to have the `Manage Message` and `Message History` Permission!").queue();
                return;
            }

            //Parse String to int, detect it the input is valid.
            Integer msgs = 0;
            try {
                msgs = Integer.parseInt(ctx.getArgs().get(0));
                LOGGER.info("PruneCommand Called to prune " + msgs + " messages.");
            } catch (NumberFormatException nfe) {
                ctx.getChannel().sendMessage("\uD83D\uDED1 Please enter a valid number.").queue();
            }

            if(msgs <= 1 || msgs > 100) {
                ctx.getChannel().sendMessage("\uD83D\uDED1 Please enter a number between **2 ~ 100**.").queue();
                return;
            }

            //Delete command call
            ctx.getChannel().deleteMessageById(ctx.getMessage().getId()).complete();

            chan.getHistory().retrievePast(msgs).queue((List<Message> mess) -> {
                try {
                    ctx.getChannel().deleteMessages(mess).queue(
                            success ->
                                    chan.sendMessage("✔ `" + ctx.getArgs().get(0) + "` messages deleted.")
                                            .queue(message -> {
                                                message.delete().queueAfter(2, TimeUnit.SECONDS);
                                            }),
                            error -> chan.sendMessage("\uD83D\uDED1 An Error occurred!").queue());
                } catch (IllegalArgumentException iae) {
                    ctx.getChannel().sendMessage("\uD83D\uDED1 Cannot delete messages older than 2 weeks.").queue();
                } catch (PermissionException pe) {
                    throw pe;
                }
            });

        }
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp(String prefix) {
        return "Clears a specific amount of messages\n" +
                "Usage: `" + prefix + "clear [amount of messages]`";
    }
}
