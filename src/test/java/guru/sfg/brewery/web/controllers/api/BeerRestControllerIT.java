package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

    @Test
    void findByBeerId() throws Exception {
        mockMvc.perform(get("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/061245489648945"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82").header("Api-Key", "spring").header("Api-Secret", "calebe"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82")
                .with(httpBasic("spring", "calebe")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82")
                .header("Api-Key", "spring").header("Api-Secret", "calebeXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerUrlParams() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82")
                        .param("apiKey", "spring").param("apiSecret", "calebe"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerUrlParamsBadCreds() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/f3116f77-db69-44ad-af1b-9556bb18be82")
                        .param("apiKey", "spring").param("apiSecret", "calebeXXX"))
                .andExpect(status().isUnauthorized());
    }
}