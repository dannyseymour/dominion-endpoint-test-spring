package edu.cnm.deepdive.dominionendpointtestspring;


import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card.CardType;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.DiscardPile;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.DrawPile;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Hand;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Turn;
import java.io.Serializable;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerStateInfo implements Serializable {
  Logger logger = LoggerFactory.getLogger(PlayerStateInfo.class);
//  private TurnRepository turnRepository;


 // private CardRepository cardRepository;


  //private PlayerRepository playerRepository;
  private Turn turn;
  private Player player;
  private Game game;
  private DrawPile drawPile;
  private Hand hand;
  private DiscardPile discardPile;
  private PhaseState phaseState;
  private ArrayList<Card> cardsInHand;
  private ArrayList<Card> cardsInDiscard;
  private ArrayList<Card> cardsInDrawPile;
 // private HandRepository handRepository;
 // private DrawPileRepository drawPileRepository;
 // private DiscardPileRepository discardPileRepository;

  PlayerStateInfo(Game game, Player player) {
    this.game=game;
    this.player=player;
    this.turn = new Turn(game, player);
    cardsInHand = new ArrayList<>();
    cardsInHand.add(new Card("Militia", CardType.MILITIA, 4));
    cardsInHand.add(new Card("Copper", CardType.COPPER, 0));
    cardsInHand.add(new Card("Moat", CardType.MOAT, 2));
    cardsInHand.add(new Card("Cellar", CardType.CELLAR, 2));
    cardsInHand.add(new Card("Market", CardType.MARKET, 4));
    this.hand = new Hand(cardsInHand);
    cardsInDiscard = new ArrayList<>();
    cardsInDiscard.add(new Card("Militia", CardType.MILITIA, 4));
    cardsInDiscard.add(new Card("Copper", CardType.COPPER, 0));
    cardsInDiscard.add(new Card("Moat", CardType.MOAT, 2));
    cardsInDiscard.add(new Card("Cellar", CardType.CELLAR, 2));
    cardsInDiscard.add(new Card("Market", CardType.MARKET, 4));
    this.discardPile = new DiscardPile(cardsInDiscard);
    cardsInDrawPile = new ArrayList<>();
    cardsInDrawPile.add(new Card("Copper", CardType.COPPER, 0));
    cardsInDrawPile.add(new Card("Copper", CardType.COPPER, 0));
    cardsInDrawPile.add(new Card("Estate", CardType.ESTATE, 1));
    cardsInDrawPile.add(new Card("Estate", CardType.ESTATE, 1));
    cardsInDrawPile.add(new Card("Copper", CardType.COPPER, 0));
    cardsInDrawPile.add(new Card("Copper", CardType.COPPER, 0));

    this.drawPile = new DrawPile(cardsInDrawPile);
   // this.turn = turnRepository.getCurrentTurn().get();
   // this.hand = handRepository.getLastByPlayer(player);
  //  this.discardPile = discardPileRepository.getLastByPlayer(player);
  //  this.drawPile = drawPileRepository.getLastByPlayer(player);
  }


  /**public void saveAll(){
    this.turnRepository.save(turn);
    for (Card card: this.hand.getCardsInHand()){
      this.cardRepository.save(card);
    }
    for (Card card: this.drawPile.getDrawPileCards()){
      this.cardRepository.save(card);
    }
    for (Card card: this.discardPile.getDiscardCards()){
      this.cardRepository.save(card);
    }
    this.discardPileRepository.save(new DiscardPile(this.discardPile.getDiscardCards()));
    // this.handRepository.save(new Hand(this.hand.getCardsInHand()));
    this.drawPileRepository.save(new DrawPile(this.drawPile.getDrawPileCards()));


  }
*/
  public PhaseState getPhaseState() {
    return phaseState;
  }

  public void setPhaseState(
      PhaseState phaseState) {
    this.phaseState = phaseState;
  }

  public Turn getTurn() {
    return turn;
  }

  public void setTurn(Turn turn) {
    this.turn = turn;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public DrawPile getDrawPile() {
    return drawPile;
  }

  public void setDrawPile(DrawPile drawPile) {
    this.drawPile = drawPile;
  }

  public Hand getHand() {
    return hand;
  }

  public void setHand(Hand hand) {
    this.hand = hand;
  }

  public DiscardPile getDiscardPile() {
    return discardPile;
  }

  public void setDiscardPile(DiscardPile discardPile) {
    this.discardPile = discardPile;
  }

  public int calculateBuyingPower() {
    int buyingPower = 0;

    return buyingPower;
  }

  public enum PhaseState {
    DISCARDING,
    TAKING_ACTION,
    DOING_BUYS,
    PASSIVE;
  }



}
