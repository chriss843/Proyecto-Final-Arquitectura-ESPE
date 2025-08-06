package com.academic.publications.auth.config;

import com.academic.publications.auth.security.FiltroAutenticacionJwt;
import com.academic.publications.auth.security.FiltroAutorizacionJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final UserDetailsService servicioDetallesUsuario;
    private final BCryptPasswordEncoder codificadorPassword;
    private final FiltroAutorizacionJwt filtroAutorizacionJwt;
    private final FiltroAutenticacionJwt filtroAutenticacionJwt;

    public ConfiguracionSeguridad(UserDetailsService servicioDetallesUsuario,
                                  BCryptPasswordEncoder codificadorPassword,
                                  FiltroAutorizacionJwt filtroAutorizacionJwt,
                                  FiltroAutenticacionJwt filtroAutenticacionJwt) {
        this.servicioDetallesUsuario = servicioDetallesUsuario;
        this.codificadorPassword = codificadorPassword;
        this.filtroAutorizacionJwt = filtroAutorizacionJwt;
        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(servicioDetallesUsuario)
                .passwordEncoder(codificadorPassword);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/usuarios/registro").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(filtroAutenticacionJwt)
                .addFilterBefore(filtroAutorizacionJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}