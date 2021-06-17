package com.general_hello.commands.commands.Blackjack;

public class Card {
    public enum Value {
        TWO("two", "2", 2),
        THREE("tree", "3", 3),
        FOUR("four", "4", 4),
        FIVE("five", "5", 5),
        SIX("six", "6", 6),
        SEVEN("seven", "7", 7),
        EIGHT("eight", "8", 8),
        NINE("nine", "9", 9),
        TEN("ten", "10", 10),
        JACK("jack", "J", 10),
        QUEEN("queen", "Q", 10),
        KING("king", "K", 10),
        ACE("ace", "A", 11);

        private String displayName;
        private String emoticon;
        private int value;


        Value(String displayName, String emoticon, int value){
            this.displayName = displayName;
            this.emoticon = emoticon;
            this.value = value;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmoticon() {
            return emoticon;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Face {
        CLUBS("clubs", ":clubs:"),
        DIAMONDS("diamonds", ":diamonds:"),
        HEARTS("hearts", ":hearts:"),
        SPADES("spades", ":spades:");

        private final String displayName;
        private final String emoticon;

        Face(String displayName, String emoticon) {
            this.displayName = displayName;
            this.emoticon = emoticon;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmoticon() {
            return emoticon;
        }

    }

    private Face face;
    private Value value;


    public Card(Face face, Value value){
        this.face = face;
        this.value = value;
    }

    public Face getFace() {
        return this.face;
    }

    public Value getValue(){
        return this.value;
    }

    public String toString(){
        return String.format("%s of %s", value.displayName, face.displayName);
    }

    public String getEmoticon(){
        return String.format("%s**%s**", face.emoticon, value.emoticon);
    }

}

