package edu.cnm.deepdive.dominionendpointtestspring.model.DAO;


import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {


  Optional<Player> findPlayerById(Long id);
  Optional<Player> getPlayerByUserName(String oauthKey);
  @Override
  void deleteAll();

  Optional<Player> getPlayerById(Long id);

}