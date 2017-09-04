package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.SplittableRandom;

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
        
        updateRevealedCounter(hand[choice]);
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
    public Action getUserAction() {
        int choice = Math.abs(rand.nextInt() % actions.size());
        
        return actions.get(choice);
        
    }
    
    @Override
    public Reaction getUserReaction() {
        int choice = Math.abs(rand.nextInt() % reactions.size());
        
        return reactions.get(choice);
    }
    
    @Override
    public boolean wantsChallengeReaction(Reaction aReaction) {
        int choice = Math.abs(rand.nextInt() % 2);
        
        return choice == 1 ? true: false;
    }
    
    @Override
    public boolean wantsReaction(Action anAction) {
        
        //Always react to impossible scenarios
        if (anAction == Action.ASSASSINATE && 
                assassinCounter + assassinCounterDeck   == 3) {
            return true;
        }
        if (anAction == Action.STEAL && 
                captainCounter + captainCounterDeck == 3) {
            return true;
        }
        if(anAction == Action.EXCHANGE && 
                ambassadorCounter + ambassadorCounterDeck == 3) {
            return true;
        }
        if (anAction == Action.TAX && 
                dukeCounter + dukeCounterDeck == 3) {
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
        
        resetDeckCounters();
        
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
                    
                    //Update internal memory - we know a card in the deck
                    updateInDeckCounter(returnedCards[j]);
                    j++;
                }
            }
            
            return returnedCards;
            
        }
        //Only 1 card to exchange, 3 possible choices
        else {
            
            int choice = Math.abs(rand.nextInt() % 3);
            
            for (int i = 0; i < 2; i++) {
                if (!hand[i].revealed()) {
                    hand[i] = choices.get(choice);
                }
            }
            
            choices.remove(choice);
            Card[] returnedCards = new Card[2];
            
            //Update cards we know to be in the deck
            updateInDeckCounter(returnedCards[0]);
            updateInDeckCounter(returnedCards[1]);
            
            return choices.toArray(returnedCards);
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
        if (anAction != Action.FOREIGN_AID && dukeCounter < 3) {
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
        
        //Otherwise add the correct reaction based on the action
        reactions.add(Action.correspondingReaction(anAction));
    }
    
    /**
     * Updates in deck counter when we exchange cards into deck
     */
    @Override
    public Card exchangeCard(CardType type, Card exchangeCard) throws Exception {
        Card ret = super.exchangeCard(type,  exchangeCard);
        
        //Need to reset deck counters because we don't know if the card
        //we received is one of the previously counted cards in the 
        //deck
        resetDeckCounters();
        
        //Update the in deck counter
        updateInDeckCounter(type);
        
        return ret;
    }
    
    
}
