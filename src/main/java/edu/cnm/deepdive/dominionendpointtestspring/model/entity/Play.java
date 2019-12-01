package edu.cnm.deepdive.dominionendpointtestspring.model.entity;


import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import java.io.Serializable;

/**
 * Keeps a log of the significant player actions taken during each turn, sorted by player and turn.
*/
public class Play implements Serializable {

  public Play(Long id, Turn turn, Card cardPlayed) {
    this.id = id;
    this.turnId = turn.getId();
    this.cardPlayed = cardPlayed.getCardName();
  }

  public Play(Long id, Turn turn, int goldSpent,
      Card cardBought) {
    this.id = id;
    this.turnId = turn.getId();
    this.goldSpent = goldSpent;
    this.cardBought = cardBought.getCardName();
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

  private int turnId;

  /**
   * Each play is a associated with zero or more cards played. This records a log of which card was
   * played and when.
   */

  private String playerName;

  private String cardPlayed;

  /**
   * Each play is associated with zero or more gold spent, typically to buy new cards.
   */

  private int goldSpent;

  /**
   * Each play is associated with zero or more cards bought.
   */


  private String cardBought;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getTurn() {
    return turnId;
  }

  public void setTurn(int turnId) {
    this.turnId = turnId;
  }

  public String getCardPlayed() {
    return cardPlayed;
  }

  public void setCardPlayed(Card cardPlayed) {
    this.cardPlayed = cardPlayed.getCardName();
  }

  public int getGoldSpent() {
    return goldSpent;
  }

  public void setGoldSpent(int goldSpent) {
    this.goldSpent = goldSpent;
  }


  public String getCardBought() {
    return cardBought;
  }

  public void setCardBought(Card cardBought) {
    this.cardBought = cardBought.getCardName();
  }

  @Override
  public String toString() {
    String play;
    if (cardPlayed != null) {
       play = "Danny played "
          + cardPlayed;
    }
    else if (cardBought!= null){
       play = "Danny bought "
          + cardBought
          + "for "
          + goldSpent;
    }else{
      play = "No Actions or Buys";
    }
    return play;
  }
}
