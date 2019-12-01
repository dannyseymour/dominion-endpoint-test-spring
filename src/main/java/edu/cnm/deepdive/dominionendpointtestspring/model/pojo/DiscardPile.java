package edu.cnm.deepdive.dominionendpointtestspring.model.pojo;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.lang.NonNull;


public class DiscardPile {

  private ArrayList<Card> discardCards;

  public DiscardPile(ArrayList<Card> cards) {
    this.discardCards = cards;
  }


  //takes the discard pile, shuffles it, and returns
  public ArrayList<Card> makeNewDrawPile() {
    Collections.shuffle(discardCards);
    return discardCards;
  }

  private int id;

  private Player player;


  private Game game;

  public void addToDiscard(Card card){
    discardCards.add(card);
  }

  public void addToDiscard(ArrayList<Card> cards){
    discardCards.addAll(cards);
  }

  public void discardCard(Card c) {
    discardCards.add(c);
  }



  public ArrayList<Card> getDiscardCards() {
    return discardCards;
  }

  public void setDiscardCards(
      ArrayList<Card> discardCards) {
    this.discardCards = discardCards;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NonNull
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull Player player) {
    this.player = player;
  }

  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame(@NonNull Game game) {
    this.game = game;
  }

  public ArrayList<Card> addToDiscard(List<Card> additionalCards) {
    this.discardCards.addAll(additionalCards);
    return this.discardCards;
  }
}