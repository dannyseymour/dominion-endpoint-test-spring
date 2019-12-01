package edu.cnm.deepdive.dominionendpointtestspring.model.entity;


import edu.cnm.deepdive.dominionendpointtestspring.model.pojo.Card;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.lang.NonNull;

/**
 * Keeps a log of the significant player actions taken during each turn, sorted by player and turn.
*/
@Entity
@Table
public class Play implements Serializable {

  public Play(Long id, Turn turn, Card cardPlayed) {
    this.id = id;
    //this.turnId = turn.getTurnId();
    this.cardPlayed = cardPlayed.getCardName();
  }

  public Play(Long id, Turn turn, int goldSpent,
      Card cardBought) {
    this.id = id;
    //this.turnId = turn.getTurnId();
    this.goldSpent = goldSpent;
    this.cardBought = cardBought.getCardName();
  }





  /**
   * Each action taken by a player generates a new play. Typically, most of the fields will be null
   * or zero in the interest of separating out each action individually. Example: play: 1, Player:1,
   *  Turn: 1, cardPlayed: 45, goldSpent: 0, cardBought: 0. Note that cards are referred to by
   * integer indices.
   */
  @Id
  @GeneratedValue
  @Column(name = "play_id", updatable = false, nullable = false)
  private Long id;


  /**
   * Each action taken by a player generates a new play. Typically, most of the fields will be null
   * or zero in the interest of separating out each action individually.
   */

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "turn_id", updatable = false)
  private Turn turn;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "player_id", updatable = false)
  private Player player;

  /**
   * Each play is a associated with zero or more cards played. This records a log of which card was
   * played and when.
   */

  @Column(name = "player_name", nullable = true, updatable = false)
  private String playerName;

  @Column(name = "card_played", nullable = true, updatable = false)
  private String cardPlayed;

  /**
   * Each play is associated with zero or more gold spent, typically to buy new cards.
   */
  @Column(name = "gold_spent", updatable = false)
  private int goldSpent;

  /**
   * Each play is associated with zero or more cards bought.
   */

  @Column(name = "card_bought", nullable = true, updatable = false)
  private String cardBought;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
/**
  public int getTurn() {
    return turnId;
  }

  public void setTurn(int turnId) {
    this.turnId = turnId;
  }
*/
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
