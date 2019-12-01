package edu.cnm.deepdive.dominionendpointtestspring.controller;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfoTransferObject;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.TurnRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.aggregates.GameStateInfo;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.service.GameLogic;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@ExposesResourceFor(Game.class)
public class GameController {

  @Autowired
  private StateMachine<GameStates, GameEvents> stateMachine;
  @Autowired
  private  GameRepository gameRepository;
  @Autowired
  private TurnRepository turnRepository;

  @Autowired
  private GameLogic gameLogic;


  private static GameStateInfo gameStateInfo;

  private static GameParameters gameParameters;

  @Autowired
  public GameController() {

  }

  @PostMapping(value = "/newgame")
  public GameStateInfoTransferObject newGame() {
    GameStateInfo gameState = startNewGame();
    return buildTransferObject(gameState);
  }

 private GameStateInfo startNewGame() {
  GameParameters.setPlayer1(new Player("Danny"));
  GameParameters.setPlayer2(new Player("Erica"));
  Game game = new Game("Danny", "Erica");
  gameRepository.save(game);
  GameParameters.setCurrentGame(game);
  Turn firstTurn = new Turn(game, gameParameters.getPlayer1());
  GameParameters.setCurrentTurn(firstTurn);
  turnRepository.save(firstTurn);
  GameParameters.setCurrentPlayer(gameParameters.getPlayer1());
  stateMachine.sendEvent(GameEvents.START_GAME);
  GameParameters.setStacks(initializeStacks());
  GameStateInfo gameState = new GameStateInfo(game, GameParameters.getCurrentTurn());
  return gameState;
 }

 public HashMap<String, Integer> initializeStacks() {
    HashMap<String, Integer> stack = new HashMap<>();
    stack.put("Copper", 60);
    stack.put("Silver", 40);
    stack.put("Gold", 30);
    stack.put("Estate", 24);
    stack.put("Duchy", 12);
    stack.put("Province", 12);
    stack.put("Cellar", 10);
    stack.put("Moat", 10);
    stack.put("Village", 10);
    stack.put("Workshop", 10);
    stack.put("Smithy", 10);
    stack.put("Remodel", 10);
    stack.put("Militia", 10);
    stack.put("Market", 10);
    stack.put("Mine", 10);
    stack.put("Merchant", 10);
    stack.put("Trash", 0);
    return stack;
  }



  @GetMapping(value = "/getstate")
  public String getState() {
    return stateMachine.getState().getId().toString();
  }

  @PostMapping("/{playerid}/endphase")
 public String endPhase(@PathVariable("playerid") long playerId) {
  boolean isOver;
  Turn turn = GameParameters.getCurrentTurn();
  switch (stateMachine.getState().getId()) {
   case PLAYER_1_DISCARDING:
    if (playerId==1L) {
     stateMachine.sendEvent(GameEvents.PLAYER_1_END_DISCARDS);
    } else{
     return "Invalid action";
    }
    break;
   case PLAYER_1_ACTION:
    if (playerId==1L) {
     stateMachine.sendEvent(GameEvents.PLAYER_1_END_ACTIONS);
    } else{
     return "Invalid action";
    }
    break;
   case PLAYER_1_BUYING:
    if (playerId==1L) {
     isOver = gameLogic.testForVictory();
     if (isOver) {
      stateMachine.sendEvent(GameEvents.END_GAME);
     } else {
      stateMachine.sendEvent(GameEvents.PLAYER_1_END_BUYS);
      turn = new Turn(GameParameters.getCurrentGame(),
          GameParameters.getCurrentPlayer());
      GameParameters.setCurrentPlayer(GameParameters.getPlayer2());
      GameParameters.setCurrentTurn(turn);
      turnRepository.save(turn);
     }
    } else{
     return "Invalid action";
    }
    break;
   case PLAYER_2_DISCARDING:
    if (playerId==2L) {
     stateMachine.sendEvent(GameEvents.PLAYER_2_END_DISCARDS);
    } else{
     return "Invalid action";
    }
    break;
   case PLAYER_2_ACTION:
    if (playerId==2L) {
     stateMachine.sendEvent(GameEvents.PLAYER_2_END_ACTIONS);
    } else{
     return "Invalid action";
    }
    break;
   case PLAYER_2_BUYING:
    if (playerId==2L) {
     isOver = gameLogic.testForVictory();
     if (isOver) {
      stateMachine.sendEvent(GameEvents.END_GAME);
     } else {
      stateMachine.sendEvent(GameEvents.PLAYER_2_END_BUYS);
      turn = new Turn(GameParameters.getCurrentGame(),
          GameParameters.getCurrentPlayer());
      GameParameters.setCurrentPlayer(GameParameters.getPlayer1());
      GameParameters.setCurrentTurn(turn);
      turnRepository.save(turn);
     }
    } else{
     return "Invalid action";
    }
    break;
   default:
    return "Invalid Request";
  }
  GameParameters.setCurrentState(stateMachine.getState().getId());
  return "Turn: " + turn.getTurnId() + stateMachine.getState().getId().toString();
 }
 @PostMapping("/endphase")
 public GameStateInfoTransferObject endPhase() {
  boolean isOver;
  Turn turn = GameParameters.getCurrentTurn();
  switch (stateMachine.getState().getId()) {
   case PLAYER_1_DISCARDING:

     stateMachine.sendEvent(GameEvents.PLAYER_1_END_DISCARDS);
    break;
   case PLAYER_1_ACTION:
     stateMachine.sendEvent(GameEvents.PLAYER_1_END_ACTIONS);

    break;
   case PLAYER_1_BUYING:

     isOver = gameLogic.testForVictory();
     if (isOver) {
      stateMachine.sendEvent(GameEvents.END_GAME);
     } else {

      if (GameParameters.getCurrentTurn().isDidAttack()){
        stateMachine.sendEvent(GameEvents.PLAYER_1_END_BUYS);
      }else{
        stateMachine.sendEvent(GameEvents.PLAYER_2_END_DISCARDS);
      }
     createNewTurn();
     }

    break;
   case PLAYER_2_DISCARDING:

     stateMachine.sendEvent(GameEvents.PLAYER_2_END_DISCARDS);

    break;
   case PLAYER_2_ACTION:

     stateMachine.sendEvent(GameEvents.PLAYER_2_END_ACTIONS);

    break;
   case PLAYER_2_BUYING:
     isOver = gameLogic.testForVictory();
     if (isOver) {
      stateMachine.sendEvent(GameEvents.END_GAME);
     } else {

       if (GameParameters.getCurrentTurn().isDidAttack()) {
         stateMachine.sendEvent(GameEvents.PLAYER_1_END_BUYS);
       } else {
         stateMachine.sendEvent(GameEvents.PLAYER_2_END_DISCARDS);
       }
       createNewTurn();
     }
    break;
   default:
    break;
  }
  GameParameters.setCurrentState(stateMachine.getState().getId());
 GameStateInfo gameState = new GameStateInfo(GameParameters.getCurrentGame(), GameParameters.getCurrentTurn());
  GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
  return gameStateInfoTransferObject;
 }
private void createNewTurn(){
  Turn turn = new Turn(GameParameters.getCurrentGame(),
      GameParameters.getCurrentPlayer());
  GameParameters.setCurrentPlayer(GameParameters.getPlayer1());
  GameParameters.setCurrentTurn(turn);
  turnRepository.save(turn);
}
  @GetMapping(value = "/gamestateinfo")
  public GameStateInfoTransferObject getGameInfo() {
   GameStateInfo gameState;
   if (GameParameters.getCurrentGame()==null){
   gameState = startNewGame();
   }else{
    gameState = new GameStateInfo(GameParameters.getCurrentGame(), GameParameters.getCurrentTurn());
   }
    GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
    return gameStateInfoTransferObject;
  }

  private GameStateInfoTransferObject buildTransferObject(GameStateInfo gameState) {
    GameStateInfoTransferObject transfer = new GameStateInfoTransferObject(
        gameState.getPlayerStateInfoPlayer1().getHand().getCardsInHand(),
        gameState.getPlayerStateInfoPlayer1().getPlayer().getPlayerScore(),
        gameState.getPlayerStateInfoPlayer2().getPlayer().getPlayerScore(),
        gameState.getPlayerStateInfoPlayer1().getTurn().getActionsRemaining(),
        gameState.getPlayerStateInfoPlayer1().getTurn().getBuysRemaining(),
        gameState.getPlayerStateInfoPlayer1().calculateBuyingPower(),
        GameParameters.getStacks(),
        gameState.getPlaysInPreviousTurn(),
        this.stateMachine.getState().getId().toString()
    );
    return transfer;
  }
 private GameStateInfoTransferObject buildTransferObjectWithWrapper(GameStateInfo gameState, boolean wasSuccessful, String message) {
  GameStateInfoTransferObject transfer = new GameStateInfoTransferObject(
      gameState.getPlayerStateInfoPlayer1().getHand().getCardsInHand(),
      gameState.getPlayerStateInfoPlayer1().getPlayer().getPlayerScore(),
      gameState.getPlayerStateInfoPlayer2().getPlayer().getPlayerScore(),
      gameState.getPlayerStateInfoPlayer1().getTurn().getActionsRemaining(),
      gameState.getPlayerStateInfoPlayer1().getTurn().getBuysRemaining(),
      gameState.getPlayerStateInfoPlayer1().calculateBuyingPower(),
      GameParameters.getStacks(),
      gameState.getPlaysInPreviousTurn(),
      this.stateMachine.getState().getId().toString(),
      message,
      wasSuccessful
  );
  return transfer;
 }



  /**
   * @PostMapping("{cardid}/buy") public GameStateInfo playerBuysTarget(@PathVariable int gameId,
   * int playerId, CardType cardType ){ //return gameLogic.buyTarget(cardType, playerId, gameId);
   * return new GameStateInfo(); }
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(Exception.class)
  public void badRequest() {
  }

  /**
   * @Bean public DefaultStateMachineAdapter<GameStates, GameEvents, Game> orderStateMachineAdapter(
   * StateMachineFactory<GameStates, GameEvents> stateMachineFactory,
   * StateMachinePersister<GameStates, GameEvents, ContextEntity<GameStates, GameEvents,
   * Serializable>> persister) { return new DefaultStateMachineAdapter<GameStates, GameEvents,
   * Game>(stateMachineFactory, persister); }
   * @Bean public ContextObjectResourceProcessor<GameStates, GameEvents, Game>
   * orderResourceProcessor( EntityLinks entityLinks, DefaultStateMachineAdapter<GameStates,
   * GameEvents, ContextEntity<GameStates, GameEvents, Game>> gameStateMachineAdapter) { return new
   * ContextObjectResourceProcessor<GameStates, GameEvents, Game>(entityLinks,
   * gameStateMachineAdapter); }
   * @Bean public DefaultStateMachinePersister<GameStates, GameEvents, Game> persister(
   * StateMachinePersist<GameStates, GameEvents, ContextEntity<GameStates, GameEvents,
   * Serializable>> persist) { return new DefaultStateMachinePersister<GameStates, GameEvents,
   * Game>(persist); }
   * @Bean public StateMachinePersist<GameStates,GameEvents, Game> persist() { return new
   * StateMachinePersist<GameStates, GameEvents, Game>() {
   * @Override public void write(StateMachineContext<GameStates, GameEvents> stateMachineContext,
   * Game game) throws Exception { game.setStateMachineContext(stateMachineContext); }
   * @Override public StateMachineContext<GameStates, GameEvents> read(Game game) throws Exception {
   * return game.getStateMachineContext(); }
   * <p>
   * <p>
   * <p>
   * <p>
   * }; }
   */
  public static class GameParameters {

    private static Game currentGame;
    private static Player player1;
    private static Player player2;
    private static Turn currentTurn;
    private static ArrayList<Player> players = new ArrayList<>();
    private static Player currentPlayer;
    private static GameStates currentState;
    private static HashMap<String, Integer> stacks;

    static {

    }

    public static HashMap<String, Integer> getStacks() {
      return stacks;
    }

    public static void setStacks(HashMap<String, Integer> newStacks) {
     stacks = newStacks;
    }

    public static Game getCurrentGame() {
      return currentGame;
    }

    public static void setCurrentGame(
        Game currentGame) {
      GameParameters.currentGame = currentGame;
    }

    public static GameStates getCurrentState() {
      return currentState;
    }

    public static void setCurrentState(
        GameStates currentState) {
      GameParameters.currentState = currentState;
    }

    public static Player getPlayer1() {
      return player1;
    }

    public static void setPlayer1(Player player1) {
      GameParameters.player1 = player1;
    }

    public static Player getPlayer2() {
      return player2;
    }

    public static void setPlayer2(Player player2) {
      GameParameters.player2 = player2;
    }

    public static Turn getCurrentTurn() {
      return currentTurn;
    }

    public static void setCurrentTurn(
        Turn currentTurn) {
      GameParameters.currentTurn = currentTurn;
    }

    public static ArrayList<Player> getPlayers() {
      return players;
    }

    public static void setPlayers(
        ArrayList<Player> players) {
      GameParameters.players = players;
    }

   public static Player getCurrentPlayer() {
    return currentPlayer;
   }

   public static void setCurrentPlayer(
       Player currentPlayer) {
    GameParameters.currentPlayer = currentPlayer;
   }
  }

}
