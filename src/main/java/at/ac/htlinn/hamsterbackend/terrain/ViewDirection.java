package at.ac.htlinn.hamsterbackend.terrain;

import java.util.HashMap;
import java.util.Map;

public enum ViewDirection {
    NORTH, EAST, SOUTH, WEST;
    public static final Map<ViewDirection, Integer> viewDirectionIntegerMap = new HashMap<ViewDirection, Integer>() {{
        put(NORTH, 0);
        put(EAST, 1);
        put(SOUTH, 2);
        put(WEST,3);
    }};
};

