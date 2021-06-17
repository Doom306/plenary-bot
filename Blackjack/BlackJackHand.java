package com.general_hello.commands.commands.Blackjack;

import java.util.ArrayList;

public class BlackJackHand {

    private ArrayList<Card> cards;

    public BlackJackHand(){
        cards = new ArrayList<>();
    }

    public int getValue(){
        int v = 0;
        int aces = 0;
        for (Card card : cards){
            Card.Value value = card.getValue();
            if (value == Card.Value.ACE){
                aces ++;
            }
            v += value.getValue();
        }
        while (aces > 0 && v > 21){
            v -= 10;
            aces--;
        }
        return v;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public Card removeCard(int index){
        return cards.remove(index);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Card card : cards){
            sb.append(card.getEmoticon()).append(" ");
        }
        return sb.toString();
    }

}