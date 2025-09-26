package com.api.api.service.serviceInterface;

import java.time.LocalDate;
import java.util.List;

import com.api.api.model.Tratamiento;


public interface TratamientoService {
    Tratamiento crearTratamiento(LocalDate fecha, int cantidad, Long idMascota, Long idMedicamento, Long idVeterinario);
    List<Tratamiento> obtenerTratamientosPorMascota(Long idMascota);
    Tratamiento administrarMedicamento(Long idMascota, Long idMedicamento, Long idVeterinario, int cantidadUsada);
}
