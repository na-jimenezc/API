package com.api.api.service.serviceImplementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.api.model.Mascota;
import com.api.api.model.Medicamento;
import com.api.api.model.Tratamiento;
import com.api.api.model.Veterinario;
import com.api.api.repository.MascotaRepository;
import com.api.api.repository.MedicamentoRepository;
import com.api.api.repository.TratamientoRepository;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceInterface.TratamientoService;

@Service
public class TratamientoServiceImpl implements TratamientoService {

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Override
    public Tratamiento crearTratamiento(LocalDate fecha, int cantidad, Long idMascota, Long idMedicamento, Long idVeterinario) {
        Mascota mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + idMascota));

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id: " + idMedicamento));

        Veterinario veterinario = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + idVeterinario));

        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setFecha(fecha);
        tratamiento.setCantidadUsada(cantidad);
        tratamiento.setMascota(mascota);
        tratamiento.setMedicamento(medicamento);
        tratamiento.setVeterinario(veterinario);

        return tratamientoRepository.save(tratamiento);
    }


        @Override
        public List<Tratamiento> obtenerTratamientosPorMascota(Long mascotaId) {
        return tratamientoRepository.findByMascotaIdWithRelations(mascotaId);
 }

    @Override
    public Tratamiento administrarMedicamento(Long idMascota, Long idMedicamento, Long idVeterinario, int cantidadUsada) {
        Mascota mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + idMascota));

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id: " + idMedicamento));

        Veterinario veterinario = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + idVeterinario));

        if (medicamento.getUnidadesDisponibles() < cantidadUsada) {
            throw new RuntimeException("No hay suficiente stock de " + medicamento.getNombre());
        }
        //Actualizar stock
        medicamento.disminuirStock(cantidadUsada);
        medicamentoRepository.save(medicamento);

        //Guardar el tratamiento
        return crearTratamiento(LocalDate.now(), cantidadUsada, idMascota, idMedicamento, idVeterinario);
    }
}