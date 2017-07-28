package game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Action {
    INCOME(false),
    FOREIGN_AID(true),
    COUP(false),
    TAX(false),
    ASSASSINATE(true),
    EXCHANGE(false),
    STEAL(true);
    
    private final boolean blockable;
    private Action(final boolean blockable) {
        this.blockable = blockable;
    }
    
    private static final Map<Action, Reaction> reactionMap = Collections.unmodifiableMap(initializeActionMapping());
   
    private static Map<Action,Reaction> initializeActionMapping() {
        Map<Action, Reaction> myMap = new HashMap<Action, Reaction>();
        myMap.put(FOREIGN_AID, Reaction.BLOCK_FOREIGN_AID);
        myMap.put(ASSASSINATE, Reaction.BLOCK_ASSASSINATE);
        myMap.put(STEAL, Reaction.BLOCK_STEAL);
        
        return myMap;
    }
    
    public static Reaction correspondingReaction(Action anAction) {
        return reactionMap.get(anAction);
    }
    
    public boolean blockable() { return this.blockable;}
    
}
