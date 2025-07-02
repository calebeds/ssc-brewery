package guru.sfg.brewery.config;

import guru.sfg.brewery.security.CalebePasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return CalebePasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ((HttpSecurity)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // do not use in production
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                            .mvcMatchers( "/brewery/breweries").hasAnyRole("ADMIN", "CUSTOMER")
                            .mvcMatchers( HttpMethod.GET, "/brewery/api/v1/breweries/**").hasAnyRole("ADMIN", "CUSTOMER")
                            .mvcMatchers("/beers/find", "/beers/{beerId}").hasAnyRole("ADMIN", "CUSTOMER", "USER");
                })
                .authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic()
                .and().csrf().disable();

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }



//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                .password("{bcrypt}$2a$10$a5q6.c17.EezLTkodgS8r.QCXfan0IbMRIHo4S25mGG.kChym1oSe")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}e56a7b0c671510f8ee8d304c6baea23b98696a9690dc7082e54add1159cdd0545738a82afb39c329")
//                .roles("USER")
//                .and()
//                .withUser("scott")
//                .password("{bcrypt15}$2a$15$b4GARq.G5Uqp5Lp/6vh9QenEj804uyaTT50J78o8SggA/GuJH6bNW")
//                .roles("CUSTOMER")
//                .roles("USER");

//    }

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("calebe")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
