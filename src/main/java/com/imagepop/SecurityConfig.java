package com.imagepop;

import org.jose4j.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by nyanaga on 3/29/16.
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenHandler tokenHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        StatelessAuthenticationFilter statelessAuthenticationFilter = statelessAuthenticationFilter();

        http
                .authorizeRequests()

                    // Allow anonymous resource requests
                    .antMatchers("/").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    .antMatchers("**/*.html").permitAll()
                    .antMatchers("**/*.css").permitAll()
                    .antMatchers("**/*.js").permitAll()

                    // Allow anonymous registrations
                    .antMatchers("/api/users/register").permitAll()

                // All other request need to be authenticated
                .anyRequest().authenticated().and()
                .formLogin()
                    .loginPage("/api/users/login")
                    .usernameParameter("email")
                    .permitAll()
                    .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            clearAuthenticationAttributes(request);
                            statelessAuthenticationFilter.addAuthenticationInfoToResponse(response, (UserDetails) authentication.getPrincipal());
                        }
                    })
                    .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .and()
                // Custom Token based authentication based on the header previously given to the client
                .addFilterBefore(statelessAuthenticationFilter, LogoutFilter.class)
                .logout()
                    .logoutSuccessHandler((request, response, authentication) -> {})
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll()
                    .and()
                .csrf().disable();

    }

    public StatelessAuthenticationFilter statelessAuthenticationFilter() throws Exception {
        StatelessAuthenticationFilter statelessAuthenticationFilter = new StatelessAuthenticationFilter();
        statelessAuthenticationFilter.setAuthenticationManager(authenticationManager());
        statelessAuthenticationFilter.setTokenHandler(tokenHandler);
        return statelessAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        PreAuthenticatedAuthenticationProvider preauthAuthProvider = new PreAuthenticatedAuthenticationProvider();
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper = token -> (UserDetails) token.getPrincipal();
        preauthAuthProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper);
        auth.authenticationProvider(preauthAuthProvider);
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, failed) -> {
            response.setHeader("location", "/api/users/login");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> res = new HashMap<>();
            res.put("status", "error");
            res.put("statusText", failed.getLocalizedMessage());
            try (PrintWriter writer = response.getWriter()) {
                JsonUtil.writeJson(res, writer);
            }
        };
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
