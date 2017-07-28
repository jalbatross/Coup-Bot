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
    
    private static final Map<CardType, Action> actionMap = Collections.unmodifiableMap(initializeActionMapping());
    private static final Map<CardType, Reaction> reactionMap = Collections.unmodifiableMap(initializeReactionMapping());
    private static Map<CardType,Action> initializeActionMapping() {
        Map<CardType, Action> myMap = new HashMap<CardType, Action>();
        myMap.put(DUKE, Action.TAX);
        myMap.put(AMBASSADOR, Action.EXCHANGE);
        myMap.put(ASSASSIN, Action.ASSASSINATE);
        myMap.put(CAPTAIN, Action.STEAL);
        
        return myMap;
    }
    
    private static Map<CardType,Reaction> initializeReactionMapping() {
        Map<CardType, Reaction> myMap = new HashMap<CardType, Reaction>();
        myMap.put(CONTESSA, Reaction.BLOCK_ASSASSINATE);
        myMap.put(CAPTAIN, Reaction.BLOCK_STEAL);
        myMap.put(DUKE, Reaction.BLOCK_FOREIGN_AID);
        myMap.put(AMBASSADOR, Reaction.BLOCK_STEAL);
        
        return myMap;
    }
    
    public static Action action(CardType type) {
        return actionMap.get(type);
    }

    public static Reaction reaction(CardType type) {
        return reactionMap.get(type);
    }
}
