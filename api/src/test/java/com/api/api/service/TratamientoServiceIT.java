package com.api.api.service;

import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.MedicamentoRepository;
import com.api.api.repository.TratamientoRepository;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceInterface.TratamientoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class TratamientoServiceIT {

    @Autowired private TratamientoService tratamientoService;
    @Autowired private TratamientoRepository tratamientoRepository;
    @Autowired private MascotaRepository mascotaRepository;
    @Autowired private MedicamentoRepository medicamentoRepository;
    @Autowired private VeterinarioRepository veterinarioRepository;

    private Mascota mascota;
    private Medicamento medicamento;
    private Veterinario veterinario;

    @BeforeEach
    void setUp() {
        Mascota m = new Mascota();
        m.setNombre("Firulais");
        m.setRaza("Mestizo");
        m.setEdad(3);
        m.setTipo("Perro");
        m.setEnfermedad("Ninguna");
        m.setPeso(12.5);
        m.setFotoURL("foto://x");
        m.setEstado("SANO");
        m.setActivo(true);
        mascota = mascotaRepository.save(m);

        Medicamento med = new Medicamento();
        med.setNombre("Amoxicilina");
        med.setUnidadesDisponibles(10);
        med.setPrecioCompra(5.0f);
        med.setPrecioVenta(8.0f);
        medicamento = medicamentoRepository.save(med);

        Veterinario v = new Veterinario();
        v.setNombre("Dra. López");
        v.setEspecialidad("General");
        v.setNombreUsuario("dralopez");
        v.setContrasenia("secret");
        v.setImagen("avatar://1");
        v.setActivo(1);
        v.setConsultas(0);
        veterinario = veterinarioRepository.save(v);
    }

    @Test
    @DisplayName("crearTratamiento: crea y persiste con relaciones")
    void crearTratamiento_creaYPersisteConRelaciones() {
        Tratamiento t = tratamientoService.crearTratamiento(
                LocalDate.now(), 2, mascota.getId(), medicamento.getId(), veterinario.getId());

        assertNotNull(t.getId());
        assertEquals(2, t.getCantidadUsada());
        assertEquals(mascota.getId(), t.getMascota().getId());
        assertEquals(medicamento.getId(), t.getMedicamento().getId());
        assertEquals(veterinario.getId(), t.getVeterinario().getId());

        Tratamiento enBD = tratamientoRepository.findById(t.getId()).orElseThrow();
        assertEquals(t.getFecha(), enBD.getFecha());
    }

    @Test
    @DisplayName("administrarMedicamento: descuenta stock y registra tratamiento")
    void administrarMedicamento_descuentaStockYCreaTratamiento() {
        int stockInicial = medicamento.getUnidadesDisponibles();

        Tratamiento t = tratamientoService.administrarMedicamento(
                mascota.getId(), medicamento.getId(), veterinario.getId(), 3);

        Medicamento actualizado = medicamentoRepository.findById(medicamento.getId()).orElseThrow();
        assertEquals(stockInicial - 3, actualizado.getUnidadesDisponibles());
        assertNotNull(t.getId());
        assertEquals(3, t.getCantidadUsada());

        List<Tratamiento> lista = tratamientoRepository.findByMascotaIdWithRelations(mascota.getId());
        assertFalse(lista.isEmpty());
    }

    @Test
    @DisplayName("obtenerTratamientosPorMascota: devuelve ordenado (fecha desc)")
    void obtenerTratamientosPorMascota_ordenado() {
        tratamientoService.crearTratamiento(
                LocalDate.now().minusDays(1), 1, mascota.getId(), medicamento.getId(), veterinario.getId());
        tratamientoService.crearTratamiento(
                LocalDate.now(), 1, mascota.getId(), medicamento.getId(), veterinario.getId());

        List<Tratamiento> result = tratamientoService.obtenerTratamientosPorMascota(mascota.getId());

        assertEquals(2, result.size());
        assertTrue(
            result.get(0).getFecha().isAfter(result.get(1).getFecha())
            || result.get(0).getFecha().isEqual(result.get(1).getFecha())
        );
    }

    @Test
    @DisplayName("administrarMedicamento: lanza excepción si no hay stock")
    void administrarMedicamento_sinStock_lanzaExcepcion() {
        medicamento.setUnidadesDisponibles(2);
        medicamentoRepository.save(medicamento);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> tratamientoService.administrarMedicamento(
                        mascota.getId(), medicamento.getId(), veterinario.getId(), 5));

        assertTrue(ex.getMessage().contains("No hay suficiente stock"));
    }
}