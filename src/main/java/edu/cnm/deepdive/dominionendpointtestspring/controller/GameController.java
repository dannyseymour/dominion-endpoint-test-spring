package edu.cnm.deepdive.dominionendpointtestspring.controller;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfo;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfoTransferObject;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Game;
import edu.cnm.deepdive.dominionendpointtestspring.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@ExposesResourceFor(Game.class)
public class GameController {

  //private final GameRepository gameRepository;

 @Autowired
  public GameController() {

  }




  //TODO: Consider replacing with Firebase
  /**
  @PostMapping(value = "/create")
  public GameStateInfo createNewGame(@RequestBody Game newGame) {
   //TODO
    GameStateInfo gameStateInfo = new GameStateInfo(newGame);
    return gameStateInfo;
  }
*/

  @GetMapping(value = "/gamestateinfo")
  public GameStateInfoTransferObject getGameInfo(){
   Game game = new Game();
   Player player1 = new Player(1L, 10, 1, 1, 1, 0);
   Player player2 = new Player(2L, 8, 1, 1, 1, 0);
   List<Player> players = new ArrayList<>();
   players.add(player1);
   players.add(player2);
   game.setPlayers(players);
   GameStateInfo gameState = new GameStateInfo(game);
   GameStateInfoTransferObject gameStateInfoTransferObject = buildTransferObject(gameState);
    return gameStateInfoTransferObject;
  }

 private GameStateInfoTransferObject buildTransferObject(GameStateInfo gameState) {
   GameStateInfoTransferObject gameStateInfoTransferObject = new GameStateInfoTransferObject(
       gameState.getCurrentPlayerStateInfo().getHand().getCardsInHand(),
       gameState.getCurrentPlayerStateInfo().getPlayer().getPlayerScore(),
       gameState.getPlayerStateInfoPlayer2().getPlayer().getPlayerScore(),
       gameState.getCurrentPlayerStateInfo().getPlayer().getNumAction(),
       gameState.getCurrentPlayerStateInfo().getPlayer().getNumBuy(),
       4,
       gameState.getStacks(),
       gameState.getPlaysInPreviousTurn(),
       gameState.getCurrentPlayerStateInfo().getPhaseState().toString()
   );
       return gameStateInfoTransferObject;
 }

 @GetMapping("{gameid}/state")
  public GameStateInfo getCurrentTurnState(@PathVariable("gameid") long gameId) {
    //TODO add filter so GameStateInfo does not return all the info on the contents of other persons hand, either deck
   // GameStateInfo gameStateInfo = new GameStateInfo(gameRepository.getGameById(gameId));
    return new GameStateInfo();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(Exception.class)
  public void badRequest() {
  }

}
