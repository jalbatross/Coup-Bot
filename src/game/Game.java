package game;

import java.util.ArrayList;

public class Game {
    private Player player1;
    private Player player2;
    
    private ArrayList<Card> cards;
    public Game(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;
        
        cards = new ArrayList<Card>(15);
        for (int i = 0; i < 15; i++) {
            if (i % 4 == 0) {
                cards.add(new Card(CardType.CONTESSA));
            }
            else if (i % 4 == 1) {
                cards.add(new Card(CardType.DUKE));
            }
            else if (i % 4 == 2) {
                cards.add(new Card(CardType.ASSASSIN));
            }
            else if (i % 4 == 3) {
                cards.add(new Card(CardType.AMBASSADOR));
            }
            else {
                cards.add(new Card(CardType.CAPTAIN));
            }
        }
    }
}
