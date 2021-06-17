package com.general_hello.commands.commands.Uno;

import java.util.ArrayList;

public class UnoHand {

    private ArrayList<UnoCard> cards;
    private long channelId;
    private long messageId;
    private long playerId;
    private String playerName;
    private boolean drawn;


    public UnoHand(long playerId, String playerName){
        this.playerId = playerId;
        this.playerName = playerName;
        cards = new ArrayList<>();
        drawn = false;
        channelId = -1;
        messageId = -1;
    }


    public boolean canPlay(UnoCard unoCard){
        return ((!drawn && cards.contains(unoCard)) || (drawn && cards.get(cards.size() - 1).equals(unoCard)));
    }

    public void addCard(UnoCard card, boolean drawn){
        cards.add(card);
        this.drawn = drawn;
    }

    public void endTurn(UnoCard card){
        if (card != null){
            cards.remove(card);
        }
        drawn = false;
    }

    public ArrayList<UnoCard> getCards() {
        return cards;
    }

    public long getChannelId() {
        return channelId;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
