package cardgame;

import cardgame.Card.Suit;
import java.util.List;

public class BasicStrategy implements PlayerStrategy {

  int playerId;
  List<Integer> opponentIds;
  List<Card> hand;
  private Card nextPlay;

  @Override
  public void init(int playerId, List<Integer> opponentIds) {
    this.playerId = playerId;
    this.opponentIds = opponentIds;
  }

  @Override
  public void receiveInitialCards(List<Card> cards) {
    hand = cards;
  }

  @Override
  public boolean shouldDrawCard(Card topPileCard, Suit changedSuit) {
    if (changedSuit != null) {
      for (Card i : hand) {
        if (i.getSuit() == changedSuit) {
          nextPlay = i;
          return false;
        }
      }
    }
    for (Card i : hand) {
      if (i.getSuit() == topPileCard.getSuit() || i.getRank() == topPileCard.getRank()) {
        nextPlay = i;
        return false;
      }
    }
    return true;
  }

  @Override
  public void receiveCard(Card drawnCard) {
    hand.add(drawnCard);
  }

  @Override
  public Card playCard() {
    hand.remove(nextPlay);
    return nextPlay;
  }

  @Override
  public Suit declareSuit() {
    int randomSuitGenerator = (int) (Math.random() * 4);
    return Card.Suit.values()[randomSuitGenerator];

  }

  @Override
  public void processOpponentActions(List<PlayerTurn> opponentActions) {
    return;
  }

  @Override
  public void reset() {
    hand = null;
    nextPlay = null;
  }
}
