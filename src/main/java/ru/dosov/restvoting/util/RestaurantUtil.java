package ru.dosov.restvoting.util;

import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static RestaurantTo getTo(Restaurant rest) {
        return new RestaurantTo(
                rest.getId(),
                rest.getName()
        );
    }

    public static List<RestaurantTo> getListTo(List<Restaurant> rests) {
        return rests == null
                ? List.of()
                : rests.stream().map(RestaurantUtil::getTo).collect(Collectors.toList());
    }
}