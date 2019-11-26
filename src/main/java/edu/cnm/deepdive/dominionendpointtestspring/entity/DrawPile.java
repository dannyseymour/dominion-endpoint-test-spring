package edu.cnm.deepdive.dominionendpointtestspring.entity;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.springframework.lang.NonNull;


public class DrawPile implements Iterable{

  private ArrayList<Card> drawPileCards;


  private int id;


  private Player player;


  private Game game;



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

  public DrawPile(ArrayList<Card> drawPileCards) {
    this.drawPileCards = drawPileCards;
  }


  public Card getTopCard(GameStateInfo gameStateInfo) {
    if (drawPileCards.isEmpty()) {
      //if drawPile exhausted, get shuffled discardPile and make new drawPile
      this.drawPileCards = gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().makeNewDrawPile();
    }
    Card topCard = drawPileCards.get(0);
    drawPileCards.remove(0);
    return topCard;
  }
  public void setDrawPileCards(
      ArrayList<Card> drawPileCards) {
    this.drawPileCards = drawPileCards;
  }

  /**
   public static DrawPile newDeck(ArrayList<Card> cards ) {
   return new DrawPile(cards);
   }
   */
  public ArrayList<Card> getDrawPileCards() {
    return drawPileCards;
  }



  public void setDeckCards(
      ArrayList<Card> drawPileCards) {
    this.drawPileCards = drawPileCards;
  }

  @Override
  public Iterator iterator() {
    return null;
  }

  @Override
  public void forEach(Consumer action) {

  }

  @Override
  public Spliterator spliterator() {
    return null;
  }
}