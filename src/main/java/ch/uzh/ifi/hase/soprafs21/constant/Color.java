package ch.uzh.ifi.hase.soprafs21.constant;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Color {

    RED("RED"),
    BLUE("BLUE"),
    YELLOW("YELLOW"),
    GREEN("GREEN");

    Color(final String id){
        this.id = id;
    }

    private final String id;

    public String getId(){
        return id;
    }

    static final Map<String, Color> ids = Arrays.stream(Color.values()).collect(Collectors.toMap(Color::getId, Function.identity()));
    public static Color fromId (final String id){
        return ids.get(id);
    }

}
