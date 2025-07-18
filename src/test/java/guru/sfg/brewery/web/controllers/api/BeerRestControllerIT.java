package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("List Beers")
    @Nested
    class ListBeers {
        @Test
        void findBeersNoAuth() throws Exception {
            mockMvc.perform(get("/api/v1/beer"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamAllUsers")
        void findBeersAuth(final String user, final String pwd) throws Exception {
            mockMvc.perform(get("/api/v1/beer")
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void findByBeerIdNoAuth() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamAllUsers")
        void findByBeerIdAuth(final String user, final String pwd) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerByUpcNoAuth() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beerUpc/" + beer.getUpc()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamAllUsers")
        void findBeerByUpcAuth(final String user, final String pwd) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beerUpc/" + beer.getUpc())
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerFormADMIN() throws Exception {
            mockMvc.perform(get("/beers").param("beerName", "")
                            .with(httpBasic("spring", "calebe")))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {
        public Beer beerToDelete() {
            Random random = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                            .beerName("Delete Me Beer")
                            .beerStyle(BeerStyleEnum.IPA)
                            .minOnHand(12)
                            .quantityToBrew(200)
                            .upc(String.valueOf(random.nextInt(999999)))
                            .build());
        }

        @Test
        void deleteBeerHttpBasicAdminRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("spring", "calebe")))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamNotAdmin")
        void deleteBeerHttpBasicNotAuth(String user, String pwd) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }

    }

    @DisplayName("List Breweries")
    @Nested
    class ListBreweries {
        @Test
        void listBreweriesCUSTOMER() throws Exception {
            mockMvc.perform(get("/brewery/breweries")
                            .with(httpBasic("scott", "tiger")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void listBreweriesADMIN() throws Exception {
            mockMvc.perform(get("/brewery/breweries")
                            .with(httpBasic("spring", "calebe")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void listBreweriesNOAUTH() throws Exception {
            mockMvc.perform(get("/brewery/breweries"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Get Breweries")
    @Nested
    class GetBreweries {
        @Test
        void getBreweriesJsonCUSTOMER() throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("scott", "tiger")))
                    .andExpect(status().is2xxSuccessful());
        }


        @Test
        void getBreweriesJsonADMIN() throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("spring", "calebe")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void getBreweriesJsonUSER() throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }


        @Test
        void getBreweriesJsonNOAUTH() throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries"))
                    .andExpect(status().isUnauthorized());
        }
    }
}