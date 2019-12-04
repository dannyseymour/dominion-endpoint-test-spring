package edu.cnm.deepdive.dominionendpointtestspring.controller;

import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GamePlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("games")
@ExposesResourceFor(Game.class)
public class GameController {


  private final GameRepository gameRepository;

  private final GamePlayerRepository gamePlayerRepository;

  @Autowired
  public GameController(
      GameRepository gameRepository,
      GamePlayerRepository gamePlayerRepository) {

    this.gameRepository = gameRepository;
    this.gamePlayerRepository = gamePlayerRepository;
  }

  @PostMapping(value = "newgame")
  public Game joinGame(Authentication authentication) {
    Player player = (Player) authentication.getPrincipal();
    /**
     * If this is the first game in the database, create new and set to Waiting
     */
    Game game = gameRepository.findFirstByCurrentState(GameState.WAITING)
        .filter(
            g -> g.getGamePlayers().stream().noneMatch(gp -> gp.getPlayer().getId()==player.getId())
        )
        .orElse(new Game());
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(player);
    gamePlayer.setGame(game);
    game.getGamePlayers().add(gamePlayer);
    //TODO update state and create turn as necessary
    return gameRepository.save(game);
  }

  @GetMapping(value = "{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Game get(@PathVariable long id, Authentication authentication){
    Player player = (Player) authentication.getPrincipal();
    //TODO check authentication
    return gameRepository.findByIdAndPlayer(id,player.getId()).get();
  }

  /*@PostMapping(value = "{id}/plays",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public Game play(@PathVariable long id,@RequestBody Play play,Authentication authentication){
    Player player = (Player) authentication.getPrincipal();
    Game game = get(id,authentication);
    //TODO use gamelogic service to validate play against game and process play if valid
    return gameRepository.save(game);
  }*/



  @PostMapping("/getmynewgame/{gameid}")
  public GameStateInfo getMyNewGame(Authentication authentication, @PathVariable("gameid") int gameId) {
    ArrayList<String> initialCards = new ArrayList<>();
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Estate");
    LinkedHashMap<String, Integer> stacks = new LinkedHashMap<>();
    stacks.put("Copper", 60);
    stacks.put("Silver",40);
    stacks.put("Gold",30);
    stacks.put("Estate",8);
    stacks.put("Duchy",8);
    stacks.put("Province",8);
    stacks.put("Cellar",10);
    stacks.put("Moat",10);
    stacks.put("Merchant",10);
    stacks.put("Village",10);
    stacks.put("Workshop",10);
    stacks.put("Smithy",10);
    stacks.put("Remodel",10);
    stacks.put("Militia", 10);
    stacks.put("Market",10);
    stacks.put("Mine",10);
    stacks.put("Trash",0);
    return new GameStateInfo(gameId, initialCards, 3, 3, 1, 1,
        4, false, stacks, null, PhaseState.ACTING, false, true);
  }


  @PostMapping("/action/{gameid}/{cardname}")
  public GameStateInfo doAction(Authentication authentication, @PathVariable("gameid") int gameId,
      @PathVariable("cardname") String cardName, @RequestBody List<String> cards) {
    ArrayList<String> initialCards = new ArrayList<>();
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Estate");
    LinkedHashMap<String, Integer> stacks = new LinkedHashMap<>();
    stacks.put("Copper", 60);
    stacks.put("Silver",40);
    stacks.put("Gold",30);
    stacks.put("Estate",8);
    stacks.put("Duchy",8);
    stacks.put("Province",8);
    stacks.put("Cellar",10);
    stacks.put("Moat",10);
    stacks.put("Merchant",10);
    stacks.put("Village",10);
    stacks.put("Workshop",10);
    stacks.put("Smithy",10);
    stacks.put("Remodel",10);
    stacks.put("Militia", 10);
    stacks.put("Market",10);
    stacks.put("Mine",10);
    stacks.put("Trash",0);
    return new GameStateInfo(gameId, initialCards, 3, 0, 1, 1,
        4, false, stacks, null, PhaseState.BUYING, false, true);
  }

  @PostMapping("/buy/{gameid}/{cardname}")
  public GameStateInfo buyCard(Authentication authentication, @PathVariable("gameid") int gameId,
      @PathVariable("cardname") String cardName, @RequestBody List<String>cards) {
    ArrayList<String> initialCards = new ArrayList<>();
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Estate");
    LinkedHashMap<String, Integer> stacks = new LinkedHashMap<>();
    stacks.put("Copper", 60);
    stacks.put("Silver",40);
    stacks.put("Gold",30);
    stacks.put("Estate",8);
    stacks.put("Duchy",8);
    stacks.put("Province",8);
    stacks.put("Cellar",10);
    stacks.put("Moat",10);
    stacks.put("Merchant",10);
    stacks.put("Village",10);
    stacks.put("Workshop",10);
    stacks.put("Smithy",10);
    stacks.put("Remodel",10);
    stacks.put("Militia", 10);
    stacks.put("Market",10);
    stacks.put("Mine",10);
    stacks.put("Trash",0);
    return new GameStateInfo(gameId, initialCards, 0, 0, 1, 1,
        4, false, stacks, null, PhaseState.PASSIVE, false, true);
  }

  @PostMapping("/endphase/{gameid}")
  public GameStateInfo endPhase(Authentication authentication, @PathVariable("gameid") int gameId) {
    ArrayList<String> initialCards = new ArrayList<>();
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Estate");
    LinkedHashMap<String, Integer> stacks = new LinkedHashMap<>();
    stacks.put("Copper", 60);
    stacks.put("Silver",40);
    stacks.put("Gold",30);
    stacks.put("Estate",8);
    stacks.put("Duchy",8);
    stacks.put("Province",8);
    stacks.put("Cellar",10);
    stacks.put("Moat",10);
    stacks.put("Merchant",10);
    stacks.put("Village",10);
    stacks.put("Workshop",10);
    stacks.put("Smithy",10);
    stacks.put("Remodel",10);
    stacks.put("Militia", 10);
    stacks.put("Market",10);
    stacks.put("Mine",10);
    stacks.put("Trash",0);
    return new GameStateInfo(gameId, initialCards, 0, 0, 1, 1,
        4, false, stacks, null, PhaseState.PASSIVE, false, true);
  }


  @PostMapping("/discard/{gameid}/{cardname}")
  public GameStateInfo discardCard(Authentication authentication, @PathVariable("gameid") int gameId,
      @PathVariable("cardname") String cardName) {
    ArrayList<String> initialCards = new ArrayList<>();
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Copper");
    initialCards.add("Estate");
    LinkedHashMap<String, Integer> stacks = new LinkedHashMap<>();
    stacks.put("Copper", 60);
    stacks.put("Silver",40);
    stacks.put("Gold",30);
    stacks.put("Estate",8);
    stacks.put("Duchy",8);
    stacks.put("Province",8);
    stacks.put("Cellar",10);
    stacks.put("Moat",10);
    stacks.put("Merchant",10);
    stacks.put("Village",10);
    stacks.put("Workshop",10);
    stacks.put("Smithy",10);
    stacks.put("Remodel",10);
    stacks.put("Militia", 10);
    stacks.put("Market",10);
    stacks.put("Mine",10);
    stacks.put("Trash",0);
    return new GameStateInfo(gameId, initialCards, 0, 0, 1, 1,
        4, false, stacks, null, PhaseState.ACTING, false, true);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {
  }
}
