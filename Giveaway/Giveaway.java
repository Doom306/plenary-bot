package com.general_hello.commands.commands.Giveaway;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Giveaway {

    int seconds;
    Message message;
    String item;
    Role req;
    public static HashMap<Long, Message> messageHashMap = new HashMap<>();
    public static HashMap<Message, ArrayList<Member>> membersArrayListHashMap = new HashMap<>();
    public static HashMap<Message, Role> requirements = new HashMap<>();


    public Giveaway(int time, Message message, String item, Role req) {
        seconds = time;
        this.message = message;
        this.item = item;
        this.req = req;
    }

    public void start() {
        new Thread(() -> {
            while (seconds > 5) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("\uD83C\uDF89  **GIVEAWAY!**  \uD83C\uDF89");
                embedBuilder.setFooter("Made by Wow #3153");
                String a = null;
                if (req != null) {
                    a = req.getName();
                }

                embedBuilder.addField(((item != null ? "\u25AB*`" + item + "`*\u25AB\n" : "") + "React with \uD83C\uDF89 to enter!\nRequirement " + a + "\nTime remaining: " + secondsToTime(seconds)), "", false);
                embedBuilder.setColor(Color.cyan);
                message.editMessage(embedBuilder.build()).queue();
                seconds -= 5;
                try {
                    Thread.sleep(5000);
                } catch (Exception ignored) {
                }
            }

            while (seconds > 0) {
                String a = null;
                if (req != null) {
                    a = req.getName();
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("\uD83C\uDF89  **GIVEAWAY!**  \uD83C\uDF89");
                embedBuilder.setFooter("Made by Wow #3153");
                embedBuilder.addField("LAST CHANCE TO ENTER!!!\n" + ((item != null ? "\u25AB*`" + item + "`*\u25AB\n" : "") + "React with \uD83C\uDF89 to enter!\nRequiremnt - " + a + "\n" + "Time remaining: " + secondsToTime(seconds)), "", false);
                embedBuilder.setColor(Color.red);
                message.editMessage(embedBuilder.build()).queue();
                seconds--;
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {
                }
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            ArrayList<Member> members = membersArrayListHashMap.get(message);

            if (members.isEmpty()) {
                embedBuilder.setTitle("🎉 **GIVEAWAY ENDED!** 🎉");
                embedBuilder.setColor(Color.RED);
                embedBuilder.addField("Winner: No one", "No one won the giveaway since no one joined", false);
                message.editMessage(embedBuilder.build()).queue();
                return;
            }

                String id = members.get((int) (Math.random() * members.size())).getId();
                embedBuilder.setTitle("🎉 **GIVEAWAY ENDED!** 🎉");
                embedBuilder.setColor(Color.green);
                embedBuilder.setDescription((item != null ? "\u25AB*`" + item + "`*\u25AB\n" : "") + "\nWinner: " + message.getJDA().retrieveUserById(id).complete().getName() + " \uD83C\uDF89");
                message.editMessage(embedBuilder.build()).queue();
                embedBuilder.clear();
                embedBuilder.setTitle("CONGRATULATIONS");
                embedBuilder.setColor(Color.green);
                embedBuilder.setDescription("Congratulations to " + message.getJDA().retrieveUserById(id).complete().getName() + "! You won" + (item == null ? "" : " the " + item) + "!");
                message.getChannel().sendMessage(embedBuilder.build()).queue();
        }).start();
    }

    public static String secondsToTime(long timeseconds) {
        StringBuilder builder = new StringBuilder();
        int years = (int) (timeseconds / (60 * 60 * 24 * 365));
        if (years > 0) {
            builder.append("**").append(years).append("** years, ");
            timeseconds = timeseconds % (60 * 60 * 24 * 365);
        }
        int weeks = (int) (timeseconds / (60 * 60 * 24 * 365));
        if (weeks > 0) {
            builder.append("**").append(weeks).append("** weeks, ");
            timeseconds = timeseconds % (60 * 60 * 24 * 7);
        }
        int days = (int) (timeseconds / (60 * 60 * 24));
        if (days > 0) {
            builder.append("**").append(days).append("** days, ");
            timeseconds = timeseconds % (60 * 60 * 24);
        }
        int hours = (int) (timeseconds / (60 * 60));
        if (hours > 0) {
            builder.append("**").append(hours).append("** hours, ");
            timeseconds = timeseconds % (60 * 60);
        }
        int minutes = (int) (timeseconds / (60));
        if (minutes > 0) {
            builder.append("**").append(minutes).append("** minutes, ");
            timeseconds = timeseconds % (60);
        }
        if (timeseconds > 0)
            builder.append("**").append(timeseconds).append("** seconds");
        String str = builder.toString();
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2);
        if (str.equals(""))
            str = "**No time**";
        return str;
    }
}
