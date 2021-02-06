package ru.dosov.restvoting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.dosov.restvoting.model.Vote;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.dosov.restvoting.TestData.*;

@DataJpaTest
//@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
class RepositoriesTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantRepository restRepository;

    @Autowired
    MenuRepository menuRepository;

    @Test
    void createVote() {
        entityManager.persist(restaurant);
        entityManager.persist(user);
        entityManager.flush();

        Vote created = voteRepository.save(getNewVote());
        int id = created.getId();
        Vote newVote = getNewVote();
        newVote.setId(id);
        assertThat(created).usingRecursiveComparison().isEqualTo(newVote);
        assertThat(voteRepository.findById(id).get()).usingRecursiveComparison().isEqualTo(newVote);
    }
}