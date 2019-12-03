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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
  @PostMapping(value="/newgame")
  public GameStateInfoTransferObject startNewGame(Authentication authentication) {
    Player player = (Player) authentication.getPrincipal();
    /**
     * If this is the first game in the database, create new and set to Waiting
     */
    if (gameRepository.getAllByOrderByIdDesc().size()==0){
      Game newGame = new Game();
      newGame.getPlayers().add(player);
      player.setGameOrder(1);
      newGame.setCurrentState(GameStates.WAITING);
      gameRepository.save(newGame);
      int gameID = newGame.getId();
     // firebaseMessagingSnippets.sendToToken("Game Has Begun. Waiting for Other Player.");
      return newGameSolo(GameStates.INITIAL, gameID);
    }
    /**
     * otherwise get the first game in the database that is in the WAITING state and sets the players
     */
    Optional<Game> newGame = Optional
        .ofNullable(gameRepository.getFirstByCurrentState(GameStates.WAITING));
    if (newGame.isPresent()){
      newGame.get().join(player);
      player.setGameOrder(2);
      Player playerTwo = player;
      Player playerOne = newGame.get().getPlayers().get(0);
     // Player playerTwo = playerRepository.getPlayerByOauthKey(uid);
     // playerTwo.setGameOrder(2);
     // Player playerOne = playerRepository.getPlayerByOauthKey(newGame.get().getPlayer1UID());
     // playerOne.setGameOrder(1);
      Turn firstTurn = new Turn(newGame.get(), playerOne);
      stateMachine.sendEvent(GameEvents.START_GAME);
      newGame.get().setStack(gameLogic.initializeStacks());
      GameStateInfo gameState = new GameStateInfo(newGame.get(), firstTurn,
         playerOne,
          playerTwo);
      return buildTransferObjectWithWrapper(gameState, true, "Game Created!");
    }
    else{
      Game newGameNonNull = new Game(player);
      gameRepository.save(newGameNonNull);
      newGameNonNull.setCurrentState(GameStates.WAITING);
      int gameID = newGameNonNull.getId();
      //  firebaseMessagingSnippets.sendToToken("Game Has Begun. Waiting for Other Player.");
      return newGameSolo(GameStates.WAITING, gameID);
    }

  }

  public GameStateInfoTransferObject newGameSolo(GameStates gameStates, int gameId) {
    return new GameStateInfoTransferObject(gameStates, gameId);

  }
  @GetMapping("/getmynewgame/{gameid}")
  public GameStateInfoTransferObject getInitialConditions(@PathVariable("gameid") int gameId, Authentication authentication){
    Game game = gameRepository.getGameById(gameId);
    Player player = (Player) authentication.getPrincipal();
    Turn firstTurn = new Turn(game,player);
    stateMachine.sendEvent(GameEvents.START_GAME);
    game.setStack(gameLogic.initializeStacks());
    GameStateInfo gameState = new GameStateInfo(game, firstTurn, playerRepository.getPlayerByOauthKey(game.getPlayer1UID())
        ,playerRepository.getPlayerByOauthKey(game.getPlayer2UID()));
    return buildTransferObjectWithWrapper(gameState, true, "Game Created!");
  }




  @GetMapping("getstate/{gameid}")
  public String getState() {
    return stateMachine.getState().getId().toString();
  }


  @PostMapping("/endphase/{gameid}")
  public GameStateInfoTransferObject endPhase(Authentication authentication, @PathVariable("gameid") int gameId) {
    Game thisGame = gameRepository.getGameById(gameId);
    Player player1 = thisGame.getPlayers().get(0);
    Player player2 = thisGame.getPlayers().get(1);
    Player thisPlayer = (Player) authentication.getPrincipal();
    int playerPlace = thisPlayer.getGameOrder();
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
  @GetMapping("gamestateinfo/{gameid}")
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



 /** private GameStateInfoTransferObject buildTransferObject(GameStateInfo gameState) {
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
  }*/


  private GameStateInfoTransferObject buildTransferObjectWithWrapper(GameStateInfo gameState,
      boolean wasSuccessful, String message) {
    Turn lastTurn = getSpecificTurnsAgo(2);
    GameStateInfoTransferObject transfer = new GameStateInfoTransferObject(gameState.getGame().getId(),
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


  @PostMapping("/buy/{gameid}/{cardname}")
  public GameStateInfoTransferObject playerBuysTarget(Authentication authentication,
      @PathVariable("gameid") int gameId,
      @PathVariable ("cardname") String cardName,
      @RequestBody List<String> otherCards) {
    Game thisGame = gameRepository.getGameById(gameId);
    Player thisPlayer = (Player) authentication.getPrincipal();
    Turn thisTurn = getSpecificTurnsAgo(1);
    GameStateInfo gameStateInfo = new GameStateInfo(thisGame,
        thisTurn,  thisGame.getPlayers().get(0),thisGame.getPlayers().get(1));
    CardType cardType = CardType.valueOf(cardName);
    switch(thisPlayer.getGameOrder()){
      case 1:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(0))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_1_BUYING)){
          GameStateInfo gameState = gameLogic.buyTarget(cardType, thisGame.getPlayers().get(1), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "Bought "+cardName);
          return gameStateInfoTransferObject;
        }

      case 2:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(1))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_2_BUYING)){
          GameStateInfo gameState = gameLogic.buyTarget(cardType, thisGame.getPlayers().get(1), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "Bought "+cardName);
          return gameStateInfoTransferObject;
        }
    }

    GameStateInfoTransferObject transfer = buildTransferObjectWithWrapper(gameStateInfo, false, "Invalid Action");
   return transfer;
  }
  @PostMapping("/action/{gameid}/{cardname}")
  public GameStateInfoTransferObject playCard(@PathVariable ("gameid") int gameId,
      Authentication authentication,
      @PathVariable ("cardname") String cardName, @RequestBody ArrayList<String> otherCards){
    Game thisGame = gameRepository.getGameById(gameId);
    Player thisPlayer = (Player) authentication.getPrincipal();
    Turn thisTurn = getSpecificTurnsAgo(1);
    GameStateInfo gameStateInfo = new GameStateInfo(thisGame,
        thisTurn,  thisGame.getPlayers().get(0),thisGame.getPlayers().get(1));
    ArrayList<Card> cards = new ArrayList<>();
    for (String string : otherCards){
      CardType cardType = CardType.valueOf(string);
      cards.add(new Card(cardType));
    }
    CardType cardType = CardType.valueOf(cardName);
    switch(thisPlayer.getGameOrder()){
      case 1:

        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(0))
          && stateMachine.getState().getId().equals(GameStates.PLAYER_1_ACTION)){
          GameStateInfo gameState = gameLogic.playCardWithCards(cardType, thisGame.getPlayers().get(0), cards, gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "Played "+cardName);
          return gameStateInfoTransferObject;
        }

      case 2:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(1))&&
            stateMachine.getState().getId().equals(GameStates.PLAYER_2_ACTION)){
          GameStateInfo gameState = gameLogic.playCardWithCards(cardType, thisGame.getPlayers().get(1), cards, gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "Played "+cardName);
          return gameStateInfoTransferObject;
        }
    }

    GameStateInfoTransferObject transfer = buildTransferObjectWithWrapper(gameStateInfo, false, "Invalid Action");
    return transfer;
  }

  @PostMapping("/discard/{gameid}/{cardname}")
  public GameStateInfoTransferObject discardForMilitia(@PathVariable ("gameid") int gameId,
      Authentication authentication,
      @PathVariable ("cardname") String cardName){
    Game thisGame = gameRepository.getGameById(gameId);
    Player thisPlayer = (Player) authentication.getPrincipal();
    Turn thisTurn = getSpecificTurnsAgo(1);
    CardType cardType = CardType.valueOf(cardName);
    Card card = new Card(cardType);
    GameStateInfo gameStateInfo = new GameStateInfo(thisGame,
        thisTurn,  thisGame.getPlayers().get(0),thisGame.getPlayers().get(1));
    switch(thisPlayer.getGameOrder()){
      case 1:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(0))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_1_DISCARDING)){
          GameStateInfo gameState = gameLogic.militiaDiscard(card, thisGame.getPlayers().get(0), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "Discarded " +cardName);
          return gameStateInfoTransferObject;
        }

      case 2:
        if (gameStateInfo.getCurrentPlayerStateInfo().getPlayer().equals(thisGame.getPlayers().get(1))
            && stateMachine.getState().getId().equals(GameStates.PLAYER_2_DISCARDING)){
          GameStateInfo gameState = gameLogic.militiaDiscard(card, thisGame.getPlayers().get(1), gameStateInfo);
          GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObjectWithWrapper(gameState, true, "Discarded "+cardName );
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
