package com.general_hello.commands.commands.Verify;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;

public class VerifyData {
    public static HashMap<Guild, Role> role = new HashMap<>();
    public static HashMap<Guild, String> message = new HashMap<>();
    public static HashMap<Guild, TextChannel> channel = new HashMap<>();
    public static ArrayList<Guild> guilds = new ArrayList<>();
}
