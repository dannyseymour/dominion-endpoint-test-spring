package edu.cnm.deepdive.dominionendpointtestspring.controller;

import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GamePlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.dao.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.GamePlayer;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.service.GameLogic;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameState;
import java.util.ArrayList;
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

  private final GameLogic gameLogic;

  private final PlayerRepository playerRepository;

  @Autowired
  public GameController(
      GameRepository gameRepository,
      GamePlayerRepository gamePlayerRepository,
      GameLogic gameLogic,
      PlayerRepository playerRepository) {

    this.gameRepository = gameRepository;
    this.gamePlayerRepository = gamePlayerRepository;
    this.gameLogic = gameLogic;
    this.playerRepository = playerRepository;
  }

  @PostMapping(value = "newgame",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public Game joinGame(Authentication authentication) {
    Player player = (Player) authentication.getPrincipal();
    /**
     * If this is the first game in the database, create new and set to Waiting
     */
    Game game = gameRepository.findFirstByCurrentState(GameState.WAITING)
        .filter(
            g -> g.getGamePlayers().stream().noneMatch(gp -> gp.getPlayer().getId()
                .equals(player.getId()))
        )
        .orElse(new Game());
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(player);
    gamePlayer.setGame(game);
    game.getGamePlayers().add(gamePlayer);
    if (game.getGamePlayers().size()==2) {
      GamePlayer gamePlayer1 = game.getGamePlayers().get(0);
      GamePlayer gamePlayer2 = game.getGamePlayers().get(1);
      game.setCurrentGamePlayer(gamePlayer1);
      game= gameLogic.initializeGame(game, gamePlayer1, gamePlayer2);
    }
    else if (game.getGamePlayers().size()==1){
      game.setCurrentState(GameState.WAITING);
    }
    return gameRepository.save(game);
  }

  @GetMapping(value = "{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Game get(@PathVariable long id, Authentication authentication){
    Player player = (Player) authentication.getPrincipal();
    //TODO check player
    Game game = gameRepository.findByIdAndPlayer(id,player.getId()).get();
    return gameRepository.save(game);
  }



  @PostMapping(value="/action/{gameid}/{cardname}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public Game doAction(Authentication authentication, @PathVariable("gameid") int gameId,
      @PathVariable("cardname") String cardName, @RequestBody List<String> cards) {
   Player player = (Player) authentication.getPrincipal();
    Game game = gameRepository.findByIdAndPlayer(gameId,player.getId()).get();
    if (player.equals(game.getCurrentPlayer())) {
      GamePlayer gamePlayer = matchPlayerToGamePlayer(player);
      if (cards == null){
        game = gameLogic.playCard(cardName, game, gamePlayer);
        return gameRepository.save(game);
      }else{
       game= gameLogic.playCard(cardName,game, gamePlayer, (ArrayList<String>) cards);
        return gameRepository.save(game);
      }
    }
    else{
      return game;
    }
  }

  @PostMapping(value="/buy/{gameid}/{cardname}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public Game buyCard(Authentication authentication, @PathVariable("gameid") int gameId,
      @PathVariable("cardname") String cardName) {
    Player player = (Player) authentication.getPrincipal();
    Game game = gameRepository.findByIdAndPlayer(gameId,player.getId()).get();
    if (player.equals(game.getCurrentPlayer())) {
      GamePlayer gamePlayer = matchPlayerToGamePlayer(player);
        game= gameLogic.buyCard(cardName, game, gamePlayer);
        return gameRepository.save(game);
    }
    else{
      return game;
    }

  }


  @PostMapping(value="/endphase/{gameid}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public Game endPhase(Authentication authentication, @PathVariable("gameid") int gameId) {

    Player player = (Player) authentication.getPrincipal();
    Game game = gameRepository.findByIdAndPlayer(gameId,player.getId()).get();
    if (player.equals(game.getCurrentPlayer())) {
      GamePlayer gamePlayer = matchPlayerToGamePlayer(player);
      game = gameLogic.endPhase(game, gamePlayer);
      return gameRepository.save(game);
    }
    else{
      return game;
    }
  }


  @PostMapping(value="/discard/{gameid}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public Game discardCard(Authentication authentication, @PathVariable("gameid") int gameId,
      @RequestBody List<String> cardNames) {
    Player player = (Player) authentication.getPrincipal();
    Game game = gameRepository.findByIdAndPlayer(gameId,player.getId()).get();
    if (player.equals(game.getCurrentPlayer())) {
      GamePlayer gamePlayer = matchPlayerToGamePlayer(player);
      game = gameLogic.discardForMilitia(game, gamePlayer, cardNames);
      return gameRepository.save(game);
    }
    else{
      return game;
    }
  }

  @GetMapping("/plays/{gameid}")
  public List<String> getPlaysFromLastTurn(Authentication authentication, @PathVariable("gameid")int gameId){
    Player player = (Player) authentication.getPrincipal();
    Game game = gameRepository.findByIdAndPlayer(gameId,player.getId()).get();
    return gameLogic.getListOfPlaysStrings(game);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {
  }

  private GamePlayer matchPlayerToGamePlayer(Player player){
    return gamePlayerRepository.getByPlayer(player);
  }

  private Player matchGamePlayerToPlayer(GamePlayer gamePlayer, Game game){
    return playerRepository.getPlayerByGamePlayerAndGameId(gamePlayer, game.getId());
  }
}
