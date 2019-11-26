package edu.cnm.deepdive.dominionendpointtestspring.entity;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfo;
import java.io.Serializable;
import java.util.List;
import org.springframework.lang.NonNull;


public class Card implements Serializable {
//  private final CardRepository cardRepository;

  public Card(String cardName, CardType cardType, int cost) {
    this.cardName = cardName;
    this.cardType = cardType;
    this.cost = cost;
  }

  public Card(CardType cardType) {
    this.cardType = cardType;

    //TODO implement constructor using card type
    //need to get info from database do not understand this
  }

  //Static factory method to make card
  public Card newCard(String cardName, CardType cardType, int cost) {
    return new Card(cardName, cardType, cost);
  }

     private int id;

  private CardType cardType;



  private int index;



  private DrawPile drawPile;

  private DiscardPile discardPile;

  private Hand hand;

  /***
   * cost of card
*/
  private int cost;

  /***
   * name of card
   */

  private String cardName;

  public int getCost() {
    return cost;
  }

  public String getCardName() {
    return cardName;
  }

  public CardType getCardType() {
    return cardType;
  }

  public void setCardType(@NonNull CardType cardType) {
    this.cardType = cardType;
  }

  public DrawPile getDrawPile() {
    return drawPile;
  }

  public void setDrawPile(DrawPile drawPile) {
    this.drawPile = drawPile;
  }

  public DiscardPile getDiscardPile() {
    return discardPile;
  }

  public void setDiscardPile(DiscardPile discardPile) {
    this.discardPile = discardPile;
  }

  public Hand getHand() {
    return hand;
  }

  public void setHand(Hand hand) {
    this.hand = hand;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public void setCardName(@NonNull String cardName) {
    this.cardName = cardName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }


  public enum CardType {
    COPPER {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}

      @Override
      public String toString() {
        return "Copper";
      }
    },
    SILVER {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}
      @Override
      public String toString() {
        return "Silver";
      }
    },
    GOLD {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}
      @Override
      public String toString() {
        return "Gold";
      }
    },
    ESTATE {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}
      @Override
      public String toString() {
        return "Estate";
      }
    },
    DUCHY {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}
      @Override
      public String toString() {
        return "Duchy";
      }
    },
    PROVINCE {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}
      @Override
      public String toString() {
        return "Province";
      }
    },

    CELLAR {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
       /** Hand currentHand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        currentHand.discardFromHand(additionalCards);
         gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile().addToDiscard(additionalCards);
        gameStateInfo.getCurrentPlayerStateInfo().setHand(currentHand);
        //discard any number of cards from hand, redraw that many cards
        // need to select which cards to be deleted
        int numDiscarded = additionalCards.size();
        //for (int i = 0; i < numDiscarded; i++) {
         // gameStateInfo.getCurrentPlayerStateInfo(.additionalCards.get(i));
        //}
        for (int i = 0; i < numDiscarded; i++) {
          DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
          Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
          hand.draw(drawPile,gameStateInfo, 1);
        }
        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining + 1);
*/
      }
      @Override
      public String toString() {
        return "Cellar";
      }
    },
    MOAT {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile,gameStateInfo, 2);

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);
         */
      }
      @Override
      public String toString() {
        return "Moat";
      }
    },

    MARKET {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile,gameStateInfo, 1);

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining + 1);

        //  playerInfo.addBuy();
        int buysRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuysRemaining();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuysRemaining(buysRemaining + 1);

        //  playerInfo.addBuyingPower();\
        int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuyingPower();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuyingPower(buyingPower + 1);
         */
      }
      @Override
      public String toString() {
        return "Market";
      }
    },

    MERCHANT {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile,gameStateInfo, 1);

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining + 1);

        gameStateInfo.getCurrentPlayerStateInfo().getTurn().addGoldIfSilver();
        //TODO add gold when playing silver
*/
      }
      @Override
      public String toString() {
        return "Merchant";
      }
    },
    MILITIA {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuyingPower();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuyingPower(buyingPower + 2);
         */
      }
      @Override
      public String toString() {
        return "Militia";
      }
    },

    MINE {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        //TODO make sure additional cards has a card in it (how to hand error state)
        //gameStateInfo.trashCard(additionalCards.get(0));
       // gameStateInfo.getTreasure();

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        //TODO Gain a Treasure to your hand costing up to 3 more than it
         */
      }
      @Override
      public String toString() {
        return "Mine";
      }
    },

    REMODEL {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        //TODO make sure additional cards has a card in it (how to hand error state)
        //gameStateInfo.trashCard(additionalCards.get(0));

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        //TODO Gain a card costing up to 2 more gold than the one you trashed.
*/
      }
      @Override
      public String toString() {
        return "Remodel";
      }
    },

    SMITHY {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile,gameStateInfo, 3); // How many cards????
        gameStateInfo.getCurrentPlayerStateInfo().setHand(hand);

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);
         */
      }
      @Override
      public String toString() {
        return "Smithy";
      }
    },

    VILLAGE {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {
        /**
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile,gameStateInfo, 1);

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining + 2);

*/
      }
      @Override
      public String toString() {
        return "Village";
      }
    },
    WORKSHOP {
      @Override
      public void play(GameStateInfo gameStateInfo, List<Card> additionalCards) {
/**

        int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        //TODO Gain card costing up to 4 gold
 */
      }
      @Override
      public String toString() {
        return "Workshop";
      }
    };


    public abstract void play(GameStateInfo gameStateInfo,
        List<Card> additionalCards);

  }

}
