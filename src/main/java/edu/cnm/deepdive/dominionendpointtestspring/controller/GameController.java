package edu.cnm.deepdive.dominionendpointtestspring.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfoTransferObject;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.TurnRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.aggregates.GameStateInfo;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card.CardType;
import edu.cnm.deepdive.dominionendpointtestspring.service.FirebaseMessagingSnippets;
import edu.cnm.deepdive.dominionendpointtestspring.service.GameLogic;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private GameRepository gameRepository;
  @Autowired
  private TurnRepository turnRepository;
  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private GameLogic gameLogic;
  @Autowired
  private FirebaseMessagingSnippets firebaseMessagingSnippets;




  @Autowired
  public GameController() {

  }
  @PostMapping(value="/newgame/{uid}")
  public GameStateInfoTransferObject startNewGame(@PathVariable("uid") String uid)
      throws FirebaseMessagingException {
    if (gameRepository.getAllByOrderByIdDesc().size()==0){
      Game newGame = new Game(uid);
      newGame.setCurrentState(GameStates.INITIAL);
      gameRepository.save(newGame);
      int gameID = newGame.getId();
     // firebaseMessagingSnippets.sendToToken("Game Has Begun. Waiting for Other Player.");
      return newGameSolo(GameStates.INITIAL, gameID);
    }
    ArrayList<Game> games = gameRepository.getAllByOrderByIdDesc();

    for (Game game : games){
      if (game.getCurrentState().equals(GameStates.INITIAL)){
       game.join(uid);
       Player playerTwo = playerRepository.getPlayerByUid(uid);
       playerTwo.setGameOrder(2);
       Player playerOne = playerRepository.getPlayerByUid(game.getPlayer1UID());
       playerOne.setGameOrder(1);
       ArrayList<Player> players = new ArrayList<>();
       players.add(playerOne);
       players.add(playerTwo);
        game.setPlayers(players);
       // firebaseMessagingSnippets.sendToTokenGameStart(playerTwo.getPlayerFCMRegistrationToken(),
       //     game.getId(),playerOne.getUserName(), 2);
       // firebaseMessagingSnippets.sendToTokenGameStart(playerOne.getPlayerFCMRegistrationToken(),
       //     game.getId(), playerTwo.getUserName(), 1);
        //TODO fix
        return null;
      }else{
        Game newGame = new Game(uid);
        gameRepository.save(newGame);
        int gameID = newGame.getId();
      //  firebaseMessagingSnippets.sendToToken("Game Has Begun. Waiting for Other Player.");
        return newGameSolo(GameStates.INITIAL, gameID);
      }
    }
    return null;
  }

  public GameStateInfoTransferObject newGameSolo(GameStates gameStates, int gameId) {
    return new GameStateInfoTransferObject(gameStates, gameId);

  }
  @GetMapping(value="/getmynewgame/{gameid}/{uid}")
  public GameStateInfoTransferObject getInitialConditions(@PathVariable("gameid") int gameId,
   @PathVariable("uid") String uid){
    Game game = gameRepository.getGameById(gameId);
    Player player = playerRepository.getPlayerByUid(uid);
    Turn firstTurn = new Turn(game, playerRepository.getPlayerByUid(game.getPlayer1UID()));
    stateMachine.sendEvent(GameEvents.START_GAME);
    game.setStack(gameLogic.initializeStacks());
    GameStateInfo gameState = new GameStateInfo(game, firstTurn, playerRepository.getPlayerByUid(game.getPlayer1UID())
        ,playerRepository.getPlayerByUid(game.getPlayer2UID()));
    return buildTransferObjectWithWrapper(gameState, true, "Game Created!");
  }
  private GameStateInfo startNewGame() {
    //GameParameters.setPlayer1(new Player("Danny", 1L));
  //  GameParameters.setPlayer2(new Player("Erica", 2L));
    //Game game = new Game("Danny", "Erica");
   // gameRepository.save(game);
   // GameParameters.setCurrentGame(game);

 //   GameParameters.setCurrentTurn(firstTurn);
  //  turnRepository.save(firstTurn);
  //  GameParameters.setCurrentPlayer(gameParameters.getPlayer1());
  //  stateMachine.sendEvent(GameEvents.START_GAME);
  //  GameParameters.setStacks(initializeStacks());
 //   GameStateInfo gameState = new GameStateInfo(game, GameParameters.getCurrentTurn(), GameParameters.getPlayer1(),GameParameters.getPlayer2());
  return null;
  }



  @GetMapping(value = "/getstate")
  public String getState() {
    return stateMachine.getState().getId().toString();
  }


  @PostMapping("/{uid}/{gameid}/endphase")
  public GameStateInfoTransferObject endPhase(@PathVariable("uid") String uid, @PathVariable("gameid") int gameId) {
    Game thisGame = gameRepository.getGameById(gameId);
    Player player1 = thisGame.getPlayers().get(0);
    Player player2 = thisGame.getPlayers().get(1);
    int playerPlace = playerRepository.getPlayerByUid(uid).getGameOrder();
    boolean isOver;
    String message = "";
    boolean wasSuccessful = true;
    Turn turn = getSpecificTurnsAgo(1);
    switch (stateMachine.getState().getId()) {
      case PLAYER_1_DISCARDING:
        if (playerPlace == 1) {
          stateMachine.sendEvent(GameEvents.PLAYER_1_END_DISCARDS);
        } else {
          message = "Invalid action";
          wasSuccessful = false;
        }
        break;
      case PLAYER_1_ACTION:
        if (playerPlace == 1) {
          stateMachine.sendEvent(GameEvents.PLAYER_1_END_ACTIONS);
        } else {
          message = "Invalid action";
          wasSuccessful = false;
        }
        break;
      case PLAYER_1_BUYING:
        if (playerPlace == 1) {
          isOver = gameLogic.testForVictory();
          if (isOver) {
            stateMachine.sendEvent(GameEvents.END_GAME);
          } else {
            stateMachine.sendEvent(GameEvents.PLAYER_1_END_BUYS);
            turn = new Turn(thisGame,player2);
            turnRepository.save(turn);
          }
        } else {
          message = "Invalid action";
          wasSuccessful = false;
        }
        break;
      case PLAYER_2_DISCARDING:
        if (playerPlace == 2) {
          stateMachine.sendEvent(GameEvents.PLAYER_2_END_DISCARDS);
        } else {
          message = "Invalid action";
          wasSuccessful = false;
        }
        break;
      case PLAYER_2_ACTION:
        if (playerPlace == 2) {
          stateMachine.sendEvent(GameEvents.PLAYER_2_END_ACTIONS);
        } else {
          message = "Invalid action";
          wasSuccessful = false;
        }
        break;
      case PLAYER_2_BUYING:
        if (playerPlace == 2) {
          isOver = gameLogic.testForVictory();
          if (isOver) {
            stateMachine.sendEvent(GameEvents.END_GAME);
          } else {
            stateMachine.sendEvent(GameEvents.PLAYER_2_END_BUYS);
            turn = new Turn(thisGame, player1);
            turnRepository.save(turn);
          }
        } else {
          message = "Invalid action";
          wasSuccessful = false;
        }
        break;
      default:
        message = "Invalid action";
        wasSuccessful = false;
    }
    GameStateInfo gameState = new GameStateInfo(thisGame,
       turn, player1,player2);
    return buildTransferObjectWithWrapper(gameState, wasSuccessful, message);

  }
/**
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

         // if (GameParameters.getCurrentTurn().isDidAttack()) {
        //    stateMachine.sendEvent(GameEvents.PLAYER_1_END_BUYS);
        //  } else {
            stateMachine.sendEvent(GameEvents.PLAYER_2_END_DISCARDS);
         // }
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
    GameStateInfo gameState = new GameStateInfo(GameParameters.getCurrentGame(),
        GameParameters.getCurrentTurn(), GameParameters.getPlayer1(),GameParameters.getPlayer2());
    GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
    return gameStateInfoTransferObject;
  }
*/
/**
  private void createNewTurn() {
    Turn turn = new Turn(GameParameters.getCurrentGame(),
        GameParameters.getCurrentPlayer());
    GameParameters.setCurrentPlayer(GameParameters.getPlayer1());
    GameParameters.setCurrentTurn(turn);
    turnRepository.save(turn);
  }*/
  @GetMapping(value = "/gamestateinfo")
  public GameStateInfoTransferObject getGameState() {
    GameStateInfo gameState;
    //gameState = startNewGame();
   // GameParameters.setPlayer1(new Player("Danny", 1L));
  //  GameParameters.setPlayer2(new Player("Erica", 2L));
   // Game game = new Game("Danny", "Erica");
    //gameRepository.save(game);
  //  GameParameters.setCurrentGame(game);
  //  Turn firstTurn = new Turn(game, gameParameters.getPlayer1());
  //  GameParameters.setCurrentTurn(firstTurn);
    //turnRepository.save(firstTurn);
  //  GameParameters.setCurrentPlayer(gameParameters.getPlayer1());
   // stateMachine.sendEvent(GameEvents.START_GAME);
  //  GameParameters.setStacks(initializeStacks());
  //  gameState = new GameStateInfo(game, GameParameters.getCurrentTurn(), GameParameters.getPlayer1(),GameParameters.getPlayer2());
    /** if (GameParameters.getCurrentGame() == null) {
     gameState = startNewGame();
     } else {
     gameState = new GameStateInfo(GameParameters.getCurrentGame(),
     GameParameters.getCurrentTurn(), GameParameters.getPlayer1(),GameParameters.getPlayer2());
     }*/
 //   GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "You need to discard before taking actions!");
    return null;
  }

  @PostMapping(value = "/newgame")
  public GameStateInfoTransferObject getNewGame() {
    /**
    GameStateInfo gameState;
    //gameState = startNewGame();
    GameParameters.setPlayer1(new Player("Danny", 1L));
    GameParameters.setPlayer2(new Player("Erica", 2L));
    Game game = new Game("Danny", "Erica");
    //gameRepository.save(game);
    GameParameters.setCurrentGame(game);
    Turn firstTurn = new Turn(game, gameParameters.getPlayer1());
    GameParameters.setCurrentTurn(firstTurn);
    //turnRepository.save(firstTurn);
    GameParameters.setCurrentPlayer(gameParameters.getPlayer1());
    stateMachine.sendEvent(GameEvents.START_GAME);
    GameParameters.setStacks(initializeStacks());
   gameState = new GameStateInfo(game, GameParameters.getCurrentTurn(), GameParameters.getPlayer1(),GameParameters.getPlayer2());
   /** if (GameParameters.getCurrentGame() == null) {
      gameState = startNewGame();
    } else {
      gameState = new GameStateInfo(GameParameters.getCurrentGame(),
          GameParameters.getCurrentTurn(), GameParameters.getPlayer1(),GameParameters.getPlayer2());
    }*/
  //  GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "You need to discard before taking actions!");
    //return gameStateInfoTransferObject;
  return null;
  }


  private GameStateInfoTransferObject buildTransferObject(GameStateInfo gameState) {
    Turn lastTurn = getSpecificTurnsAgo(2);
    GameStateInfoTransferObject transfer = new GameStateInfoTransferObject(
        gameState.getPlayerStateInfoPlayer1().getHand().getCardsInHand(),
        gameState.getPlayerStateInfoPlayer1().getPlayer().getPlayerScore(),
        gameState.getPlayerStateInfoPlayer2().getPlayer().getPlayerScore(),
        gameState.getCurrentPlayerStateInfo().getTurn().getActionsRemaining(),
        gameState.getCurrentPlayerStateInfo().getTurn().getBuysRemaining(),
        gameState.getCurrentPlayerStateInfo().calculateBuyingPower(),
        gameState.getGame().getStacks(),
        gameState.getPlaysInPreviousTurn(),
        this.stateMachine.getState().getId().toString(),
        (lastTurn.isDidAttack()&&(!gameState.getCurrentPlayerStateInfo().getPlayer().isHasMoat()))
    );
    return transfer;
  }


  private GameStateInfoTransferObject buildTransferObjectWithWrapper(GameStateInfo gameState,
      boolean wasSuccessful, String message) {
    Turn lastTurn = getSpecificTurnsAgo(2);
    GameStateInfoTransferObject transfer = new GameStateInfoTransferObject(
        gameState.getPlayerStateInfoPlayer1().getHand().getCardsInHand(),
        gameState.getPlayerStateInfoPlayer1().getPlayer().getPlayerScore(),
        gameState.getPlayerStateInfoPlayer2().getPlayer().getPlayerScore(),
        gameState.getCurrentPlayerStateInfo().getTurn().getActionsRemaining(),
        gameState.getCurrentPlayerStateInfo().getTurn().getBuysRemaining(),
        gameState.getCurrentPlayerStateInfo().calculateBuyingPower(),
        gameState.getGame().getStacks(),
        gameState.getPlaysInPreviousTurn(),
        this.stateMachine.getState().getId().toString(),
        (lastTurn.isDidAttack()&&(!gameState.getCurrentPlayerStateInfo().getPlayer().isHasMoat())),
       message,
        wasSuccessful
    );
    return transfer;
  }

  //i= 1 get this turn, i=2 get last turn
  private Turn getSpecificTurnsAgo(int i) {
    ArrayList<Turn> turns = turnRepository.getAllByOrderByTurnIdDesc();
    return turns.get(turns.size() - i);
  }


  @PostMapping("/{gameid}/{playeruid}{cardname}/buy")
  public GameStateInfoTransferObject playerBuysTarget(@PathVariable ("gameid") int gameId,
      @PathVariable("playeruid") String uid,
      @PathVariable ("cardname") String cardName) {
    Game thisGame = gameRepository.getGameById(gameId);
    Player thisPlayer = playerRepository.getPlayerByUid(uid);
    Turn thisTurn = getSpecificTurnsAgo(1);
    GameStateInfo gameStateInfo = new GameStateInfo(thisGame,
        thisTurn,  thisGame.getPlayers().get(0),thisGame.getPlayers().get(1));
    CardType cardType = CardType.valueOf(cardName);
    switch(thisPlayer.getGameOrder()){
      case 1:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(0))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_1_BUYING)){
          GameStateInfo gameState = gameLogic.buyTarget(cardType, thisGame.getPlayers().get(1), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
          return gameStateInfoTransferObject;
        }

      case 2:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(1))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_2_BUYING)){
          GameStateInfo gameState = gameLogic.buyTarget(cardType, thisGame.getPlayers().get(1), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
          return gameStateInfoTransferObject;
        }
    }

    GameStateInfoTransferObject transfer = buildTransferObjectWithWrapper(gameStateInfo, false, "Invalid Action");
   return transfer;
  }
  @PostMapping("/{gameid}/{uid}/{cardname}/action")
  public GameStateInfoTransferObject playCard(@PathVariable ("gameid") int gameId,
      @PathVariable ("uid") String uid,
      @PathVariable ("cardname") String cardName, @RequestBody ArrayList<Card> cards){
    Game thisGame = gameRepository.getGameById(gameId);
    Player thisPlayer = playerRepository.getPlayerByUid(uid);
    Turn thisTurn = getSpecificTurnsAgo(1);
    GameStateInfo gameStateInfo = new GameStateInfo(thisGame,
        thisTurn,  thisGame.getPlayers().get(0),thisGame.getPlayers().get(1));
    CardType cardType = CardType.valueOf(cardName);
    switch(thisPlayer.getGameOrder()){
      case 1:

        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(0))
          && stateMachine.getState().getId().equals(GameStates.PLAYER_1_ACTION)){
          GameStateInfo gameState = gameLogic.playCardWithCards(cardType, thisGame.getPlayers().get(0), cards, gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
          return gameStateInfoTransferObject;
        }

      case 2:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(1))&&
            stateMachine.getState().getId().equals(GameStates.PLAYER_2_ACTION)){
          GameStateInfo gameState = gameLogic.playCardWithCards(cardType, thisGame.getPlayers().get(1), cards, gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
          return gameStateInfoTransferObject;
        }
    }

    GameStateInfoTransferObject transfer = buildTransferObjectWithWrapper(gameStateInfo, false, "Invalid Action");
    return transfer;
  }

  @PostMapping("/{playerid}/discard")
  public GameStateInfoTransferObject discardForMilitia(@PathVariable ("gameid") int gameId,
      @PathVariable ("uid") String uid,
      @PathVariable ("cardname") String cardName, @RequestBody ArrayList<Card> cards){
    Game thisGame = gameRepository.getGameById(gameId);
    Player thisPlayer = playerRepository.getPlayerByUid(uid);
    Turn thisTurn = getSpecificTurnsAgo(1);
    GameStateInfo gameStateInfo = new GameStateInfo(thisGame,
        thisTurn,  thisGame.getPlayers().get(0),thisGame.getPlayers().get(1));
    switch(thisPlayer.getGameOrder()){
      case 1:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(0))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_1_DISCARDING)){
          GameStateInfo gameState = gameLogic.militiaDiscard(cards, thisGame.getPlayers().get(0), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
          return gameStateInfoTransferObject;
        }

      case 2:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(1))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_2_DISCARDING)){
          GameStateInfo gameState = gameLogic.militiaDiscard(cards, thisGame.getPlayers().get(0), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
          return gameStateInfoTransferObject;
        }
    }
    GameStateInfoTransferObject transfer = buildTransferObjectWithWrapper(gameStateInfo, false, "Invalid Action");
    return transfer;
  }

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
}
