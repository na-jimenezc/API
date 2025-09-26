package com.api.api.service.serviceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.api.model.Mascota;
import com.api.api.repository.MascotaRepository;
import com.api.api.service.serviceInterface.MascotaService;

@Service
public class MascotaServiceImpl implements MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public List<Mascota> obtenerTodasMascotas() {
        return mascotaRepository.findAll();
    }

    @Override
    public Mascota obtenerMascotaPorId(Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    @Override
    public Mascota guardarMascota(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    @Override
    public void eliminarMascota(Long id) {
        mascotaRepository.findById(id).ifPresent(mascota -> {
            mascota.setActivo(false); 
            mascotaRepository.save(mascota);
        });
    }

    @Override
    @Transactional
    public void eliminarMascotaHard(Long id) {
        mascotaRepository.deleteById(id); 
    }

    @Override
    public Mascota actualizarMascota(Long id,
                                     String nombre,
                                     String tipo,
                                     String raza,
                                     String enfermedad,
                                     String fotoURL,
                                     Boolean activo) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (nombre != null && !nombre.isBlank()) mascota.setNombre(nombre);
        if (tipo != null && !tipo.isBlank()) mascota.setTipo(tipo);
        if (raza != null && !raza.isBlank()) mascota.setRaza(raza);
        if (enfermedad != null && !enfermedad.isBlank()) mascota.setEnfermedad(enfermedad);
        if (fotoURL != null && !fotoURL.isBlank()) mascota.setFotoURL(fotoURL);
        if (activo != null) mascota.setActivo(activo);

        return mascotaRepository.save(mascota);
    }

    @Override
    public List<Mascota> obtenerMascotasPorClienteId(Long clienteId) {
        return mascotaRepository.findByClienteId(clienteId);
    }

    @Override
    public void desactivarMascota(Long id) {
        mascotaRepository.findById(id).ifPresent(mascota -> {
            mascota.setActivo(false);
            mascotaRepository.save(mascota);
        });
    }

    @Override
    public Mascota registrarMascota(Mascota mascota) {
        mascota.setActivo(true); 
        return mascotaRepository.save(mascota);
    }

    @Override
    public Page<Mascota> obtenerMascotasPaginadas(Pageable pageable) {
        return mascotaRepository.findAll(pageable);
    }
}