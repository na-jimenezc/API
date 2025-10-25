package com.api.api.repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;


@DataJpaTest
public class TratamientoRepositoryTest {

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

     @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

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
        vet1 = new Veterinario("Dr. Juan Pérez", "Cirugía", "juanPerez", "123", "/assets/images/drJuan.jpeg", 1, 100);
        vet2 = new Veterinario("Dra. Laura Gómez", "Dermatología", "lauraGomez", "123", "/assets/images/draLaura.jpeg", 1, 85);
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


    //PRUEBA #21 - PRUEBA #4 DE LAS QUERIES PERSONALIZADAS
    //Probar queries personalizadas
    /*@Modifying
        @Transactional
        @Query("DELETE FROM Tratamiento t WHERE t.mascota.cliente.id = :clienteId")
        void deleteByClienteId(@Param("clienteId") Long clienteId);*/

    @Test
    @Transactional
    public void TratamientoRepository_deleteByClienteId_EliminaTratamientosDeCliente() {
        Long idCliente = clienteRepository.findAll().get(0).getId();

        //Verificar tratamientos antes
        List<Mascota> mascotas = mascotaRepository.findByClienteId(idCliente);
        List<Tratamiento> antes = mascotas.stream()
                .flatMap(m -> tratamientoRepository.findByMascotaId(m.getId()).stream())
                .toList();
        Assertions.assertThat(antes).isNotEmpty();

        //Ejecutar la eliminación
        tratamientoRepository.deleteByClienteId(idCliente);

        //Verificar después
        List<Tratamiento> despues = mascotas.stream()
                .flatMap(m -> tratamientoRepository.findByMascotaId(m.getId()).stream())
                .toList();
        Assertions.assertThat(despues).isEmpty();
    }



    //PRUEBA #22 - PRUEBA #4 DE LAS QUERIES PERSONALIZADAS
    //Probar queries personalizadas
    /*@Query("SELECT t FROM Tratamiento t " +
            "LEFT JOIN FETCH t.medicamento " +
            "LEFT JOIN FETCH t.veterinario " +
            "WHERE t.mascota.id = :mascotaId " +
            "ORDER BY t.fecha DESC")
        List<Tratamiento> findByMascotaIdWithRelations(@Param("mascotaId") Long mascotaId); */
    //Probat que estén todas las relaciones por tratamiento
    @Test
    public void TratamientoRepository_findAllWithRelations_DevuelveTodosConRelaciones() {
        List<Tratamiento> tratamientos = tratamientoRepository.findAllWithRelations();

        Assertions.assertThat(tratamientos).isNotNull();
        Assertions.assertThat(tratamientos.size()).isGreaterThanOrEqualTo(2);
        Assertions.assertThat(tratamientos.get(0).getMedicamento()).isNotNull();
        Assertions.assertThat(tratamientos.get(0).getMascota()).isNotNull();
        Assertions.assertThat(tratamientos.get(0).getVeterinario()).isNotNull();
    }

    //PRUEBA #23 - PRUEBA #4 DE LAS QUERIES PERSONALIZADAS
    //Probar queries personalizadas
    /*  @Query("SELECT t FROM Tratamiento t " +
        "LEFT JOIN FETCH t.medicamento " +
        "LEFT JOIN FETCH t.veterinario " +
        "LEFT JOIN FETCH t.mascota")
        List<Tratamiento> findAllWithRelations(); */
    @Test
    //Probar que estén todas las relaciones por tratamiento de una mascota específica
    public void TratamientoRepository_findByMascotaIdWithRelations_DevuelveTratamientosOrdenados() {
        Long idMascota = mascotaRepository.findAll().get(0).getId();

        List<Tratamiento> tratamientos = tratamientoRepository.findByMascotaIdWithRelations(idMascota);

        Assertions.assertThat(tratamientos).isNotNull();
        Assertions.assertThat(tratamientos.size()).isGreaterThan(0);
        Assertions.assertThat(tratamientos.get(0).getVeterinario()).isNotNull();
        Assertions.assertThat(tratamientos.get(0).getMedicamento()).isNotNull();
    }

}
