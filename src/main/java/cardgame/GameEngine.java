package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GameEngine {

  public static void main() {
    PlayerStrategy a = new BasicStrategy();
    PlayerStrategy b = new BasicStrategy();
    PlayerStrategy c = new CardCountingStrategy();
    PlayerStrategy d = new CardCountingStrategy();
    try {
      startTournament(a, b, c, d);
    }catch (Exception e){
      System.out.println("Cheating Detected");
    }

  }

  public static void startTournament(PlayerStrategy... players) throws Exception {
    List<Integer> idList = new ArrayList<Integer>();
    for (int i = 0; i < players.length; i++) {
      idList.add(i);
    }
    for (int i = 0; i < players.length; i++) {
      List<Integer> opponentIds = new ArrayList<Integer>();
      opponentIds = idList;
      opponentIds.remove(i);
      players[i].init(idList.get(i), opponentIds);
    }

    List<Integer> playerScores = new ArrayList<Integer>();
    while (Collections.max(playerScores) < 200) {
      Game game = new Game(players);
      Map<PlayerStrategy,List<Card>> scores = game.getScores();
      int tempScores =0;
      for(List <Card> i : scores.values())
      {
        for(Card j : i)
        tempScores += j.getPointValue();
      }
      for (int i = 0; i < players.length; i++) {
        playerScores.set(i, playerScores.get(i)+tempScores);
      }
    }
  }
}




