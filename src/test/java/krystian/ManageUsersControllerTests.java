package krystian;


import com.fasterxml.jackson.databind.ObjectMapper;
import krystian.security.user.User;
import krystian.security.user.UserRepository;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static krystian.IotServerTests.test;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 2/11/2017 8:24 PM
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(test)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManageUsersControllerTests {

    private static long createdUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void listContainsDefaultUser() throws Exception {
        this.mockMvc.perform(get("/manageUsers")).andExpect(status().isOk())
                .andExpect(content().string(containsString("<a href=\"#\" class=\"sidebar-item\" data-dev-id=\"1\">Admin</a>")));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void getUserContainsEditForm() throws Exception {
        this.mockMvc.perform(get("/manageUsers/get/1")).andExpect(status().isOk())
                .andExpect(content().string(containsString("<form id=\"updateUser\">")))
                .andExpect(content().string(containsString("<input class=\"form-control\" type=\"text\" value=\"Admin\" id=\"login\" name=\"login\" />")));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void getUserContainsRemoveForm() throws Exception {
        this.mockMvc.perform(get("/manageUsers/get/1")).andExpect(status().isOk())
                .andExpect(content().string(containsString("<button class=\"btn btn-primary\" id=\"remove\">")));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void newUserPageContainsCreateForm() throws Exception {
        this.mockMvc.perform(get("/manageUsers/get/new")).andExpect(status().isOk())
                .andExpect(content().string(containsString(" id=\"newUser\">")))
                .andExpect(content().string(containsString("<input class=\"form-control\" type=\"text\" value=\"\" id=\"login\" name=\"login\" />")))
                .andExpect(content().string(containsString("<input class=\"form-control\" type=\"text\" value=\"\" id=\"pass\" name=\"pass\" />")))
                .andExpect(content().string(containsString("<select class=\"form-control\" id=\"role\" name=\"role\">")))
                .andExpect(content().string(containsString("<button type=\"submit\" class=\"btn btn-primary\">")));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t1createUserInvalidData() throws Exception {
        mockMvc.perform(post("/manageUsers/create")
                .param("login", "test")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("You need to complete all fields.")))
                .andExpect(jsonPath("$.createdId", is(-1)))
                .andExpect(jsonPath("$", hasKey("message")));
        Assert.assertEquals(1, userRepository.count());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t2createUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/manageUsers/create")
                .param("login", "test").param("pass", "testpass").param("role", "ADMIN")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$", hasKey("message")))
                .andExpect(jsonPath("$", hasKey("createdId")))
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        createdUserId = objectMapper.readTree(result.getResponse().getContentAsString()).path("createdId").asLong();
        User u = userRepository.findOne(createdUserId);
        Assert.assertNotNull(u);
        Assert.assertEquals("test", u.getLogin());
        Assert.assertEquals("testpass", u.getPassword());
        Assert.assertEquals(2, userRepository.count());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t3createDuplicateUser() throws Exception {
        mockMvc.perform(post("/manageUsers/create")
                .param("login", "test").param("pass", "testpass").param("role", "ADMIN")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.createdId", is(-1)))
                .andExpect(jsonPath("$", hasKey("message")));

        Assert.assertEquals(2, userRepository.count());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t4AlterUserLogin() throws Exception {
        mockMvc.perform(post("/manageUsers/" + createdUserId + "/update")
                .param("login", "testAltered")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ok")));
        User u = userRepository.findOne(createdUserId);
        Assert.assertNotNull(u);
        Assert.assertEquals("testAltered", u.getLogin());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t5AlterUserPassword() throws Exception {
        mockMvc.perform(post("/manageUsers/" + createdUserId + "/update")
                .param("pass", "testAltered")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ok")));
        User u = userRepository.findOne(createdUserId);
        Assert.assertNotNull(u);
        Assert.assertEquals("testAltered", u.getPassword());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t6RemoveUser() throws Exception {
        mockMvc.perform(delete("/manageUsers/" + createdUserId + "/remove")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ok")));
        Assert.assertEquals(1, userRepository.count());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t7RemoveNonexistentUser() throws Exception {
        mockMvc.perform(delete("/manageUsers/" + createdUserId + "/remove")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("error")));
        Assert.assertEquals(1, userRepository.count());
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void t8alterNonexistentUserPassword() throws Exception {
        mockMvc.perform(post("/manageUsers/" + createdUserId + "/update")
                .param("pass", "testAltered")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("error")));
    }
}
