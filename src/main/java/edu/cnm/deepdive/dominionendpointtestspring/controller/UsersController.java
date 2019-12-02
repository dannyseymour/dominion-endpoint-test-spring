package edu.cnm.deepdive.dominionendpointtestspring.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.GameRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.DAO.PlayerRepository;
import edu.cnm.deepdive.dominionendpointtestspring.model.entity.Player;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameEvents;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStates;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@ExposesResourceFor(Player.class)
public class UsersController {

  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private PlayerRepository playerRepository;

  @PostMapping("/validate/")
  public String validateToken(@RequestBody String idToken) throws FirebaseAuthException {
    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
    String uid = decodedToken.getUid();
    ArrayList<Player> registeredUsers = (ArrayList<Player>) playerRepository.findAll();
    boolean existingUser = false;
    for (Player player : registeredUsers){
      if (player.getUid().equals(uid)){
        existingUser = true;
      }
    }
    if (existingUser){
      return uid;

    }else{
      Player newPlayer = new Player(uid);
      playerRepository.save(newPlayer);
      return uid;
    }
  }


}
