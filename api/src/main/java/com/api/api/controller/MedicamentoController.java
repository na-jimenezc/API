package com.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.api.model.Medicamento;
import com.api.api.service.serviceInterface.MedicamentoService;


@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;


    //Función para listar todos los medicamentos
    @GetMapping
    public List<Medicamento> listarMedicamentos() {
        return medicamentoService.obtenerTodos();
    }

    //Función para obtener medicamento por id
    @GetMapping("/{id}")
    public Medicamento obtenerMedicamento(@PathVariable Long id) {
        Medicamento medicamento = medicamentoService.obtenerPorId(id);
        if (medicamento == null) {
            return null;
        }
        return medicamento;
    }

    //Función para crear un nuevo medicamento (solo admin)
    @PostMapping
    public Medicamento crearMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.guardar(medicamento);
    }

    //Función para actualiizar un medicamento (solo admin)
    @PutMapping("/{id}")
    public void actualizarMedicamento(@PathVariable Long id,@RequestBody Medicamento medicamento) {
        Medicamento existente = medicamentoService.obtenerPorId(id);
        if (existente == null) {
            return;
        }
        medicamento.setId(id);
        medicamentoService.guardar(existente);
    }

    //Función para eliminar un medicamento (solo admin)
    @DeleteMapping("/{id}")
    public void eliminarMedicamento(@PathVariable Long id) {
        Medicamento existente = medicamentoService.obtenerPorId(id);
        if (existente == null) {
            return;
        }
        medicamentoService.eliminar(id);
    }
}