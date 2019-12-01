package edu.cnm.deepdive.dominionendpointtestspring.model.aggregates;

import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.TurnRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Play;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card.CardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.statemachine.StateMachine;

public class GameStateInfo implements Serializable {

  private PlayerStateInfo playerStateInfoPlayer1;
  private PlayerStateInfo playerStateInfoPlayer2;
  private static boolean militiaFlag;
  private TurnRepository turnRepository;
  private PlayerRepository playerRepository;
  private Game game;
  private List<Turn> previousTurns;
  private long currentPlayerId;
  private StateMachine stateMachine;
  private HashMap<String, Integer> stacks;
  private PlayRepository playRepository;
  private Turn thisTurn;



  public GameStateInfo(Game game, Turn turn) {

    this.game=game;
    this.thisTurn = turn;
    this.stacks = game.getStacks();
    Player player1 = new Player ("Erica");
    Player player2= new Player("Danny");
    intializePlayer(player1);
    intializePlayer(player2);
    playerStateInfoPlayer1 = new PlayerStateInfo(game, player1);
    playerStateInfoPlayer2 = new PlayerStateInfo(game, player2);
    previousTurns = new ArrayList<>();
    previousTurns.add(new Turn(game, player2));
        //turnRepository.getAllByOrderByTurnIdAsc();
    //go out to state machine and get who is playing

  }

  private void intializePlayer(Player player) {
    player.setNumBuy(1);
    player.setNumAction(1);
  }

  public PlayerStateInfo getCurrentPlayerStateInfo(){
    switch((int) currentPlayerId) {
      case 1:
        return playerStateInfoPlayer1;
      case 2:
        return playerStateInfoPlayer2;
    }
    return null;
  }
  public PlayerStateInfo getPlayerStateInfoPlayer1() {
    return playerStateInfoPlayer1;
  }

  public void setPlayerStateInfoPlayer1(
      PlayerStateInfo playerStateInfoPlayer1) {
    this.playerStateInfoPlayer1 = playerStateInfoPlayer1;
  }

  public PlayerStateInfo getPlayerStateInfoPlayer2() {
    return playerStateInfoPlayer2;
  }

  public void setPlayerStateInfoPlayer2(
      PlayerStateInfo playerStateInfoPlayer2) {
    this.playerStateInfoPlayer2 = playerStateInfoPlayer2;
  }


  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public List<Turn> getPreviousTurns() {
    return previousTurns;
  }

  public void setPreviousTurns(
      List<Turn> previousTurns) {
    this.previousTurns = previousTurns;
  }
  public Optional<Player> getCurrentPlayer() {
    return playerRepository.findPlayerById(currentPlayerId);
  }

  public void setCurrentPlayer(long playerId) {
    this.currentPlayerId = playerId;
  }
/**
  public void saveAll() {
    playerStateInfoPlayer2.saveAll();
    playerStateInfoPlayer1.saveAll();
  }
*/
  public List<String> getPlaysInPreviousTurn(){
    List<String> playList = new ArrayList<>();
    Play play1 = new Play((long) 1, new Turn(game, getPlayerStateInfoPlayer1().getPlayer()),
        new Card(CardType.MARKET));
    Play play2 = new Play((long) 2, new Turn(game, getPlayerStateInfoPlayer1().getPlayer()), 2,
        new Card(CardType.CELLAR));
    playList.add(play1.toString());
    playList.add(play2.toString());
    return playList;
  }





  public enum StackTypes {
    Bronze,
    Silver,
    Gold,
    Estate,
    Duchy,
    Province,
    Cellar,
    Moat,
    Village,
    Workshop,
    Smithy,
    Remodel,
    Militia,
    Market,
    Mine,
    Merchant,
    Trash;

    String[] symbols = {"Bronze", "Silver", "Gold", "Estate", "Duchy", "Province", "Cellar", "Moat",
        "Village",
        "Workshop", "Smithy", "Remodel", "Militia", "Market", "Mine", "Merchant", "Trash"};



    private String getSymbol() {
      return symbols[ordinal()];
    }
  }
   //public void currentPlayerDraws(){
   //  getCurrentPlayer().drawCard();
 // }

}
