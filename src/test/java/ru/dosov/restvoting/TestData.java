package ru.dosov.restvoting;

import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.model.Vote;

import java.time.LocalDate;
import java.util.EnumSet;

public class TestData {

    private final static Integer START_SEQ = 100000;
    public final static User user = new User(null, "Dummy", "dummy@mail.ru", "dummypassword", EnumSet.of(Role.USER));
    //    private final Dish dish1 = new Dish(START_SEQ, "potato", 3000L, true);
//    private final Dish dish2 = new Dish(START_SEQ + 1, "meat", 6500L, true);
//    private final Dish dish3 = new Dish(START_SEQ + 2, "bread", 1000L, true);
    public final static Restaurant restaurant = new Restaurant(null, "Cozy immigrant");

    public static Vote getNewVote() {
        return new Vote(
                null,
                LocalDate.now(),
                user,
                restaurant
        );
    }
}
