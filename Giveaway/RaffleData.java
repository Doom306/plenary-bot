package com.general_hello.commands.commands.Giveaway;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.HashMap;

public class RaffleData {
    public static ArrayList<Integer> count = new ArrayList<>();
    public static HashMap<Guild, ArrayList<Member>> memberHashMap = new HashMap<>();
    public static HashMap<Member, ArrayList<Integer>> id = new HashMap<>();
    public static HashMap<Member, Boolean> allowed = new HashMap<>();
}
