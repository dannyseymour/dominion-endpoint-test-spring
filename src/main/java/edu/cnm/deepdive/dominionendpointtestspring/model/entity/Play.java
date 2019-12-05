package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.springframework.lang.NonNull;

@Entity
public class Play {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "play_id")
  private int id;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name= "game_player_id", nullable = false, updatable = false)
  private GamePlayer player;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name= "turn_id", nullable = false, updatable = false)
  private Turn turn;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name= "game_id", nullable = false, updatable = false)
  private Game game;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="card_id")
  private Card card;

  private Type type;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NonNull
  public GamePlayer getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull GamePlayer player) {
    this.player = player;
  }

  @NonNull
  public Turn getTurn() {
    return turn;
  }

  public void setTurn(@NonNull Turn turn) {
    this.turn = turn;
  }

  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame(@NonNull Game game) {
    this.game = game;
  }

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public enum Type{
    BOUGHT,
    PLAYED,
    DISCARDED,
    TRASHED,
    DREW,
    WON,
    LOST;
  }




}
