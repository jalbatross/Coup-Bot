package game;

import java.util.ArrayList;

public class Player {
    
    protected Card[] hand;
    protected int coins;
    protected ArrayList<Action> actions;
    
    public Player() {
        hand = new Card[2];
        coins = 0;
        setPossibleActions();
    }
    
    public void setHand(Card card1, Card card2) {
        hand[0] = card1;
        hand[1] = card2;
    }
    
    public String handString() {
        String ret = hand[0] + ", " + hand[1];
        return ret;
    }
    
    /**
     * 
     * Exchanges a card from the hand of the player with another
     * card.
     * 
     * @param type         CardType to be extracted from hand
     * @param exchangeCard Card to be exchanged into the hand
     * @return             Card of CardType type that was exchanged
     *                     with exchangeCard
     * @throws Exception   No valid card in hand if no suitable card
     *                     was found
     */
    public Card exchangeCard(CardType type, Card exchangeCard) throws Exception {
        
        Card cardInHand = null;
        
        //Search the hand for a non-revealed card of CardType type
        for (int i = 0; i < 2; i++) {
            if (hand[i].influence == type && !hand[i].revealed()) {
                cardInHand = hand[i];
                hand[i] = exchangeCard;
            }
        }
        
        if (cardInHand == null) {
            throw new Exception("No valid card in hand!");
        }
                
        return cardInHand; 
    }
    
    public int coins() {
        return coins;
    }
    
    public String toString() {
        return new String(handString() + "\n" + 
        "Coins: " + coins +"\n" +
        "Actions: " + possibleActionsString() + "\n");
    }
    
    public boolean isAlive() {
        return !hand[0].revealed() || !hand[1].revealed();
    }
    
    public void setPossibleActions() {
        this.actions = new ArrayList<Action>();
        for (Action enumActions : Action.values()) {
            if (enumActions == Action.ASSASSINATE && coins < 3) {
                continue;
            }
            if (enumActions == Action.COUP && coins < 7) {
                continue;
            }
            
            this.actions.add(enumActions);
        }
    }
    
    public void setCoins(int num) {
        coins = num;
    }
    
    public String possibleActionsString() {
        String ret = "";
        for (int i = 0; i < actions.size(); i++) {
            if (i != actions.size() - 1) {
                ret += actions.get(i).toString() + ", ";
            }
            else {
                ret += actions.get(i).toString();
            }
        }
        
        return ret;
    }
    
    public Action performAction(Action anAction, Player opponent) throws Exception {
        if (!actions.contains(anAction)) {
            throw new IndexOutOfBoundsException();
        }
        
        switch(anAction) {
            case INCOME:
                //Do income
                break;
            case FOREIGN_AID:
                //Do foreign aid
                break;
            case COUP:
                //Do coup
                break;
            case TAX:
                //Do tax (Duke action)
                break;
            case ASSASSINATE:
                //Do assassinate
                break;
            case EXCHANGE:
                //Do exchange (Ambassador)
                break;
            case STEAL:
                //Do steal
                break;
                
        }
        return anAction;
    }
    
    public CardType getResponse() {
        return hand[0].influence;
    }

    public Card revealCard() {
        
        //TODO: Ask user which card they want to reveal
        hand[0].reveal();
        return hand[0];
        
    }
}
