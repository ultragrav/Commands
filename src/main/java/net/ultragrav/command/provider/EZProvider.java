package net.ultragrav.command.provider;

import lombok.NonNull;
import lombok.Setter;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EZProvider<T> extends UltraProvider<T> {
    private String name;
    private Function<UltraSender, ? extends Collection<String>> lister;
    private BiFunction<String, UltraSender, T> converter;

    @Setter
    private boolean caseCorrect = true;

    public EZProvider(String name, Function<UltraSender, ? extends Collection<String>> lister, BiFunction<String, UltraSender, T> converter) {
        this.name = name;
        this.lister = lister;
        this.converter = converter;
    }

    public EZProvider(String name, Function<UltraSender, Map<String, T>> method) {
        this.name = name;
        this.lister = pl -> method.apply(pl).keySet();
        this.converter = (str, pl) -> method.apply(pl).get(str);
    }

    public EZProvider(String name, Map<String, T> map) {
        this.name = name;
        this.lister = pl -> map.keySet();
        this.converter = (str, pl) -> map.get(str);
    }

    @Override
    public T convert(String toConvert, UltraSender sender) throws CommandException {
        T t = converter.apply(toConvert, sender);
        if (t == null) {
            if (caseCorrect) {
                Collection<String> poss = lister.apply(sender);
                boolean found = false;
                for (String str : poss) {
                    if (toConvert.equalsIgnoreCase(str)) {
                        toConvert = str;
                        found = true;
                        break;
                    }
                }
                if (found)
                    t = converter.apply(toConvert, sender);
                if (t == null) {
                    throw new CommandException("&cInvalid " + name + ": " + toConvert);
                }
            }
        }
        return t;
    }

    @Override
    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        toComplete = toComplete.toLowerCase();
        Collection<String> poss = lister.apply(sender);
        List<String> ret = new ArrayList<>();
        for (String str : poss) {
            if (str.toLowerCase().startsWith(toComplete))
                ret.add(str);
        }
        return ret;
    }

    @Override
    public String getArgumentDescription() {
        return name;
    }

    public EZProvider<T> caseCorrect(boolean b) {
        this.caseCorrect = b;
        return this;
    }
}
