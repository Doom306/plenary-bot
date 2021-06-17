package com.general_hello.commands.commands.Uno;

public class UnoCard {

    public enum Color {
        RED("Red", "r"),
        BLUE("Blue", "b"),
        GREEN("Green", "g"),
        YELLOW("Yellow", "y");

        public String getName() {
            return name;
        }

        public String getToken() {
            return token;
        }

        private String name;
        private String token;

        Color(String name, String token){
            this.name = name;
            this.token = token;
        }
    }

    public enum Value{
        ZERO("zero", "0", 0),
        ONE("one", "1", 1),
        TWO("two", "2", 2),
        THREE("tree", "3", 3),
        FOUR("four", "4", 4),
        FIVE("five", "5", 5),
        SIX("six", "6", 6),
        SEVEN("seven", "7", 7),
        EIGHT("eight", "8", 8),
        NINE("nine", "9", 9),
        //TEN("ten", "10", 10),
        REVERSE("Reverse", "e", 20),
        PLUSTWO("PlusTwo", "t", 20),
        SKIP("Skip", "s", 20),
        PLUSFOUR("PlusFour", "f", 50),
        WILD("Wild", "w", 50);

        private String name;
        private String token;
        private int value;

        public String getName() {
            return name;
        }

        public String getToken() {
            return token;
        }

        public int getValue() {
            return value;
        }

        Value(String name, String token, int value){
            this.name = name;
            this.token = token;
            this.value = value;
        }
    }

    private Color color;
    private final Value value;

    public UnoCard(Color color, Value value){
        this.color = color;
        this.value = value;
    }

    public static UnoCard fromString(String card){
        Color color = null;
        Value value = null;
        card = card.toLowerCase();
        for (Color c : Color.values()){
            if (card.contains(c.getName().toLowerCase())){{
                if (color == null){
                    card = card.replaceFirst(c.getName().toLowerCase(), "");
                    color = c;
                } else {
                    return null;
                }
            }}
        }
        for (Value v : Value.values()){
            if (card.contains(v.getName().toLowerCase())){
                if (!((v == Value.FOUR || v == Value.TWO) && (card.contains(Value.PLUSTWO.getName().toLowerCase()) || card.contains(Value.PLUSFOUR.getName().toLowerCase())))){
                    if (value == null){
                        card = card.replaceFirst(v.getName().toLowerCase(), "");
                        value = v;
                    } else {
                        return null;
                    }
                }

            }
        }
        for (Color c : Color.values()){
            if (card.contains(c.getToken())){{
                if (color == null){
                    card = card.replaceFirst(c.getToken(), "");
                    color = c;
                } else {
                    return null;
                }
            }}
        }
        for (Value v : Value.values()){
            if (card.contains(v.getToken())){
                if (value == null){
                    card = card.replaceFirst(v.getToken(), "");
                    value = v;
                } else {
                    return null;
                }
            }
        }
        if (color == null || value == null || !card.equalsIgnoreCase("")) return null;
        return new UnoCard(color, value);


    }


    public boolean canBePlayed(UnoCard card){
        return card.value == Value.PLUSFOUR || card.value == Value.WILD || this.color == card.color || this.value == card.value;
    }

    public String toString(){
        return color.getName() + (value.getValue() < 10 ? value.getToken() : value.getName());
    }



    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof UnoCard)) return false;
        UnoCard othercard = (UnoCard) other;
        return (this.value == othercard.value && this.color == othercard.color) || (this.value == Value.WILD && othercard.value == Value.WILD) || (this.value == Value.PLUSFOUR && othercard.value == Value.PLUSFOUR);

    }


}
