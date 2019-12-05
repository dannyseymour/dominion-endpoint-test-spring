package edu.cnm.deepdive.dominionendpointtestspring.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import java.util.Date;

public interface FlatPlayerGamePlayer {

  Long getId();

  Date getCreated();


  @JsonSerialize(as=FlatGame.class)
  Game getGame();

}