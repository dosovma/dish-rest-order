package ru.dosov.restvoting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import ru.dosov.restvoting.AbstractTest;
import ru.dosov.restvoting.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dosov.restvoting.TestData.*;


class RepositoriesTest extends AbstractTest {

    @Test
    public void createVote() {
        Vote first = voteRepository.save(getNewVote(restaurant1));
        int id = first.getId();
        Vote second = getNewVote(restaurant1);
        second.setId(id);
        assertThat(first).usingRecursiveComparison().isEqualTo(second);
        assertThat(voteRepository.findById(id).get()).usingRecursiveComparison().isEqualTo(second);
    }

    @Test
    public void createDuplicateVote() {
        voteRepository.save(getNewVote(restaurant1));
        Vote secondVote = getNewVote(restaurant2);
        assertThrows(DataIntegrityViolationException.class, () -> voteRepository.save(secondVote));
    }

    @Test
    public void createUser() {
        User first = userRepository.save(getNewUser());
        int id = first.getId();
        User second = getNewUser();
        second.setId(id);
        assertThat(first).usingRecursiveComparison().isEqualTo(second);
        assertThat(userRepository.findById(id).get()).usingRecursiveComparison().isEqualTo(second);
    }

    @Test
    public void getUserVotes() {
        List<Vote> userVotesFromDB = voteRepository.getAllByUserOrDate(user.getId(), date1, date2);
        assertThat(userVotesFromDB).usingElementComparatorIgnoringFields().isEqualTo(userVotes);
    }

    @Test
    public void getRestaurantsWithMenus() {
        List<Restaurant> restaurantsFromDB = restRepository.getAllWithDishes(date1);
        assertThat(restaurantsFromDB).usingElementComparatorIgnoringFields().isEqualTo(getRestaurantsWithMenu());

        List<Menu> menusFromDB = restaurantsFromDB.stream().map(Restaurant::getMenus).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        assertThat(menusFromDB).usingElementComparatorIgnoringFields().isEqualTo(menus);

        List<Dish> dishesFromDB = menusFromDB.stream().map(Menu::getDishes).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        assertThat(dishesFromDB).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("menus").isEqualTo(dishes);
    }

    @Test
    public void createDuplicateMenu() {
        menuRepository.save(getNewMenu());
        assertThrows(DataIntegrityViolationException.class, () -> menuRepository.save(getNewMenu()));
    }
}