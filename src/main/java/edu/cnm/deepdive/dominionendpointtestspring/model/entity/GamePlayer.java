package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGameGamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayerGamePlayer;
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
import org.springframework.lang.NonNull;

@Entity
public class GamePlayer implements FlatGameGamePlayer, FlatPlayerGamePlayer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "game_player_id")
  private Long id;

  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;

  @NonNull
  @ManyToOne (fetch = FetchType.EAGER)
  @JoinColumn (name="game_id", nullable = false, updatable = false)
  private Game game;

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn (name= "player_id", nullable = false, updatable = false)
  private Player player;


  private GamePlayerState playerState;

  private int actionsRemaining;

  private int buysRemaining;

  public int getActionsRemaining() {
    return actionsRemaining;
  }

  public void setActionsRemaining(int actionsRemaining) {
    this.actionsRemaining = actionsRemaining;
  }

  public int getBuysRemaining() {
    return buysRemaining;
  }

  public void setBuysRemaining(int buysRemaining) {
    this.buysRemaining = buysRemaining;
  }


@Override
  public Long getId() {
    return id;
  }
@Override
  @NonNull
  public Date getCreated() {
    return created;
  }
@Override
  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame(@NonNull Game game) {
    this.game = game;
  }
@Override
  @NonNull
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull Player player) {
    this.player = player;
  }

  public GamePlayerState getGamePlayerState() {
    return playerState;
  }

  public void setGamePlayerState(GamePlayerState playerState) {
    this.playerState = playerState;
  }

  public enum GamePlayerState{
    ACTIVE,
    PASSIVE;
  }


}
