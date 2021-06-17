package com.general_hello.commands;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.Blackjack.GameHandler;
import com.general_hello.commands.commands.Giveaway.Giveaway;
import com.general_hello.commands.commands.Giveaway.RaffleData;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.PrefixStoring;
import com.general_hello.commands.commands.Pro.ProData;
import com.general_hello.commands.commands.ReactionRole.ReactionRoleData;
import com.general_hello.commands.commands.Settings.SettingsData;
import com.general_hello.commands.commands.Uno.ImageHandler;
import com.general_hello.commands.commands.Uno.UnoGame;
import com.general_hello.commands.commands.Uno.UnoHand;
import com.general_hello.commands.commands.Utils.UtilString;
import com.general_hello.commands.commands.Verify.VerifyData;
import com.general_hello.commands.commands.api.ApiData;
import com.general_hello.commands.commands.api.BasketBallContinue;
import com.general_hello.commands.commands.trivia.TriviaCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.general_hello.commands.commands.Utils.UtilBot.getStatusEmoji;

public class Listener extends ListenerAdapter {
    private final CommandManager manager;
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    public static HashMap<String, Integer> count = new HashMap<>();
    private boolean onetime = true;
    private boolean onetime1 = true;
    private static OffsetDateTime timeDisconnected = OffsetDateTime.now();
    public static JDA jda;

    public Listener(EventWaiter waiter) {
        manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onReconnected(@NotNull ReconnectedEvent event) {
        LOGGER.info("{} is reconnected!! Response number {}", event.getJDA().getSelfUser().getAsTag(), event.getResponseNumber());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final String useGuildSpecificSettingsInstead = String.format("Thank you for adding %s to %s!!!\n" +
                        "\nTo know about this bot feel free to type **+about**\n" +
                        "You can change the prefix by typing **+setprefix [prefix]**\n" +
                        "To know more about a command type **+help [command name]**",
                event.getJDA().getSelfUser().getAsMention(), event.getGuild().getName());

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Hello!").setDescription(useGuildSpecificSettingsInstead);
        event.getJDA().getUserById(Config.get("owner_id")).openPrivateChannel().complete().sendMessage("Someone added me to " + event.getGuild().getName()).queue();
        event.getJDA().getUserById(Config.get("owner_id")).openPrivateChannel().complete().sendMessage("Invite link is " + event.getGuild().getDefaultChannel().retrieveInvites().complete().get(0).getUrl()).queue();
        SettingsData.antiRobServer.put(event.getGuild(), false);
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public void onResumed(@NotNull ResumedEvent event)  {
        TextChannel guildChannelById = event.getJDA().getTextChannelById(852338750519640116L);
        EmbedBuilder em = new EmbedBuilder().setColor(Color.RED).setTitle("🔴 Disconnected");
        em.setDescription("The bot disconnected for " +
                (OffsetDateTime.now().getHour() - timeDisconnected.getHour())  + " hour(s) " +
                (OffsetDateTime.now().getMinute() - timeDisconnected.getMinute()) + " minute(s) " +
                (OffsetDateTime.now().getSecond() - timeDisconnected.getSecond()) + " second(s) and " +
                (timeDisconnected.getNano() /1000000) + " milliseconds due to connectivity issues!\n" +
                "Response number: " + event.getResponseNumber()).setTimestamp(OffsetDateTime.now());
        guildChannelById.sendMessage(em.build()).queue();
        User owner_id = event.getJDA().getUserById(Config.get("owner_id"));
        owner_id.openPrivateChannel().complete().sendMessage(em.build()).queue();
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        timeDisconnected = event.getTimeDisconnected();
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println("Shut downed the bot at " +
                event.getTimeShutdown().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+ " due to maintenance.\n" +
                "With response number of " + event.getResponseNumber() + "\n" +
                "With the code of " + event.getCloseCode().getCode() + "\n");
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (ReactionRoleData.messages.contains(event.getMessageIdLong())) {
            final Role role = ReactionRoleData.roles.get(event.getMessageIdLong());
            final User user = event.getUser();
            if (!user.isBot()) {
                event.getGuild().addRoleToMember(user.getIdLong(), role).queue();
            }
        }

        if (Giveaway.messageHashMap.containsKey(event.getMessageIdLong())) {
            final Message message = Giveaway.messageHashMap.get(event.getMessageIdLong());
            final Role req = Giveaway.requirements.get(message);

            if (event.getUser() != event.getJDA().getSelfUser()) {
                if (event.getReactionEmote().getName().equals("\uD83C\uDF89") && !event.getUser().isBot()) {
                    if (req != null) {
                        if (event.getMember().getRoles().contains(req)) {
                            ArrayList<Member> members = Giveaway.membersArrayListHashMap.get(message);
                            members.add(event.getMember());
                            Giveaway.membersArrayListHashMap.remove(message);
                            Giveaway.membersArrayListHashMap.put(message, members);
                        } else {
                            event.getReaction().removeReaction(event.getUser()).queue();
                            event.getUser().openPrivateChannel().complete().sendMessage("You lack the " + req.getName() + " role in " + event.getGuild().getName() + " to join the giveaway!!!!").queue();
                        }
                    } else {
                        ArrayList<Member> members = Giveaway.membersArrayListHashMap.get(message);
                        members.add(event.getMember());
                        Giveaway.membersArrayListHashMap.remove(message);
                        Giveaway.membersArrayListHashMap.put(message, members);
                    }
                }

                if (ReactionRoleData.messages.contains(event.getMessageIdLong())) {
                    final Role role = ReactionRoleData.roles.get(event.getMessageIdLong());
                    final User user = event.getUser();
                    if (!user.isBot()) {
                        event.getGuild().addRoleToMember(user.getIdLong(), role).queue();
                    }
                }

                if (!event.getUser().isBot()) handleUnoReaction(event.getMember(), event.retrieveMessage().complete(), event.getReactionEmote());
            }
        }

        if (!event.getUser().equals(event.getJDA().getSelfUser()))
            handleUnoReaction(event.getMember(), event.retrieveMessage().complete(), event.getReactionEmote());
    }

    public void handleUnoReaction(Member member, Message message, MessageReaction.ReactionEmote emoji) {
        Guild guild = message.getGuild();
        UnoGame unoGame = GameHandler.getUnoGame(guild.getIdLong());
        if (emoji.isEmoji() && unoGame != null && message.getIdLong() == unoGame.getMessageID()) {
            ArrayList<UnoHand> hands = unoGame.getHands();
            switch (emoji.getEmoji()) {
                case "▶️":
                    if (unoGame.getStarter() == member.getIdLong() && unoGame.getTurn() == -1) {
                        int turn = unoGame.start();
                        if (turn != -1) {
                            guild.createCategory("Uno")
                                    .addMemberPermissionOverride(guild.getSelfMember().getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                                    .addRolePermissionOverride(guild.getIdLong(), Collections.emptyList(), Collections.singletonList(Permission.VIEW_CHANNEL)).queue(category -> {
                                unoGame.setCategory(category.getIdLong());
                                guild.modifyCategoryPositions().selectPosition(category.getPosition()).moveTo(Math.min(guild.getCategories().size() - 1, 2)).queue();
                                for (UnoHand hand : hands) {
                                    category.createTextChannel(String.format("%s-uno", hand.getPlayerName()))
                                            .addMemberPermissionOverride(hand.getPlayerId(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                                            .addMemberPermissionOverride(guild.getSelfMember().getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), Collections.emptyList())
                                            .addRolePermissionOverride(guild.getIdLong(), Collections.emptyList(), Collections.singletonList(Permission.VIEW_CHANNEL)).setTopic("Run !help to show which commands you can use").queue(channel -> {
                                        channel.sendFile(ImageHandler.getCardsImage(hand.getCards()), "hand.png").embed(unoGame.createEmbed(hand.getPlayerId()).setColor(guild.getSelfMember().getColor()).build()).queue(mes -> {
                                            hand.setChannelId(channel.getIdLong());
                                            hand.setMessageId(mes.getIdLong());
                                        });
                                    });
                                }
                            });
                        } else {
                            message.getChannel().sendMessage("Not enough people!").queue();
                            message.removeReaction(emoji.getEmoji(), member.getUser()).queue();
                        }
                    }
                    break;
                case "❌":
                    if (unoGame.getStarter() == member.getIdLong()) {
                        for (long channelId : unoGame.getHands().stream().map(UnoHand::getChannelId).collect(Collectors.toList())) {
                            if (channelId != -1) guild.getTextChannelById(channelId).delete().queue();
                        }
                        if (unoGame.getTurn() != -1) {
                            guild.getCategoryById(unoGame.getCategory()).delete().queue();
                        }
                        MessageEmbed me = message.getEmbeds().get(0);
                        EmbedBuilder eb = new EmbedBuilder(me);
                        eb.setTitle("The game of uno has been canceled");
                        message.editMessage(eb.build()).queue();
                        GameHandler.removeUnoGame(guild.getIdLong());
                        message.delete().queueAfter(20, TimeUnit.SECONDS);
                    }
                    break;
                case "\uD83D\uDD90️":
                    if (unoGame.getTurn() == -1 && !hands.stream().map(UnoHand::getPlayerId).collect(Collectors.toList()).contains(member.getIdLong())) {
                        unoGame.addPlayer(member.getIdLong(), member.getEffectiveName());
                        MessageEmbed me = message.getEmbeds().get(0);
                        EmbedBuilder eb = new EmbedBuilder(me);
                        eb.clearFields();
                        MessageEmbed.Field f = me.getFields().get(0);
                        StringBuilder sb = new StringBuilder();
                        for (String name : hands.stream().map(UnoHand::getPlayerName).collect(Collectors.toList())) {
                            sb.append(name);
                            sb.append("\n");
                        }
                        eb.addField(f.getName(), sb.toString().trim(), false);
                        message.editMessage(eb.build()).queue();
                    }
                    break;
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        EmbedBuilder em = new EmbedBuilder().setColor(Color.YELLOW).setAuthor("Message from: " + event.getAuthor().getAsTag(), null, event.getAuthor().getAvatarUrl());
        String inviteLink;

        try {
            inviteLink = event.getGuild().retrieveInvites().complete().get(0).getUrl();
        } catch (Exception e) {
            inviteLink = "https://discord.com/404";
        }

        if (event.getChannel() != event.getJDA().getTextChannelById("854187935104237599")) {
            em.setDescription("Message: " + event.getMessage().getContentRaw() + "\n" +
                    "Sent in the channel ***" +
                    event.getChannel().getName() + "*** in ***" +
                    event.getGuild().getName() + "***\n" +
                    "Invite link of the server [here](" + inviteLink + ")");

            event.getJDA().getTextChannelById("854187935104237599").sendMessage(em.build()).queue();
        }

        final long guildID = event.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (event.getAuthor().getId().equals(Config.get("owner_id"))) {
            if(event.getMessage().getContentRaw().equals(prefix + "load")) {
                event.getAuthor().openPrivateChannel().complete().sendMessage("Successfully loaded the bot!").queue();
                store(event);
            }
        }

        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            if (!MoneyData.money.containsKey(event.getJDA().getSelfUser())) {
                RaffleData.count.add(0);
                MoneyData.money.put(event.getMessage().getAuthor(), 2000000d);
                ProData.isPro.put(event.getMessage().getAuthor(), true);
                MoneyData.moneyGoalProgress.put(event.getMessage().getAuthor(), 0d);
                MoneyData.goal.put(event.getMessage().getAuthor(), 5000d);
                MoneyData.users.add(event.getAuthor());
                MoneyData.robGoalProgress.put(event.getMessage().getAuthor(), 0d);
                MoneyData.bank.put(event.getMessage().getAuthor(), 100000d);
                MoneyData.robGoal.put(event.getMessage().getAuthor(), 2000d);
                LOGGER.info("Legendary Bot added 20,000 to " + event.getMessage().getAuthor().getAsMention() + " ...");
                return;
            }
        }


        if (event.getAuthor().getId().equals(Config.get("owner_id_partner"))) {
            if (onetime1) {
                MoneyData.money.put(event.getMessage().getAuthor(), 20000000d);
                onetime1 = false;
                ProData.isPro.put(event.getMessage().getAuthor(), false);
                MoneyData.bank.put(event.getMessage().getAuthor(), 0d);
                MoneyData.moneyGoalProgress.put(event.getMessage().getAuthor(), 0d);
                MoneyData.goal.put(event.getMessage().getAuthor(), 5000d);
                MoneyData.users.add(event.getAuthor());
                MoneyData.robGoalProgress.put(event.getMessage().getAuthor(), 0d);
                MoneyData.robGoal.put(event.getMessage().getAuthor(), 2000d);
                MoneyData.timeRob.put(event.getAuthor(), LocalDateTime.now());
                MoneyData.timeBankRob.put(event.getAuthor(), LocalDateTime.now());
                double amount = MoneyData.money.get(event.getAuthor());
                LOGGER.info("Legendary Bot added " + amount + " to " + event.getMessage().getAuthor().getAsMention() + " ...");
                return;
            }
        }

        if (!MoneyData.money.containsKey(event.getAuthor())) {
            MoneyData.users.add(event.getAuthor());
            MoneyData.goal.put(event.getMessage().getAuthor(), 5000d);
            MoneyData.money.put(event.getMessage().getAuthor(), 500d);
            MoneyData.moneyGoalProgress.put(event.getMessage().getAuthor(), 0d);
            MoneyData.bank.put(event.getMessage().getAuthor(), 0d);
            MoneyData.robGoalProgress.put(event.getMessage().getAuthor(), 0d);
            MoneyData.robGoal.put(event.getMessage().getAuthor(), 1000d);
            ProData.isPro.put(event.getMessage().getAuthor(), false);
            MoneyData.timeRob.put(event.getAuthor(), LocalDateTime.now());
            SettingsData.pingForPrefix.put(event.getAuthor(), true);
            MoneyData.timeBankRob.put(event.getAuthor(), LocalDateTime.now());

            LOGGER.info("Legendary Bot added 1,000 to " + event.getMessage().getAuthor().getAsMention() + " ...");
        }

        if (ApiData.waitBasketball.containsKey(event.getMember())) {
            if (ApiData.waitBasketball.get(event.getMember())) {
                try {
                    BasketBallContinue.basketball(event);
                } catch (IOException | InterruptedException e) {
                    event.getChannel().sendMessage("An error occurred!!! Kindly try again and make sure you chose the correct Game ID!!!").queue();
                }
            }
        }

        try {
            if (SettingsData.antiRobServer.get(event.getGuild())) {
                if (event.getMessage().getContentRaw().startsWith("pls rob") || event.getMessage().getContentRaw().startsWith(prefix + "rob")) {
                    EmbedBuilder messageBuilder = new EmbedBuilder().setTitle("Warning!!!").setThumbnail(event.getAuthor().getAvatarUrl()).setColor(Color.RED);
                    String a = "false";

                    if (event.getAuthor().isBot()) {
                        a = "true";
                    }

                    String name, id, dis, nickname, status, statusEmoji, game, join, register;

                    User user = event.getAuthor();
                    Member member = event.getMember();

                    /* Identity */
                    name = user.getName();
                    id = user.getId();
                    dis = user.getDiscriminator();
                    nickname = member == null || member.getNickname() == null ? "N/A" : member.getEffectiveName();

                    /* Status */
                    OnlineStatus stat = member == null ? null : member.getOnlineStatus();
                    status = stat == null ? "N?A" : UtilString.VariableToString("_", stat.getKey());
                    statusEmoji = stat == null ? "" : getStatusEmoji(stat);
                    game = stat == null ? "N/A" : member.getActivities().isEmpty() ? "N/A" : member.getActivities().get(0).getName();

                    /* Time */
                    join = member == null ? "N?A" : UtilString.formatOffsetDateTime(member.getTimeJoined());
                    register = UtilString.formatOffsetDateTime(user.getTimeCreated());

                    messageBuilder.addField("***ALERT*** Someone is try to rob!!!!", "Details below.", false);

                    messageBuilder.addField("Identity", "ID `" + id + "`\n" +
                            "Nickname `" + nickname + "` | Discriminator `" + dis + "`", false);

                    messageBuilder.addField("Status", "🎮 " + " `" + game + "`\n"
                            + statusEmoji + " `" + status + "`\n", true);
                    messageBuilder.addField("Is bot?", a, true);

                    messageBuilder.addField("⌚ " + "Time", "Join - \n`" + join + "`\n" +
                            "Register `" + register + "`\n", true);
                    messageBuilder.addField("Name", name, false);
                    messageBuilder.addField("Message", event.getMessage().getContentRaw(), true);


                    event.getChannel().sendMessage(messageBuilder.build()).queue();
                    return;
                }
            }
        } catch (Exception ignored) {}

        if (!MoneyData.users.contains(event.getAuthor())) {
            MoneyData.users.add(event.getAuthor());
        }

        if (TriviaCommand.storeUser.contains(event.getAuthor())) {
            final String answer = event.getMessage().getContentRaw();
            if (answer.toLowerCase().contains(TriviaCommand.storeAnswer.get(event.getAuthor()).toLowerCase())) {
                event.getChannel().sendMessage("Correct answer!!!!\n" +
                        "You got \uD83E\uDE99 500 for getting the correct answer").queue();
                final Double money = MoneyData.money.get(event.getAuthor());
                MoneyData.money.put(event.getAuthor(), money + 500);
            } else {
                EmbedBuilder e = new EmbedBuilder();
                e.setTitle("Incorrect answer");
                e.setFooter("A correct answer gives you \uD83E\uDE99 500");
                e.addField("The correct answer is " + TriviaCommand.storeAnswer.get(event.getAuthor()).toUpperCase(), "Better luck next time", false);
                event.getChannel().sendMessage(e.build()).queue();
            }
            TriviaCommand.storeUser.remove(event.getAuthor());
            TriviaCommand.storeAnswer.remove(event.getAuthor());
        }

        try {
            if (SettingsData.pingForPrefix.get(event.getAuthor())) {
                if (event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser())) {
                    event.getChannel().sendMessage("Psst. Check your **DMS** for the prefix of this bot").queue();
                    event.getMessage().getAuthor().openPrivateChannel().complete().sendMessage("The prefix for this bot is `" + prefix + "`").queue();
                }
            }
        } catch (Exception e) {
            SettingsData.pingForPrefix.put(event.getAuthor(), true);
        }

        jda = event.getJDA();

        if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id"))) {
            shutdown(event, true);
            return;
        } else if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id_partner"))) {
            shutdown(event, false);
            return;
        }

        if (raw.startsWith(prefix)) {
            try {
                manager.handle(event, prefix);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String commandsCount() {
        int x = 0;
        int size = CommandManager.commandNames.size();
        StringBuilder result = new StringBuilder();

        while (x < size) {
            String commandName = CommandManager.commandNames.get(x);
            result.append(x+1).append(".) ").append(commandName).append(" - ").append(count.get(commandName)).append("\n");
            x++;
        }

        return String.valueOf(result);
    }

    public static void shutdown(GuildMessageReceivedEvent event, boolean isOwner) {
        LOGGER.info("The bot " + event.getAuthor().getAsMention() + " is shutting down.\n" +
                "Thank you for using General_Hello's Code!!!");

        event.getChannel().sendMessage("Shutting down...").queue();
        event.getChannel().sendMessage("Bot successfully shutdown!!!!").queue();
        EmbedBuilder em = new EmbedBuilder().setTitle("Shutdown details!!!!").setColor(Color.red).setFooter("Shutdown on ").setTimestamp(LocalDateTime.now());
        em.addField("Shutdown made by ", event.getAuthor().getName(), false);
        em.addField("Date", LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getDayOfMonth() + "/" + LocalDateTime.now().getYear(), false);
        em.addField("Total number of Commands used in this session....", CommandManager.commandNames.size() + " commands", false);
        em.addField("List of Commands used in this session....", commandsCount(), false);
        event.getAuthor().openPrivateChannel().complete().sendMessage(em.build()).queue();

        if (!isOwner) {
            User owner = event.getJDA().retrieveUserById(Config.get("owner_id")).complete();
            owner.openPrivateChannel().complete().sendMessage(em.build()).queue();
        }

        dataPlace(event);

        event.getJDA().shutdown();
        BotCommons.shutdown(event.getJDA());
    }

    public static void store(GuildMessageReceivedEvent event) {
        TextChannel channelMoneyData = event.getJDA().getTextChannelById(847615054677147708L);
        TextChannel log = event.getJDA().getTextChannelById(844713439271321610L);
        TextChannel channelMoneyBankData = event.getJDA().getTextChannelById(847629286704807976L);
        TextChannel goalData = event.getJDA().getTextChannelById(847630333691166772L);
        TextChannel robGoal = event.getJDA().getTextChannelById(847631955267682344L);
        TextChannel robGoalProgress = event.getJDA().getTextChannelById(847632769596129290L);
        TextChannel moneyGoalProgress = event.getJDA().getTextChannelById(847633201760829510L);
        TextChannel userData = event.getJDA().getTextChannelById(847640488164851733L);
        TextChannel count = event.getJDA().getTextChannelById(849125262620360734L);
        TextChannel verifyData = event.getJDA().getTextChannelById(850184517800034395L);
        TextChannel reactData = event.getJDA().getTextChannelById(851656329281929216L);
        TextChannel proData = event.getJDA().getTextChannelById(851662907132608543L);
        TextChannel count1 = event.getJDA().getTextChannelById(852373588580368414L);
        TextChannel count2 = event.getJDA().getTextChannelById(852376330787356713L);
        TextChannel settingsData = event.getJDA().getTextChannelById(852378011704492043L);
        TextChannel settingsData1 = event.getJDA().getTextChannelById(852379719558627408L);
        TextChannel count3 = event.getJDA().getTextChannelById(852382778452607006L);

        count.getHistory().retrievePast(1).queue(messages -> {
            int num = Integer.parseInt(messages.get(0).getContentRaw());

            int x = 0;
            do {
                int finalX = x;
                channelMoneyData.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //money
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.money.put(user, Double.valueOf(split[1]));
                    log.sendMessage("Stored money of " + user.getAsTag() + " which is 💲" + split[1]).queue();
                });

                channelMoneyBankData.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //bank money
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.bank.put(user, Double.valueOf(split[1]));
                    log.sendMessage("Stored bank money of " + user.getAsTag() + " which is 💲" + split[1]).queue();
                });

                goalData.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //goal data
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.goal.put(user, Double.valueOf(split[1]));
                    log.sendMessage("Stored deposit goal data of " + user.getAsTag() + " which is 💲" + split[1]).queue();
                });

                robGoal.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //rob goal
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.robGoal.put(user, Double.valueOf(split[1]));
                    log.sendMessage("Stored goal of robbing of " + user.getAsTag() + " which is 💲" + split[1]).queue();
                });

                robGoalProgress.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //rob goal progress
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.robGoalProgress.put(user, Double.valueOf(split[1]));
                    log.sendMessage("Stored the progress of the money robbed of " + user.getAsTag() + " which is 💲" + split[1]).queue();
                });

                moneyGoalProgress.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //money deposit goal progress
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.moneyGoalProgress.put(user, Double.valueOf(split[1]));
                    log.sendMessage("Stored the progress of depositing money in the bank of " + user.getAsTag() + " which is 💲" + split[1]).queue();
                });

                userData.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //users
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().retrieveUserById(split[0]).complete();
                    MoneyData.users.add(user);
                    log.sendMessage("Stored the user with the tag of " + user.getAsTag() + " and the id of " + split[1]).queue();
                });

                proData.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //users
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User user = event.getJDA().getUserById(split[0]);
                    boolean isPro = Boolean.parseBoolean(split[1]);
                    ProData.isPro.put(user, isPro);
                    //here
                    log.sendMessage("Stored the Pro data of the user " + user.getAsTag() + " with the Pro data of " + isPro).queue();
                });

                settingsData.getHistory().retrievePast(num).queue(uwuMessages -> {
                    //users
                    String contentRaw = uwuMessages.get(finalX).getContentRaw();
                    String[] split = contentRaw.split("-");
                    User userById = event.getJDA().getUserById(split[0]);
                    boolean isPfp = Boolean.parseBoolean(split[1]);
                    SettingsData.pingForPrefix.put(userById, isPfp);
                    //here
                    log.sendMessage("Stored the Settings data of ping for prefix for the user " + userById.getAsTag() + " with the Settings data of " + isPfp).queue();
                });

                //others
                x++;
            } while (x < num);
        });

        count1.getHistory().retrievePast(1).queue(msg -> {
            int anum = Integer.parseInt(msg.get(0).getContentRaw());

            int y = 0;

            while (y < anum) {
                int finalY = y;
                verifyData.getHistory().retrievePast(anum).queue(uwuMessages -> {
                    //users
                    String contentRaw = uwuMessages.get(finalY).getContentRaw();
                    String[] split = contentRaw.split("-");
                    Role role = event.getJDA().getRoleById(split[1]);
                    Guild guild = event.getJDA().getGuildById(split[0]);
                    VerifyData.role.put(guild, role);
                    VerifyData.message.put(guild, split[2]);
                    VerifyData.channel.put(guild, event.getJDA().getTextChannelById(split[3]));
                    //here
                    log.sendMessage("Stored the verification data of " + guild.getName() + " with the role of " + role.getName() + " and the message of " + split[2]).queue();
                });

                y++;
            }
        });

        count2.getHistory().retrievePast(1).queue(msg -> {
            int anum = Integer.parseInt(msg.get(0).getContentRaw());

            int y = 0;

            while (y < anum) {
                int finalY = y;

                reactData.getHistory().retrievePast(anum).queue(uwuMessages -> {
                    //users
                    String contentRaw = uwuMessages.get(finalY).getContentRaw();
                    String[] split = contentRaw.split("-");
                    Long messageId = Long.valueOf(split[0]);
                    Role roleById = event.getJDA().getRoleById(split[1]);
                    ReactionRoleData.messages.add(messageId);
                    ReactionRoleData.roles.put(messageId, roleById);
                    //here
                    log.sendMessage("Stored the Reaction role data of the message id " + messageId + " with the role of " + roleById.getName()).queue();
                });

                y++;
            }
        });

        count3.getHistory().retrievePast(1).queue(msg -> {
            int anum = Integer.parseInt(msg.get(0).getContentRaw());

            int y = 0;

            while (y < anum) {
                int finalY = y;
                settingsData1.getHistory().retrievePast(anum).queue(uwuMessages -> {
                    //users
                    String contentRaw = uwuMessages.get(finalY).getContentRaw();
                    String[] split = contentRaw.split("-");
                    Guild guild = event.getJDA().getGuildById(split[0]);
                    boolean isAntiRob = Boolean.parseBoolean(split[1]);
                    SettingsData.guilds.add(guild);
                    SettingsData.antiRobServer.put(guild, isAntiRob);
                    //here
                    log.sendMessage("Stored the settings data of " + guild.getName() + " with value of " + isAntiRob + " for Anti-Rob").queue();
                });

                y++;
            }
        });
    }

    public static void dataPlace(GuildMessageReceivedEvent event) {
        TextChannel channelMoneyData = event.getJDA().getTextChannelById(847615054677147708L);
        TextChannel log = event.getJDA().getTextChannelById(844713439271321610L);
        TextChannel channelMoneyBankData = event.getJDA().getTextChannelById(847629286704807976L);
        TextChannel goalData = event.getJDA().getTextChannelById(847630333691166772L);
        TextChannel robGoal = event.getJDA().getTextChannelById(847631955267682344L);
        TextChannel robGoalProgress = event.getJDA().getTextChannelById(847632769596129290L);
        TextChannel moneyGoalProgress = event.getJDA().getTextChannelById(847633201760829510L);
        TextChannel userData = event.getJDA().getTextChannelById(847640488164851733L);
        TextChannel count = event.getJDA().getTextChannelById(849125262620360734L);
        TextChannel verifyData = event.getJDA().getTextChannelById(850184517800034395L);
        TextChannel reactData = event.getJDA().getTextChannelById(851656329281929216L);
        TextChannel proData = event.getJDA().getTextChannelById(851662907132608543L);
        TextChannel count1 = event.getJDA().getTextChannelById(852373588580368414L);
        TextChannel count2 = event.getJDA().getTextChannelById(852376330787356713L);
        TextChannel settingsData = event.getJDA().getTextChannelById(852378011704492043L);
        TextChannel settingsData1 = event.getJDA().getTextChannelById(852379719558627408L);
        TextChannel count3 = event.getJDA().getTextChannelById(852382778452607006L);

        int x = 0;

        //currency
         do {
             User user = MoneyData.users.get(x);

             //money
             Double unknow = MoneyData.money.get(user);
             channelMoneyData.sendMessage(user.getId() + "-" + unknow).queue();
             log.sendMessage("Stored money of " + user.getAsTag() + " which is 💲" + unknow).queue();

             //bank money
             unknow = MoneyData.bank.get(user);
             channelMoneyBankData.sendMessage(user.getId() + "-" + unknow).queue();
             log.sendMessage("Stored bank money of " + user.getAsTag() + " which is 💲" + unknow).queue();

             //goal data
             unknow = MoneyData.goal.get(user);
             goalData.sendMessage(user.getId() + "-" + unknow).queue();
             log.sendMessage("Stored deposit goal data of " + user.getAsTag() + " which is 💲" + unknow).queue();

             //rob goal
             unknow = MoneyData.robGoal.get(user);
             robGoal.sendMessage(user.getId() + "-" + unknow).queue();
             log.sendMessage("Stored goal of robbing of " + user.getAsTag() + " which is 💲" + unknow).queue();

             //rob goal progress
             unknow = MoneyData.robGoalProgress.get(user);
             robGoalProgress.sendMessage(user.getId() + "-" + unknow).queue();
             log.sendMessage("Stored the progress of the money robbed of " + user.getAsTag() + " which is 💲" + unknow).queue();

             //money deposit goal progress
             unknow = MoneyData.moneyGoalProgress.get(user);
             moneyGoalProgress.sendMessage(user.getId() + "-" + unknow).queue();
             log.sendMessage("Stored the progress of depositing money in the bank of " + user.getAsTag() + " which is 💲" + unknow).queue();

             //users
             User lol = MoneyData.users.get(x);
             userData.sendMessage(lol.getId()).queue();
             log.sendMessage("Stored the user with the tag of " + user.getAsTag() + " and the id of " + unknow).queue();

             //others
             Boolean isPro = ProData.isPro.get(user);
             proData.sendMessage(user.getId() + "-" + isPro).queue();

             Boolean pfp = SettingsData.pingForPrefix.get(user);
             settingsData.sendMessage(user.getId() + "-" + pfp).queue();

             x++;
        } while (x < MoneyData.users.size());

         count.sendMessage(String.valueOf(MoneyData.users.size())).queue();

         x = 0;

         while (x < VerifyData.guilds.size()) {
             Guild guild = VerifyData.guilds.get(x);
             verifyData.sendMessage(guild.getId() + "-" + VerifyData.role.get(guild).getId() + "-" + VerifyData.message.get(guild) + "-" + VerifyData.channel.get(guild).getId()).queue();
             x++;
         }
         count1.sendMessage(String.valueOf(VerifyData.guilds.size())).queue();
         x = 0;

         while (x < ReactionRoleData.messages.size()) {
            Long messageId = ReactionRoleData.messages.get(x);
            Role role = ReactionRoleData.roles.get(messageId);
            reactData.sendMessage(messageId + "-" + role.getId()).queue();
            x++;
        }

         count2.sendMessage(String.valueOf(ReactionRoleData.messages.size())).queue();

         x = 0;
         while (x < SettingsData.guilds.size()) {
            Guild guild = SettingsData.guilds.get(x);
            settingsData1.sendMessage(guild.getId() + "-" + SettingsData.antiRobServer.get(guild)).queue();
            x++;
        }

         count3.sendMessage(String.valueOf(SettingsData.guilds.size())).queue();
    }
}