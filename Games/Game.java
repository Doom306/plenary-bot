package com.general_hello.commands.commands.Games;


import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public interface Game {
    public void startGame(); //Keep the game run'in

    /*{
        e.getChannel().sendMessage("game Mode ON!\nInput: row column").queue();
        turn = starter;
    }*/

    public void endGame(); //Stop da game

    /*{
        if(e.getAuthor() == starter || e.getAuthor() == opponent)
            e.getChannel().sendMessage(Emoji.E_success + " game Ended!").queue();
    }*/

    public void sendInput(List<String> in, GuildMessageReceivedEvent event);  //Set the input called by a command class


    /*{
    public void switchTurn();
        if("starter".equals(turn))
            turn = opponent;
        else
            turn = starter;
    }*/
}