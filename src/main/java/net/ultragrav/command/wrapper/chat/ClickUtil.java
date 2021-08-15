package net.ultragrav.command.wrapper.chat;

import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.command.wrapper.player.UltraPlayer;

import java.util.function.Consumer;

public class ClickUtil {
    /**
     * Open a URL, this creates a client-side popup when clicked asking to open the link
     *
     * @param url URL
     * @return Click event
     */
    public static ClickEvent url(String url) {
        return new ClickEvent(ClickEvent.Action.OPEN_URL, url);
    }

    /**
     * Open a file, not that this file must be on the player's computer, for example a screenshot
     * Not usually a good choice to use server-side
     *
     * @param file File
     * @return Click event
     */
    public static ClickEvent file(String file) {
        return new ClickEvent(ClickEvent.Action.OPEN_FILE, file);
    }

    /**
     * Change the page of a book, no clue what it does in chat
     *
     * @param page Page
     * @return Click event
     */
    public static ClickEvent page(String page) {
        return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page);
    }

    /**
     * Run a command
     *
     * @param command Command
     * @return Click event
     */
    public static ClickEvent command(String command) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
    }

    /**
     * Suggest a command
     *
     * @param command Command
     * @return Click event
     */
    public static ClickEvent commandSuggest(String command) {
        return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
    }

    /**
     * Run a runnable
     * Expires after 60 seconds
     *
     * @param run Runnable
     * @return Click event
     */
    public static ClickEvent runnable(Consumer<UltraPlayer> run) {
        return command(Handler.getHandler().registerHandler(run));
    }

    /**
     * Run a runnable
     * Expires after a set amount of time
     *
     * @param run    Runnable
     * @param expiry Time until expiry (milliseconds)
     * @return Click event
     */
    public static ClickEvent runnable(Consumer<UltraPlayer> run, long expiry) {
        return command(Handler.getHandler().registerHandler(run, expiry));
    }

    /**
     * Run a runnable
     * Expires after a set amount of time
     *
     * @param run    Runnable
     * @param expiry Time until expiry (milliseconds)
     * @param uses Max uses
     * @return Click event
     */
    public static ClickEvent runnable(Consumer<UltraPlayer> run, long expiry, int uses) {
        return command(Handler.getHandler().registerHandler(run, expiry, uses));
    }
}
