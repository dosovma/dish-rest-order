package ru.dosov.restvoting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.web.AuthUser;

import java.util.Optional;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final UserRepository userRepository;
    private final AppConfig appConfig;

    @Autowired
    public WebSecurityConfig(UserRepository userRepository, AppConfig appConfig) {
        this.userRepository = userRepository;
        this.appConfig = appConfig;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Optional<User> optionalUser = userRepository.getByEmail(email);
            return new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(appConfig.getBaseurl() + "/account").anonymous()
                .antMatchers(appConfig.getBaseurl() + "/account/{\\d+}").hasRole(Role.USER.name())
                .antMatchers(appConfig.getBaseurl() + "/account/{\\d+}/votes").hasRole(Role.USER.name())
                .antMatchers(appConfig.getBaseurl() + "/account/{\\d+}/votes/{\\d+}").hasRole(Role.USER.name())
                .antMatchers(appConfig.getBaseurl() + "/restaurants").hasRole(Role.USER.name())
                .antMatchers(appConfig.getBaseurl() + "/restaurants/{\\d+}").hasRole(Role.USER.name())
                .antMatchers(appConfig.getBaseurl() + "/votes/**").hasRole(Role.USER.name())
                .antMatchers(appConfig.getBaseurl() + "/**").hasRole(Role.ADMIN.name())
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}