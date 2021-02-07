package ru.dosov.restvoting;

import ru.dosov.restvoting.model.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TestData {

    public final static LocalDate date1 = LocalDate.of(2021, 1, 12);
    public final static LocalDate date2 = LocalDate.of(2021, 1, 13);

    public final static User user = new User(3, "Kate", "kate@mail.ru", "katepassword", EnumSet.of(Role.USER));

    public final static Dish dish1 = new Dish(1, "potato", 4005L, true);
    public final static Dish dish2 = new Dish(2, "pea soup", 3500L, true);
    public final static Dish dish3 = new Dish(3, "tea", 1000L, true);
    public final static Dish dish4 = new Dish(4, "bread", 510L, true);
    public final static Dish dish5 = new Dish(5, "rice", 3000L, true);
    public final static Dish dish6 = new Dish(6, "buckwheat", 3570L, true);
    public final static Dish dish7 = new Dish(7, "oatmeal", 2000L, true);
    public final static Dish dish8 = new Dish(8, "meat", 4500L, true);
    public final static Dish dish9 = new Dish(9, "wine", 2550L, true);

    public final static List<Dish> dishes = List.of(dish1, dish2, dish3, dish4, dish5, dish6, dish7, dish8, dish9);

    public final static Restaurant restaurant1 = new Restaurant(1, "White House");
    public final static Restaurant restaurant2 = new Restaurant(2, "ENOT");
    public final static Restaurant restaurant3 = new Restaurant(3, "Green tree");

    public final static Menu menu1 = new Menu(1, date1, restaurant1, Set.of(dish3, dish2, dish1));
    public final static Menu menu2 = new Menu(2, date1, restaurant2, Set.of(dish6, dish5, dish4));
    public final static Menu menu3 = new Menu(3, date1, restaurant3, Set.of(dish9, dish8, dish7));
    public final static List<Menu> menus = List.of(menu3, menu2, menu1);

    public static Vote vote1 = new Vote(3, date1, user, restaurant3);
    public static Vote vote2 = new Vote(6, date2, user, restaurant1);
    public static List<Vote> userVotes = List.of(vote2, vote1);

    public static Vote getNewVote(Restaurant restaurant) {
        return new Vote(
                null,
                LocalDate.now(),
                user,
                restaurant
        );
    }

    public static User getNewUser() {
        return new User(
                null,
                "dummy",
                "dummy@mail.ru",
                "dummypassword",
                EnumSet.of(Role.USER)
        );
    }

    public static Menu getNewMenu() {
        return new Menu(
                null,
                LocalDate.now(),
                restaurant1,
                Set.of(dish8, dish5, dish2)
        );
    }

    public static List<Restaurant> getRestaurantsWithMenu() {
        restaurant1.setMenus(Set.of(menu1));
        restaurant2.setMenus(Set.of(menu2));
        restaurant3.setMenus(Set.of(menu3));
        return List.of(restaurant3, restaurant2, restaurant1);
    }
}
