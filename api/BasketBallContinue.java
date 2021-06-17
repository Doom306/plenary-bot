package com.general_hello.commands.commands.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class BasketBallContinue {
    public static void basketball (GuildMessageReceivedEvent ctx) throws IOException, InterruptedException {
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://livescore6.p.rapidapi.com/matches/v2/detail?Eid=" + ctx.getMessage().getContentRaw() + "&Category=basketball&LiveTable=false"))
                .header("x-rapidapi-key", "91742b6d5bmsha1815c0de8d2462p139460jsncdd90a928fcf")
                .header("x-rapidapi-host", "livescore6.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());

        try {
            JSONObject theObj = new JSONObject(response1.body());

            String team1 = theObj.getJSONArray("T1").getJSONObject(0).getString("Nm");
            String team2 = theObj.getJSONArray("T2").getJSONObject(0).getString("Nm");

            EmbedBuilder embedBuilder = new EmbedBuilder().setFooter(LocalDateTime.now().getDayOfWeek().name()).setThumbnail(ctx.getAuthor().getDefaultAvatarUrl()).setColor(Color.cyan).setTimestamp(LocalDateTime.now());

            embedBuilder.addField("Basketball Game!!!!", "Game id " + ctx.getMessage().getContentRaw(), false);
            embedBuilder.addField(team1 + " vs " + team2, "", false);
            embedBuilder.addField(team1 + " - " + theObj.getString("Tr1OR") + " ~~~~~ " + team2 + " - " + theObj.getString("Tr2OR"), "", false);
            embedBuilder.addBlankField(false);
            embedBuilder.addField(team1 + " scores:", "", false);
            try {
                embedBuilder.addField("First Quarter", theObj.getString("Tr1Q1"), false);
            } catch (JSONException e) {
                embedBuilder.addField("First Quarter", "NONE", false);
            }

            try {
                embedBuilder.addField("Second Quarter", theObj.getString("Tr1Q2"), false);
            } catch (JSONException e) {
                embedBuilder.addField("Second Quarter", "NONE", false);
            }

            try {
                embedBuilder.addField("Third Quarter", theObj.getString("Tr1Q3"), false);
            } catch (JSONException e) {
                embedBuilder.addField("Third Quarter", "NONE", false);
            }

            try {
                embedBuilder.addField("Fourth Quarter", theObj.getString("Tr1Q4"), false);
            } catch (JSONException e) {
                embedBuilder.addField("Fourth Quarter", "NONE", false);
            }

            embedBuilder.addBlankField(false);
            embedBuilder.addField(team2 + " scores:", "", false);

            try {
                embedBuilder.addField("First Quarter", theObj.getString("Tr2Q1"), false);
            } catch (JSONException e) {
                embedBuilder.addField("First Quarter", "NONE", false);
            }

            try {
                embedBuilder.addField("Second Quarter", theObj.getString("Tr2Q2"), false);
            } catch (JSONException e) {
                embedBuilder.addField("Second Quarter", "NONE", false);
            }

            try {
                embedBuilder.addField("Third Quarter", theObj.getString("Tr2Q3"), false);
            } catch (JSONException e) {
                embedBuilder.addField("Third Quarter", "NONE", false);
            }

            try {
                embedBuilder.addField("Fourth Quarter", theObj.getString("Tr2Q4"), false);
            } catch (JSONException e) {
                embedBuilder.addField("Fourth Quarter", "NONE", false);
            }

            ctx.getChannel().sendMessage(embedBuilder.build()).queue();
            ApiData.waitBasketball.put(ctx.getMember(), false);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("Game ID incorrect!!!").queue();
            ApiData.waitBasketball.put(ctx.getMember(), false);
        }
    }
}
