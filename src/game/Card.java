package game;

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
}
