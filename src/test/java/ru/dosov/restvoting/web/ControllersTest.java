package ru.dosov.restvoting.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.dosov.restvoting.AbstractTest;
import ru.dosov.restvoting.config.AppConfig;
import ru.dosov.restvoting.model.*;
import ru.dosov.restvoting.to.MenuTo;
import ru.dosov.restvoting.to.VoteTo;
import ru.dosov.restvoting.util.MenuUtil;
import ru.dosov.restvoting.util.VoteUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.dosov.restvoting.TestData.*;

class ControllersTest extends AbstractTest {

    @Value(value = "${appattributes.baseurl}")
    private String REST_URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MessageSourceAccessor messageSourceAccessor;

    @Test
    public void createVote() throws Exception {
        VoteTo voteTo = VoteUtil.getTo(getNewVote(restaurant1));
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteTo))
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword())));

        VoteTo created = objectMapper.readValue(action.andReturn().getResponse().getContentAsString(), VoteTo.class);
        int newId = created.getId();
        voteTo.setId(newId);
        assertThat(created).isEqualTo(voteTo);
        assertThat(VoteUtil.getTo(voteRepository.findById(newId).get())).isEqualTo(voteTo);
    }

    @Test
    public void createDuplicateVote() throws Exception {
        voteRepository.save(getNewVote(restaurant1));
        VoteTo voteTo = VoteUtil.getTo(getNewVote(restaurant2));

        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteTo))
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value(messageSourceAccessor.getMessage("exception.vote.duplicateDateTime")));
    }

    @Test
    public void updateVoteAfterDeadline() throws Exception {
        Vote first = voteRepository.save(getNewVote(restaurant1));
        Vote second = getNewVote(restaurant2);
        int id = first.getId();

        AppConfig.DEAD_LINE = LocalTime.now().minusHours(1);

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/votes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(VoteUtil.getTo(second)))
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", Matchers.containsString("You can't change or delete your vote after")));
    }

    @Test
    public void updateVoteBeforeDeadline() throws Exception {
        Vote first = voteRepository.save(getNewVote(restaurant1));
        Vote second = getNewVote(restaurant2);
        int id = first.getId();
        second.setId(id);

        AppConfig.DEAD_LINE = LocalTime.now().plusHours(1);

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/votes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(VoteUtil.getTo(second)))
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword())))
                .andExpect(status().isOk());

        assertThat(voteRepository.findById(id).get()).isEqualTo(second);
    }

    @Test
    public void createUser() throws Exception {
        User user = getNewUser();
        String jsonUser = objectMapper.writeValueAsString(user).replaceFirst("}", ",\"password\": \"" + user.getPassword() + "\"}");
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());

        User created = objectMapper.readValue(action.andReturn().getResponse().getContentAsString(), User.class);
        int newId = created.getId();
        user.setId(newId);
        assertThat(created).isEqualTo(user);
        assertThat(userRepository.findById(newId).get()).isEqualTo(user);
    }

    @Test
    public void getUserVotes() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/account/votes")
                .param("start", date1.toString())
                .param("end", date2.toString())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<VoteTo> votes = objectMapper.readValue(action.andReturn().getResponse().getContentAsString(), new TypeReference<List<VoteTo>>() {
        });
        assertThat(votes).usingRecursiveComparison().isEqualTo(VoteUtil.getListTo(userVotes));
    }

    @Test
    public void getRestaurantsWithMenus() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/restaurants/menus")
                .param("menuDate", date1.toString())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Restaurant> restaurants = objectMapper.readValue(action.andReturn().getResponse().getContentAsString(), new TypeReference<List<Restaurant>>() {
        });
        assertThat(restaurants).usingElementComparatorIgnoringFields("enabled").isEqualTo(getRestaurantsWithMenu());

        List<Menu> menusFromDB = restaurants.stream().map(Restaurant::getMenus).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        assertThat(menusFromDB).usingElementComparatorIgnoringFields("enabled").isEqualTo(menus);

        List<Dish> dishesFromDB = menusFromDB.stream().map(Menu::getDishes).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        assertThat(dishesFromDB).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("menus", "enabled").isEqualTo(dishes);
    }

    @Test
    public void createDuplicateMenu() throws Exception {
        menuRepository.save(getNewMenu());
        MenuTo menuTo = MenuUtil.getTo(getNewMenu());

        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuTo))
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(admin.getEmail(), admin.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value(messageSourceAccessor.getMessage("exception.restaurant.duplicateMenuDateTime")));
    }
}