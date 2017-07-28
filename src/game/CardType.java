package game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum CardType {
    DUKE,
    CONTESSA,
    AMBASSADOR,
    ASSASSIN,
    CAPTAIN;
    
    private static final Map<CardType, Action> map = Collections.unmodifiableMap(initializeMapping());
    
    private static Map initializeMapping() {
        Map<CardType, Action> myMap = new HashMap<CardType, Action>();
        myMap.put(DUKE, Action.TAX);
        myMap.put(AMBASSADOR, Action.EXCHANGE);
        myMap.put(ASSASSIN, Action.ASSASSINATE);
        myMap.put(CAPTAIN, Action.STEAL);
        
        return myMap;
    }
    
    public static Action action(CardType type) {
        return map.get(type);
    }
}
