package com.general_hello.commands.commands.DefaultCommands;

import com.general_hello.commands.CommandManager;
import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, (id) -> Config.get("prefix"));

        if (args.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Groups");
            embedBuilder.setColor(Color.cyan);
            embedBuilder.addField("🎲 | Games (5)","Fun, exciting games that you can play to earn money or just to have fun", false);
            embedBuilder.addField("🎶 | Music ***NEW***(11)", "Plays music straight out of spotify/youtube!", false);
            embedBuilder.addField("😀 | Fun (10)","Cool commands that you surely will like!", false);
            embedBuilder.addField("🚫 | Moderation (10)","Basic to advanced moderation tools used by staff to control or monitor the server.", false);
            embedBuilder.addField("❓ | Info (3)", "Shows basic to complex information about users, servers, or mods", false);
            embedBuilder.addField("🤖 | Bot (7)", "Shows basic to complex information about the bot", false);
            embedBuilder.addField("💵 | Currency (11)", "Earn money, be rich, and celebrate!", false);
            embedBuilder.addField("🎉 | Giveaway (2)", "Set up giveaways, re-roll giveaways, and much more!", false);
            embedBuilder.addField("♦ | Blackjack (5)", "Play blackjack with the bot!", false);
            embedBuilder.addField("🃏 | Uno (4)", "Play uno with your friends!", false);
            embedBuilder.addField("🎫 | Raffle (4)", "Make raffles, join raffles, and many more!", false);
            embedBuilder.addField("✅ | Verify (4)", "Let's user verify to join your awesome server!", false);

            embedBuilder.setFooter("Type " + prefix + "help [group name] to see their commands");


            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("fun")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedBuilder1 = new EmbedBuilder();
            embedBuilder.setTitle("Fun Commands");
            embedBuilder.setColor(Color.green);
            embedBuilder1.setColor(Color.green);
            embedBuilder1.addField("1.) Solve the Math Problem Command", "`" + prefix + "math`", false);
            embedBuilder.addField("2.) Meme Command","`" + prefix + "meme`", false);
            embedBuilder.addField("3.) Joke Command","`" + prefix + "joke`", false);
            embedBuilder.addField("4.) Font Command","`" + prefix + "font`", false);
            embedBuilder.addField("5.) Share Code Command","`" + prefix + "sharecode`", false);
            embedBuilder.addField("6.) Avatar Command","`" + prefix + "avatar`", false);
            embedBuilder1.addField("9.) Say Command", "`" + prefix + "say`", false);
            embedBuilder1.addField("10.) Emoji Command","`" + prefix + "emoji`", false);
            embedBuilder.addField("11.) Death wish Command`BETA`", "`" + prefix + "death`", false);
            embedBuilder.addField("12.) Basketball Scores Command`BETA`", "`" + prefix + "basketball`", false);
            embedBuilder.addField("13.) Fontify Command`BETA`", "`" + prefix + "fontify`", false);

            embedBuilder1.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            channel.sendMessage(embedBuilder1.build()).queue();
            return;
        }

        if (args.get(0).equals("music")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Music Commands");
            embedBuilder.setColor(Color.yellow);
            embedBuilder.addField("1.) Join Command","`" + prefix + "connect`", false);
            embedBuilder.addField("2.) Play Command","`" + prefix + "play`", false);
            embedBuilder.addField("3.) Stop Command","`" + prefix + "stop`", false);
            embedBuilder.addField("4.) Pause Command","`" + prefix + "pause`", false);
            embedBuilder.addField("5.) Now Playing Command","`" + prefix + "playinginfo`", false);
            embedBuilder.addField("6.) Repeat Command","`" + prefix + "repeat`", false);
            embedBuilder.addField("7.) Skip Command","`" + prefix + "skip`", false);
            embedBuilder.addField("8.) Leave Command","`" + prefix + "leave`", false);
            embedBuilder.addField("9.) Set volume Command ***Premium***","`" + prefix + "setvolume`", false);
            embedBuilder.addField("10.) Queue Command","`" + prefix + "queue`", false);
            embedBuilder.addField("11.) Pause Command ***Premium***","`" + prefix + "pause`", false);
            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("games")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Games Commands");
            embedBuilder.setColor(Color.ORANGE);
            embedBuilder.addField("1.) Hang man Command*\n","`" + prefix + "hm`", false);
            embedBuilder.addField("2.) Guess the number Command*","`" + prefix + "gn`", false);
            embedBuilder.addField("3.) Rock Paper Scissors Command*","`" + prefix + "rps`", false);
            embedBuilder.addField("4.) Number Command", "`" + prefix + "number`", false);
            embedBuilder.addField("5.) Trivia Command", "`" + prefix + "trivia`", false);
            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("giveaway")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Giveaway Commands");
            embedBuilder.setColor(Color.ORANGE);
            embedBuilder.addField("1.) Create Giveaway Command*","`" + prefix + "start`", false);
            embedBuilder.addField("2.) Re-roll Giveaway Winner Command*\n","`" + prefix + "reroll`", false);
            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("bj")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Blackjack Commands");
            embedBuilder.setColor(Color.PINK);
            embedBuilder.addField("1.) Start Blackjack Game Command","`" + prefix + "bj`", false);
            embedBuilder.addField("2.) Hit Command\n","`" + prefix + "hit`", false);
            embedBuilder.addField("3.) Stand Command\n","`" + prefix + "stand`", false);
            embedBuilder.addField("4.) Double Command\n","`" + prefix + "double`", false);
            embedBuilder.addField("5.) Split the deck Command\n","`" + prefix + "split`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }


        if (args.get(0).equalsIgnoreCase("raffle")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Raffle Commands");
            embedBuilder.setColor(Color.YELLOW);
            embedBuilder.addField("1.) Join Raffle Command","`" + prefix + "join`", false);
            embedBuilder.addField("2.) Raffle results Command `MODS`\n","`" + prefix + "result`", false);
            embedBuilder.addField("3.) Remove User from Raffle Command `MODS`\n","`" + prefix + "raffle`", false);
            embedBuilder.addField("4.) Allow User to Use Raffle Command `MODS`\n","`" + prefix + "allow`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("uno")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Uno Commands");
            embedBuilder.setColor(Color.blue);
            embedBuilder.addField("1.) Start Uno Game Command","`" + prefix + "startuno`", false);
            embedBuilder.addField("2.) Play Card Command\n","`" + prefix + "play`", false);
            embedBuilder.addField("3.) Draw Card Command\n","`" + prefix + "draw`", false);
            embedBuilder.addField("4.) Challenge the User Command `+4 Card`\n","`" + prefix + "double`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("info")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Information Commands");
            embedBuilder.setColor(Color.red);
            embedBuilder.addField("1.) User Information Command","`" + prefix + "profile`", false);
            embedBuilder.addField("2.) Server Information Command","`" + prefix + "serverinfo`", false);
            embedBuilder.addField("3.) Mod Information Command ","`" + prefix + "mods`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("bot")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("About the Bot Commands");
            embedBuilder.setColor(Color.blue);
            embedBuilder.addField("1.) Server List Command","`" + prefix + "serverlist`", false);
            embedBuilder.addField("2.) Server Count Command","`" + prefix + "server`", false);
            embedBuilder.addField("3.) Bug Command","`" + prefix + "bug`", false);
            embedBuilder.addField("4.) Setting Command ","`" + prefix + "settings`", false);
            embedBuilder.addField("5.) Upgrade to ***Premium*** Command ","`" + prefix + "premium`", false);
            embedBuilder.addField("6.) Set Prefix Command", "`" + prefix + "setprefix`", false);
            embedBuilder.addField("7.) Use Code Command", "`" + prefix + "code`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("verify")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("About the Verify Commands");
            embedBuilder.setColor(Color.blue);
            embedBuilder.addField("1.) Verify Command ","`" + prefix + "verify`", false);
            embedBuilder.addField("2.) Set Verify Message Command `Only for mods`", "`" + prefix + "verifymessage`", false);
            embedBuilder.addField("3.) Set Verify Role Command `Only for mods`", "`" + prefix + "verifyrole`", false);
            embedBuilder.addField("3.) Set Verify Role Channel `Only for mods` **PREMIUM***", "`" + prefix + "verifychannel`", false);

            embedBuilder.setFooter("\nType " + prefix + "help [command name] to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("mod") || args.get(0).equalsIgnoreCase("moderation")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilder embedBuilder1 = new EmbedBuilder();
            embedBuilder.setTitle("Moderation Commands");
            embedBuilder.setColor(Color.red);
            embedBuilder1.setColor(Color.red);
            embedBuilder.addField("1.) Kick Command", "`" + prefix + "kick`", false);
            embedBuilder.addField("2.) Ban Command", "`" + prefix + "ban`", false);
            embedBuilder.addField("3.) Mute Command", "`" + prefix + "mute`", false);
            embedBuilder.addField("4.) Deafen Command", "`" + prefix + "deafen`", false);
            embedBuilder.addField("5.) Clear Command", "`" + prefix + "clear`", false);
            embedBuilder.addField("6.) Delete Command", "`" + prefix + "delete`", false);
            embedBuilder1.addField("7.) Add Role Command", "`" + prefix + "role`", false);
            embedBuilder1.addField("8.) Reaction Role Command", "`" + prefix + "reactrole`", false);
            embedBuilder1.addField("9.) Add Poll Command", "`" + prefix + "setprefix`", false);
            embedBuilder1.addField("10.) Warn Members Command `Only for mods`", "`" + prefix + "warn`", false);


            embedBuilder1.setFooter("\nType `" + prefix + "help [command name]` to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            channel.sendMessage(embedBuilder1.build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("currency")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currency Commands");
            embedBuilder.setColor(Color.red);
            embedBuilder.addField("1.) Deposit Command", "`" + prefix + "dep`", false);
            embedBuilder.addField("2.) Balance Command", "`" + prefix + "bal`", false);
            embedBuilder.addField("3.) Bank Balance Command", "`" + prefix + "bank`", false);
            embedBuilder.addField("4.) Steal Command", "`" + prefix + "steal`", false);
            embedBuilder.addField("5.) Rob Command", "`" + prefix + "rob`", false);
            embedBuilder.addField("6.) Withdraw Command", "`" + prefix + "with`", false);
            embedBuilder.addField("7.) Hourly Command", "`" + prefix + "hourly`", false);
            embedBuilder.addField("8.) Weekly Command", "`" + prefix + "weekly`", false);
            embedBuilder.addField("9.) Daily Command", "`" + prefix + "daily`", false);
            embedBuilder.addField("10.) Monthly Command", "`" + prefix + "monthly`", false);
            embedBuilder.addField("11.) Hack Command","`" + prefix + "hack`", false);


            embedBuilder.setFooter("\nType `" + prefix + "help [command name]` to see what they do");

            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Nothing found for " + search).queue();
            return;
        }

        embedBuilder.setTitle("Help!!!");
        embedBuilder.setColor(Color.cyan);
        embedBuilder.addField(command.getHelp(prefix), "Try it now!", false);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        embedBuilder.setFooter("Time: " + dtf.format(now));
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows the list with commands in the bot\n" +
                "Usage: `" + prefix + "help [command]`";
    }

    @Override
    public List<String> getAliases() {
        List<String> strings = new java.util.ArrayList<>();
        strings.add("commands");
        strings.add("cmds");
        strings.add("commandlist");
        return strings;
    }
}
