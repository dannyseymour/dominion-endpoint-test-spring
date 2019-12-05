package edu.cnm.deepdive.dominionendpointtestspring.service;



import edu.cnm.deepdive.dominionendpointtestspring.model.dao.CardRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.PlayRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.TurnRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Location;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Type;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Play;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStateInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Transactional
@WithStateMachine
public class GameLogic {


  private final GameRepository gameRepository;


  private final TurnRepository turnRepository;


  private final PlayRepository playRepository;


  private final PlayerRepository playerRepository;


  private final CardRepository cardRepository;

  @Autowired
  public GameLogic(
      GameRepository gameRepository,
      TurnRepository turnRepository,
      PlayRepository playRepository,
      PlayerRepository playerRepository,
      CardRepository cardRepository) {
    this.gameRepository = gameRepository;
    this.turnRepository = turnRepository;
    this.playRepository = playRepository;
    this.playerRepository = playerRepository;
    this.cardRepository = cardRepository;
  }

  public Turn getCurrentTurn(Game game){
    return turnRepository.getAllByGameOrderByTurnIdDesc(game).get().get(turnRepository.getAllByOrderByTurnIdDesc().get().size()-1);
  }
  public Turn getPreviousTurn(Game game){
    return turnRepository.getAllByGameOrderByTurnIdDesc(game).get().get(turnRepository.getAllByOrderByTurnIdDesc().get().size()-2);
  }

  public Game getCurrentGame(List<GamePlayer> players){
    return gameRepository.getFirstByGamePlayers(players);
  }

  public Game playCardWithCards(String cardType, Game game, Player player, ArrayList<String> cardStrings) {
    Turn turn = getCurrentTurn(game);
    if(cardStrings.size()==0){
      cardStrings = new ArrayList<String>();
    }
    GameStateInfo gameStateInfo = new GameStateInfo(game, turn, player, game.getCurrentState());
    boolean hasSilver = false;
    for (String s: cardStrings){
      if (s.equals("Silver")){
        hasSilver = true;
      }
    }
    if(cardRepository.getByLocationAndPlayerAndType(Location.HAND, player, Type.valueOf(cardType)).isPresent()) {
      Card playingCard =cardRepository.getByLocationAndPlayerAndType(Location.HAND, player, Type.valueOf(cardType)).get();
          playCardProcessing(game, turn, player, playingCard, hasSilver, cardStrings);
          playingCard.setLocation(Location.DISCARD);

      if(player.getActionsRemaining()==0){
        game.setCurrentState(GameState.BUYING);
      }
      return game;
    }else{
      return game;
    }
  }



  public Game buyCard(String cardName, Game game, Player player){
    Card.Type cardType = Card.Type.valueOf(cardName.toUpperCase());
    Turn turn = getCurrentTurn(game);

   int buyingPower = turn.getBuyingPower();
    if (buyingPower > 0){

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
    initTurn(game game, )
    return game;
  }

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


  public void endTurn(Game game, Player player){
   Turn turn = getCurrentTurn();
    GameStateInfo gameStateInfo = new GameStateInfo(game,turn, player, game.getCurrentState());
    game.setCurrentState();
    gameStateInfo.getCurrentPlayer().get().setPlayerState(PlayerState.WATCHING);
    if (gameStateInfo.getCurrentPlayer().get().getId() == 1) {
      signalMachine(GameEvents.PLAYER_1_END_BUYS);
    }else{
      signalMachine(GameEvents.PLAYER_2_END_BUYS);
    }
    //gameStateInfo.saveAll();
    //TODO update other player
  }

   public List updateOtherPlayer(){
   ArrayList<Turn> allTurns = (ArrayList<Turn>) turnRepository.getAllByOrderByTurnIdDesc().get();
   Turn lastTurn = allTurns.get(allTurns.size()-2);
   return (List) playRepository.getAllByTurn(lastTurn);
   }


  public Game endPhase(@PathVariable("uid") String uid, @PathVariable("gameid") int gameId) {
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


  }
  //i= 1 get this turn, i=2 get last turn
  private Turn getSpecificTurnsAgo(int i) {
    ArrayList<Turn> turns = (ArrayList<Turn>) turnRepository.getAllByOrderByTurnIdDesc().get();
    return turns.get(turns.size() - i);
  }


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


  public Game initializeGame(Game game, Player player1, Player player2) {
    setupStacks(game);
    deal(game, player1);
    deal(game, player2);
    game.setCurrentPlayer(player1);
    game = initTurn(game, player1, true);
    return game;
  }

    private void setupStacks(Game game){
      LinkedList<Card> initialCards = new LinkedList<>();
    for (Card.Type type : Card.Type.values()){
      for (int i = 0; i< type.getInitialTotalCount(); i++){
        Card card = new Card(type);
        card.setLocation(Location.STACK);
        card.setGame(game);
        initialCards.add(card);
      }
      }
    cardRepository.saveAll(initialCards);
    }


  private void deal(Game game, Player player){
    LinkedList<Card> initialCardsForPlayer = new LinkedList<>();
    for (Card.Type type : Card.Type.values()){
      for (int i = 0; i< type.getInitialPlayerCount(); i++){
        Card card = new Card(type);
        card.setLocation(Location.DRAW_PILE);
        card.setGame(game);
        card.setPlayer(player);
        initialCardsForPlayer.add(card);
        card.setOrderInLocation(initialCardsForPlayer.indexOf(card));
      }
    }
    shuffleAndDraw(player);
    cardRepository.saveAll(initialCardsForPlayer);
  }

private void shuffleAndDraw(Player player){
    List<Card> cardsInDraw = cardRepository.getAllByPlayerAndLocation(player, Location.DRAW_PILE).get();
    List<Card> cardsInHand = new LinkedList<>();

    //SHUFFLE
  //DRAW
}


  private List<String> getListOfPlaysStrings(Turn turn){
    List<Play> plays = playRepository.getAllByTurn(turn).get();
    List<String> playStrings = new ArrayList<>();
    for (Play play: plays){
      playStrings.add(playToString(play));
    }
    return playStrings;
  }
  private String playToString(Play play){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(play.getPlayer().getDisplayName())
        .append(" ")
        .append(play.getType().toString())
        .append(" ")
        .append(play.getCard().getType().toString());
    return stringBuilder.toString();
  }
private void draw(Player player){

}
  private Game initTurn(Game game, Player player, boolean isFirstTurn){
    Turn turn = new Turn(game, player);
    turnRepository.save(turn);
    turn.setBuyingPower(calculateInitialBuyingPower(game, player));
    player.setActionsRemaining(1);
    player.setBuysRemaining(1);
    Turn previousTurn = getPreviousTurn(game);
    draw(player);
    boolean needsToDiscard = false;
    List<Card> cardsInHand = cardRepository.getAllByPlayerAndLocation(player, Location.HAND).get();
    if (!isFirstTurn) {
      List<Play> playsFromOtherPlayer = playRepository.getAllByGameAndTurn(game, previousTurn)
          .get();
      for (Play play : playsFromOtherPlayer) {
        if (play.getCard() != null && play.getCard().getType().equals(Type.MILITIA)) {
          needsToDiscard = true;
          for (Card card : cardsInHand) {
            if (card.getType().equals(Type.MOAT)) {
              needsToDiscard = false;
            }
          }
        }
      }
      if (needsToDiscard) {
        game.setCurrentState(GameState.DISCARDING);
      } else {
        game.setCurrentState(GameState.ACTION);
      }
    }else{
      game.setCurrentState(GameState.ACTION);
    }
    game.setCurrentPlayer(player);
    return game;
  }

  private int calculateInitialBuyingPower(Game game, Player player) {
    ArrayList<Card> cardsInHand = (ArrayList<Card>) cardRepository
        .getAllByPlayerAndLocation(player, Location.HAND).get();
    int buyingPower = 0;
    boolean hasSilver = false;
    for (Card card : cardsInHand) {
      switch (card.getType()) {
        case COPPER:
          buyingPower += 1;
          break;
        case SILVER:
          buyingPower += 3;
          hasSilver = true;
          break;
        case GOLD:
          buyingPower += 5;
          break;
        default:
          break;
      }
    }
    return buyingPower;
  }


  private int calculateVictoryPoints(Player player){
    ArrayList<Card> allPlayersCards = cardRepository.getAllByPlayer(player).get();
    int victoryCounter = 0;
    for (Card card: allPlayersCards){
      switch(card.getType()){
        case ESTATE:
          victoryCounter+=1;
          break;
        case DUCHY:
          victoryCounter+=3;
          break;
        case PROVINCE:
          victoryCounter+=5;
          break;
        default:
          break;
      }

    }
    return victoryCounter;
  }

  private void constructNormalPlay(Game game, Turn turn, Player player, Card card, boolean hasSilver){
    int currentActionsRemaining = player.getActionsRemaining();
    player.setActionsRemaining(currentActionsRemaining -1 + card.getType().getExtraActions());

    int currentBuysRemaining = player.getBuysRemaining();
    player.setBuysRemaining(currentBuysRemaining-1+card.getType().getExtraBuys());

    int currentBuyingPower = turn.getBuyingPower();
    if(hasSilver){
      currentBuyingPower+=card.getType().getExtraGoldIfSilver();
    }
    turn.setBuyingPower(currentBuyingPower+card.getType().getExtraGold());

    for (int i = 0; i<card.getType().getDrawCards(); i++){
      draw(player);
    }

  }

  private void playCardProcessing(Game game, Turn turn, Player player, Card card, boolean hasSilver, List<String> otherCardsStrings){
    constructNormalPlay(game, turn, player, card, hasSilver);
    ArrayList<Card> otherCards = new ArrayList<>();
    switch(card.getType()){
      case MILITIA:
        break;
      case WORKSHOP:
        int limit = 4;
        Card buyCard = cardRepository.getAllByTypeAndLocation(Type.valueOf(otherCardsStrings.get(0)), Location.STACK).get().get(0);
        if (buyCard.getType().getCost()<=4){
          buyCard.setLocation(Location.HAND);
          buyCard.setPlayer(player);
        }
        break;
      case CELLAR:
        for(String s: otherCardsStrings) {

        }
        break;
      case MINE:
        break;
      case REMODEL:
        break;


    }
  }

  private boolean testForGameEnd(Game game){
    int emptyStacks = 0;
    boolean provincesEmpty = false;
    if (cardRepository.countAllByTypeAndLocation(Type.PROVINCE, Location.STACK).get()==0){
      return true;
    }
    for (Type type : Type.values()){
      if (cardRepository.countAllByTypeAndLocation(type, Location.STACK).get()==0 && !type.equals(Type.PROVINCE)){
        emptyStacks+=1;
      }
    }
    if (emptyStacks>=3){
      return true;
    }
    return false;
  }
  private String whoWins(Game game, Player player1, Player player2){
    if (calculateVictoryPoints(player1) > calculateVictoryPoints(player2)){
      return player1.getDisplayName();
    }else if (calculateVictoryPoints(player2) > calculateVictoryPoints(player1)){
      return player2.getDisplayName();
    }else{
      return "Tie";
    }
  }

}

