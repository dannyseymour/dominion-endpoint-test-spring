package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayer;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayerGamePlayer;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 * The type Player.
 */
@Entity
@Table(
    indexes = @Index(columnList = "oauthKey", unique = true))
public class Player implements FlatPlayer {

  public Player() {
  }

  public Player(String oauthKey) {

    this.oauthKey = oauthKey;
  }

  @Id
  @Column(name = "player_id", updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @JsonIgnore
  private String oauthKey;

  private String displayName;


  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OrderBy("id")
  @JsonSerialize(contentAs = FlatPlayerGamePlayer.class)
  private List<GamePlayer> gamePlayers = new LinkedList<>();

  @Override
  public Long getId() {
    return id;
  }

  public String getOauthKey() {
    return oauthKey;
  }

  public void setOauthKey(String oauthKey) {
    this.oauthKey = oauthKey;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

  public void setId(Long id) {
    this.id = id;
  }



  public void setGamePlayers(
      List<GamePlayer> gamePlayers) {
    this.gamePlayers = gamePlayers;
  }
}