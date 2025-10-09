package com.api.api.service.serviceInterface;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.api.api.model.Mascota;

public interface MascotaService {
    List<Mascota> obtenerTodasMascotas();
    Mascota obtenerMascotaPorId(Long id);
    Mascota guardarMascota(Mascota mascota);
    void eliminarMascota(Long id);
    void eliminarMascotaHard(Long id);
    Mascota actualizarMascota(Long id,
                          String nombre,
                          String tipo,
                          String raza,
                          String enfermedad,
                          String fotoURL,
                          Boolean activo);
    List<Mascota> obtenerMascotasPorClienteId(Long clienteId);
    void desactivarMascota(Long id);

    public Mascota registrarMascota(Mascota mascota);
    public Page<Mascota> obtenerMascotasPaginadas(Pageable pageable);
}