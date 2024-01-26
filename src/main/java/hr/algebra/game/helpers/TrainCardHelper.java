package hr.algebra.game.helpers;

import hr.algebra.game.model.RouteColor;
import hr.algebra.game.model.TrainCard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class TrainCardHelper {
    private static Queue<TrainCard> deck = new LinkedList<>();
    static {
        init();
    }
    private static void init() {

        int cardsPerColor = 27;

        for (RouteColor color : RouteColor.values()) {
            if (color == RouteColor.EMPTY)
                continue;

            for (int i = 0; i < cardsPerColor; i++) {
                deck.offer(new TrainCard(color));
            }
        }


        shuffleDeck();
    }


    private static void shuffleDeck() {

        Collections.shuffle((LinkedList<TrainCard>) deck);
    }

    public static TrainCard drawCard() {
        return deck.poll();
    }


}