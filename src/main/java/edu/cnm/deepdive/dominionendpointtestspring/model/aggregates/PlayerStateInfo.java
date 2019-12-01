package edu.cnm.deepdive.dominionendpointtestspring.model.aggregates;

import edu.cnm.deepdive.dominionservice.model.dao.CardRepository;
import edu.cnm.deepdive.dominionservice.model.dao.DiscardPileRepository;
import edu.cnm.deepdive.dominionservice.model.dao.DrawPileRepository;
import edu.cnm.deepdive.dominionservice.model.dao.HandRepository;
import edu.cnm.deepdive.dominionservice.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionservice.model.dao.TurnRepository;
import edu.cnm.deepdive.dominionservice.model.entity.Card;
import edu.cnm.deepdive.dominionservice.model.entity.DiscardPile;
import edu.cnm.deepdive.dominionservice.model.entity.DrawPile;
import edu.cnm.deepdive.dominionservice.model.entity.Game;
import edu.cnm.deepdive.dominionservice.model.entity.Hand;
import edu.cnm.deepdive.dominionservice.model.entity.Player;
import edu.cnm.deepdive.dominionservice.model.entity.Turn;
import java.io.Serializable;
import java.util.ArrayList;

public class PlayerStateInfo implements Serializable {

  private TurnRepository turnRepository;


      private CardRepository cardRepository;


      private PlayerRepository playerRepository;
  private Turn thisTurn;
  private Player player;
  private Game game;
  private DrawPile drawPile;
  private Hand hand;
  private DiscardPile discardPile;
  private PhaseState phaseState;
  private HandRepository handRepository;
  private DrawPileRepository drawPileRepository;
  private DiscardPileRepository discardPileRepository;
  ArrayList<Turn> allTurns;


  PlayerStateInfo(Game game, Player player) {
    this.game=game;
    this.player=player;
    allTurns = turnRepository.getAllByOrderByIdDesc();
    this.thisTurn = allTurns.get(allTurns.size()-1);
      this.hand = handRepository.getLastByPlayer(player);
      this.discardPile = discardPileRepository.getLastByPlayer(player);
      this.drawPile = drawPileRepository.getLastByPlayer(player);
    }

  public void saveAll(){
    this.turnRepository.save(thisTurn);
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

  public PhaseState getPhaseState() {
    return phaseState;
  }

  public void setPhaseState(
      PhaseState phaseState) {
    this.phaseState = phaseState;
  }

  public Turn getThisTurn() {
    return thisTurn;
  }

  public void setThisTurn(Turn thisTurn) {
    this.thisTurn = thisTurn;
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
