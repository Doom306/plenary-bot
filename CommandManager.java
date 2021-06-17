package com.general_hello.commands;

import com.general_hello.commands.commands.Blackjack.*;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.DefaultCommands.BugCommand;
import com.general_hello.commands.commands.DefaultCommands.PingCommand;
import com.general_hello.commands.commands.DefaultCommands.*;
import com.general_hello.commands.commands.Emoji.EmojiCommand;
import com.general_hello.commands.commands.Fonts.AsciiCommand;
import com.general_hello.commands.commands.Games.*;
import com.general_hello.commands.commands.Giveaway.*;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Info.AboutCommand;
import com.general_hello.commands.commands.Info.InfoServerCommand;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.general_hello.commands.commands.Math.MathCommand;
import com.general_hello.commands.commands.Money.*;
import com.general_hello.commands.commands.MusicCommands.*;
import com.general_hello.commands.commands.Others.*;
import com.general_hello.commands.commands.Poll.PollCommand;
import com.general_hello.commands.commands.Pro.GiveProCommand;
import com.general_hello.commands.commands.Pro.ProCommand;
import com.general_hello.commands.commands.ReactionRole.ReactionRoleCommand;
import com.general_hello.commands.commands.Settings.SettingsCommand;
import com.general_hello.commands.commands.Uno.ChallengeCommand;
import com.general_hello.commands.commands.Uno.DrawCommand;
import com.general_hello.commands.commands.Uno.PlayCardCommand;
import com.general_hello.commands.commands.Uno.UnoCommand;
import com.general_hello.commands.commands.Verify.SetMessageCommand;
import com.general_hello.commands.commands.Verify.SetRoleCommand;
import com.general_hello.commands.commands.Verify.VerifyChannel;
import com.general_hello.commands.commands.Verify.VerifyCommand;
import com.general_hello.commands.commands.api.BasketballCommand;
import com.general_hello.commands.commands.trivia.TriviaCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    public static ArrayList<String> commandNames = new ArrayList<>();
    public CommandManager(EventWaiter waiter) {

        //Default Commands
        addCommand(new ServerCountCommand());
        addCommand(new ServerListCommand());
        addCommand(new BugCommand());
        addCommand(new SetPrefixCommand());

        //Song Commands
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new PlayCommand());
        addCommand(new RepeatCommand());
        addCommand(new SkipCommand());
        addCommand(new StopCommand());
        addCommand(new HelpCommand(this));
        addCommand(new MinuteCommand());
        addCommand(new HourlyCommand());
        addCommand(new DailyCommand());
        addCommand(new QueueCommand());
        addCommand(new ProCommand());
        addCommand(new PauseCommand());
        addCommand(new SettingsCommand());
        addCommand(new BalanceCommand());
        addCommand(new GiveProCommand());
        addCommand(new WithdrawCommand());
        addCommand(new ShareCommand());
        addCommand(new SetRoleCommand());
        addCommand(new Monthly());
        addCommand(new StealCommand());
        addCommand(new RPSCommand());
        addCommand(new RobCommand());
        addCommand(new EmojiCommand());
        addCommand(new NumberCommand());
        addCommand(new HangManCommand());
        addCommand(new SetVolumeCommand());
        addCommand(new GuessNumberCommand());
        addCommand(new BankCommand());
        GameHandler gameHandler = new GameHandler();
        addCommand(new BlackjackCommand(gameHandler));
        addCommand(new PingCommand());
        addCommand(new StartGiveawayCommand());
        addCommand(new RerollCommand());
        addCommand(new AboutCommand(Color.cyan, "A discord bot that has moderation, currency, and game commands!", Permission.EMPTY_PERMISSIONS));
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new AsciiCommand());
        addCommand(new SplitCommand());
        addCommand(new DoubleDownCommand());
        addCommand(new HitCommand());
        addCommand(new PruneCommand());
        addCommand(new LockChannelCommand());
        addCommand(new AvatarCommand());
        addCommand(new StandCommand());
        addCommand(new ChallengeCommand());
        addCommand(new DrawCommand(gameHandler));
        addCommand(new RemoveUserRaffleCommand());
        addCommand(new PlayCardCommand(gameHandler));
        addCommand(new UnoCommand(gameHandler));
        addCommand(new DeathWishCommand());
        addCommand(new ReactionRoleCommand());
        addCommand(new Clear());
        addCommand(new TriviaCommand());
        addCommand(new UnLockChannelCommand());
        addCommand(new Hack());
        addCommand(new VerifyCommand());
        addCommand(new VerifyChannel());
        addCommand(new SetMessageCommand());
        addCommand(new CodeCommand());
        addCommand(new PollCommand());
        addCommand(new BasketballCommand());
        addCommand(new EightballCommand());

        addCommand(new AddToServer());

        addCommand(new SetRaffleCount());
        addCommand(new AllowedRaffleCommand());
        addCommand(new WarnCommand());
        addCommand(new RaffleResultCommand());
        addCommand(new JoinRaffleCommand());
        addCommand(new KickCommand());
        addCommand(new BanCommand());
        addCommand(new MuteCommand());
        addCommand(new DeafenCommand());
        addCommand(new SoftBanCommand());
        addCommand(new AddRoleCommand());
        addCommand(new WebhookCommand());
        addCommand(new PasteCommand());
        addCommand(new InfoUserCommand());
        addCommand(new InfoServerCommand());
        addCommand(new SayCommand());
        addCommand(new MathCommand());

    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        System.out.println("Loaded the +" + cmd.getName() + " -> " + cmd.getClass());
        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present, " + cmd.getName() + " in " + cmd.getClass());
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }


    void handle(GuildMessageReceivedEvent event, String prefix) throws InterruptedException, IOException {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            if (!Listener.count.containsKey(invoke)) {
                Listener.count.put(invoke, 1);
                commandNames.add(invoke);
            } else {
                Integer lastCount = Listener.count.get(invoke);
                Listener.count.put(invoke, lastCount + 1);

                if (!commandNames.contains(invoke)) commandNames.add(invoke);
            }

            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);

        }
    }
}
