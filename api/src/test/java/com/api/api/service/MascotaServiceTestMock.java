package com.api.api.service;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.api.api.model.Mascota;
import com.api.api.repository.MascotaRepository;
import com.api.api.service.serviceImplementation.MascotaServiceImpl;
import com.api.api.service.serviceInterface.MascotaService;
import org.assertj.core.api.Assertions;

//@SpringBootTest

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MascotaServiceTestMock {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaServiceImpl mascotaService;

    @BeforeEach
    public void init(){

    }

    //when().thenReturn() "Cuando algo ocurra se retorna algo"

    //Prueba para obtener las mascotas por el servicio
    @Test
    public void MascotaService_obtenerMascotas_Mascotas() {

        Mascota mascota1 = new Mascota("Firulais", "Criollo", 3, "Perro", "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);      
        Mascota mascota2 = new Mascota("Roberto", "Persa", 2, "Gato", "Ninguna", 4.8, "/assets/images/roberto.jpeg", "Sano", true);
        Mascota mascota3 = new Mascota("Rocky", "Criollo", 5, "Perro", "Patita torcida", 30.2, "/assets/images/rocky.jpeg", "Enfermo", false);
        Mascota mascota4 = new Mascota("Luna", "Siamés", 4, "Gato", "Problemas digestivos", 3.5, "/assets/images/luna.jpeg", "Enfermo", true);
        Mascota mascota5 = new Mascota("Max", "Labrador", 2, "Perro", "Alergias", 28.0, "/assets/images/max.jpeg", "Sano", false);

        List<Mascota> mascotas = List.of(mascota1, mascota2, mascota3, mascota4, mascota5);

        //Se retornan las mascotas quemadas 
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        List<Mascota> resultado = mascotaService.obtenerTodasMascotas();

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado).hasSize(5);
        Assertions.assertThat(resultado.get(0).getNombre()).isEqualTo("Firulais");
    }

    //Prueba para tener la mascota por id
    @Test
    public void MascotaService_obtenerMascotaPorId_Mascota() {
        Mascota mascota = new Mascota("Firulais", "Criollo", 3, "Perro", "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);

        when(mascotaRepository.findById(1L)).thenReturn(java.util.Optional.of(mascota));

        Mascota resultado = mascotaService.obtenerMascotaPorId(1L);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getNombre()).isEqualTo("Firulais");
    }

    //Prueba para guardar una nueva mascota
    @Test
    public void MascotaService_guardarMascota_Mascota() {
        Mascota mascota = new Mascota("Luna", "Siamés", 4, "Gato", "Digestión", 3.5,"/assets/images/luna.jpeg", "Sano", true);

        when(mascotaRepository.save(mascota)).thenReturn(mascota);

        Mascota resultado = mascotaService.guardarMascota(mascota);

        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getNombre()).isEqualTo("Luna");
    }

    //Prueba para poner a una mascota inactiva
    @Test
    public void MascotaService_eliminarMascota_ActivoFalso() {
        Mascota mascota = new Mascota("Rocky", "Criollo", 5, "Perro", "Patita torcida", 30.2,"/assets/images/rocky.jpeg", "Enfermo", true);

        when(mascotaRepository.findById(1L)).thenReturn(java.util.Optional.of(mascota));
        when(mascotaRepository.save(mascota)).thenReturn(mascota);

        mascotaService.eliminarMascota(1L);

        Assertions.assertThat(mascota.getActivo()).isFalse();
    }

    //Prueba para actualizar una mascota
    @Test
    public void MascotaService_actualizarMascota_ActualizaCampos() {

        Mascota mascotaExistente = new Mascota("Firulais","Criollo",3,"Perro", "Ninguna",25.4,"/assets/images/firulais.jpg","Sano",true             );

        Long idMascota = 1L;

        when(mascotaRepository.findById(idMascota)).thenReturn(java.util.Optional.of(mascotaExistente));

        when(mascotaRepository.save(mascotaExistente)).thenReturn(mascotaExistente);

        //Cambio los campos
        Mascota mascotaActualizada = mascotaService.actualizarMascota(
            idMascota,"Maximus","Gato","Persa","Alergia", "/assets/images/maximus.jpg", false                   
        );

        //Los reviso
        Assertions.assertThat(mascotaActualizada).isNotNull();
        Assertions.assertThat(mascotaActualizada.getNombre()).isEqualTo("Maximus");
        Assertions.assertThat(mascotaActualizada.getTipo()).isEqualTo("Gato");
        Assertions.assertThat(mascotaActualizada.getRaza()).isEqualTo("Persa");
        Assertions.assertThat(mascotaActualizada.getEnfermedad()).isEqualTo("Alergia");
        Assertions.assertThat(mascotaActualizada.getFotoURL()).isEqualTo("/assets/images/maximus.jpg");
        Assertions.assertThat(mascotaActualizada.getActivo()).isFalse();
    }

    //Prueba para actualizar una mascota
    @Test
    public void MascotaService_registrarMascota_MascotaActiva() {
        Mascota mascota = new Mascota("Nina", "Beagle", 1, "Perro", "Ninguna", 10.0,"/assets/images/nina.jpeg", "Sano", false);

        when(mascotaRepository.save(mascota)).thenReturn(mascota);

        Mascota resultado = mascotaService.registrarMascota(mascota);
        Assertions.assertThat(resultado.getActivo()).isTrue();
    }

    //Prueba para ELIMINAR totalmente una mascota (esto no está activo dentro de la lógica de negocio pero al igual
    //lo voy a dejar)
    @Test
    public void MascotaService_eliminarMascotaHard_EliminarMascota() {
        mascotaService.eliminarMascotaHard(5L);
        verify(mascotaRepository).deleteById(5L);
    }
    
}
