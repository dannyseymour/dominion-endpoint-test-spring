package edu.cnm.deepdive.dominionendpointtestspring.model.pojo;

import edu.cnm.deepdive.dominionendpointtestspring.GameStateInfo;
import java.io.Serializable;
import java.util.List;
import org.springframework.lang.NonNull;


public class Card implements Serializable {
//  private final CardRepository cardRepository;



  public Card(CardType cardType) {
    this.cardType = cardType;
    this.cost = cardType.getCost();
    this.cardName = cardType.toString();
    this.moneyValue = cardType.getMoneyValue();
    this.extraGoldIfSilver = cardType.getExtraGoldIfSilver();
    this.victoryPoints = cardType.getVictoryPoints();


    //TODO implement constructor using card type
    //need to get info from database do not understand this
  }



  private CardType cardType;

  private int victoryPoints;

  private int extraGoldIfSilver;

  /***
   * cost of card
*/
  private int cost;

  /***
   * name of card
   */

  private String cardName;
  private int moneyValue;

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



  public void setCost(int cost) {
    this.cost = cost;
  }

  public void setCardName(@NonNull String cardName) {
    this.cardName = cardName;
  }




  public enum CardType {
    COPPER {
      @Override
      public void play(GameStateInfo gameStateInfo,
          List<Card> additionalCards) {}

      @Override
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
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
      public int getCost() {
        return 0;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 0;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

      @Override
      public String toString() {
        return "Workshop";
      }
    };


    public abstract void play(GameStateInfo gameStateInfo,
        List<Card> additionalCards);
      public abstract int getCost();
      public abstract int getVictoryPoints();
      public abstract int getExtraGoldIfSilver();
      public abstract int getMoneyValue();
  }


}
