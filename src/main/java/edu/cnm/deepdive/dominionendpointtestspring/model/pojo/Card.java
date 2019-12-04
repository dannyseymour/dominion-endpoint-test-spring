package edu.cnm.deepdive.dominionendpointtestspring.model.pojo;

import com.google.gson.annotations.Expose;

public class Card {

  public Card(String cardName) {
    this.cardName = cardName;
  }

  @Expose
  private CardType cardType;
  @Expose
  private int cost;
  @Expose
  private String cardName;

//  public Card(String cardName){
//    //TODO need to know about cardType... probably better to make an enum matching server
//    //should have costa
//    switch (cardName){
//      case "cellar":
//        cardType = CardType.Cellar
//
//    }
//  }
  public CardType getCardType() {
    return cardType;
  }

  public void setCardType(CardType cardType) {
    this.cardType = cardType;
  }

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public String getCardName() {
    return cardName;
  }

  public void setCardName(String cardName) {
    this.cardName = cardName;
  }

  @Override
  public String toString() {
    return "CardType: " + cardType + ", Cost: " + cost  + ", CardName: " + cardName;
  }

  public enum CardType {
    COPPER{
      @Override
      public int cost() {
        return 0;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    SILVER{
      @Override
      public int cost() {
        return 3;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    GOLD{
      @Override
      public int cost() {
        return 6;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    ESTATE{
      @Override
      public int cost() {
        return 2;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    DUCHY{
      @Override
      public int cost() {
        return 5;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    PROVINCE{
      @Override
      public int cost() {
        return 8;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    CELLAR{
      @Override
      public int cost() {
        return 2;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return true;
      }
    },

    MOAT{
      @Override
      public int cost() {
        return 2;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    MERCHANT{
      @Override
      public int cost() {
        return 3;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    VILLAGE{
      @Override
      public int cost() {
        return 3;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    WORKSHOP{
      @Override
      public int cost() {
        return 3;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    SMITHY{
      @Override
      public int cost() {
        return 4;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    REMODEL{
      @Override
      public int cost() {
        return 4;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return true;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    MILITIA{
      @Override
      public int cost() {
        return 4;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    MARKET{
      @Override
      public int cost() {
        return 5;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    MINE{
      @Override
      public int cost() {
        return 5;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return true;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    },

    TRASH{
      @Override
      public int cost() {
        return 0;
      }

      @Override
      public boolean requiresAdditionalCard() {
        return false;
      }

      @Override
      public boolean requiresMultipleCards() {
        return false;
      }
    };

    public abstract int cost();
    public abstract boolean requiresAdditionalCard();
    public abstract boolean requiresMultipleCards();
  }



}
