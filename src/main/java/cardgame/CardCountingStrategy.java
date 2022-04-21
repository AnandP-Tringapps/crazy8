package cardgame;

import cardgame.Card.Rank;
import cardgame.Card.Suit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardCountingStrategy implements PlayerStrategy{

  int playerId;
  List <Integer>  opponentIds;
  List <Card> hand;
  Map <Suit, Integer> suitCount;
  Map <Rank, Integer> rankCount;
  List <Card> goodPlay = new ArrayList<Card>();
  List <Suit> badPlay = new ArrayList<Suit>();
  Card topCard;
  Suit changedSuit;

  @Override
  public void init(int playerId, List<Integer> opponentIds) {
    this.playerId = playerId;
    this.opponentIds = opponentIds;
  }

  @Override
  public void receiveInitialCards(List<Card> cards) {
    hand = cards;
    for (Suit i : Card.Suit.values()){
      suitCount.put(i ,0);
    }
    for (Rank i : Card.Rank.values()){
      rankCount.put(i ,0);
    }
    for (Card i : hand){
      suitCount.replace(i.getSuit(), suitCount.get(i.getSuit())+1);
      rankCount.replace(i.getRank(), rankCount.get(i.getRank())+1);
    }
  }

  @Override
  public boolean shouldDrawCard(Card topPileCard, Suit changedSuit) {
    topCard =topPileCard;
    this.changedSuit = changedSuit;
    if (changedSuit != null){
      for (Card i : hand){
        if (i.getSuit() == changedSuit){
          return false;
        }
      }
    }
    for (Card i : hand){
      if (i.getSuit() == topPileCard.getSuit() || i.getRank() == topPileCard.getRank()){
        return false;
      }
    }
    return true;
  }

  @Override
  public void receiveCard(Card drawnCard) {
    hand.add(drawnCard);
    suitCount.replace(drawnCard.getSuit(), suitCount.get(drawnCard.getSuit())+1);
    rankCount.replace(drawnCard.getRank(), rankCount.get(drawnCard.getRank())+1);
  }

  @Override
  public Card playCard() {
    Card bestCard = null;
    double bestCurrentScore = -1;
    for (Card i : hand){
      if (i.getSuit().equals(topCard.getSuit())|| (i.getRank().equals(topCard.getRank()))){
        double score = suitCount.get(i.getSuit()) / 13d + (rankCount.get(i.getRank()) /4);
        for (Card j  : goodPlay){
          if (i.getSuit().equals(j.getSuit())||(i.getRank().equals(j.getRank()))){
            score += 0.25;
          }
        }
        for (Suit j  : badPlay){
          if (i.getSuit().equals(j)){
            score -= 0.25;
          }
        }
        if(score > bestCurrentScore){
          bestCurrentScore = score;
          bestCard = i;
        }
      }
    }
    hand.remove(bestCard);
    return bestCard;
  }

  @Override
  public Suit declareSuit() {
    Suit bestSuit = null;
    double bestCurrentScore = 0;
    for (Suit i : Suit.values()){
      double score = -1;
      boolean inHand = false;
      for(Card j : hand){
        if (j.getSuit().equals(i)){
          inHand = true;
          break;
        }
      }

      if (inHand){
        for (Card j  : goodPlay){
          if (i.equals(j.getSuit())){
            score += 0.25;
          }
        }
        for (Suit j  : badPlay){
          if (i.equals(j)){
            score -= 0.25;
          }
        }
        if(score > bestCurrentScore){
          bestCurrentScore = score;
          bestSuit = i;
        }
      }
    }
    return bestSuit;
  }

  @Override
  public void processOpponentActions(List<PlayerTurn> opponentActions) {
    for (PlayerTurn i: opponentActions){
      Card cardPlayed = i.playedCard;
      if(cardPlayed != null){
        suitCount.replace(cardPlayed.getSuit(), suitCount.get(cardPlayed.getSuit())+1);
        rankCount.replace(cardPlayed.getRank(), rankCount.get(cardPlayed.getRank())+1);
      }
      else if(i.drewACard && i.playerId > playerId){
        for(PlayerTurn j :opponentActions){
          if(j.playerId < i.playerId ){
            goodPlay.add(j.playedCard);
          }
        }
      }
      else if(i.declaredSuit != null){
        badPlay.add(i.declaredSuit);
      }
    }
  }

  @Override
  public void reset() {
    hand = null;
  }
}
