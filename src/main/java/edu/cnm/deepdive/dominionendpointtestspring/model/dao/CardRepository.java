package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Location;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Type;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Game, Long> {

  Optional<List<Card>> getAllByGamePlayerAndLocation(GamePlayer player, Location location);

  Optional<List<Card>> getAllByLocationAndType(Location location, Type type);


  Optional<Card> getByLocationAndGamePlayerAndType(Location location, GamePlayer player, Type type);

  Optional<ArrayList<Card>> getAllByPlayer(Player player);

  void save(Card card);

  void saveAll(List<Card> cards);

  Optional<Card> getByType(Type valueOf);

  Optional<List<Card>> getAllByTypeAndLocation(Type type, Location stack);

  Optional<Integer> countAllByTypeAndLocation(Type type, Location location);

  Optional<ArrayList<Card>> getAllByGamePlayer(GamePlayer gamePlayer);

  Optional<Integer> countAllByGamePlayerAndLocation(GamePlayer gamePlayer, Location hand);

  ArrayList<Card> getAllByGamePlayerAndLocationOrderByOrderInLocation(GamePlayer gamePlayer, Location location);
}