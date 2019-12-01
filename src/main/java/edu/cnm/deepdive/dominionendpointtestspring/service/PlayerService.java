package edu.cnm.deepdive.dominionendpointtestspring.service;


import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

  private final PlayerRepository repository;

  public PlayerService(PlayerRepository repository) {
    this.repository = repository;
  }
/**
  public Player getOrCreatePlayer(String oauthKey) {
    return repository.getPlayerByUserName(oauthKey)
        .orElseGet(() -> {
        //  Player player = new Player(oauthKey);
         // player.setOauthKey(oauthKey);
         // return repository.save(player);
        });
  }
*/
}
