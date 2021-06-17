package com.general_hello.commands.commands.Games;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.Listener;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HangManCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    HangMan game;
    @Override
    public void handle(CommandContext ctx) {
        final long guildID = ctx.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);

        if(ctx.getArgs().size() == 0)
        {
            ctx.getChannel().sendMessage(getHelp(prefix)).queue();
        }

        else if(ctx.getArgs().size() > 0 && "start".equals(ctx.getArgs().get(0)))
        {
            LOGGER.info(this.getClass().getName(), "HangMan Started.");
            game = new HangMan(ctx.getEvent());
        }

        else if(ctx.getArgs().size() > 0 && "end".equals(ctx.getArgs().get(0)))
        {
            if(ctx.getAuthor() == HangMan.starter)
                game.endGame();
            else
                ctx.getChannel().sendMessage("🛑 Only the game starter can end the game.").queue();
        }

        else
        {
            try {
                game.sendInput(ctx.getArgs(), ctx.getEvent());
            } catch(NullPointerException en) {
                ctx.getChannel().sendMessage("🛑 Game haven't started yet!").queue();
                LOGGER.info(en + this.getClass().getName(), "Game haven't started.");
            }
        }
    }

    @Override
    public String getName() {
        return "hm";
    }

    @Override
    public String getHelp(String prefix) {
        return "Play Hangman with anyone\n" +
                "Usage: `" + prefix + "hm`" +
                "Options: `start | [letter] | end | null";
    }
}
