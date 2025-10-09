package com.api.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api.api.dto.MedicamentoDTO;
import com.api.api.dto.TratamientoDTO;
import com.api.api.dto.VeterinarioDTO;
import com.api.api.model.Tratamiento;
import com.api.api.service.serviceInterface.MascotaService;
import com.api.api.service.serviceInterface.MedicamentoService;
import com.api.api.service.serviceInterface.TratamientoService;
import com.api.api.service.serviceInterface.VeterinarioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoService tratamientoService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping("/{mascotaId}")
    public List<TratamientoDTO> obtenerTratamientos(@PathVariable Long mascotaId) {
        List<Tratamiento> tratamientos = tratamientoService.obtenerTratamientosPorMascota(mascotaId);
        
        return tratamientos.stream()
                .map(this::convertirATratamientoDTO)
                .collect(Collectors.toList());
    }

    private TratamientoDTO convertirATratamientoDTO(Tratamiento tratamiento) {
        TratamientoDTO dto = new TratamientoDTO();
        dto.setId(tratamiento.getId());
        
        if (tratamiento.getFecha() != null) {
            dto.setFecha(tratamiento.getFecha().toString()); 
        }
        
        dto.setCantidadUsada(tratamiento.getCantidadUsada());
        
        if (tratamiento.getMedicamento() != null) {
            MedicamentoDTO medicamentoDTO = new MedicamentoDTO(
                tratamiento.getMedicamento().getId(),
                tratamiento.getMedicamento().getNombre(),
                tratamiento.getMedicamento().getPrecioCompra(),
                tratamiento.getMedicamento().getPrecioVenta(),
                tratamiento.getMedicamento().getUnidadesDisponibles(),
                tratamiento.getMedicamento().getUnidadesVendidas()
            );
            dto.setMedicamento(medicamentoDTO);
        }
        
        if (tratamiento.getVeterinario() != null) {
            VeterinarioDTO veterinarioDTO = new VeterinarioDTO(
                tratamiento.getVeterinario().getId(),
                tratamiento.getVeterinario().getNombre(),
                tratamiento.getVeterinario().getEspecialidad(),
                tratamiento.getVeterinario().getNombreUsuario(),
                tratamiento.getVeterinario().getImagen(),
                tratamiento.getVeterinario().getActivo(),
                tratamiento.getVeterinario().getConsultas()
            );
            dto.setVeterinario(veterinarioDTO);
        }
        
        return dto;
    }

    @PostMapping("/administrar")
    public Tratamiento administrarMedicamento(@RequestParam Long idMascota,
                                              @RequestParam Long idMedicamento,
                                              @RequestParam Long idVeterinario,
                                              @RequestParam int cantidadUsada) {
        return tratamientoService.administrarMedicamento(idMascota, idMedicamento, idVeterinario, cantidadUsada);
    }
}