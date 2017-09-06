package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SplittableRandom;
import java.util.Stack;

/**
 * A simple bot that performs a random possible action at every opportunity
 * and watches out for impossible moves.
 * 
 * @author @jalbatross(Joey A.)
 *
 */

public class SmarterRandomBot extends Player {
    public SplittableRandom rand;
    
    //Counters for cards the AI knows to be revealed
    private int assassinCounter = 0;
    private int dukeCounter = 0;
    private int ambassadorCounter = 0;
    private int captainCounter = 0;
    private int contessaCounter = 0;
    
    private int assassinCounterDeck = 0;
    private int dukeCounterDeck = 0;
    private int ambassadorCounterDeck = 0;
    private int captainCounterDeck = 0;
    private int contessaCounterDeck = 0;
    
    private int assassinCounterHand = 0;
    private int dukeCounterHand = 0;
    private int ambassadorCounterHand = 0;
    private int captainCounterHand = 0;
    private int contessaCounterHand = 0;
    
    private double score = 0;
    
    public SmarterRandomBot() {
        super();
        this.name = "SmarterRandomBot";
        rand = new SplittableRandom();
    }
    
    public String handStringBot() {
        String ret = "";
        if (hand[0].revealed()) {
            ret += hand[0];
        }
        else {
            ret += "??";
        }
        if (hand[1].revealed()) {
            ret += ", " + hand[1];
        }
        else {
            ret+= ", ??";
        }
        return ret;
    }

    public String toStringBot() {
        return new String(handStringBot() + "\n" + 
                "Coins: " + coins +"\n" +
                "Actions: " + possibleActionsString() + "\n");
    }
    
    @Override
    public CardType getActionChallengeResponse(Action action) {
        
        System.out.println(name + ", your action of " + action + " is being challenged");
        System.out.println("Reveal a card:\n" + hiddenCardsString());
        
        System.out.println("Enter a number corresponding to your choice");
        
        int choice = Math.abs(rand.nextInt() % 2);
        
        if (hand[choice].revealed()) {
            choice = (choice + 1) % 2;
        }
        
        return hand[choice].influence;
    }
    
    @Override
    public CardType getReactionChallengeResponse(Reaction reaction) {
        System.out.println(name + ", your reaction of " + reaction + " is being challenged");
        System.out.println("Reveal a card:\n" + hiddenCardsString());
        
        System.out.println("Enter a number corresponding to your choice");
        
        int choice = Math.abs(rand.nextInt() % 2);
        
        if (hand[choice].revealed()) {
            choice = (choice + 1) % 2;
        }
        
        return hand[choice].influence;
    }

    @Override
    public Card revealCard() {
        System.out.println(name + ", you must reveal a card");
        System.out.println("Your choices: " + hiddenCardsString());
        
        System.out.println("Enter a number corresponding to your choice");
        
        int choice = Math.abs(rand.nextInt() % 2);
        
        if (hand[choice].revealed()) {
            choice = (choice + 1) % 2;
        }
        
        //Update revealed counter
        updateRevealedCounter(hand[choice]);
        
        //If a card is revealed it doesn't count as being in hand anymore
        decrementHandCounter(hand[choice]);
        
        hand[choice].reveal();
        return hand[choice];
    }
    
    /**
     * Updates the appropriate counter for the AI's memory based on 
     * cardRevealed
     * @param cardRevealed    A revealed card
     */
    public void updateRevealedCounter(Card cardRevealed) {
        switch(cardRevealed.influence) {
            case DUKE:
                dukeCounter++;
                break;
            case ASSASSIN:
                assassinCounter++;
                break;
            case CONTESSA:
                contessaCounter++;
                break;
            case AMBASSADOR:
                ambassadorCounter++;
                break;
            case CAPTAIN:
                captainCounter++;
                break;
            default:
                //Shouldn't reach here    
        }
        
    }
    
    /**
     * Updates the appropriate counter for the AI's memory if they see
     * a card in the deck.
     * 
     * @param cardInDeck   Card in the deck
     */
    public void updateInDeckCounter(Card cardInDeck) {
        updateInDeckCounter(cardInDeck.influence);
    }
    
    /**
     * Updates the appropriate counter for the AI's memory if they see
     * a card in the deck.
     * 
     * @param typeInDeck   Type of card in the deck
     */
    public void updateInDeckCounter(CardType typeInDeck) {
        switch(typeInDeck) {
            case DUKE:
                dukeCounterDeck++;
                break;
            case ASSASSIN:
                assassinCounterDeck++;
                break;
            case CONTESSA:
                contessaCounterDeck++;
                break;
            case AMBASSADOR:
                ambassadorCounterDeck++;
                break;
            case CAPTAIN:
                captainCounterDeck++;
                break;
            default:
                //Shouldn't reach here   
        }
    }
    
    public void setInDeckCounter(CardType typeInDeck, int val) {
        if (val < 1 || val > 2) {
            return;
        }
        
        switch(typeInDeck) {
            case DUKE:
                dukeCounterDeck = val;
                break;
            case ASSASSIN:
                assassinCounterDeck = val;
                break;
            case CONTESSA:
                contessaCounterDeck = val;
                break;
            case AMBASSADOR:
                ambassadorCounterDeck = val;
                break;
            case CAPTAIN:
                captainCounterDeck = val;
                break;
            default:
                //Shouldn't reach here   
        }
    }
    
    /**
     * Resets card in deck counters, necessary when the deck is shuffled or
     * a card is extracted from the deck.
     */
    public void resetDeckCounters() {
        dukeCounterDeck = 0;
        captainCounterDeck = 0;
        ambassadorCounterDeck = 0;
        contessaCounterDeck = 0;
        assassinCounterDeck = 0;
    }
    
    @Override
    public void setHand(Card card1, Card card2) {
        super.setHand(card1, card2);
        incrementHandCounter(card1);
        incrementHandCounter(card2);
    }
    
    /**
     * Increases the appropriate counter for the AI's memory for a card
     * in their hand
     * 
     * @param cardInHand a card in SmarterRandomBot's hand
     */
    private void incrementHandCounter(Card cardInHand) {
        incrementHandCounter(cardInHand.influence);
    }
    
    /**
     * Increases the appropriate counter for the AI's memory for a card
     * in their hand
     * 
     * @param typeInDeck   Type of card in hand
     */
    private void incrementHandCounter(CardType typeInHand) {
        switch(typeInHand) {
            case DUKE:
                dukeCounterHand++;
                break;
            case ASSASSIN:
                assassinCounterHand++;
                break;
            case CONTESSA:
                contessaCounterHand++;
                break;
            case AMBASSADOR:
                ambassadorCounterHand++;
                break;
            case CAPTAIN:
                captainCounterHand++;
                break;
            default:
                //Shouldn't reach here   
        }
    }
    
    /**
     * Updates the appropriate counter for the AI's memory for a card
     * in their hand
     * 
     * @param cardInHand a card in SmarterRandomBot's hand
     */
    private void decrementHandCounter(Card cardInHand) {
        decrementHandCounter(cardInHand.influence);
    }
    
    /**
     * Updates the appropriate counter for the AI's memory for a card
     * in their hand
     * 
     * @param typeInDeck   Type of card in hand
     */
    private void decrementHandCounter(CardType typeInHand) {
        switch(typeInHand) {
            case DUKE:
                dukeCounterHand--;
                break;
            case ASSASSIN:
                assassinCounterHand--;
                break;
            case CONTESSA:
                contessaCounterHand--;
                break;
            case AMBASSADOR:
                ambassadorCounterHand--;
                break;
            case CAPTAIN:
                captainCounterHand--;
                break;
            default:
                //Shouldn't reach here   
        }
    }
    
    /**
     * Resets in hand counters
     */
    private void resetHandCounters() {
        ambassadorCounterHand = 0;
        assassinCounterHand = 0;
        captainCounterHand = 0;
        contessaCounterHand = 0;
        dukeCounterHand = 0;
    }

    @Override
    public Action getUserAction() {
        int choice = Math.abs(rand.nextInt() % actions.size());
        
        return actions.get(choice);
        
    }
    
    @Override
    public Reaction getUserReaction() {
        int choice = Math.abs(rand.nextInt() % reactions.size());
        
        return reactions.get(choice);
    }
    
    /**
     * SmarterRandomBot will always challenge reactions that it knows are
     * impossible.
     */
    @Override
    public boolean wantsChallengeReaction(Reaction aReaction) {
        if (aReaction == Reaction.BLOCK_ASSASSINATE &&
                contessaCounter + contessaCounterDeck + contessaCounterHand == 3) {
            return true;
        }
        if (aReaction == Reaction.BLOCK_FOREIGN_AID && 
                dukeCounter + dukeCounterDeck + dukeCounterHand == 3 ) {
            return true;
        }
        if (aReaction == Reaction.BLOCK_STEAL && 
                captainCounter + captainCounterDeck + captainCounterHand  == 3 &&
                ambassadorCounter + ambassadorCounterDeck + ambassadorCounterHand == 3) {
            return true;
        }
        
        int choice = Math.abs(rand.nextInt() % 2);
        
        return choice == 1 ? true: false;
    }
    
    /**
     * SmarterRandomBot will always react to impossible scenarios.
     */
    @Override
    public boolean wantsReaction(Action anAction) {
        
        //Always react to impossible scenarios
        if (anAction == Action.ASSASSINATE && 
                assassinCounter + assassinCounterDeck + assassinCounterHand == 3) {
            return true;
        }
        if (anAction == Action.STEAL && 
                captainCounter + captainCounterDeck + captainCounterHand == 3) {
            return true;
        }
        if(anAction == Action.EXCHANGE && 
                ambassadorCounter + ambassadorCounterDeck + ambassadorCounterHand == 3) {
            return true;
        }
        if (anAction == Action.TAX && 
                dukeCounter + dukeCounterDeck + dukeCounterHand == 3) {
            return true;
        }
        
        //No possible reaction in this case, so we don't act
        if (anAction == Action.FOREIGN_AID && dukeCounter == 3) {
            return false;
        }
        
        //Otherwise act randomly
        int choice = Math.abs(rand.nextInt() % 2);
        
        return choice == 1 ? true: false;
    }
    
    @Override
    public Card[] ambassadorExchange(Card card1, Card card2) {
        
        //Will be updated when we choose cards
        resetHandCounters();
        
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
        
        //Choosing two cards
        if (exchangeNum == 2) {
            
            //Two cards means we have 4 choices
            int choice1 = Math.abs(rand.nextInt() % 4);
            int choice2 = Math.abs(rand.nextInt() % 4);
            
            //Make sure the choices aren't the same
            while (choice2 == choice1) {
                choice2 = Math.abs(rand.nextInt() % 4);
            }
            
            setHand(choices.get(choice1), choices.get(choice2));
            
            Card[] returnedCards = new Card[2];
            for(int i = 0, j =0; i < choices.size(); i++) {
                if (i != choice1 && i != choice2) {
                    returnedCards[j] = choices.get(i);
                    j++;
                }
            }
            
            //If the cards are the same, update in deck counter
            if (returnedCards[0].influence == returnedCards[1].influence) {
                setInDeckCounter(returnedCards[0].influence, 2);
            }
            //If the cards are different, only update the counters if we
            //haven't seen the card in the deck yet
            else {
                updateInDeckCounterConditionally(returnedCards[0].influence);
                updateInDeckCounterConditionally(returnedCards[1].influence);
            }
            
            return returnedCards;
            
        }
        //Only 1 card to exchange, 3 possible choices
        else {
            
            int choice = Math.abs(rand.nextInt() % 3);
            
            for (int i = 0; i < 2; i++) {
                if (!hand[i].revealed()) {
                    hand[i] = choices.get(choice);
                    
                    //Increment in hand counter
                    incrementHandCounter(hand[i]);
                }
            }
            
            choices.remove(choice);
            Card[] returnedCards = new Card[2];
            
            //Update cards we know to be in the deck
            updateInDeckCounterConditionally(choices.get(0).influence);
            updateInDeckCounterConditionally(choices.get(1).influence);
            
            return choices.toArray(returnedCards);
        }
       
    }
    
    /**
     * Updates the in deck counter for the influence if the card hasn't
     * been seen by the AI before.
     * @param influence an influence (card type)
     */
    private void updateInDeckCounterConditionally(CardType influence) {
        switch (influence) {
            case AMBASSADOR:
                ambassadorCounterDeck = (ambassadorCounterDeck == 0) ? 1: 0;
                break;
            case ASSASSIN:
                assassinCounterDeck = (assassinCounterDeck == 0) ? 1 : 0;
                break;
            case CAPTAIN:
                captainCounterDeck = (captainCounterDeck == 0) ? 1: 0;
                break;
            case CONTESSA:
                contessaCounterDeck = (contessaCounterDeck == 0) ? 1 : 0;
                break;
            case DUKE:
                dukeCounterDeck = (dukeCounterDeck == 0) ? 1: 0;
                break;
            default:
                //Shouldn't reach here
        }
        
    }

    /**
     * Identical to the super setPossibleActions, except omits actions that
     * are not possible due to all copies of the card being revealed.
     */
    @Override
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
            
            //Don't do actions that are known to be impossible
            if (enumActions == Action.ASSASSINATE && assassinCounter == 3) {
                continue;
            }
            if (enumActions == Action.TAX && dukeCounter == 3) {
                continue;
            }
            if (enumActions == Action.STEAL && captainCounter == 3) {
                continue;
            }
            if (enumActions == Action.EXCHANGE && ambassadorCounter == 3) {
                continue;
            }
            
            this.actions.add(enumActions);
        }
    }
    
    @Override
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
        
        //Don't do impossible reactions
        if (Action.correspondingReaction(anAction) == Reaction.BLOCK_ASSASSINATE &&
                contessaCounter == 3) {
            return;
        }
        if (Action.correspondingReaction(anAction) == Reaction.BLOCK_STEAL &&
                captainCounter == 3 &&
                ambassadorCounter == 3) {
            return;
        }
        if (Action.correspondingReaction(anAction) == Reaction.BLOCK_FOREIGN_AID &&
                dukeCounter == 3) {
            return;
        }
        
        //Otherwise add the correct reaction based on the action
        reactions.add(Action.correspondingReaction(anAction));
    }
    
    /**
     * Updates in deck counter when we exchange cards into deck
     */
    @Override
    public void exchangeCard(CardType type, Stack<Card> deck) throws Exception {
        if (deck == null || deck.size() <= 0) {
            throw new Exception("Invalid deck during card exchange");
        }
        
        int indexOfCardRemoved = -1;
        
        //Search the hand for a non-revealed card of CardType type
        for (int i = 0; i < 2; i++) {
            if (hand[i].influence == type && !hand[i].revealed()) {
                deck.add(hand[i]);
                indexOfCardRemoved = i;
                break;
            }
        }
        
        if (indexOfCardRemoved == -1) {
            throw new Exception("No valid card in hand!");
        }
        
        //Shuffle the deck
        Collections.shuffle(deck);
        
        //Place a new card from the deck into the player's hand
        hand[indexOfCardRemoved] = deck.pop();
        
        //If the AI receives a different card than it had before, update
        //the appropriate counters
        if (hand[indexOfCardRemoved].influence != type) {
            //We know a card of type is now in the deck
            updateInDeckCounter(type);
            
            //Have one less of it in hand
            decrementHandCounter(type);
            
            //Have one more of another type in our hand
            incrementHandCounter(hand[indexOfCardRemoved].influence);
        }
        
    }
   
    public void updateScore(double d) {
        score += d;
    }
    
    public double score() {return score;}
    
    
}
