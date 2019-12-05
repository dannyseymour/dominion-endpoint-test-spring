package edu.cnm.deepdive.dominionendpointtestspring.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.dominionendpointtestspring.state.GameStateInfo;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatGame;
import edu.cnm.deepdive.dominionendpointtestspring.view.FlatPlayer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class Card {

  public Card(Type type) {
    this.type = type;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "card_id")
  private Long id;

  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;

  @NonNull
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date updated;

  private Location location;


  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id", updatable = false)
  @JsonSerialize(as = FlatGame.class)
  private Game game;


  public enum Location {
    HAND,
    DISCARD,
    DRAW_PILE,
    STACK;
  }

  @NonNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "player_id",nullable = false,updatable = false)
  @JsonSerialize(as = FlatPlayer.class)
  private Player player;

  private Type type;

  public Long getId() {
    return id;
  }

  private int orderInLocation;

  public void setId(Long id) {
    this.id = id;
  }

  public int getOrderInLocation() {
    return orderInLocation;
  }

  public void setOrderInLocation(int orderInLocation) {
    this.orderInLocation = orderInLocation;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(@NonNull Date updated) {
    this.updated = updated;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @NonNull
  public Player getPlayer() {
    return player;
  }

  public void setPlayer(@NonNull Player player) {
    this.player = player;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public enum Type {
    COPPER(60, initialPlayerCount, 0, 0, 0, 1, 0, 0) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }

    },
    SILVER(initialTotalCount, initialPlayerCount, 3, 0, 0, 2, 0, 0) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }

    },
    GOLD(initialTotalCount, initialPlayerCount, 6, 0, 0, 3, 0, 0) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }


    },
    ESTATE(initialTotalCount, initialPlayerCount, 2, 1, 0, 0, 0, 0) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }


    },
    DUCHY(initialTotalCount, initialPlayerCount, 5, 3, 0, 0, 0, 0) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }


    },
    PROVINCE(initialTotalCount, initialPlayerCount, 8, 6, 0, 0, 0, 0) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        return gameStateInfo;
      }


    },

    //TODO deal with draw
    CELLAR(initialTotalCount, initialPlayerCount, 2, 0, 0, 0, 0, drawCards) {
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

    },
    MOAT(initialTotalCount, initialPlayerCount, 2, 0, 0, 0, 0, 2) {
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

    },

    MARKET(initialTotalCount, initialPlayerCount, 5, 0, 0, 0, 1, 1) {
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

    },

    MERCHANT(initialTotalCount, initialPlayerCount, 3, 0, 1, 0, 0, 1) {
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

    },
    MILITIA(initialTotalCount, initialPlayerCount, 4, 0, 0, 0, 2, drawCards) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        int buyingPower = gameStateInfo.getCurrentPlayerStateInfo().getTurn().getBuyingPower();
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setBuyingPower(buyingPower + 2);
        if (!(gameStateInfo.getOtherPlayerStateInfo().getPlayer().isHasMoat())) {
          gameStateInfo.getCurrentPlayerStateInfo().getTurn().setDidAttack(true);
        }
        return gameStateInfo;
      }

    },

    MINE(initialTotalCount, initialPlayerCount, 5, 0, 0, 0, 0, 0) {
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

    },

    REMODEL(initialTotalCount, initialPlayerCount, 4, 0, 0, 0, 0, 0) {
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {

        int upToCost = additionalCards.get().get(0).getCost() + 2;
        gameStateInfo.getCurrentPlayerStateInfo().getHand().trashCard(additionalCards.get().get(0));
        if (additionalCards.get().get(1).getCost() <= upToCost) {
          gameStateInfo.getCurrentPlayerStateInfo().getHand().getCardsInHand()
              .add(additionalCards.get().get(1));
        }
        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);

        return gameStateInfo;
      }

    },

    SMITHY(initialTotalCount, initialPlayerCount, 4, 0, 0, 0, 0, 3) {
      @Override
      public GameStateInfo play(GameStateInfo gameStateInfo,
          Optional<List<Card>> additionalCards) {
        DrawPile drawPile = gameStateInfo.getCurrentPlayerStateInfo().getDrawPile();
        Hand hand = gameStateInfo.getCurrentPlayerStateInfo().getHand();
        hand.draw(drawPile, gameStateInfo, 3); // How many cards????
        gameStateInfo.getCurrentPlayerStateInfo().setHand(hand);

        int actionsRemaining =
            gameStateInfo.getCurrentPlayerStateInfo().getTurn().getActionsRemaining() - 1;
        gameStateInfo.getCurrentPlayerStateInfo().getTurn().setActionsRemaining(actionsRemaining);
        return gameStateInfo;
      }

    },

    VILLAGE(initialTotalCount, initialPlayerCount, 3, 0, 0, 0, 0, 1) {
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

    },
    WORKSHOP(initialTotalCount, initialPlayerCount, 3, 0, 0, 0, 0, 0) {
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

    };


    private final int initialTotalCount;
    private final int initialPlayerCount;
    private final int cost;
    private final int victoryPoints;
    private final int extraGoldIfSilver;
    private final int moneyValue;
    private final int extraGold;
    private final int drawCards;

    Type(int initialTotalCount, int initialPlayerCount, int cost, int victoryPoints,
        int extraGoldIfSilver, int moneyValue,
        int extraGold,
        int drawCards) {
      this.initialTotalCount = initialTotalCount;
      this.initialPlayerCount = initialPlayerCount;
      this.cost = cost;
      this.victoryPoints = victoryPoints;
      this.extraGoldIfSilver = extraGoldIfSilver;
      this.moneyValue = moneyValue;
      this.extraGold = extraGold;
      this.drawCards = drawCards;
    }

    public int getInitialTotalCount() {
      return initialTotalCount;
    }

    public int getInitialPlayerCount() {
      return initialPlayerCount;
    }

    public int getCost() {
      return cost;
    }

    public int getVictoryPoints() {
      return victoryPoints;
    }

    public int getExtraGoldIfSilver() {
      return extraGoldIfSilver;
    }

    public int getMoneyValue() {
      return moneyValue;
    }

    public int getExtraGold() {
      return extraGold;
    }

    public int getDrawCards() {
      return drawCards;
    }

    public abstract GameStateInfo play(GameStateInfo gameStateInfo,
       List<String> additionalCards);


  }
}

}