package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer.GamePlayerState;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerRepository extends CrudRepository<GamePlayer, Long> {

  GamePlayer getByPlayer(Player player);

  GamePlayer getByGamePlayerState(GamePlayerState gamePlayerState);

}
