package edu.cnm.deepdive.dominionendpointtestspring.service;


import edu.cnm.deepdive.dominionendpointtestspring.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

  private final PlayerRepository repository;

  public PlayerService(PlayerRepository repository) {
    this.repository = repository;
  }

  public Player getOrCreatePlayer(String oauthKey, String displayName) {
    return repository.findByOauthKey(oauthKey)
        .orElseGet(() -> {
          Player player = new Player(oauthKey);
          player.setDisplayName(displayName);
          return repository.save(player);
        });
  }

}
