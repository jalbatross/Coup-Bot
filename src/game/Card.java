package game;

import java.util.ArrayList;

public class Card {
    public CardType influence;
    private boolean revealed;
    
    public Card() {
        influence = null;
        revealed = false;
    }
    
    public Card(CardType type) {
        influence = type;
        revealed = false;
    }
    
    public void reveal() {
        revealed = true;
    }
    
    public boolean revealed() {
        return revealed;
    }
    
    public String toString() {
        String ret = new String();
        ret += influence.toString();
        ret += revealed ? " (revealed)" : " (hidden)";
        
        return ret;
    }
    
}
