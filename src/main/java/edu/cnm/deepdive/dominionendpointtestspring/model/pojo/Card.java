package edu.cnm.deepdive.dominionendpointtestspring.model.pojo;

import edu.cnm.deepdive.dominionendpointtestspring.model.aggregates.GameStateInfo;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
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
    this.addValueIfPlayed = cardType.getAddValueIfPlayed();
    this.drawCardsWhenPlayed = cardType.getDrawCardsWhenPlayed();

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
  private int addValueIfPlayed;

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

  public int drawCardsWhenPlayed;


  public void setCost(int cost) {
    this.cost = cost;
  }

  public void setCardName(@NonNull String cardName) {
    this.cardName = cardName;
  }


  public enum CardType {
    COPPER {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Copper";
      }
    },
    SILVER {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }

      @Override
      public int getCost() {
        return 3;
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
        return 2;
      }

      @Override
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Silver";
      }
    },
    GOLD {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }

      @Override
      public int getCost() {
        return 6;
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
        return 3;
      }

      @Override
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Gold";
      }
    },
    ESTATE {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Estate";
      }
    },
    DUCHY {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Duchy";
      }
    },
    PROVINCE {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Province";
      }
    },

    CELLAR {
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        Hand currentHand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        currentHand.discardFromHand(additionalCards.get());
        gameStateInfo.getCurrentPlayerStateInfo().getDiscardPile()
            .addToDiscard(additionalCards.get());
        gameStateInfo.getCurrentPlayerStateInfo().setHand(currentHand);
        //discard any number of cards from hand, redraw that many cards
        // need to select which cards to be deleted
        int numDiscarded = additionalCards.get().size();
        //for (int i = 0; i < numDiscarded; i++) {
        // gameStateInfo.getCurrentPlayerStateInfo(.additionalCards.get(i));
        //}
        for (int i = 0; i < numDiscarded; i++) {
          DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
          Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
          hand.draw(drawPile, gameStateInfo, 1);
        }
        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn()
            .setActionsRemaining(actionsRemaining + 1);

        return gameStateInfo;
      }

      public int getCost() {
        return 0;
      }


      public int getVictoryPoints() {
        return 0;
      }


      public int getExtraGoldIfSilver() {
        return 0;
      }

      public int getMoneyValue() {
        return 0;
      }

      @Override
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }


      public String toString() {
        return "Cellar";
      }
    },
    MOAT {
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile, gameStateInfo, 2);

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        return gameStateInfo;
      }


      public int getCost() {
        return 2;
      }


      public int getVictoryPoints() {
        return 0;
      }


      public int getExtraGoldIfSilver() {
        return 0;
      }


      public int getMoneyValue() {
        return 0;
      }


      public int getAddValueIfPlayed() {
        return 0;
      }


      public int getDrawCardsWhenPlayed() {
        return 2;
      }

      @Override
      public String toString() {
        return "Moat";
      }
    },

    MARKET {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile, gameStateInfo, 1);

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn()
            .setActionsRemaining(actionsRemaining + 1);

        //  playerInfo.addBuy();
        int buysRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuysRemaining();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuysRemaining(buysRemaining + 1);

        //  playerInfo.addBuyingPower();\
        int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuyingPower();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuyingPower(buyingPower + 1);

        return gameStateInfo;
      }


      public int getCost() {
        return 5;
      }


      public int getAddValueIfPlayed() {
        return 1;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }


      public int getVictoryPoints() {
        return 0;
      }


      public int getExtraGoldIfSilver() {
        return 0;
      }


      public int getMoneyValue() {
        return 0;
      }


      public String toString() {
        return "Market";
      }
    },

    MERCHANT {
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile, gameStateInfo, 1);

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn()
            .setActionsRemaining(actionsRemaining + 1);
        return gameStateInfo;
      }

      @Override
      public int getCost() {
        return 3;
      }

      @Override
      public int getVictoryPoints() {
        return 0;
      }

      @Override
      public int getExtraGoldIfSilver() {
        return 1;
      }

      @Override
      public int getMoneyValue() {
        return 0;
      }

      @Override
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Merchant";
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 1;
      }
    },
    MILITIA {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuyingPower();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuyingPower(buyingPower + 2);
        if (!(gameStateInfo.getOtherPlayerStateInfo().getPlayer().isHasMoat())){
          gameStateInfo.getCurrentPlayerStateInfo().getTurn().setDidAttack(true);
        }
        return gameStateInfo;
      }

      @Override
      public int getCost() {
        return 4;
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
      public int getAddValueIfPlayed() {
        return 2;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Militia";
      }
    },

    MINE {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {


        gameStateInfo.getCurrentPlayerStateInfo().getHand().trashCard(additionalCards.get().get(0));
        gameStateInfo.getCurrentPlayerStateInfo().getHand().getCardsInHand()
            .add(additionalCards.get().get(1));

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        //TODO Gain a Treasure to your hand costing up to 3 more than it

        return gameStateInfo;
      }

      @Override
      public int getCost() {
        return 5;
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

      @Override
      public int getAddValueIfPlayed() {
        return 3;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }
    },

    REMODEL {
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        int upToCost = additionalCards.get().get(0).getCost() +2;
        gameStateInfo.getCurrentPlayerStateInfo().getHand().trashCard(additionalCards.get().get(0));
        if (additionalCards.get().get(1).getCost()<=upToCost){
          gameStateInfo.getCurrentPlayerStateInfo().getHand().getCardsInHand()
              .add(additionalCards.get().get(1));
        }
         int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
         gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);


        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Remodel";
      }
    },

    SMITHY {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
         DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
         Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
         hand.draw(drawPile,gameStateInfo, 3); // How many cards????
         gameStateInfo.getCurrentPlayerStateInfo().setHand(hand);

         int actionsRemaining = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
         gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);
        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Smithy";
      }
    },

    VILLAGE {
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile, gameStateInfo, 1);

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn()
            .setActionsRemaining(actionsRemaining + 2);

        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Village";
      }
    },
    WORKSHOP {
      public GameStateInfo play(GameStateInfo gameStateInfo, Optional<List<Card>> additionalCards) {

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        if (additionalCards.get().get(0).getCardType().getCost() <= 4) {
          hand.getCardsInHand().add(additionalCards.get().get(0));
        }
        //else return error

        return gameStateInfo;
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
      public int getAddValueIfPlayed() {
        return 0;
      }

      @Override
      public int getDrawCardsWhenPlayed() {
        return 0;
      }

      @Override
      public String toString() {
        return "Workshop";
      }
    };


    public abstract GameStateInfo play(GameStateInfo gameStateInfo,
        Optional<List<Card>> additionalCards);

    public abstract int getCost();

    public abstract int getVictoryPoints();

    public abstract int getExtraGoldIfSilver();

    public abstract int getMoneyValue();

    public abstract int getAddValueIfPlayed();

    public abstract int getDrawCardsWhenPlayed();
  }


}
