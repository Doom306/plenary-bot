package com.general_hello.commands.commands.Giveaway;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.List;

public class RerollCommand implements ICommand {
    @Override
    public void handle(CommandContext event) throws InterruptedException {
        if(!PermissionUtil.checkPermission(event.getChannel(), event.getMember(), Permission.MANAGE_SERVER))
        {
            event.getChannel().sendMessage("You must have Manage Server perms to use this!").queue();
            return;
        }
        String id = event.getArgs().get(0);
        Message m = event.getChannel().retrieveMessageById(id).complete();
        if(m==null)
        {
            event.getChannel().sendMessage("Message not found!").queue();
            return;
        }
        m.getReactions()
                .stream().filter(mr -> mr.getReactionEmote().getName().equals("\uD83C\uDF89"))
                .findAny().ifPresent(mr -> {
            List<User> users = mr.retrieveUsers().complete();
            users.remove(m.getJDA().getSelfUser());
            String uid = users.get((int)(Math.random()*users.size())).getId();
            event.getChannel().sendMessage("Congratulations to <@"+uid+">! You won the re-roll! " + m.getJumpUrl()).queue();
        });
    }

    @Override
    public String getName() {
        return "reroll";
    }

    @Override
    public String getHelp(String prefix) {
        return "Re-rolls a winner for the giveaway on the provided message\n" +
                "Usage: `/reroll <messageid>`\n" +
                "Don't include <> nor []; <> means required, [] means optional";
    }
}
