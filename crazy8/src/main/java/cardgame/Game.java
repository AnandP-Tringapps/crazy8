package cardgame;

import cardgame.Card.Rank;
import cardgame.Card.Suit;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

  List<Card> drawPile = Card.getDeck();
  Card topCard;
  Map<PlayerStrategy, PlayerTurn> playerTurns = new HashMap<PlayerStrategy, PlayerTurn>();
  List<PlayerStrategy> playerList;
  Suit suitChange;
  List <Card> discardPile = new ArrayList <Card>();
  Map <PlayerStrategy,List<Card>> hands = new HashMap<PlayerStrategy, List<Card>>();

  Game(PlayerStrategy... players) throws Exception {
    playerList = Arrays.asList(players);
    Collections.shuffle(drawPile);
    // This for loop assigns the ID of the player.
    for (PlayerStrategy i : players) {
      PlayerTurn temp = new PlayerTurn();
      temp.playerId = (int) (Math.random() * 10000);
      playerTurns.put(i, temp);

      i.reset();
      List<Card> playerHand = drawPile.subList(0, 5);
      i.receiveInitialCards(playerHand);
      hands.put(i,playerHand);

    }
    // This flips over the top card.
    do {
      topCard = drawPile.remove(0);
    } while (topCard.getRank().equals(Rank.EIGHT));
    suitChange = null;
    while (drawPile.size() != 0) {
      startRound();
    }
  }

  private void startRound() throws Exception{
    for (PlayerStrategy i : playerList) {
      resetPlayerTurn(i);
      if (i.shouldDrawCard(topCard, suitChange)) {
        drawCard(i);
      } else {
        playCard(i);
      }
    }
    for (PlayerStrategy i : playerList) {
      List<PlayerTurn> playerTurnsReturn = new ArrayList<PlayerTurn>();
      for (PlayerStrategy j : playerTurns.keySet()) {
        if (!j.equals(i)) {
          playerTurnsReturn.add(playerTurns.get(j));
        }
      }
      i.processOpponentActions(playerTurnsReturn);
    }
  }

   void drawCard(PlayerStrategy i) {
    playerTurns.get(i).drewACard = true;
    Card temp = drawPile.get(0);
    for (int j = 0; j < 3; j++) {
      if (temp.getSuit().equals(topCard.getSuit()) || temp.getRank()
          .equals(topCard.getRank())) {
        topCard = temp;
        if (suitChange != null) {
          suitChange = null;
        }
        break;
      } else {
        i.receiveCard(temp);
        hands.get(i).add(temp);
      }
    }
  }

  private void playCard(PlayerStrategy i) throws Exception {
    Card playedCard = i.playCard();
    if(isCheat(playedCard)){
      Exception e = new Exception("Cheating Detected");
      throw e;
    }
    discardPile.add(topCard);
    topCard = i.playCard();
    hands.get(i).remove(topCard);
    playerTurns.get(i).playedCard = topCard;
    if (suitChange != null) {
      suitChange = null;
    }
    if (topCard.getRank().equals(Rank.EIGHT)) {
      suitChange = i.declareSuit();
      playerTurns.get(i).declaredSuit = topCard.getSuit();
    }
  }

  boolean isCheat(Card i){
    return (!(drawPile.contains(i) || discardPile.contains(i)));
  }

  private void resetPlayerTurn(PlayerStrategy i) {
    PlayerTurn temp = playerTurns.get(i);
    temp.declaredSuit = null;
    temp.playedCard = null;
    temp.drewACard = false;
  }

  public Map<PlayerStrategy, List<Card>> getScores() {
    return hands;
  }
}
