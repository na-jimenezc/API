package com.api.api.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.api.model.Administrador;
import com.api.api.model.Cliente;
import com.api.api.model.Rol;
import com.api.api.model.UserEntity;
import com.api.api.model.Veterinario;
import com.api.api.repository.RolRepository;
import com.api.api.repository.UserEntityRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {
    
    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    RolRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*Unico método para traer la informacion de un usuario a traves de su username */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       UserEntity userDB = userRepository.findByUsername(username).orElseThrow(
           () -> new UsernameNotFoundException("User not found")
       );

       UserDetails userDetails = new User(userDB.getUsername(),userDB.getPassword(),mapRolesToAuthorities(userDB.getRoles()));

       return userDetails;
    }


    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Rol> roles) {
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
    }

    //SAVE PARA CADA TIPO DE USUARIO
    public UserEntity ClienteToUser(Cliente cliente){
        UserEntity user = new UserEntity();
        user.setUsername(cliente.getCorreo());
        user.setPassword(passwordEncoder.encode(cliente.getCedula()));

        Rol roles = roleRepository.findByNombre("CLI").get();
        user.setRoles(List.of(roles));
        return user;
    }

    public UserEntity saveCliente(Cliente cliente) {
        UserEntity user = new UserEntity();
        user.setUsername(cliente.getCorreo());
        user.setPassword(passwordEncoder.encode(cliente.getCedula()));
        
        Rol roles = roleRepository.findByNombre("CLI")
            .orElseThrow(() -> new RuntimeException("Rol CLI no encontrado"));
        user.setRoles(List.of(roles));
        
        UserEntity savedUser = userRepository.save(user);
        System.out.println("UserEntity guardado con ID: " + savedUser.getId());
        System.out.println("Username: " + savedUser.getUsername());
        System.out.println("Password cifrado: " + savedUser.getPassword());
        
        return savedUser;
    }

    //Métodos de cada Admin para mapear y guardar
    public UserEntity AdministradorToUser(Administrador administrador){
        UserEntity user = new UserEntity();
        user.setUsername(administrador.getCorreo());
        user.setPassword(passwordEncoder.encode(administrador.getClave()));

        Rol roles = roleRepository.findByNombre("ADM").get();
        user.setRoles(List.of(roles));
        return user;
    }

     public UserEntity saveAdministrador(Administrador administrador) {
        UserEntity user = AdministradorToUser(administrador);
        return userRepository.save(user);
    }

    //Métodos de cada Veterinario para mapear y guardar
    public UserEntity VeterinarioToUser(Veterinario veterinario){
        UserEntity user = new UserEntity();
        user.setUsername(veterinario.getNombreUsuario());
        user.setPassword(passwordEncoder.encode(veterinario.getContrasenia()));

        Rol roles = roleRepository.findByNombre("VET").get();
        user.setRoles(List.of(roles));
        return user;
    }

    public UserEntity saveVeterinario(Veterinario veterinario) {
        UserEntity user = VeterinarioToUser(veterinario);
        return userRepository.save(user);
    }
            
    
}