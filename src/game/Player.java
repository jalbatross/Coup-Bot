package game;

import java.io.BufferedReader;
import java.io.IOException;
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
        coins = 2;
        setPossibleActions();
    }
    public Player(String _name) {
        hand = new Card[2];
        coins = 2;
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
    
    public String hiddenCardsString() {
        String ret = "";
        for (int i = 0; i < 2; i++) {
            if (!hand[i].revealed()) {
                ret += i +": " + hand[i].toString() + "\n";
            }
        }
        
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
        System.out.println("Placing " + exchangeCard.toString() + " into the hand of " + name);
        
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
        if (coins >=10) {
            this.actions.add(Action.COUP);
            return;
        }
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
    
    /**
     * Performs the given action. Ambassador Exchange must be handled by the
     * Game object and is not implemented here. Assassin payment is assumed 
     * to be handled by the Game object.
     * 
     * @param anAction   An action to execute by an acting player
     * @param opponent   Acting player's possible target
     * @return           anAction
     * @throws Exception IndexOutOfBounds if the action is not possible
     */
    public Action performAction(Action anAction, Player opponent) throws Exception {
        if (!actions.contains(anAction)) {
            throw new IndexOutOfBoundsException();
        }
        
        switch(anAction) {
            case INCOME:
                coins++;
                break;
            case FOREIGN_AID:
                coins += 2;
                break;
            case COUP:
                coins -= 7;
                opponent.revealCard();
                break;
            case TAX:
                coins += 3;
                break;
            case ASSASSINATE:
                opponent.revealCard();
                break;
            case EXCHANGE:
                //Game takes care of this, no-op
                break;
            case STEAL:
                if (opponent.coins < 2) {
                    coins += opponent.coins;
                    opponent.coins = 0;
                }
                else {
                    coins +=2; 
                    opponent.coins -= 2;
                }
                break;
                
        }
        return anAction;
    }
    
    public CardType getActionChallengeResponse(Action action) {
        
        System.out.println(name + ", your action of " + action + " is being challenged");
        System.out.println("Reveal a card:\n" + hiddenCardsString());
        
        System.out.println("Enter a number corresponding to your choice");
        
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
        System.out.println("Reveal a card:\n" + hiddenCardsString());
        
        System.out.println("Enter a number corresponding to your choice");
        
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
        System.out.println("Your choices: " + hiddenCardsString());
        
        System.out.println("Enter a number corresponding to your choice");
        
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
    
    public Card revealCard(CardType type) throws Exception {
        Card revealed = null;
        for (int i = 0; i < 2; i++) {
            if (hand[i].influence == type && !hand[i].revealed()) {
                hand[i].reveal();
                revealed = hand[i];
                //Only want one card revealed
                break;
            }
        }
        if (revealed == null) {
            throw new Exception("Couldn't find card to reveal!");
        }
        return revealed;
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
    
    /**
     * No-op; intended for use by AI subclasses 
     */
    public void updateRevealedCounter(Card revealedCard) {
        
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
        
        //All actions can be challenged except Foreign Aid
        if (anAction != Action.FOREIGN_AID) {
            reactions.add(Reaction.CHALLENGE);
        }
        
        //If there are no blocks possible, finish adding reactions
        if (!anAction.blockable()) {
            return;
        }
        
        //Otherwise add the correct reaction based on the action
        reactions.add(Action.correspondingReaction(anAction));
        
    }
    
    public void setBlockReaction(Action anAction) {
        this.reactions = new ArrayList<Reaction>();
        reactions.add(Action.correspondingReaction(anAction));
    }
    
    /**
     * Returns an array of the two cards which the player who
     * used Ambassador decided not to choose.
     * @param card1   First card from the deck
     * @param card2   Second card from the deck
     * @return        Two cards which Player does not choose
     */
    public Card[] ambassadorExchange(Card card1, Card card2) {
        System.out.println("Your hand: " + handString());
        System.out.println("Cards from the deck: " + card1.toString() 
        + ", " + card2.toString());
        
        int exchangeNum = numHiddenCards();
        
        System.out.println("Choose any " + exchangeNum + " cards to keep. "
                + "The rest will be placed back into the deck.");
        
        if (exchangeNum == 2) {
        System.out.println("Enter your choice as two numbers separated by"
                + " a comma, for instance 0,1 or 2,1");
        }
        else {
            System.out.println("Enter your choice");
        }
        
        ArrayList<Card> choices = new ArrayList<Card>();
        for (int i = 0; i < 2; i++) {
            if (!hand[i].revealed()) {
                choices.add(hand[i]);
            }
        }
        choices.add(card1);
        choices.add(card2);
        
        for (int i = 0; i < choices.size(); i++) {
            System.out.println(i + ": " + choices.get(i).toString());
        }
        
        if (exchangeNum == 2) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String choiceString = "";
            int choice1 = 0;
            int choice2 = 0;
            
            try {
                choiceString = br.readLine();
            }
            catch (IOException e) {
                choice1 = 0;
                choice2 = 1;
            }
            
            if (choiceString.length() != 3) {
                choice1 = 0;
                choice2 = 1;
            }
            else {
                choice1 = Character.getNumericValue(choiceString.charAt(0)) % choices.size();
                choice2 = Character.getNumericValue(choiceString.charAt(2)) % choices.size();
                
                if (choice1 == choice2) {
                    choice1 = (choice1 + 1) % choices.size();
                }
            }
            
            setHand(choices.get(choice1), choices.get(choice2));
            Card[] returnedCards = new Card[2];
            for(int i = 0, j =0; i < choices.size(); i++) {
                if (i != choice1 && i != choice2) {
                    returnedCards[j] = choices.get(i);
                    j++;
                }
            }
            
            return returnedCards;
            
        }
        else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int choice = 0;
            
            try {
                choice = Integer.parseInt(br.readLine()) % choices.size();
            }
            catch (IOException e) {
                choice = 0;
            }
            
            for (int i = 0; i < 2; i++) {
                if (!hand[i].revealed()) {
                    hand[i] = choices.get(choice);
                }
            }
            
            choices.remove(choice);
            Card[] returnedCards = new Card[2];
            
            return choices.toArray(returnedCards);
        }
       
    }
    
    public int numHiddenCards() {
        int count = 0;
        for (int i = 0; i < 2; i ++) {
            if (!hand[i].revealed()) {
                count++;
            }
        }
        
        return count;
    }


}
