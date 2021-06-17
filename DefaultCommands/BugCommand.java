package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

public class BugCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("No information placed!!!").queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bug Form");
        embedBuilder.setFooter("We will contact you shortly");
        embedBuilder.addField("Kindly fill the form in the link below", Config.get("bug_link"), false);

        ctx.getChannel().sendMessage(embedBuilder.build()).queue();

        JDA jda = ctx.getJDA();
        User user = jda.retrieveUserById(Config.get("owner_id")).complete();
        user.openPrivateChannel().queue(PrivateChannel ->
                PrivateChannel.sendMessage("***New Bug Form by " + ctx.getAuthor().getAsMention() + "***\n"
                        + ctx.getArgs().get(0)
                        + "*** in ***" + ctx.getGuild().getName())
                        .queue());
        user = jda.retrieveUserById(Config.get("owner_id_partner")).complete();
        user.openPrivateChannel().queue(PrivateChannel ->
                PrivateChannel.sendMessage("***New Bug Form by " + ctx.getAuthor().getAsMention() + "***\n"
                        + ctx.getArgs().get(0)
                        + "*** in ***" + ctx.getGuild().getName())
                        .queue());
    }

    @Override
    public String getName() {
        return "bug";
    }

    @Override
    public String getHelp(String prefix) {
        return "Fill out a form to report a bug.\n" +
                "Usage: `" + prefix + "bug [info]`";
    }
}
