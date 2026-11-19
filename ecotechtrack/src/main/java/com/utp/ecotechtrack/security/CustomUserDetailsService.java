package com.utp.ecotechtrack.security;

import com.utp.ecotechtrack.model.Usuario;
import com.utp.ecotechtrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio personalizado de UserDetailsService requerido por Spring Security.
 * Recupera los usuarios y sus credenciales/roles directamente desde la base de datos relacional.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(usernameOrEmail)
                .or(() -> usuarioRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con credencial: " + usernameOrEmail
                ));

        return CustomUserDetails.build(usuario);
    }
}
