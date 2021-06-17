package com.general_hello.commands.commands;

import java.io.IOException;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws InterruptedException, IOException;

    String getName();

    String getHelp(String prefix);

    default List<String> getAliases() { return List.of(); }
}
