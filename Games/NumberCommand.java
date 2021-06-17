package com.general_hello.commands.commands.Games;


import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.Emoji.Emoji;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import com.general_hello.commands.commands.Utils.UtilNum;

public class NumberCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final long guildID = ctx.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);

        if(ctx.getArgs().isEmpty()) ctx.getChannel().sendMessage(getHelp(prefix)).queue();
            //Number Counter
        else if("count".equals(ctx.getArgs().get(0)) | "cunt".equals(ctx.getArgs().get(0)) | "c".equals(ctx.getArgs().get(0)))
        {
            if(ctx.getArgs().size() == 1) //Default count from 1 to 4
            {
                ctx.getChannel().sendMessage("Counting from 1 to 4...").queue();
                int from = 1;
                int to = 4;
                while(from != to+1){ //Counting
                    ctx.getChannel().sendMessage(from + "\n").complete();
                    from ++;
                }
            }

            else //Count from sepcified range
            {
                try {
                    Long from = Long.parseLong(ctx.getArgs().get(1));
                    Long to = Long.parseLong(ctx.getArgs().get(2));

                    if(to < from)
                    {
                        Long temp = to;
                        to = from;
                        from = temp;
                    }

                    if("count".equals(ctx.getArgs().get(0)))
                        ctx.getChannel().sendMessage("Counting...").queue();
                    else
                        ctx.getChannel().sendMessage("Counting...").queue();

                    while(!from.equals(to+1)){ //Counting
                        ctx.getChannel().sendMessage(from + "\n").queue();
                        from ++;
                    }
                } catch (NumberFormatException nfe) {
                    ctx.getChannel().sendMessage(Emoji.ERROR + " Please enter two valid integers.").queue();
                } catch (ArrayIndexOutOfBoundsException aioe) {
                    ctx.getChannel().sendMessage(Emoji.ERROR + " Please enter two integer with a space in between.").queue();
                }
            }
        }

        //Random Number Generator and Dice Roller
        else if("random".equals(ctx.getArgs().get(0)) | "roll".equals(ctx.getArgs().get(0)) | "r".equals(ctx.getArgs().get(0)))
        {
            try {
                int num;

                if("roll".equals(ctx.getArgs().get(0))) //Roll the dice
                {
                    num = UtilNum.randomNum(1, 6);
                    String number = numToEmoji(num);
                    ctx.getChannel().sendMessage( "🎲 Dice Rolled: " + number).queue();
                }

                else if(ctx.getArgs().size() == 1) //Default random range 1~100
                {
                    num = UtilNum.randomNum(1, 100);
                    String number = stringToEmoji(num + "");
                    ctx.getChannel().sendMessage(Emoji.NUMBER + " Random Number generated: " + number
                            + "\nBy default range `0~100`").queue();
                }

                else //Set range
                {
                    Integer low = Integer.parseInt(ctx.getArgs().get(1));
                    Integer high = Integer.parseInt(ctx.getArgs().get(2));

                    int numlong = UtilNum.randomNum(high, low);
                    String number = stringToEmoji(numlong + "");

                    ctx.getChannel().sendMessage(Emoji.NUMBER + " Random Number generated: " + number
                            + "\nBy specified range ` " + low + "~" + high + "`").queue();
                }

            } catch (NumberFormatException nfe) {
                ctx.getChannel().sendMessage(Emoji.ERROR + " Please enter valid numbers.").queue();
            } catch (ArrayIndexOutOfBoundsException aiobe) {
                ctx.getChannel().sendMessage(Emoji.ERROR + " Please enter two numbers.").queue();
            }
        }

        //Coin Flip
        else if("coinflip".equals(ctx.getArgs().get(0)) | "cf".equals(ctx.getArgs().get(0)))
        {
            if (Math.random() < 0.5) ctx.getChannel().sendMessage(Emoji.UP + " Head!").queue();
            else ctx.getChannel().sendMessage(Emoji.DOWN + " Tail!").queue();
        }
    }

    @Override
    public String getName() {
        return "number";
    }

    @Override
    public String getHelp(String prefix) {
        return "This command is for Number tools.\n"
                + "Command Usage: `" + prefix + "number`,\n"
                + "Parameter: `count [from] [to] | random [from] [to] | roll | coinflip`\n"
                + "Roll: Dice roller, generates random number between 1 and 6.";
    }

    public static String numToEmoji(int digit)
    {
        String output = "";
        switch(digit) {
            case 1:
                output += Emoji.ONE;
                break;
            case 2:
                output += Emoji.TWO;
                break;
            case 3:
                output += Emoji.THREE;
                break;
            case 4:
                output += Emoji.FOUR;
                break;
            case 5:
                output += Emoji.FIVE;
                break;
            case 6:
                output += Emoji.SIX;
                break;
            case 7:
                output += Emoji.SEVEN;
                break;
            case 8:
                output += Emoji.EIGHT;
                break;
            case 9:
                output += Emoji.NINE;
                break;
            default:
                output += Emoji.ZERO;
                break;
        }

        return output;
    }

    public static String stringToEmoji(String input)
    {
        String output = "";
        for (int i = 0; i < input.length(); i++)
        {
            String letters = input.substring(i,i+1);
            char letterc = input.charAt(i);

            /*
             * Number More than ONE digit
             */
            //1234
            if(input.length() >= i+4 && "1234".equals(input.substring(i,i+4))) {
                output += Emoji.NUMBER;
                i+=3;
                continue;
            }
            //100
            if(input.length() >= i+3 && "100".equals(input.substring(i,i+3))) {
                output += Emoji.HUNDRED;
                i+=2;
                continue;
            }

            /*
             * Character more than ONE digit
             */
            //ABCD or ABCD
            if(input.length() >= i+4 && "abcd".equalsIgnoreCase(input.substring(i,i+4))) {
                output += Emoji.ABCD;
                i+=3;
                continue;
            }
            //ABC or ABC
            else if(input.length() >= i+3 && "abc".equalsIgnoreCase(input.substring(i,i+3))) {
                output += Emoji.ABC;
                i+=2;
                continue;
            }
            if(input.length() >= i+2 && "vs".equalsIgnoreCase(input.substring(i,i+2))) {
                output += Emoji.VS;
                i+=1;
                continue;
            }
            if(input.length() >= i+5 && "music".equalsIgnoreCase(input.substring(i,i+5))) {
                output += Emoji.NOTES;
                i+=4;
                continue;
            }
            if(input.length() >= i+4 && "cool".equalsIgnoreCase(input.substring(i,i+4))) {
                output += Emoji.COOL;
                i+=3;
                continue;
            }
            if(input.length() >= i+3 && "new".equalsIgnoreCase(input.substring(i,i+3))) {
                output += Emoji.NEW_WORD;
                i+=2;
                continue;
            }
            if(input.length() >= i+4 && "free".equalsIgnoreCase(input.substring(i,i+4))) {
                output += Emoji.FREE;
                i+=3;
                continue;
            }
            if(input.length() >= i+2 && "ok".equalsIgnoreCase(input.substring(i,i+2))) {
                output += Emoji.OK;
                i+=1;
                continue;
            }
            if(input.length() >= i+2 && ".n".equalsIgnoreCase(input.substring(i,i+2))) {
                output += "\n";
                i+=1;
                continue;
            }

            /*
             * Check One Letter at a Time
             */
            if(Character.isAlphabetic(letterc)) {
                output += lettersToEmoji(letters);
            }
            else if(Character.isDigit(letterc)) {
                output += numToEmoji(Integer.parseInt(letters));
            }
            //Spacing
            else if(Character.isWhitespace(letterc)) {
                output += " ";
            }
            else if(!Character.isAlphabetic(letterc)) {
                switch(letters) {
                    case ".":
                        output += Emoji.DOT;
                        break;
                    case "?":
                        output += Emoji.MARK_QUESTION;
                        break;
                    case "!":
                        output += Emoji.MARK_EXCLAMATION;
                        break;
                    case "#":
                        output += Emoji.MARK_HASH;
                        break;
                    case "*":
                        output += Emoji.MARK_ASTERISK;
                        break;
                    case "&":
                    case "%":
                        output += Emoji.SYMBOLS;
                        break;
                    case "+":
                        output += Emoji.MARK_PLUS_SIGN;
                        break;
                    case "-":
                        output += Emoji.MARK_MINUS_SIGN;
                        break;
                    case "/":
                        output += Emoji.MARK_DEVIDE_SIGN;
                        break;
                    default:
                        break;
                }
            }
        }
        return output;
    }

    public static String lettersToEmoji(String input)
    {
        String output = ":regional_indicator_" + input.toLowerCase() + ":";
        return output;
    }
}
