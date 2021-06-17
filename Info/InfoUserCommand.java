package com.general_hello.commands.commands.Info;


import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.Emoji.Emoji;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Money.MoneyData;
import com.general_hello.commands.commands.Pro.ProData;
import com.general_hello.commands.commands.Utils.UtilString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class InfoUserCommand implements ICommand {

    public final static String GUILD_ONLINE = Emoji.GUILD_ONLINE;
    public final static String GUILD_IDLE = Emoji.GUILD_IDLE;
    public final static String GUILD_DND = Emoji.GUILD_DND;
    public final static String GUILD_OFFLINE = Emoji.GUILD_OFFLINE;

    @Override
    public void handle(CommandContext ctx) {
        if(ctx.getArgs().isEmpty()) {
            embedUser(ctx.getAuthor(), ctx.getMember(), ctx.getEvent());
        } else {
            List<User> userMention = ctx.getMessage().getMentionedUsers();
            for(User user : userMention) {
                embedUser(user, ctx.getGuild().getMember(user), ctx.getEvent());
            }
        }
    }

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getHelp(String prefix) {
        return "Get info about the specified user\n" +
                "Usage: `" + prefix + "profile [mentioned member]`";
    }


    public static String getStatusEmoji(OnlineStatus stat)
    {
        String status;
        switch (stat) {
            case ONLINE:
                status = GUILD_ONLINE;
                break;
            case IDLE:
                status = GUILD_IDLE;
                break;
            case DO_NOT_DISTURB:
                status = GUILD_DND;
                break;
            case INVISIBLE:
            case OFFLINE:
                status = GUILD_OFFLINE;
                break;
            default:
                status = Emoji.GUILD_STREAMING;
        }
        return status;
    }

    public static Color randomColor() {
        Random colorpicker = new Random();
        int red = colorpicker.nextInt(255) + 1;
        int green = colorpicker.nextInt(255) + 1;
        int blue = colorpicker.nextInt(255) + 1;
        return new Color(red, green, blue);
    }

    private void embedUser(User user, Member member, GuildMessageReceivedEvent e) {
        String name, id, dis, nickname, icon, status, statusEmoji, game, join, register;

        if (!MoneyData.money.containsKey(user)) MoneyData.money.put(user, 50d);

        if (!MoneyData.bank.containsKey(user)) MoneyData.bank.put(user, 50d);

        if (!MoneyData.moneyGoalProgress.containsKey(user)) MoneyData.moneyGoalProgress.put(user, 0d);

        if (!MoneyData.robGoalProgress.containsKey(user)) MoneyData.robGoalProgress.put(user, 0d);

        icon = user.getEffectiveAvatarUrl();

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

        boolean b = ProData.isPro.containsKey(e.getAuthor());

        String isPro = "";

        if (b) {
            if (ProData.isPro.get(e.getAuthor())) isPro = " 🥇";
        }
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(name + isPro, null, icon)
                .setColor(randomColor()).setThumbnail(icon).setTimestamp(Instant.now())
                .setFooter("User Info", null);

        embed.addField("Identity", "ID `"+id+"`\n"+
                "Nickname `"+nickname+"` | Discriminator `"+dis+"`", true);

        embed.addField("Status", "🎮 "+" `"+game+"`\n"
                +statusEmoji+" `"+status+"`\n", true);

        embed.addField("⌚ "+"Time", "Join - \n`"+join+"`\n"+
                "Register `"+register+"`\n", true);

        DecimalFormat formatter = new DecimalFormat("#,###.00");

        embed.addField("Money", "Wallet - \n\uD83E\uDE99 " + formatter.format(MoneyData.money.get(user)) + "\n" +
                "Bank - \n\uD83E\uDE99 " + formatter.format(MoneyData.bank.get(user)), true);
        embed.addField("Achievements","Money deposited - \n\uD83E\uDE99 " + formatter.format(MoneyData.moneyGoalProgress.get(user)) +
                "\nMoney robbed - \n\uD83E\uDE99 " + formatter.format(MoneyData.robGoalProgress.get(user)) , true);

        e.getChannel().sendMessage(embed.build()).queue();
    }
}
