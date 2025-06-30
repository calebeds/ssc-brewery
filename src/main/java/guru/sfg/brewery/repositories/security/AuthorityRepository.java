package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}
