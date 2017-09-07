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
        if (player2 instanceof SmarterRandomBot) {
            System.out.println(((SmarterRandomBot) player2).toStringBot());
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
        SmarterRandomBot player2 = new SmarterRandomBot();
               
        Game game = new Game(player1,player2);

        game.gameState();
        
        //Game loop
        while (player1.isAlive() && player2.isAlive()) {
            int[] aiScores = {0,0,0};
            
            if (player1 instanceof SmarterRandomBot || 
                    player2 instanceof SmarterRandomBot) {
                aiScores = getAiStats(player1,player2);
            }
            
            //Do turn
            game.gameLoop(); 
            
            //Evaluate AI's score
            player2.updateScore(evaluateAI(aiScores, getAiStats(player1, player2)));
            
            game.nextPlayer();
            game.gameState();
        }
        
        if (player1.isAlive()) {
            System.out.println(player1.name + " won!");
        }
        else {
            System.out.println(player2.name + " won!");
        }
        
        System.out.println("AI Score: " + player2.score());
    }
    
    /**
     * Evaluates AI based on a difference between two int[] which correspond
     * to its scores in gold and card count respectively
     * 
     * Stats are represented as int[] of the form {aiGold, numAICardsHidden, numOppCardsHidden}
     * @param statsBeforeTurn ai's stats before beginning of turn
     * @param aiStats ai's stats after turn
     * @return A score in the interval [-1,1] based on the change between the 
     * two stats
     */
    private static double evaluateAI(int[] statsBeforeTurn, int[] statsAfterTurn) {
        double score = 0;
        
        //If the AI increased its gold, add to its score
        if (statsBeforeTurn[0] > statsAfterTurn[0]) {
            score += 0.1;
        }
        
        //If it decreased, decrease its score by 0.1
        if (statsBeforeTurn[0] < statsAfterTurn[0]) {
            score -= 0.1;
        }
        
        //If it lost a card, decrease its score by 0.5 additionally
        if (statsBeforeTurn[1] < statsAfterTurn[1]) {
            score -= 0.5;
        }
        
        //If its opponent lost a card, increase its score by 0.5
        if (statsBeforeTurn[2] < statsAfterTurn[2]) {
            score += 0.5;
        }
        
        //If it killed the opponent, give it the maximum score for the round
        if (statsAfterTurn[2] == 0) {
            return 1;
        }
        
        //If the AI died this turn, give it the lowest score possible
        if (statsAfterTurn[1] == 0) {
            return -1;
        }
            
        return score;
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
        
        //Immediately pay 3 coins if the action is assassinate
        if (turnAction == Action.ASSASSINATE) {
            turnPlayer.setCoins(turnPlayer.coins() - 3);
        }
        
        //Reset deck counters for the AI if the deck was shuffled by
        //its opponent
        if (turnAction == Action.EXCHANGE && 
                opponent instanceof SmarterRandomBot) {
            SmarterRandomBot bot = (SmarterRandomBot) opponent;
            bot.resetDeckCounters();
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
        
        //Evaluate computer's performance
        
        
        System.out.println("--- Ending " + turnPlayer.name + "'s turn---");
    }
    
    /**
     * 
     * @param p1
     * @param p2
     * @return int[] of ai's stats = {aiPlayerGold, aiPlayerCardsAlive, aiOppCardsAlive}
     * @throws Exception
     */
    private static int[] getAiStats(Player p1, Player p2) throws Exception {
        if (p1 instanceof SmarterRandomBot && p2 instanceof SmarterRandomBot) {
            throw new Exception("Two AIs detected!!");
        }
        
        if (!(p1 instanceof SmarterRandomBot) && !(p2 instanceof SmarterRandomBot)) {
            throw new Exception("No AI detected for stats.");
        }
        
        SmarterRandomBot ai = null;
        Player human = null;
        if (p1 instanceof SmarterRandomBot) {
            ai = (SmarterRandomBot) p1;
            human = p2;
        }
        else {
            ai = (SmarterRandomBot) p2;
            human = p1;
        }
        
        int goldScore = ai.coins();
        int cardsAliveScore = ai.numHiddenCards();
        int opponentAliveScore = human.numHiddenCards();
        
        int[] scores = {goldScore, cardsAliveScore, opponentAliveScore};
        
        return scores;
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
            
            challenged.exchangeCard(response, deck);    
            
            //Updates AI memory of revealed cards if challenged is AI.
            //Only reveals card otherwise
            challenged.updateRevealedCounter(challenger.revealCard());
            return false;
        }
        //Challenge succeeds
        else {
            System.out.println("Challenge from " + challenger.name + " succeeded");
            
            //Updates AI memory of revealed card if challenger is AI.
            //Only reveals card otherwise
            challenger.updateRevealedCounter(challenged.revealCard(response));
            return true;
        }
        
    }
    
    private boolean resolveReactionChallenge(Player challenged, Player challenger,
            Reaction aReaction) throws Exception {
        
        CardType response = challenged.getReactionChallengeResponse(aReaction);
        
        //Challenge fails
        if (CardType.reaction(response) == aReaction) {
            System.out.println("Challenge from " + challenger.name + " failed");
            
            challenged.exchangeCard(response, deck);
            
            //Update revealed card counter if challenged is AI
            challenged.updateRevealedCounter(challenger.revealCard());
            return false;
        }
        //Challenge succeeds
        else {
            System.out.println("Challenge from " + challenger.name +  " succeeded");
            
            //Update revealed card counter if challenger is AI
            challenger.updateRevealedCounter(challenged.revealCard(response));
            return true;
        }
        
    }
    
    private boolean canRespond(Action anAction) {
        return anAction != Action.INCOME && anAction != Action.COUP;
    }
    
}
