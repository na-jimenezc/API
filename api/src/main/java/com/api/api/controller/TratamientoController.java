package com.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    //Función Obtener todos los tratamientos de una mascota
    @GetMapping("/mascota/{idMascota}")
    public List<Tratamiento> obtenerTratamientosPorMascota(@PathVariable Long idMascota) {
        return tratamientoService.obtenerTratamientosPorMascota(idMascota);
    }

    //Función para administrar un medicamento a una mascota
    @PostMapping("/administrar")
    public Tratamiento administrarMedicamento(@RequestParam Long idMascota,
                                              @RequestParam Long idMedicamento,
                                              @RequestParam Long idVeterinario,
                                              @RequestParam int cantidadUsada) {
        return tratamientoService.administrarMedicamento(idMascota, idMedicamento, idVeterinario, cantidadUsada);
    }
}