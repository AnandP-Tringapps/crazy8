package cardgame;

import static cardgame.Card.Rank;
import static cardgame.Card.Suit;
import static cardgame.Card.getDeck;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GameEngineTesting {
  Game testing;

  @Before
      public void test() throws Exception {
    Game testing = new Game();
  }
  @Test
  public void recieveInitialCards() {
    BasicStrategy obj = new BasicStrategy();
    List<Card> eights = Arrays.asList(
        new Card(Suit.DIAMONDS, Rank.EIGHT),
        new Card(Suit.HEARTS, Rank.EIGHT),
        new Card(Suit.SPADES, Rank.EIGHT),
        new Card(Suit.CLUBS, Rank.EIGHT)
    );
    obj.receiveInitialCards(eights);
    assertEquals(obj.hand, eights);

  }

  @Test
  public void recieveCardsTest() {
    BasicStrategy obj = new BasicStrategy();
    List<Card> recievedCard = (Arrays.asList(new Card(Suit.DIAMONDS,Rank.ACE)));
    obj.receiveCard(new Card(Suit.DIAMONDS,Rank.ACE));
    assertEquals(obj.hand, recievedCard);

  }
}
