package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }

    private void loadSecurityData() {
        // beer auths
        Authority createBeerAuthority = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority updateBeerAuthority = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority readBeerAuthority = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority deleteBeerAuthority = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        // customer auths
        Authority createCustomerAuthority = authorityRepository.save(Authority.builder().permission("customer.create").build());
        Authority updateCustomerAuthority = authorityRepository.save(Authority.builder().permission("customer.update").build());
        Authority readCustomerAuthority = authorityRepository.save(Authority.builder().permission("customer.read").build());
        Authority deleteCustomerAuthority = authorityRepository.save(Authority.builder().permission("customer.delete").build());

        // brewery auths
        Authority createBreweryAuthority = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        Authority updateBreweryAuthority = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        Authority readBreweryAuthority = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        Authority deleteBreweryAuthority = authorityRepository.save(Authority.builder().permission("brewery.delete").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(createBeerAuthority, updateBeerAuthority, readBeerAuthority, deleteBeerAuthority,
                                        createCustomerAuthority, updateCustomerAuthority, readCustomerAuthority, deleteCustomerAuthority,
                                        createBreweryAuthority, updateBreweryAuthority, readBreweryAuthority, deleteBreweryAuthority)));

        customerRole.setAuthorities(new HashSet<>(Set.of(readBeerAuthority, readCustomerAuthority, readBreweryAuthority)));

        userRole.setAuthorities(new HashSet<>(Set.of(readBeerAuthority)));

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        userRepository.save(User.builder()
                        .username("spring")
                        .password(passwordEncoder.encode("calebe"))
                        .role(adminRole)
                        .build());

        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(customerRole)
                .build());

        log.debug("Users loaded: {}", userRepository.count());
    }
}
