package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Player {
    
    protected Card[] hand;
    protected int coins;
    protected ArrayList<Action> actions;
    protected ArrayList<Reaction> reactions;
    public String name;
    
    public Player() {
        hand = new Card[2];
        coins = 0;
        setPossibleActions();
    }
    public Player(String _name) {
        hand = new Card[2];
        coins = 0;
        setPossibleActions();
        name = _name;
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
    
    public CardType getActionChallengeResponse(Action action) {
        
        System.out.println(name + ", your action of " + action + " is being challenged");
        System.out.println("Your hand: " + handString());
        
        System.out.println("You may reveal any card that is hidden");
        System.out.println("Enter 0 for the first card and 1 for the second");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine()) % 2;
        }
        catch (Exception e){
            System.out.println("User tried to do invalid action");
            choice = 0; 
        }
        
        if (hand[choice].revealed()) {
            choice = (choice + 1) % 2;
        }
        
        return hand[choice].influence;
    }
    
    public CardType getReactionChallengeResponse(Reaction reaction) {
        System.out.println(name + ", your reaction of " + reaction + " is being challenged");
        System.out.println("Your hand: " + handString());
        
        System.out.println("You may reveal any card that is hidden");
        System.out.println("Enter 0 for the first card and 1 for the second");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine()) % 2;
        }
        catch (Exception e){
            System.out.println("User tried to do invalid action");
            choice = 0; 
        }
        
        if (hand[choice].revealed()) {
            choice = (choice + 1) % 2;
        }
        
        return hand[choice].influence;
    }

    public Card revealCard() {
        System.out.println(name + ", you must reveal a card");
        System.out.println("Your hand: " + handString());
        
        System.out.println("You may reveal any card that is hidden");
        System.out.println("Enter 0 for the first card and 1 for the second");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine()) % 2;
        }
        catch (Exception e){
            System.out.println("User tried to do invalid action");
            choice = 0; 
        }
        
        if (hand[choice].revealed()) {
            choice = (choice + 1) % 2;
        }
        
        hand[choice].reveal();
        return hand[choice];
    }
    
    public Action getUserAction() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        showActionChoices();
        try {
            choice = Integer.parseInt(br.readLine()) % actions.size();
        }
        catch (Exception e){
            System.out.println("User tried to do invalid action");
            choice = 0; 
        }
        
        return actions.get(choice);
        
    }
    
    public Reaction getUserReaction() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        showReactionChoices();
        try {
            choice = Integer.parseInt(br.readLine()) % reactions.size();
        }
        catch (Exception e){
            System.out.println("User tried to do invalid action");
            choice = 0; 
        }
        
        return reactions.get(choice);
        
    }
    
    
    private void showActionChoices() {
        System.out.println("Action choices: ");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println(i + ": " + actions.get(i).toString());
        }
    }
    
    private void showReactionChoices() {
        System.out.println("Reaction choices: ");
        for (int i = 0; i < reactions.size(); i++) {
            System.out.println(i + ": " + reactions.get(i).toString());
        }
    }

    public boolean wantsChallengeReaction(Reaction aReaction) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        System.out.println("Would you like to challenge " + aReaction.toString() + "?");
        System.out.println("1 for YES, 0 for NO");
        
        try {
            choice = Integer.parseInt(br.readLine());
        }
        catch(Exception e){
            choice = 0;
        }
        
        return choice == 1 ? true: false;
    }

    public boolean wantsReaction(Action anAction) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice;
        
        System.out.println("Would you like to react to " + anAction.toString() + "?");
        System.out.println("1 for YES, 0 for NO");
        
        try {
            choice = Integer.parseInt(br.readLine());
        }
        catch(Exception e){
            choice = 0;
        }
        
        return choice == 1 ? true: false;
    }

    public void setPossibleReactions(Action anAction) {
        this.reactions = new ArrayList<Reaction>();
        reactions.add(Reaction.CHALLENGE);
        if (!anAction.blockable()) {
            return;
        }
        reactions.add(Action.correspondingReaction(anAction));
        
    }
    
    public void setBlockReaction(Action anAction) {
        this.reactions = new ArrayList<Reaction>();
        reactions.add(Action.correspondingReaction(anAction));
    }



}
