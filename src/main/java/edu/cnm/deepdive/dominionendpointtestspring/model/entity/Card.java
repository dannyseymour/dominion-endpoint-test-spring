package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStateInfo;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGame;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "card_id")
  private Long id;
  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;
  @NonNull
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date updated;

  private Location location;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id", updatable = false)
  @JsonSerialize(as = FlatGame.class)
  private Game game;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "player_id", nullable = false, updatable = false)
  @JsonSerialize(as = FlatPlayer.class)
  private Player player;

  private Type type;
  private int orderInLocation;

  public Card(Type type) {
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getOrderInLocation() {
    return orderInLocation;
  }

  public void setOrderInLocation(int orderInLocation) {
    this.orderInLocation = orderInLocation;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(@NonNull Date updated) {
    this.updated = updated;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @NonNull
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull Player player) {
    this.player = player;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public enum Location {
    HAND,
    DISCARD,
    DRAW_PILE,
    STACK,
    TRASH;
  }

  public enum Type {
    COPPER(60, 7, 0, 0,
        0, 1, 0, 0, 0, 0),

    SILVER(30, 0, 3, 0,
        0, 2, 0, 0, 0, 0),

    GOLD(20, 0, 6, 0,
        0, 3, 0, 0, 0, 0),

    ESTATE(8, 3, 2, 1,
        0, 0, 0, 0, 0, 0),

    DUCHY(8, 0, 5, 3,
        0, 0, 0, 0, 0, 0),

    PROVINCE(8, 0, 8, 6,
        0, 0, 0, 0, 0, 0),

    CELLAR(10, 0, 2, 0,
        0, 0, 0, 0, 1, 0),

    MOAT(10, 0, 2, 0,
        0, 0, 0, 2, 0, 0),

    MARKET(10, 0, 5, 0,
        0, 0, 1, 1, 1, 1),


    MERCHANT(10, 0, 3, 0,
        1, 0, 0, 1, 1, 0),

    MILITIA(10, 0, 4, 0,
        0, 0, 2, 0, 0, 0),


    MINE(10, 0, 5, 0,
        0, 0, 0, 0, 0, 0),

    REMODEL(10, 0, 4, 0, 0,
        0, 0, 0, 0, 0),

    SMITHY(10, 0, 4, 0,
        0, 0, 0, 3, 0, 0),

    VILLAGE(10, 0, 3, 0,
        0, 0, 0, 1, 2, 0),

    WORKSHOP(10, 0, 3, 0,
        0, 0, 0, 0, 0, 0);


    private final int initialTotalCount;
    private final int initialPlayerCount;
    private final int cost;
    private final int victoryPoints;
    private final int extraGoldIfSilver;
    private final int moneyValue;
    private final int extraGold;
    private final int drawCards;
    private final int extraActions;
    private final int extraBuys;

    Type(int initialTotalCount, int initialPlayerCount, int cost, int victoryPoints,
        int extraGoldIfSilver, int moneyValue,
        int extraGold,
        int drawCards, int extraActions, int extraBuys) {
      this.initialTotalCount = initialTotalCount;
      this.initialPlayerCount = initialPlayerCount;
      this.cost = cost;
      this.victoryPoints = victoryPoints;
      this.extraGoldIfSilver = extraGoldIfSilver;
      this.moneyValue = moneyValue;
      this.extraGold = extraGold;
      this.drawCards = drawCards;
      this.extraActions = extraActions;

      this.extraBuys = extraBuys;
    }

    public int getInitialTotalCount() {
      return initialTotalCount;
    }

    public int getInitialPlayerCount() {
      return initialPlayerCount;
    }

    public int getCost() {
      return cost;
    }

    public int getVictoryPoints() {
      return victoryPoints;
    }

    public int getExtraGoldIfSilver() {
      return extraGoldIfSilver;
    }

    public int getExtraActions() {
      return extraActions;
    }

    public int getExtraBuys() {
      return extraBuys;
    }

    public int getMoneyValue() {
      return moneyValue;
    }

    public int getExtraGold() {
      return extraGold;
    }

    public int getDrawCards() {
      return drawCards;
    }

  }
}
