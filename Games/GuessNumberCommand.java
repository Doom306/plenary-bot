package com.general_hello.commands.commands.Games;

import com.general_hello.commands.Listener;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuessNumberCommand implements ICommand {

    private GuessNumber gn;
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) {
        if(ctx.getArgs().isEmpty())
        {
            ctx.getChannel().sendMessage("Invalid Input").queue();
        }

        else if(ctx.getArgs().size() == 0 || (ctx.getArgs().size() > 0 && "start".equals(ctx.getArgs().get(0))))
        {
            gn = new GuessNumber(ctx.getEvent());
        }

        else if(ctx.getArgs().size() > 0 && "end".equals(ctx.getArgs().get(0)))
        {
            gn.endGame();
            ctx.getChannel().sendMessage("Game ended! The number was " + gn.getNumber() + ".").queue();
        }

        else
        {
            try {
                if(!gn.isEnded)
                    gn.sendInput(ctx.getArgs(), ctx.getEvent());
                else
                    ctx.getChannel().sendMessage("\uD83D\uDED1 Game haven't started yet!").queue();
            } catch(NullPointerException en) {
                ctx.getChannel().sendMessage("\uD83D\uDED1 Game haven't started yet!").queue();
                LOGGER.info(en + this.getClass().getName(), "Game haven't started.");
            }
        }
    }

    @Override
    public String getName() {
        return "gn";
    }

    @Override
    public String getHelp(String prefix) {
        return "Plays Guess the Number\n" +
                "Usage: `" + prefix + "gn`" +
                "\n" +
                "Parameter: `start | number`";
    }
}
