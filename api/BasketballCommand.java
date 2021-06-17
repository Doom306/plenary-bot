package com.general_hello.commands.commands.api;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BasketballCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://livescore6.p.rapidapi.com/matches/v2/list-live?Category=basketball"))
                .header("x-rapidapi-key", "91742b6d5bmsha1815c0de8d2462p139460jsncdd90a928fcf")
                .header("x-rapidapi-host", "livescore6.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        try {
            JSONObject obj = new JSONObject(response.body());

            JSONArray arr = obj.getJSONArray("Stages");

            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Games").setColor(Color.cyan).setThumbnail(ctx.getAuthor().getAvatarUrl()).setFooter("Type the Game ID of the game you want to see the live score of!!!!");

            for (int i = 0; i < arr.length(); i++) {
                JSONArray post_id = arr.getJSONObject(i).getJSONArray("Events");
                for (int lol = 0; lol < post_id.length(); lol++) {
                    JSONObject a = post_id.getJSONObject(lol);
                    embedBuilder.addField((lol + 1) + ".) ID OF GAME", a.getString("Eid"), false);
                    embedBuilder.addField("Team 1 ", a.getJSONArray("T1").getJSONObject(0).getString("Nm"), false);
                    embedBuilder.addField("Team 2 ", a.getJSONArray("T2").getJSONObject(0).getString("Nm"), false);
                    embedBuilder.addBlankField(false);
                }
            }

            ctx.getChannel().sendMessage(embedBuilder.build()).queue();

            ApiData.waitBasketball.put(ctx.getMember(), true);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("No games currently found!!!").queue();
        }
    }

    @Override
    public String getName() {
        return "basketball";
    }

    @Override
    public String getHelp(String preifx) {
        return "Shows the basketball live scores\n" +
                "Usage: `" + preifx + "basketball`";
    }
}
