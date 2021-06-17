package com.general_hello.commands.commands.Settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsData {
    public static HashMap<Guild, Boolean> antiRobServer = new HashMap<>();
    public static HashMap<User, Boolean> pingForPrefix = new HashMap<>();
    public static ArrayList<Guild> guilds = new ArrayList<>();
}
