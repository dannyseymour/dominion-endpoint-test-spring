package edu.cnm.deepdive.dominionendpointtestspring.service;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfo;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Transactional
@WithStateMachine
public class GameLogic {

  private Game game;

  private StateMachine<GameStates, GameEvents> stateMachine;


  @Autowired
  GameRepository gameRepository;

  @Autowired
  TurnRepository turnRepository;

  @Autowired
  PlayRepository playRepository;

  @Autowired
  PlayerRepository playerRepository;





  public GameStateInfo playCardWithCards(int cardId, long gameId, int playerId, ArrayList<Card> cards) {
    GameStateInfo gameStateInfo = new GameStateInfo(gameRepository.getGameById(gameId));
    Card playingCard = new Card(cardRepository.getCardTypeById(cardId).get());
    playingCard.getCardType().play(gameStateInfo, cards);
    if(gameStateInfo.getCurrentPlayerStateInfo().getThisTurn().getActionsRemaining()==0){
      endActions(gameStateInfo);
      gameStateInfo.getCurrentPlayerStateInfo().setPhaseState(PhaseState.DOING_BUYS);
    }
    gameStateInfo.saveAll();
    return gameStateInfo;
  }

  public GameStateInfo playerEndsPhase(int gameId, int playerId, String phaseState) {
    GameStateInfo currentGameState = new GameStateInfo(gameRepository.getGameById(gameId));
    switch(currentGameState.getCurrentPlayerStateInfo().getPhaseState()){
      case DISCARDING:
        endDiscarding(currentGameState);
        currentGameState.getCurrentPlayerStateInfo().setPhaseState(PhaseState.TAKING_ACTION);
        break;
      case TAKING_ACTION:
        endActions(currentGameState);
        currentGameState.getCurrentPlayerStateInfo().setPhaseState(PhaseState.DOING_BUYS);
        break;
      case DOING_BUYS:
        endTurn(gameRepository.getGameById(gameId), currentGameState.getCurrentPlayer().get());
        currentGameState.getCurrentPlayerStateInfo().setPhaseState(PhaseState.PASSIVE);
        break;
    }
    currentGameState.saveAll();
    return currentGameState;
  }
  void signalMachine(Events event) {
    Message<Events> message = MessageBuilder
        .withPayload(event)
        .setHeader("Event Transition", event.toString())
        .build();
    stateMachine.sendEvent(message);
  }

  public void initGame(){
    this.game= new Game();
    gameRepository.save(game);
    //TODO modify to bring in lobby, Oauth credentials, etc
    playerRepository.save(new Player());
    signalMachine(Events.BEGIN_GAME);
    String[] allStacks = new String[]{"Bronze", "Silver", "Gold", "Estate", "Duchy", "Province", "Cellar", "Moat",
        "Village",
        "Workshop", "Smithy", "Remodel", "Militia", "Market", "Mine", "Merchant", "Trash"};
    int[] initialStacks = new int[]{20, 10, 10, 20, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
     // stackRepository.save(stack);
    }



  public GameStateInfo buyTarget(CardType cardType, int playerId, int gameId){
    GameStateInfo gameStateInfo = new GameStateInfo(gameRepository.getGameById(gameId));
    Card buyCard = new Card(cardType);
    int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().getThisTurn().getBuyingPower() - buyCard.getCost();
    if (buyingPower < 0){
      endTurn(gameRepository.getGameById(gameId), playerRepository.getPlayerById(playerId).get());
    }else{
      gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().addToDiscard(buyCard);
      switch(cardType){
        case PROVINCE:
        case DUCHY:
        case ESTATE:
          testForVictory(gameStateInfo);
          break;
        default:
          break;
      }
      int buysRemaining = gameStateInfo.getCurrentPlayerStateInfo().getThisTurn().getBuysRemaining()-1;
      if(buysRemaining <=0){
        gameStateInfo.getCurrentPlayerStateInfo().getThisTurn().setBuysRemaining(buysRemaining);
        gameStateInfo.saveAll();
        endTurn(this.game, gameStateInfo.getCurrentPlayer().get());
        return gameStateInfo;
      }else {
        gameStateInfo.getCurrentPlayerStateInfo().getThisTurn().setBuysRemaining(buysRemaining);
        gameStateInfo.saveAll();
        return gameStateInfo;
      }
    }
    return gameStateInfo;
  }
  public boolean testForVictory(){
    GameStateInfo currentGameState = new GameStateInfo(this.game);
    testForVictory(currentGameState);
  }

  private void testForVictory(GameStateInfo gameStateInfo) {
    List<Stack> currentStacks = gameStateInfo.getStacks();
   if (currentStacks.get(5).getStackCount()==0){
     endGame();
   }
    int zeroCounter = 0;
    for (Stack currentStack : currentStacks) {
      if (currentStack.getStackCount() == 0) {
        zeroCounter++;
      }
    }
    if (zeroCounter >=3){
      endGame();
    }
  }

  public GameStateInfo discard(GameStateInfo gameStateInfo, Card... cards) {
    ArrayList discardCards = new ArrayList(Arrays.asList(cards.clone()));
    gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().addToDiscard(discardCards);
    gameStateInfo.saveAll();
    return gameStateInfo;
  }


  public void startTurn(Optional<Player> player){
    if (player.get().getId() == 1) {
      initTurn(player.get());
      signalMachine(Events.PLAYER_1_START);
    }else{
      initTurn(player.get());
      signalMachine(Events.PLAYER_2_START);
    }
  }

  public void initTurn(Player player){
    Turn thisTurn = new Turn(this.game, player);
    turnRepository.save(thisTurn);
    GameStateInfo gameStateInfo = new GameStateInfo(game);
    if(gameStateInfo.getPreviousTurns().get(thisTurn.getId()-1).isDidAttack()){
      gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.MILITIA_RESPONSE);
    }else {
      gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.ACTION);
    }
    gameStateInfo.saveAll();
  }
  public void endDiscarding(GameStateInfo gameStateInfo){
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.ACTION);
    gameStateInfo.saveAll();
  }
  public void endActions(GameStateInfo gameStateInfo){
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.BUYING);
    gameStateInfo.getCurrentPlayerStateInfo().calculateBuyingPower();
    gameStateInfo.saveAll();
  }

  public void endTurn(Game game, Player player){
    GameStateInfo gameStateInfo = new GameStateInfo(game);
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.WATCHING);
    if (gameStateInfo.getCurrentPlayer().get().getId() == 1) {
      signalMachine(Events.PLAYER_1_END);
    }else{
      signalMachine(Events.PLAYER_2_END);
    }
    gameStateInfo.saveAll();
    //TODO update other player
  }
  public List updateOtherPlayer(){
    ArrayList<Turn> allTurns = turnRepository.getAllByOrderByIdDesc();
    Turn lastTurn = allTurns.get(allTurns.size()-2);
    return (List) playRepository.getAllByTurn(lastTurn);
  }

  public void endGame(){
    signalMachine(Events.GAME_FINISHES);
  }


  public enum Events {
    GAME_INTIALIZED,
    GAME_STARTS,
    GAME_FINISHES,
    ONE_PLAYER_JOINS,
    PLAYER_TWO_JOINS,
    RETURN_TO_LOBBY,
    END_ACTIONS,
    END_BUYS,
    END_TURN,
    BEGIN_TURN,
    BEGIN_GAME,
    PLAYER_1_START,
    PLAYER_1_END,
    PLAYER_2_START,
    PLAYER_2_END;
  }
}
