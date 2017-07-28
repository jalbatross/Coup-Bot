package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game {
    private Player player1;
    private Player player2;
    
    private Stack<Card> deck;
    
    public Game(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;
        
        init();
        
    }
    
    private void init() {
        createDeck();
        shuffleDeck();
        dealCards();
    }
    
    private void createDeck() {
        deck = new Stack<Card>();
        
        for (int i = 0; i < 15; i++) {
            if (i % 5 == 0) {
                deck.add(new Card(CardType.CONTESSA));
            }
            else if (i % 5 == 1) {
                deck.add(new Card(CardType.DUKE));
            }
            else if (i % 5 == 2) {
                deck.add(new Card(CardType.ASSASSIN));
            }
            else if (i % 5 == 3) {
                deck.add(new Card(CardType.AMBASSADOR));
            }
            else {
                deck.add(new Card(CardType.CAPTAIN));
            }
        }
    }
    
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }
    
    private void dealCards() {
        player1.setHand(deck.pop(), deck.pop());
        player2.setHand(deck.pop(), deck.pop());
    }
    
    private String deckString() {
        String ret = new String();
        
        for (Card card: deck) {
            ret += card.influence.toString() + "\n";
        }
        
        return ret;
    }
    
    /**
     * Prints the following to console:
     * 1. Current deck
     * 2. Player 1's hand and coins
     * 2. Player 2's hand and coins
     */
    public void gameState() {
        System.out.println("====Game State====\n");
        System.out.println("Deck: ");
        for (Card card : deck) {
            System.out.println(card.influence.toString());
        }
        
        System.out.println("\n--- Player 1's Info ---");
        System.out.println(player1.toString());
        System.out.println("--- Player 2's Info ---");
        System.out.println(player2.toString());
        
        System.out.println("===End Game State===");
    }
    
    /**
     * Exchanges a random card from aPlayer's hand and the deck.
     * @param aPlayer
     */
    public void exchangeRandomCard(Player aPlayer) {
        
    }
    
    public static void main (String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();
        
        Game game = new Game(player1,player2);

        game.gameState();
        
        //Game loop
        while (player1.isAlive() || player2.isAlive()) {
        }
    }
}
