package ru.dosov.restvoting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.to.VoteTo;
import ru.dosov.restvoting.util.VoteUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    }

    @Test
    public void updateVoteBeforeDeadline() throws Exception {

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

    }

    @Test
    public void getRestaurantsWithMenus() throws Exception {

    }

    @Test
    public void createDuplicateMenu() throws Exception {

    }
}