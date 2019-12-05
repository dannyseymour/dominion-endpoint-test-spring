package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGame;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGameGamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayer;
import java.util.Date;
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
public class Turn {

  public Turn(@NonNull Game game,
      @NonNull Player player) {
    this.game = game;
    this.player = player;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "turn_id")
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

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id",nullable = false,updatable = false)
  @JsonSerialize(as = FlatGame.class)
  private Game game;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "player_id",nullable = false,updatable = false)
  @JsonSerialize(as = FlatGameGamePlayer.class)
  private Player player;

  private int buyingPower;

  public Long getId() {
    return id;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  @NonNull
  public Date getUpdated() {
    return updated;
  }

  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame(@NonNull Game game) {
    this.game = game;
  }

  @NonNull
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull Player player) {
    this.player = player;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  public void setUpdated(@NonNull Date updated) {
    this.updated = updated;
  }

  public int getBuyingPower() {
    return buyingPower;
  }

  public void setBuyingPower(int buyingPower) {
    this.buyingPower = buyingPower;
  }
}
