package ru.dosov.restvoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.repository.MenuRepository;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.repository.VoteRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractTest {

    @Autowired
    protected VoteRepository voteRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RestaurantRepository restRepository;

    @Autowired
    protected MenuRepository menuRepository;

    public abstract void createVote() throws Exception;

    public abstract void createDuplicateVote() throws Exception;

    public abstract void createUser() throws Exception;

    public abstract void getUserVotes() throws Exception;

    public abstract void getRestaurantsWithMenus() throws Exception;

    public abstract void createDuplicateMenu() throws Exception;
}
