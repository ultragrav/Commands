package net.ultragrav.command.wrapper.chat;

import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.wrapper.player.UltraPlayer;

import java.util.*;
import java.util.function.Consumer;

public class Handler {
    private static class HandlerCmd extends UltraCommand {
        private Handler parent;

        public HandlerCmd(Handler parent) {
            this.parent = parent;

            addAlias(cmdName);

            setAllowConsole(false);
            setRequirePermission(false);
        }

        @Override
        protected void perform() {
            if (getArgs().size() != 1) {
                return;
            }

            String str = getArgs().get(0);
            try {
                UUID id = UUID.fromString(str);
                if (parent.data.containsKey(id)) {
                    HandledElement el = parent.data.get(id);
                    el.used++;
                    el.run.accept(getPlayer());
                    parent.update();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static final String cmdName = "_";
    private static final long DEFAULT_EXPIRY = 60000;
    private static Handler instance;

    private final Map<UUID, HandledElement> data = new HashMap<>();

    /**
     * Do not call externally
     */
    private Handler() {
        new HandlerCmd(this).register();
    }

    public static Handler getHandler() {
        if (instance == null) {
            instance = new Handler();
        }
        return instance;
    }

    /**
     * Register a handler with default timeout
     *
     * @param run Executor
     * @return Handler ID
     */
    public String registerHandler(Consumer<UltraPlayer> run) {
        return registerHandler(run, DEFAULT_EXPIRY);
    }

    /**
     * Register a handler with a custom timeout
     *
     * @param run    Executor
     * @param expiry Timeout
     * @return Handler ID
     */
    public String registerHandler(Consumer<UltraPlayer> run, long expiry) {
        UUID genId = UUID.randomUUID();
        data.put(genId, new HandledElement(run, expiry));
        update();
        return cmdName + " " + genId;
    }

    /**
     * Register a handler with a custom timeout and use count
     *
     * @param run    Executor
     * @param expiry Timeout
     * @param uses   Use count
     * @return Handler ID
     */
    public String registerHandler(Consumer<UltraPlayer> run, long expiry, int uses) {
        UUID genId = UUID.randomUUID();
        data.put(genId, new HandledElement(run, expiry, uses));
        update();
        return cmdName + " " + genId;
    }

    private void update() {
        List<UUID> marked = new ArrayList<>();
        long t = System.currentTimeMillis();
        data.forEach((i, h) -> {
            if (h.expiry < t || (h.used >= h.uses && h.uses != -1)) {
                marked.add(i);
            }
        });
        for (UUID id : marked) {
            data.remove(id);
        }
    }

    private static class HandledElement {
        private final Consumer<UltraPlayer> run;
        private final long expiry;
        private final int uses;
        private int used = 0;

        public HandledElement(Consumer<UltraPlayer> run, long length) {
            this.run = run;
            this.uses = -1;
            this.expiry = System.currentTimeMillis() + length;
        }

        public HandledElement(Consumer<UltraPlayer> run, long length, int uses) {
            this.uses = uses;
            this.run = run;
            this.expiry = System.currentTimeMillis() + length;
        }
    }
}
