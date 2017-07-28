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
    
    public static void main (String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();
        
        Game game = new Game(player1,player2);
        
        game.gameState();
    }
    
    private void init() {
        createDeck();
        shuffleDeck();
        dealCards();
    }
    
    private void createDeck() {
        deck = new Stack<Card>();
        
        for (int i = 0; i < 15; i++) {
            if (i % 4 == 0) {
                deck.add(new Card(CardType.CONTESSA));
            }
            else if (i % 4 == 1) {
                deck.add(new Card(CardType.DUKE));
            }
            else if (i % 4 == 2) {
                deck.add(new Card(CardType.ASSASSIN));
            }
            else if (i % 4 == 3) {
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
    
    /**
     * Prints the following to console:
     * 1. Current deck
     * 2. Player 1's hand and coins
     * 2. Player 2's hand and coins
     */
    public void gameState() {
        System.out.println("----Game State----");
        System.out.println("Deck: ");
        for (Card card : deck) {
            System.out.println(card.influence.toString());
        }
        
        System.out.println("--- Player 1's Hand ---");
        System.out.println(player1.handString()+"\n\n");
        System.out.println("--- Player 2's Hand ---");
        System.out.println(player2.handString() + "\n\n");
        
        System.out.println("===End Game State===");
    }
}
