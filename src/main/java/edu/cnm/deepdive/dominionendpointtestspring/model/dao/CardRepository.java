package edu.cnm.deepdive.dominionendpointtestspring.model.dao;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Location;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Card.Type;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;

import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {

  Optional<List<Card>> getAllByPlayerAndLocation(Player player, Location location);



  Optional<Card> getByLocationAndPlayerAndType(Location location, Player player, Type type);

  Optional<ArrayList<Card>> getAllByPlayer(Player player);

  

  Optional<Card> getByType(Type valueOf);

  Optional<List<Card>> getAllByTypeAndLocation(Type type, Location stack);

  Optional<Integer> countAllByTypeAndLocation(Type type, Location location);


  Optional<Integer> countAllByPlayerAndLocation(Player player, Location hand);

  ArrayList<Card> getAllByPlayerAndLocationOrderByOrderInLocation(Player player, Location location);
}