package edu.cnm.deepdive.dominionendpointtestspring;


import edu.cnm.deepdive.dominionendpointtestspring.controller.PlayController;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Player.PlayerState;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Stack;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Stack.StackType;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.enums.States;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameStateInfo implements Serializable {
  Logger logger = LoggerFactory.getLogger(GameStateInfo.class);
  private PlayerStateInfo playerStateInfoPlayer1;
  private PlayerStateInfo playerStateInfoPlayer2;
//  private StackRepository stackRepository;
//  private TurnRepository turnRepository;
//  private PlayerRepository playerRepository;
  private Game game;
  private ArrayList<Turn> previousTurns;
  private long currentPlayerId;
//  private StateMachine stateMachine;
  private ArrayList<Stack> stacks;
//  private PlayRepository playRepository;
  private String[] stackTypes;
  private States states;


  public GameStateInfo(Game game) {
    this.game=game;
    Player player1 = game.getPlayers().get(0);
    Player player2= game.getPlayers().get(1);
    player1.setPlayerState(PlayerState.ACTION);
    player2.setPlayerState(PlayerState.WATCHING);
    this.states= States.PLAYER_1_TURN;
    this.stacks = new ArrayList<Stack>();
    Random rng = new Random(10);
      stackTypes = new String[]{"Bronze", "Silver", "Gold", "Estate", "Duchy", "Province", "Cellar", "Moat",
              "Village",
              "Workshop", "Smithy", "Remodel", "Militia", "Market", "Mine", "Merchant", "Trash"};
    for(int i = 0; i<stackTypes.length; i++){
      stacks.add(new Stack(game, stackTypes[i], rng.nextInt()));
    }
    playerStateInfoPlayer1 = new PlayerStateInfo(game, player1);
    playerStateInfoPlayer2 = new PlayerStateInfo(game, player2);
    previousTurns = new ArrayList<>();
    previousTurns.add(new Turn(game, player1));
    //go out to state machine and get who is playing
    //State state = stateMachine.getState();
      currentPlayerId = 1;
  }

  public GameStateInfo() {

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


 /** public void saveAll() {
    playerStateInfoPlayer2.saveAll();
    playerStateInfoPlayer1.saveAll();
    for (Stack stack: stacks){
      stackRepository.save(stack);
    }

*/


  public List<Stack> getStacks() {
    return stacks;
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
