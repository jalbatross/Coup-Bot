package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game {
    private Player player1;
    private Player player2;
    
    private Player turnPlayer;
    private Player opponent;
    //private Player target;
    
    //private ArrayList<Player> players;
    private Stack<Card> deck;
    private Stack<Object> turnStack;
    
    public Game(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;
        turnStack = new Stack<Object>();
        init();
        
        int randomNumber = (int) (Math.random() * 1000);
        turnPlayer = (randomNumber % 2 == 0) ? player1: player2;
        opponent = (turnPlayer == player1) ? player2: player1;
        
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
        
        System.out.println("Current acting player: " + turnPlayer.name);
        
        System.out.println("\n--- " + player1.name + "'s info ---");
        System.out.println(player1.toString());
        System.out.println("--- " + player2.name + "'s info ---");
        if (player2 instanceof RandomBot) {
            System.out.println(((RandomBot) player2).toStringBot());
        }
        else {
            System.out.println(player2.toString());
        }
        
        System.out.println("===End Game State===");
    }
    
    /**
     * Exchanges a random card from aPlayer's hand and the deck.
     * @param aPlayer
     */
    public void exchangeRandomCard(Player aPlayer) {
        
    }
    
    public static void main (String[] args) throws Exception {
        Player player1 = new Player("Alice");
        RandomBot player2 = new RandomBot();
        
        Game game = new Game(player1,player2);

        game.gameState();
        
        //Game loop
        while (player1.isAlive() && player2.isAlive()) {
            game.gameLoop();
            game.nextPlayer();
            game.gameState();
        }
        
        if (player1.isAlive()) {
            System.out.println(player1.name + " won!");
        }
        else {
            System.out.println(player2.name + " won!");
        }
    }
    private void nextPlayer() {
        turnPlayer = (turnPlayer == player1) ? player2: player1;
        opponent = (turnPlayer == player1 ) ? player2: player1;
        
        turnStack = new Stack<Object>();
    }
    private void gameLoop() throws Exception {
        System.out.println("--- Beginning " + turnPlayer.name + "'s turn ---");
        turnPlayer.setPossibleActions();
        
        Action turnAction = turnPlayer.getUserAction();
        
        //Immediately pay 3 coins
        if (turnAction == Action.ASSASSINATE) {
            turnPlayer.setCoins(turnPlayer.coins() - 3);
        }
        
        turnStack.push(turnAction);

        if (canRespond(turnAction) && opponent.wantsReaction(turnAction)) {
            opponent.setPossibleReactions(turnAction);
            Reaction response = opponent.getUserReaction();
            turnStack.push(response);
            if (response == Reaction.CHALLENGE) {
                turnStack.pop();
                if (resolveActionChallenge(turnPlayer, opponent, (Action) turnStack.peek())) {
                    turnStack.clear();
                }
                else {
                    if (turnAction.blockable() && opponent.wantsReaction(turnAction)) {
                        opponent.setBlockReaction(turnAction);
                        turnStack.push(opponent.getUserReaction());
                    }
                }
            }
            
            
        }
        //Resolve reactions if they exist
        if(!turnStack.isEmpty() 
           && turnStack.peek() instanceof Reaction 
           && (Reaction) turnStack.peek() != Reaction.CHALLENGE 
           && turnPlayer.wantsChallengeReaction((Reaction) turnStack.peek())) {
            if (resolveReactionChallenge(opponent, turnPlayer, (Reaction) turnStack.peek())) {
                turnStack.pop();
            }
            else {
                turnStack.clear();
            }
        }
        
        resolveTurnStack(turnPlayer, opponent);
        System.out.println("--- Ending " + turnPlayer.name + "'s turn---");
    }
    
    private void resolveTurnStack(Player turnPlayer, Player target) {
        if (turnStack.isEmpty()) {
            return;
        }
        
        //Successful block
        if (turnStack.size() == 2) {
            //Alert users that block succeeded
            return;
        }
        
        try {
            
            Action playerAction = (Action) turnStack.pop();
            turnPlayer.performAction(playerAction, target);
            if (playerAction == Action.EXCHANGE) {
                shuffleDeck();
                Card card1 = deck.pop();
                Card card2 = deck.pop();
                
                Card[] exchangedCards = turnPlayer.ambassadorExchange(card1, card2);
                
                for (int i = 0; i < exchangedCards.length; i++) {
                    deck.push(exchangedCards[i]);
                }
                
                shuffleDeck();
            }
            
        }
        catch (Exception e) {
            System.out.println("Failed to resolve turn stack");
            e.printStackTrace();
        } 
        
    }

    private boolean resolveActionChallenge(Player challenged, Player challenger,
            Action anAction) throws Exception {
        CardType response = challenged.getActionChallengeResponse(anAction);
        
        //Challenge fails
        if (CardType.action(response) == anAction) {
            System.out.println("Challenge from " + challenger.name + " failed");
            deck.add(challenged.exchangeCard(response, deck.pop()));
            challenger.revealCard();
            return false;
        }
        //Challenge succeeds
        else {
            System.out.println("Challenge from " + challenger.name + " succeeded");
            challenged.revealCard(response);
            return true;
        }
        
    }
    
    private boolean resolveReactionChallenge(Player challenged, Player challenger,
            Reaction aReaction) throws Exception {
        
        CardType response = challenged.getReactionChallengeResponse(aReaction);
        
        //Challenge fails
        if (CardType.reaction(response) == aReaction) {
            System.out.println("Challenge from " + challenger.name + " failed");
            deck.add(challenged.exchangeCard(response, deck.pop()));
            challenger.revealCard();
            return false;
        }
        //Challenge succeeds
        else {
            System.out.println("Challenge from " + challenger.name +  " succeeded");
            challenged.revealCard(response);
            return true;
        }
        
    }
    
    private boolean canRespond(Action anAction) {
        return anAction != Action.INCOME && anAction != Action.COUP;
    }
    
}
