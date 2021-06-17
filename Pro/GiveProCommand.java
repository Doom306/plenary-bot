package com.general_hello.commands.commands.Pro;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;

public class GiveProCommand implements ICommand
{
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        if (!ctx.getAuthor().getId().equals(Config.get("owner_id"))) {
            return;
        }

        User user = ctx.getJDA().getUserById(ctx.getArgs().get(0));
        ProData.isPro.put(user, true);
        ctx.getChannel().sendMessage("Successfully upgraded " + user.getAsTag() + " to **Premium**!").queue();

        user.openPrivateChannel().complete().sendMessage("Congratulations " + user.getAsTag() + "! You can now use `Premium commands` for Plenary bot!").queue();
    }

    @Override
    public String getName() {
        return "givepremium";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives Premium to a user! ***OWNER ONLY***\n" +
                "Usage: `" + prefix + getName() + " [user id]`";
    }
}
