package krystian;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static krystian.IotServerTests.test;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 2/11/2017 8:24 PM
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(test)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DevicesControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void listContainsDefaultDevice() throws Exception {
        this.mockMvc.perform(get("/devices")).andExpect(status().isOk())
                .andExpect(content().string(containsString("<a href=\"#\" class=\"sidebar-item\" data-dev-id=\"1\">Example device</a>")));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void getDeviceContainsValidPage() throws Exception {
        this.mockMvc.perform(get("/devices/1")).andExpect(status().isOk())
                .andExpect(content().string(containsString("On</button>")))
                .andExpect(content().string(containsString("Auto</button>")))
                .andExpect(content().string(containsString("Off</button>")));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ADMIN"})
    public void getNonexistentReturnsTest() throws Exception {
        this.mockMvc.perform(get("/devices/5667567")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Confused by thoughts, the master is." +
                        "Rumor de bassus zeta, anhelare pulchritudine!" +
                        "Germanus fermium diligenter attrahendams exsul est.Eheu, orexis!Nunquam ")));
    }

}
