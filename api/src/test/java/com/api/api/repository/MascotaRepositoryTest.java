package com.api.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;


/*@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    @Query("SELECT DISTINCT m FROM Mascota m " +
       "JOIN m.tratamientos t " +
       "JOIN t.veterinario v " +
       "WHERE v.id = :veterinarioId")
       List<Mascota> findByVeterinarioIdIncluyendoTratamientos(@Param("veterinarioId") Long veterinarioId);

    @Query("SELECT m FROM Mascota m WHERE m.cliente.id = :clienteId")
       List<Mascota> findByClienteId(@Param("clienteId") Long clienteId);
       @Modifying
       @Transactional
       @Query("DELETE FROM Mascota m WHERE m.cliente.id = :clienteId")
       void deleteByClienteId(@Param("clienteId") Long clienteId);

   @EntityGraph(attributePaths = {"cliente"})
   @Override
   Page<Mascota> findAll(Pageable pageable);
}*/

@DataJpaTest
@RunWith(SpringRunner.class)
public class MascotaRepositoryTest {
    
    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private MedicamentoRepository medicamentosRepository;

    @BeforeEach
    void init(){
        Mascota mascota1 = new Mascota("Firulais", "Criollo", 3, "Perro", "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);      
        Mascota mascota2 = new Mascota("Roberto", "Persa", 2, "Gato", "Ninguna", 4.8, "/assets/images/roberto.jpeg", "Sano", true);
        Mascota mascota3 = new Mascota("Rocky", "Criollo", 5, "Perro", "Patita torcida", 30.2, "/assets/images/rocky.jpeg", "Enfermo", false);
        Mascota mascota4 = new Mascota("Luna", "Siamés", 4, "Gato", "Problemas digestivos", 3.5, "/assets/images/luna.jpeg", "Enfermo", true);
        Mascota mascota5 = new Mascota("Max", "Labrador", 2, "Perro", "Alergias", 28.0, "/assets/images/max.jpeg", "Sano", false);

        mascotaRepository.save(mascota1);     
         mascotaRepository.save(mascota2);
          mascotaRepository.save(mascota3);
           mascotaRepository.save(mascota4);
            mascotaRepository.save(mascota5);


        Veterinario vet1 = Veterinario.builder()
        .nombre("Dr. Juan Pérez")
        .especialidad("Cirugía")
        .nombreUsuario("juanPerez")
        .contrasenia("123")
        .imagen("/assets/images/drJuan.jpeg")
        .activo(1)
        .consultas(100)
        .build();
        veterinarioRepository.save(vet1);

        Cliente cliente = new Cliente("123", "Carlos Ruiz", "carlos@gmail.com", "3214567890");
        clienteRepository.save(cliente);
        mascota1.setCliente(cliente);
        mascota2.setCliente(cliente);
        mascota3.setCliente(cliente);
        mascotaRepository.save(mascota1);
        mascotaRepository.save(mascota2);
        mascotaRepository.save(mascota3);

        Medicamento medicamento = new Medicamento("Amoxicilina", 2.0f, 4.5f, 100, 10);
        medicamentosRepository.save(medicamento);

        Tratamiento tratamiento = new Tratamiento(LocalDate.now(), 2);
        tratamiento.setMascota(mascota1);
        tratamiento.setMedicamento(medicamento);
        tratamiento.setVeterinario(vet1);
        tratamientoRepository.save(tratamiento);

    }

    //PRUEBA #1
    //Probar ingresar una mascota
    @Test
    // [Lo que se prueba]_[la función]_[lo que devuelve]
    public void MascotaRepository_save_test_Mascota(){
            //Arrange: preparar lo de la prueba
            //ACT : probar
            // Assert: revisar confirmación

        Mascota mascota = new Mascota("Pipo", "Criollo", 4, "Perro", "Pulgoso", 4.6, "/assets/images/firulais.jpg", "Enfermo", true );

        Mascota mascotaGuardada = mascotaRepository.save(mascota);

        Assertions.assertThat(mascotaGuardada).isNotNull();
    }

    //PRUEBA #2
    //Probar que se estén encontrando todas las mascotas
    @Test
    public void MascotaRepository_FindAll_NotEmptyList(){

        List<Mascota> mascotas = mascotaRepository.findAll();
        
        Assertions.assertThat(mascotas).isNotNull();
        Assertions.assertThat(mascotas.size()).isEqualTo(5);
        Assertions.assertThat(mascotas.size()).isGreaterThan(0);

    }

    //PRUEBA #3
    //Probar fuera de índices
    @Test 
    public void MascotaRepository_findById_FindWrongIndex(){
        Long index = -1l;

        Optional<Mascota> mascota = mascotaRepository.findById(index);

        Assertions.assertThat(mascota).isEmpty();
    }

    //PRUEBA #4
    //Probar delete
    @Test 
    public void MascotaRepository_deleteById_EmptyMascota(){
        Long index = 2L;

        mascotaRepository.deleteById(index);

        Assertions.assertThat(mascotaRepository.findById(index)).isEmpty();

    }

    //PRUEBA #5 - PRUEBA #1 DE LAS QUERIES PERSONALIZADAS
    //Probar queries personalizadas

    /*@Query("SELECT DISTINCT m FROM Mascota m " +
       "JOIN m.tratamientos t " +
       "JOIN t.veterinario v " +
       "WHERE v.id = :veterinarioId")
    List<Mascota> findByVeterinarioIdIncluyendoTratamientos(@Param("veterinarioId") Long veterinarioId);
    */
    @Test
    public void MascotaRepository_findByVeterinarioIdIncluyendoTratamientos_Mascotas() {

        //Se pone el veterinario y se buscan sus tratamientos por el perrito
        Long idVet = veterinarioRepository.findAll().get(0).getId();

        List<Mascota> resultado = mascotaRepository.findByVeterinarioIdIncluyendoTratamientos(idVet);
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.size()).isEqualTo(1);
        Assertions.assertThat(resultado.get(0).getNombre()).isEqualTo("Firulais");
    }

    //PRUEBA #6 - PRUEBA #2 DE LAS QUERIES PERSONALIZADAS
    //Probar que se encuentren las mascotas del cliente
    /*@Query("SELECT m FROM Mascota m WHERE m.cliente.id = :clienteId")
    List<Mascota> findByClienteId(@Param("clienteId") Long clienteId); */
    @Test
    public void MascotaRepository_findByClienteId_MascotasDelCliente() {

        //Se obtiene el cliente y se revisa que tenga los 3 animalitos asociados por el setUp
        Long idCliente = clienteRepository.findAll().get(0).getId();

        List<Mascota> resultado = mascotaRepository.findByClienteId(idCliente);
        
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.size()).isEqualTo(3);
        Assertions.assertThat(resultado.get(0).getCliente().getNombre()).isEqualTo("Carlos Ruiz");
    }

    //PRUEBA #7
    //Probar quitar mascotas por id del cliente
    @Test
    @Transactional
    public void MascotaRepository_deleteByClienteId_RemovesMascotasDelCliente() {
        Long idCliente = clienteRepository.findAll().get(0).getId();

        //Se borran los tratamientos asociados para que no llore por la relación entre tratamiento y mascota
        List<Mascota> mascotasCliente = mascotaRepository.findByClienteId(idCliente);
        for (Mascota m : mascotasCliente) {
            tratamientoRepository.deleteAll(tratamientoRepository.findByMascotaId(m.getId()));
        }
        mascotaRepository.deleteByClienteId(idCliente);

        List<Mascota> resultado = mascotaRepository.findByClienteId(idCliente);
        Assertions.assertThat(resultado).isEmpty();
    }

    //PRUEBA 8
    //Probar actualización 
    @Test
    @Order(5)
    public void MascotaRepository_updateById_UpdateMascota() {
        //Se crea la mascota para actualizar y se guarda
        Mascota mascota = new Mascota("Rocky", "Labrador", 3, "Perro", 
        "Ninguna", 6.5, null, "Sano", true);
        Mascota mascotaGuardada = mascotaRepository.save(mascota);

        //Se cambian los datos
        mascotaGuardada.setEdad(4);
        mascotaGuardada.setEstado("Enfermo");
        mascotaGuardada.setPeso(7.0);
        mascotaGuardada.setEnfermedad("COVID");
        mascotaGuardada.setFotoURL("/assets/images/rockyEditado.jpeg");
        mascotaGuardada.setActivo(false);

        //Se guarda la actualización
        Mascota mascotaActualizada = mascotaRepository.save(mascotaGuardada);

        //Se hace el assert
        Assertions.assertThat(mascotaActualizada.getEdad()).isEqualTo(4);
        Assertions.assertThat(mascotaActualizada.getEstado()).isEqualTo("Enfermo");
        Assertions.assertThat(mascotaActualizada.getPeso()).isEqualTo(7.0);
        Assertions.assertThat(mascotaActualizada.getEnfermedad()).isEqualTo("COVID");
        Assertions.assertThat(mascotaActualizada.getFotoURL()).isEqualTo("/assets/images/rockyEditado.jpeg");
        Assertions.assertThat(mascotaActualizada.getActivo()).isFalse();
    }
}