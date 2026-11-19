package com.utp.ecotechtrack.security;

import com.utp.ecotechtrack.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Adaptador de seguridad que implementa UserDetails de Spring Security para la entidad Usuario.
 */
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String nombreCompleto;
    private final String email;
    private final boolean activo;
    private final Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails build(Usuario usuario) {
        Collection<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.name()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                Boolean.TRUE.equals(usuario.getActivo()),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
