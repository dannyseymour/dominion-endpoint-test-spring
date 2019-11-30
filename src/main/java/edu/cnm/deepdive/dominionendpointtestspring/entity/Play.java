package edu.cnm.deepdive.dominionendpointtestspring.entity;


import java.io.Serializable;

import org.springframework.lang.NonNull;

/**
 * Keeps a log of the significant player actions taken during each turn, sorted by player and turn.
*/
public class Play implements Serializable {

  public Play(Long id, Turn turn, Card cardPlayed) {
    this.id = id;
    this.turn = turn;
    this.cardPlayed = cardPlayed;
  }

  public Play(Long id, Turn turn, int goldSpent,
      Card cardBought) {
    this.id = id;
    this.turn = turn;
    this.goldSpent = goldSpent;
    this.cardBought = cardBought;
  }

  /**
   * Each action taken by a player generates a new play. Typically, most of the fields will be null
   * or zero in the interest of separating out each action individually. Example: play: 1, Player:1,
   *  Turn: 1, cardPlayed: 45, goldSpent: 0, cardBought: 0. Note that cards are referred to by
   * integer indices.
   */

  private Long id;


  /**
   * Each action taken by a player generates a new play. Typically, most of the fields will be null
   * or zero in the interest of separating out each action individually.
   */

  private Turn turn;

  /**
   * Each play is a associated with zero or more cards played. This records a log of which card was
   * played and when.
   */


  private Card cardPlayed;

  /**
   * Each play is associated with zero or more gold spent, typically to buy new cards.
   */

  private int goldSpent;

  /**
   * Each play is associated with zero or more cards bought.
   */


  private Card cardBought;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Turn getTurn() {
    return turn;
  }

  public void setTurn(Turn turn) {
    this.turn = turn;
  }

  public Card getCardPlayed() {
    return cardPlayed;
  }

  public void setCardPlayed(Card cardPlayed) {
    this.cardPlayed = cardPlayed;
  }

  public int getGoldSpent() {
    return goldSpent;
  }

  public void setGoldSpent(int goldSpent) {
    this.goldSpent = goldSpent;
  }


  public Card getCardBought() {
    return cardBought;
  }

  public void setCardBought(Card cardBought) {
    this.cardBought = cardBought;
  }

  @Override
  public String toString() {
    String play;
    if (cardPlayed != null) {
       play = "Danny played "
          + cardPlayed.getCardName();
    }
    else if (cardBought!= null){
       play = "Danny bought "
          + cardBought.getCardName()
          + "for "
          + goldSpent;
    }else{
      play = "No Actions or Buys";
    }
    return play;
  }
}
