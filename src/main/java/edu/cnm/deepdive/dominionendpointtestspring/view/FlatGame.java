package edu.cnm.deepdive.dominionendpointtestspring.view;

import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.util.Date;

public interface FlatGame {

   Long getId();

   GameState getCurrentState();

   Date getCreated();

   Date getUpdated();

}
