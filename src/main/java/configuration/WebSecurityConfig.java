package configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                    .loginProcessingUrl("/login")
                    .loginPage("/") // login page is root context
                    .defaultSuccessUrl("/chat") // redirect to '/chat' if login success
                    .and()
                .logout()
                    .logoutSuccessUrl("/") // redirect back to / if logout successful
                    .and()
                .authorizeRequests()
                    .antMatchers("/login", "/new-account", "/").permitAll() // allowed for everybody
                    .antMatchers(HttpMethod.POST, "/chatroom").hasRole("ADMIN") // allowed for ROLE_ADMIN
                    .anyRequest().authenticated() // allowed for logged-in users
    }
}