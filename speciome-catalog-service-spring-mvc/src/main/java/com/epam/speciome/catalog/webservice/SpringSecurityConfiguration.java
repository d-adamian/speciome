package com.epam.speciome.catalog.webservice;

import com.epam.speciome.catalog.UseCaseFactory;
import com.epam.speciome.catalog.webservice.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UseCaseFactory useCaseFactory;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsServiceImpl(useCaseFactory));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // TODO: why we are disabling CSRF here?
                .authorizeRequests()
                .antMatchers("/static/**").permitAll() // Allow access to static pages from 'speciome-reactjs'
                .antMatchers("/", "index.html", "asset-manifest.json", "favicon.ico",
                        "asset-manifest.json", "logo192.png", "logo512.png", "robots.txt").permitAll() // Allow access
                                                                             // to static pages from 'speciome-reactjs'
                .mvcMatchers("/new-user").permitAll() // Allow registering new users
                .mvcMatchers("/swagger-ui.html").permitAll() // Allow access to Swagger UI
                .mvcMatchers("/swagger-ui/**").permitAll()
                .mvcMatchers("/v3/api-docs/**").permitAll() // Allow access to OpenAPI JSON endpoint
                .mvcMatchers("/h2-ui/**").permitAll() // Allow access to H2 console. TODO: remove in prod!!!
                .mvcMatchers("/login").permitAll() // Allow access to login endpoint
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()));

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
