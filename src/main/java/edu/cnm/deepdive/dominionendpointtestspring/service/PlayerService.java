package edu.cnm.deepdive.dominionendpointtestspring.service;


import edu.cnm.deepdive.dominionservice.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionservice.model.entity.Player;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

  private final PlayerRepository repository;

  public PlayerService(PlayerRepository repository) {
    this.repository = repository;
  }

  public Player getOrCreatePlayer(String oauthKey) {
    return repository.getPlayerByOauthKey(oauthKey)
        .orElseGet(() -> {
          Player player = new Player();
          player.setOauthKey(oauthKey);
          return repository.save(player);
        });
  }

}
