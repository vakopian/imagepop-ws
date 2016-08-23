package com.imagepop;


import com.imagepop.domain.User;
import com.imagepop.domain.UserService;
import com.imagepop.fileupload.Image;
import com.imagepop.fileupload.ImageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService, ImageRepository imageRepo) {
        return (args) -> {
            User user = new User("Tommy");
            user.setLastName("Trojan");
            user.setEmail("ttrojan@usc.edu");
            user.setPassword("password");
            userService.registerNewUser(user);

            Image image = new Image(user, "Chichen Itza", 2290650);
            image.setStatus(Image.Status.POPPED);
            imageRepo.save(image);

            image = new Image(user, "Doheny Library", 1251015);
            image.setStatus(Image.Status.POPPED);
            imageRepo.save(image);
        };
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.setAllowedHeaders(Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept", StatelessAuthenticationFilter.AUTH_HEADER_NAME));
        config.setAllowedMethods(Arrays.asList("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        // return new CorsFilter(source);
        final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
