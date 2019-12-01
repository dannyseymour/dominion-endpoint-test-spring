package edu.cnm.deepdive.dominionendpointtestspring.service;

import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.TurnRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.aggregates.GameStateInfo;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player.PlayerState;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card.CardType;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

  @Autowired
  PlayerService playerService;

  public Turn getCurrentTurn(){
    return turnRepository.getAllByOrderByTurnIdDesc().get(turnRepository.getAllByOrderByTurnIdDesc().size()-1);
  }
  public Game getCurrentGame(){
    return gameRepository.getAllByOrderByIdDesc().get(gameRepository.getAllByOrderByIdDesc().size()-1);
  }


  public GameStateInfo playCardWithCards(String cardType, Game game, Player player, ArrayList<Card> cards) {
    GameStateInfo gameStateInfo = new GameStateInfo(game,getCurrentTurn());
    Card playingCard = new Card(CardType.valueOf(cardType));
    playingCard.getCardType().play(gameStateInfo, Optional.ofNullable(cards));
    if(gameStateInfo.getCurrentPlayerStateInfo().getPlayer().getNumAction()==0){
      endActions(gameStateInfo);
      gameStateInfo.getCurrentPlayerStateInfo().setPhaseState(GameStates.PLAYER_1_BUYING);
      signalMachine(GameEvents.PLAYER_1_END_ACTIONS);
    }
    //gameStateInfo.saveAll();
    return gameStateInfo;
  }


  void signalMachine(GameEvents event) {
    Message<GameEvents> message = MessageBuilder
        .withPayload(event)
        .setHeader("Event Transition", event.toString())
        .build();
    stateMachine.sendEvent(message);
  }

 /** public void initGame(String oAuthKey){
    this.game= new Game();
    game.setStacks(initializeStacks(game.getStacks()));
    gameRepository.save(game);
    //TODO modify to bring in lobby, Oauth credentials, etc

    playerRepository.save(playerService.getOrCreatePlayer(oAuthKey));
    signalMachine(GameEvents.START_GAME);
    }

*/

  public GameStateInfo buyTarget(CardType cardType, int playerId){
    GameStateInfo gameStateInfo = new GameStateInfo(getCurrentGame(), getCurrentTurn());
    Card buyCard = new Card(cardType);
    int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().calculateBuyingPower()- buyCard.getCost();
    if (buyingPower < 0){
      endTurn(getCurrentGame(), playerRepository.getPlayerById((long) playerId).get());
    }else{
      gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().addToDiscard(buyCard);
      switch(cardType){
        case PROVINCE:
        case DUCHY:
        case ESTATE:
          testForVictory();
          break;
        default:
          break;
      }
      int buysRemaining = gameStateInfo.getCurrentPlayerStateInfo().calculateBuyingPower()-1;
      if(buysRemaining <=0){
        gameStateInfo.getCurrentPlayerStateInfo().getPlayer().setNumBuy(buysRemaining);
       // gameStateInfo.saveAll();
        endTurn(getCurrentGame(), gameStateInfo.getCurrentPlayer().get());
        return gameStateInfo;
      }else {
        gameStateInfo.getCurrentPlayerStateInfo().getPlayer().setNumBuy(buysRemaining);
      //  gameStateInfo.saveAll();
        return gameStateInfo;
      }
    }
    return gameStateInfo;
  }
  public boolean testForVictory(){

    //testForVictory(currentGameState);
    return false;
  }
/**
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
*/
  public GameStateInfo discard(GameStateInfo gameStateInfo, Card... cards) {
    ArrayList discardCards = new ArrayList(Arrays.asList(cards.clone()));
    gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().addToDiscard(discardCards);
    //gameStateInfo.saveAll();
    return gameStateInfo;
  }
  public HashMap<String, Integer> initializeStacks(HashMap<String, Integer> stack) {
    stack.put("Bronze", 10);
    stack.put("Silver", 8);
    stack.put("Gold", 7);
    stack.put("Estate", 10);
    stack.put("Duchy", 12);
    stack.put("Province", 15);
    stack.put("Cellar", 9);
    stack.put("Moat", 10);
    stack.put("Village", 5);
    stack.put("Workshop", 3);
    stack.put("Smithy", 2);
    stack.put("Remodel", 15);
    stack.put("Militia", 9);
    stack.put("Market", 8);
    stack.put("Mine", 2);
    stack.put("Merchant", 3);
    stack.put("Trash", 0);
    return stack;
  }

/**
  public void startTurn(Optional<Player> player){
    if (player.get().getId() == 1) {
      initTurn(player.get());
      signalMachine(Events.PLAYER_1_START);
    }else{
      initTurn(player.get());
      signalMachine(GameEvents.PLAYER_2_START);
    }
  }
*/
  public void initTurn(Player player){
    Turn thisTurn = new Turn(getCurrentGame(), player);
    turnRepository.save(thisTurn);
    GameStateInfo gameStateInfo = new GameStateInfo(getCurrentGame(), thisTurn);
    if(gameStateInfo.getPreviousTurns().get(thisTurn.getTurnId()-1).isDidAttack()){
      gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.MILITIA_RESPONSE);
    }else {
      gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.ACTION);
    }
    //gameStateInfo.saveAll();
  }
  public void endDiscarding(GameStateInfo gameStateInfo){
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.ACTION);
   // gameStateInfo.saveAll();
  }
  public void endActions(GameStateInfo gameStateInfo){
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.BUYING);
    gameStateInfo.getCurrentPlayerStateInfo().calculateBuyingPower();
   // gameStateInfo.saveAll();
  }

  public void endTurn(Game game, Player player){
    GameStateInfo gameStateInfo = new GameStateInfo(getCurrentGame(),getCurrentTurn());
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.WATCHING);
    if (gameStateInfo.getCurrentPlayer().get().getId() == 1) {
      signalMachine(GameEvents.PLAYER_1_END_BUYS);
    }else{
      signalMachine(GameEvents.PLAYER_2_END_BUYS);
    }
    //gameStateInfo.saveAll();
    //TODO update other player
  }
  /**
  public List updateOtherPlayer(){
    ArrayList<Turn> allTurns = turnRepository.getAllByOrderByTurnIdDesc();
    Turn lastTurn = allTurns.get(allTurns.size()-2);
    return (List) playRepository.getAllByTurn(lastTurn);
  }
*/
  public void endGame(){
    signalMachine(GameEvents.END_GAME);
  }



}
