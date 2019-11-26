package edu.cnm.deepdive.dominionendpointtestspring.entity;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;

public class Hand {


 // @Autowired
//  private CardRepository cardRepository;

  private ArrayList<Card> cardsInHand;


  public Hand(ArrayList cardsInHand) {
    this.cardsInHand = cardsInHand;
  }


  private int id;


  private Player player;


  private Game game;


  private List<Card> cards;

  public void discardFromHand(List<Card> cards){
    for (int i =0; i<cards.size(); i++){
      for (int j=0; j<cardsInHand.size(); j++){
        if (cards.get(i).getCardType().equals(cardsInHand.get(j).getCardType())){
          cardsInHand.remove(cardsInHand.get(j));
          j=cardsInHand.size();
        }
      }
    }
  }


  public List<Card> draw(DrawPile drawPile, GameStateInfo gameStateInfo){
    cardsInHand.add(drawPile.getTopCard(gameStateInfo));
    return cardsInHand;
  }

  public List<Card> draw(DrawPile drawPile, GameStateInfo gameStateInfo, int numOfCards){
    for (int i = 0; i < numOfCards; i++) {
      draw(drawPile, gameStateInfo);
    }
    return cardsInHand;
  }
/**
  public CardRepository getCardRepository() {
    return cardRepository;
  }

  public void setCardRepository(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }
*/
  public List<Card> getCardsInHand() {
    return cardsInHand;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NonNull
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull Player player) {
    this.player = player;
  }

  @NonNull
  public Game getGame() {
    return game;
  }

  public void setGame(@NonNull Game game) {
    this.game = game;
  }


  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }
}