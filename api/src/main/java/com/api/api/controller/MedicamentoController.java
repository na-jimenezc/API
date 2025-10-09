package com.api.api.controller;

import java.util.List;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<Medicamento> listarMedicamentos() {
        return medicamentoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Medicamento obtenerMedicamento(@PathVariable Long id) {
        Medicamento medicamento = medicamentoService.obtenerPorId(id);
        if (medicamento == null) {
            return null;
        }
        return medicamento;
    }

    @PostMapping
    public Medicamento crearMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.guardar(medicamento);
    }

    @PutMapping("/{id}")
    public void actualizarMedicamento(@PathVariable Long id,@RequestBody Medicamento medicamento) {
        Medicamento existente = medicamentoService.obtenerPorId(id);
        if (existente == null) {
            return;
        }
        medicamento.setId(id);
        medicamentoService.guardar(existente);
    }

    @DeleteMapping("/{id}")
    public void eliminarMedicamento(@PathVariable Long id) {
        Medicamento existente = medicamentoService.obtenerPorId(id);
        if (existente == null) {
            return;
        }
        medicamentoService.eliminar(id);
    }

    @PostMapping("/importar-excel")
    public ResponseEntity<?> importarExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "replace", defaultValue = "false") boolean replace
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Archivo vacío"));
        }
        try {
            int filas = medicamentoService.importarDesdeExcel(file.getInputStream(), replace);
            return ResponseEntity.ok(Map.of("importados", filas, "replace", replace));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("error", "No se pudo procesar el Excel", "detalle", e.getMessage()));
        }
    }
}