package com.api.api.repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;

@DataJpaTest
@RunWith(SpringRunner.class)
public class VeterinarioRepositoryTest {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    private Veterinario vet1, vet2;
    private Cliente cliente1;
    private Mascota mascota1, mascota2;
    private Medicamento med1, med2;
    private Tratamiento tratamiento1, tratamiento2;

    @BeforeEach
    public void setUp() {

        
        tratamientoRepository.deleteAll();
        mascotaRepository.deleteAll();
        clienteRepository.deleteAll();
        veterinarioRepository.deleteAll();
        medicamentoRepository.deleteAll();

        //Veterinarios
        Veterinario vet1 = Veterinario.builder()
        .nombre("Dr. Juan Pérez")
        .especialidad("Cirugía")
        .nombreUsuario("juanPerez")
        .contrasenia("123")
        .imagen("/assets/images/drJuan.jpeg")
        .activo(1)
        .consultas(100)
        .build();

    Veterinario vet2 = Veterinario.builder()
        .nombre("Dra. Laura Gómez")
        .especialidad("Dermatología")
        .nombreUsuario("lauraGomez")
        .contrasenia("123")
        .imagen("/assets/images/draLaura.jpeg")
        .activo(1)
        .consultas(85)
        .build();
        veterinarioRepository.saveAll(Arrays.asList(vet1, vet2));

        //Clientes
        cliente1 = new Cliente("123", "Carlos Ruiz", "carlos@gmail.com", "3214567890");
        clienteRepository.save(cliente1);

        //Mascotas asociadas
        mascota1 = new Mascota("Firulais", "Criollo", 3, "Perro", "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        mascota1.setCliente(cliente1);

        mascota2 = new Mascota("Luna", "Siamés", 4, "Gato", "Problemas digestivos", 3.5, "/assets/images/luna.jpeg", "Enfermo", true);
        mascota2.setCliente(cliente1);

        mascotaRepository.saveAll(Arrays.asList(mascota1, mascota2));

        //Medicamentos
        med1 = new Medicamento("Amoxicilina", 2.0f, 4.5f, 100, 10);
        med2 = new Medicamento("Ivermectina", 1.5f, 3.0f, 80, 5);
        medicamentoRepository.saveAll(Arrays.asList(med1, med2));

        //Tratamientos
        tratamiento1 = new Tratamiento(LocalDate.now(), 2);
        tratamiento1.setMascota(mascota1);
        tratamiento1.setMedicamento(med1);
        tratamiento1.setVeterinario(vet1);

        tratamiento2 = new Tratamiento(LocalDate.now().minusDays(1), 1);
        tratamiento2.setMascota(mascota2);
        tratamiento2.setMedicamento(med2);
        tratamiento2.setVeterinario(vet2);

        tratamientoRepository.saveAll(Arrays.asList(tratamiento1, tratamiento2));
    }

    //PRUEBA # 20 - PRUEBA #3 DE LAS QUERIES PERSONALIZADAS
    //Probar búsqueda de veterinario por nombreUsuario y contrasenia
    /*@Query("""
            SELECT v FROM Veterinario v
            WHERE
                (:cedula IS NOT NULL AND CAST(v.id AS string) LIKE CONCAT('%', :cedula, '%'))
            OR (:nombre IS NOT NULL AND LOWER(v.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
            OR (:especialidad IS NOT NULL AND LOWER(v.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')))
        """)
        List<Veterinario> buscar(
            @Param("cedula") String cedula,
            @Param("nombre") String nombre,
            @Param("especialidad") String especialidad
        );
    } */
    @Test
    public void VeterinarioRepository_buscar_FiltraPorNombreOEspecialidad() {
        //Se bucan por nombre
        List<Veterinario> porNombre = veterinarioRepository.buscar(null, "Juan", null);
        Assertions.assertThat(porNombre).isNotEmpty();
        Assertions.assertThat(porNombre.get(0).getNombre()).contains("Juan");

        //Se buscan por especialidad
        List<Veterinario> porEspecialidad = veterinarioRepository.buscar(null, null, "Dermatología");
        Assertions.assertThat(porEspecialidad).isNotEmpty();
        Assertions.assertThat(porEspecialidad.get(0).getEspecialidad()).isEqualTo("Dermatología");
    }
}