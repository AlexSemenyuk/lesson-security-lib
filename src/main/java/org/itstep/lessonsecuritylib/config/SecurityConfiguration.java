package org.itstep.lessonsecuritylib.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        UserDetails admin = User.builder()
                .username("admin")
                .password("$2a$10$3uUBBq3ikiB5lXsNbrudeue.NwNkmFcd59t5qeDCuiFp70Af.8H9W") // соответствует строке "admin"
                .roles("ADMIN", "EDITOR", "USER")
                .build();
        UserDetails editor = User.builder()
                .username("editor")
                .roles("EDITOR", "USER")
                .password("$2a$10$PP8nOsFMpJnkyXyn8arJLu3Wq68.i6FDb2KiQalSTiiDZSuMy/iNq") // соответствует строке "editor"
                .build();
        UserDetails user = User.builder()
                .username("user")
                .roles("USER")
                .password("$2a$10$Djl8U2HDSuCmbCSm1Xxb8eJSbqczMOoLSl0MxWkr.p8kchB75hOXu") // соответствует строке "user"
                .build();
        JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager(dataSource);
        detailsManager.createUser(admin);
        detailsManager.createUser(editor);
        detailsManager.createUser(user);
        return detailsManager; //new InMemoryUserDetailsManager(admin, user);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/h2-console").authenticated()
                                .requestMatchers(HttpMethod.GET, "/books").hasAnyRole("USER", "EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/books").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/books/edit/**").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/books/edit/**").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/books/delete/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/authors").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/authors").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/authors/edit/**").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/authors/edit/**").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/authors/delete/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/publishers").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/publishers").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/publishers/edit/**").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/publishers/edit/**").hasAnyRole("EDITOR", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/publishers/delete/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .permitAll())
//                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .permitAll())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
