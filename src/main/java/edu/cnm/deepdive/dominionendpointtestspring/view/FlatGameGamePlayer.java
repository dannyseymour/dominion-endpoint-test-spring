package edu.cnm.deepdive.dominionendpointtestspring.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import java.util.Date;

public interface FlatGameGamePlayer {

  Long getId();

  Date getCreated();


  @JsonSerialize(as=FlatPlayer.class)
  Player getPlayer();

}