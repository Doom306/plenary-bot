package com.general_hello.commands.commands.Blackjack;

import com.general_hello.commands.commands.Uno.UnoGame;

import java.util.HashMap;

public class GameHandler {
    private static HashMap<Long, BlackjackGame> blackJackGames;
    private static HashMap<Long, UnoGame> unoGames;


    public GameHandler() {
        blackJackGames = new HashMap<>();
        unoGames = new HashMap<>();
    }

    public static BlackjackGame getBlackJackGame(long user){
        if (blackJackGames.containsKey(user)) {
            return blackJackGames.get(user);
        }

        return null;
    }

    public static void removeBlackJackGame(long user){
        blackJackGames.remove(user);
    }

    public static void putBlackJackGame(long user, BlackjackGame game){
        blackJackGames.put(user, game);
    }

    public static UnoGame getUnoGame(long guildId){
        return unoGames.getOrDefault(guildId, null);
    }

    public static void removeUnoGame(long guildId){
        unoGames.remove(guildId);
    }

    public void setUnoGame(long guildId, UnoGame unoGame){
        unoGames.put(guildId, unoGame);
    }
}