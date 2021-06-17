package com.general_hello.commands.commands.Fonts;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class AsciiCommand implements ICommand {

    public static Color randomColor() {
        Random colorpicker = new Random();
        int red = colorpicker.nextInt(255) + 1;
        int green = colorpicker.nextInt(255) + 1;
        int blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }

    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().size() == 0) {
            ctx.getChannel().sendMessage(new EmbedBuilder().setColor(randomColor())
                    .setDescription("Here is a [**full list of Fonts**]("+WebGetter.asciiArtUrl+"fonts_list). They will be chosen randomly.").build()).queue();
        } else {
            String input = "";
            for (int i = 0; i < ctx.getArgs().size(); i++) {
                input += i == ctx.getArgs().size() - 1 ? ctx.getArgs().get(i) : ctx.getArgs().get(i) + " ";
            }

            List<String> fonts = WebGetter.getAsciiFonts();
            String font = fonts.get(UtilNum.randomNum(0, fonts.size() - 1));

            try {
                String ascii = WebGetter.getAsciiArt(input, font);

                if (ascii.length() > 1900) {
                    ctx.getChannel().sendMessage("```fix\n\nThe fontified text is too large ;-;```").queue();
                    return;
                }

                ctx.getChannel().sendMessage("**Font:** " + font + "\n```fix\n\n" + ascii + "```").queue();
            } catch (IllegalArgumentException iae) {
                ctx.getChannel().sendMessage("```fix\n\nYour text contains unknown characters!```").queue();
            }
        }
    }

    @Override
    public String getName() {
        return "fontify";
    }

    @Override
    public String getHelp(String prefix) {
        return "Turn plain text to Legendary text\n" +
                "Usage: `" + prefix + "font`\n" +
                "Parameter: [Text] | `none`\n" +
                "none: Get a list of fonts";
    }
}
