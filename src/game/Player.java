package game;

public class Player {
    
    protected Card[] hand;
    
    public Player() {
        hand = new Card[2];
    }
    
    public void setHand(Card card1, Card card2) {
        hand[0] = card1;
        hand[1] = card2;
    }
    
    public String handString() {
        return new String(hand[0].influence.toString() + ", " + hand[1].influence.toString());
    }
    
    public void exchangeCard(int index, Card card) {
        
    }
}
