package com.api.api.service;

import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.MedicamentoRepository;
import com.api.api.repository.TratamientoRepository;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceImplementation.TratamientoServiceImpl;
import com.api.api.service.serviceInterface.TratamientoService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TratamientoServiceTestMock {

    @Mock private TratamientoRepository tratamientoRepository;
    @Mock private MascotaRepository mascotaRepository;
    @Mock private MedicamentoRepository medicamentoRepository;
    @Mock private VeterinarioRepository veterinarioRepository;

    @InjectMocks private TratamientoServiceImpl tratamientoServiceImpl;

    private TratamientoService service() { return tratamientoServiceImpl; }

    private Mascota nuevaMascota(Long id) {
        Mascota m = new Mascota();
        m.setId(id);
        m.setNombre("Firulais");
        m.setRaza("Mestizo");
        m.setEdad(3);
        m.setTipo("Perro");
        m.setEnfermedad("Ninguna");
        m.setPeso(12.5);
        m.setFotoURL("foto://x");
        m.setEstado("SANO");
        m.setActivo(Boolean.TRUE);
        return m;
    }

    private Medicamento nuevoMed(Long id, int stock) {
        Medicamento med = new Medicamento();
        med.setId(id);
        med.setNombre("Amoxicilina");
        med.setUnidadesDisponibles(stock);
        med.setPrecioCompra(5.0f);
        med.setPrecioVenta(8.0f);
        return med;
    }

    private Veterinario nuevoVet(Long id) {
        Veterinario v = new Veterinario();
        v.setId(id);
        v.setNombre("Dra. López");
        v.setEspecialidad("General");
        v.setNombreUsuario("dralopez");
        v.setContrasenia("secret");
        v.setImagen("avatar://1");
        v.setActivo(1);
        v.setConsultas(0);
        return v;
    }

    @Test
    @DisplayName("crearTratamiento: persiste con relaciones y datos correctos")
    void crearTratamiento_ok() {
        Long idMasc = 10L, idMed = 20L, idVet = 30L;
        Mascota mascota = nuevaMascota(idMasc);
        Medicamento med = nuevoMed(idMed, 10);
        Veterinario vet = nuevoVet(idVet);

        when(mascotaRepository.findById(idMasc)).thenReturn(Optional.of(mascota));
        when(medicamentoRepository.findById(idMed)).thenReturn(Optional.of(med));
        when(veterinarioRepository.findById(idVet)).thenReturn(Optional.of(vet));

        ArgumentCaptor<Tratamiento> cap = ArgumentCaptor.forClass(Tratamiento.class);
        when(tratamientoRepository.save(any(Tratamiento.class))).thenAnswer(inv -> {
            Tratamiento t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        Tratamiento creado = service().crearTratamiento(LocalDate.now(), 2, idMasc, idMed, idVet);

        assertNotNull(creado.getId());
        verify(tratamientoRepository).save(cap.capture());
        Tratamiento enviado = cap.getValue();
        assertEquals(2, enviado.getCantidadUsada());
        assertEquals(mascota, enviado.getMascota());
        assertEquals(med, enviado.getMedicamento());
        assertEquals(vet, enviado.getVeterinario());
    }

    @Test
    @DisplayName("administrarMedicamento: descuenta stock y guarda tratamiento")
    void administrarMedicamento_descuentaStock() {
        Long idMasc = 1L, idMed = 2L, idVet = 3L;
        Mascota mascota = nuevaMascota(idMasc);
        Medicamento med = nuevoMed(idMed, 10);
        Veterinario vet = nuevoVet(idVet);

        when(mascotaRepository.findById(idMasc)).thenReturn(Optional.of(mascota));
        when(medicamentoRepository.findById(idMed)).thenReturn(Optional.of(med));
        when(veterinarioRepository.findById(idVet)).thenReturn(Optional.of(vet));
        when(tratamientoRepository.save(any(Tratamiento.class))).thenAnswer(inv -> {
            Tratamiento t = inv.getArgument(0);
            t.setId(77L);
            return t;
        });

        Tratamiento t = service().administrarMedicamento(idMasc, idMed, idVet, 3);

        assertNotNull(t.getId());
        assertEquals(7, med.getUnidadesDisponibles(), "Debe descontar 3 unidades");
        verify(medicamentoRepository).save(med);
        verify(tratamientoRepository).save(any(Tratamiento.class));
    }

    @Test
    @DisplayName("administrarMedicamento: lanza excepción si no hay stock suficiente")
    void administrarMedicamento_sinStock() {
        Long idMasc = 1L, idMed = 2L, idVet = 3L;
        Mascota mascota = nuevaMascota(idMasc);
        Medicamento med = nuevoMed(idMed, 2);
        Veterinario vet = nuevoVet(idVet);

        when(mascotaRepository.findById(idMasc)).thenReturn(Optional.of(mascota));
        when(medicamentoRepository.findById(idMed)).thenReturn(Optional.of(med));
        when(veterinarioRepository.findById(idVet)).thenReturn(Optional.of(vet));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service().administrarMedicamento(idMasc, idMed, idVet, 5)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("suficiente stock"));
        verify(tratamientoRepository, never()).save(any());
        verify(medicamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerTratamientosPorMascota: delega en el repositorio")
    void obtenerTratamientosPorMascota_ok() {
        Long idMasc = 10L;
        Tratamiento a = new Tratamiento();
        a.setId(1L);
        a.setFecha(LocalDate.now());
        Tratamiento b = new Tratamiento();
        b.setId(2L);
        b.setFecha(LocalDate.now().minusDays(1));

        when(tratamientoRepository.findByMascotaIdWithRelations(idMasc))
                .thenReturn(List.of(a, b));

        List<Tratamiento> result = service().obtenerTratamientosPorMascota(idMasc);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(tratamientoRepository).findByMascotaIdWithRelations(idMasc);
    }
}