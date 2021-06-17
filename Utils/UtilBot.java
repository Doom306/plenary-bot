package com.general_hello.commands.commands.Utils;

import com.general_hello.commands.commands.Emoji.Emoji;
import net.dv8tion.jda.api.OnlineStatus;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class UtilBot {

    /**
     * gt tje OnlineStatus Emoji
     * @param stat
     * @return
     */
    public static String getStatusEmoji(OnlineStatus stat)
    {
        String status = "";
        switch (stat) {
            case ONLINE:
                status = Emoji.GUILD_ONLINE;
                break;
            case IDLE:
                status = "🌙";
                break;
            case DO_NOT_DISTURB:
                status = "🔴";
                break;
            default:
                status = Emoji.GUILD_OFFLINE;
        }
        return status;
    }
}