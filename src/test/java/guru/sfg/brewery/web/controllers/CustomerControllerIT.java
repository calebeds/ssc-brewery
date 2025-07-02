package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIT extends BaseIT {

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.CustomerControllerIT#getStreamAdminCustomer")
    void testListCustomersAUTH(final String user, final String pwd) throws Exception {
        mockMvc.perform(get("/customers")
                .with(httpBasic(user, pwd)))
                .andExpect(status().isOk());
    }

    @Test
    void testListCustomersUserForbidden() throws Exception {
        mockMvc.perform(get("/customers")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListCustomersNoAuth() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Add Customers")
    @Nested
    class AddCustomers {

        @Rollback
        @Test
        void processCreationForm() throws Exception {
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Foo Customer")
                    .with(httpBasic("spring", "calebe")))
                    .andExpect(status().is3xxRedirection());
        }

        @Rollback
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.CustomerControllerIT#getStreamNotAdmin")
        void processCreationFormForbidden(final String user, final String pwd) throws Exception {
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Foo Customer2")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void processCreationFormNoAuth() throws Exception {
            mockMvc.perform(post("/customers/new")
                            .param("customerName", "Foo Customer"))
                    .andExpect(status().isUnauthorized());
        }
    }

}
