package edu.cnm.deepdive.dominionendpointtestspring.model.aggregates;

import edu.cnm.deepdive.dominionservice.model.dao.PlayRepository;
import edu.cnm.deepdive.dominionservice.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionservice.model.dao.StackRepository;
import edu.cnm.deepdive.dominionservice.model.dao.TurnRepository;
import edu.cnm.deepdive.dominionservice.model.entity.Game;
import edu.cnm.deepdive.dominionservice.model.entity.Player;
import edu.cnm.deepdive.dominionservice.model.entity.Stack;
import edu.cnm.deepdive.dominionservice.model.entity.Turn;
import edu.cnm.deepdive.dominionservice.model.enums.States;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;

public class GameStateInfo implements Serializable {

  private PlayerStateInfo playerStateInfoPlayer1;
  private PlayerStateInfo playerStateInfoPlayer2;
  private StackRepository stackRepository;
  private TurnRepository turnRepository;
  private PlayerRepository playerRepository;
  private Game game;
  private List<Turn> previousTurns;
  private long currentPlayerId;
  private StateMachine stateMachine;
  private List<Stack> stacks;
  private PlayRepository playRepository;



  public GameStateInfo(Game game) {
    this.game=game;
    Player player1 = game.getPlayers().get(1);
    Player player2= game.getPlayers().get(2);
    this.stacks = stackRepository.getAllByGameId(game.getId());
    playerStateInfoPlayer1 = new PlayerStateInfo(game, player1);
    playerStateInfoPlayer2 = new PlayerStateInfo(game, player2);
    previousTurns = (List<Turn>) turnRepository.getAllByOrderByIdAsc();
    //go out to state machine and get who is playing
    State state = stateMachine.getState();
    if (States
        .PLAYER_1_TURN.equals(state)) {
      currentPlayerId = 1;
    } else if (States.PLAYER_2_TURN.equals(state)) {
      currentPlayerId = 2;
    }
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

  public void saveAll() {
    playerStateInfoPlayer2.saveAll();
    playerStateInfoPlayer1.saveAll();
    for (Stack stack: stacks){
      stackRepository.save(stack);
    }


  }

  public List<Stack> getStacks() {
    return stacks;
  }

  public void setStacks(List<Stack> stacks) {
    this.stacks = stacks;
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

    public String toString(Stack.StackType stackType) {
      return stackType.getSymbol();
    }

    private String getSymbol() {
      return symbols[ordinal()];
    }
  }
   //public void currentPlayerDraws(){
   //  getCurrentPlayer().drawCard();
 // }

}
