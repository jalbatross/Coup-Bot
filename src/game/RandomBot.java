package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.SplittableRandom;

/**
 * A simple bot that performs a random action at every opportunity.
 * 
 * @author @jalbatross(Joey A.)
 *
 */

public class RandomBot extends Player {
    public SplittableRandom rand;
    public RandomBot() {
        super();
        this.name = "RandomBot";
        rand = new SplittableRandom();
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
        
        hand[choice].reveal();
        return hand[choice];
    }
    
    @Override
    public Action getUserAction() {
        int choice = Math.abs(rand.nextInt() % actions.size());
        
        return actions.get(choice);
        
    }
    
    @Override
    public Reaction getUserReaction() {
        int choice = Math.abs(rand.nextInt() % actions.size());
        
        return reactions.get(choice);
    }
    
    @Override
    public boolean wantsChallengeReaction(Reaction aReaction) {
        int choice = Math.abs(rand.nextInt() % 2);
        
        return choice == 1 ? true: false;
    }
    
    @Override
    public boolean wantsReaction(Action anAction) {
        int choice = Math.abs(rand.nextInt() % 2);
        
        return choice == 1 ? true: false;
    }
    
    @Override
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
            
            return choices.toArray(returnedCards);
        }
       
    }
}
