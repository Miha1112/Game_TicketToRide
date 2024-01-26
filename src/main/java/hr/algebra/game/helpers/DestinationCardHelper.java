package hr.algebra.game.helpers;

import hr.algebra.game.model.DestinationCard;
import hr.algebra.game.model.Route;
import hr.algebra.game.model.Routes;

import java.util.*;
import java.util.stream.Collectors;

public class DestinationCardHelper {
    private static Queue<DestinationCard> deck = new LinkedList<>();
    static {
        createDestinationCards();
    }

    private static void createDestinationCards() {

        Routes.initializeRoutes();

        // Create a DestinationCard for each route
        Set<DestinationCard> destinationCards =
                Set.of(Route.values()).stream()
                        .map(route -> new DestinationCard(Set.of(route)))
                        .collect(Collectors.toSet());

        LinkedList<DestinationCard> shuffledDeck = new LinkedList<>(destinationCards);
        Collections.shuffle(shuffledDeck);
        deck.addAll(shuffledDeck);
    }

    public static DestinationCard drawCard() {
        return deck.poll();
    }

    private DestinationCardHelper() {
    }
}