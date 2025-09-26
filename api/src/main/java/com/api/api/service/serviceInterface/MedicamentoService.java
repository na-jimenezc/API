package com.api.api.service.serviceInterface;

import java.util.List;

import com.api.api.model.Medicamento;

public interface MedicamentoService {
    List<Medicamento> obtenerTodos();
    Medicamento obtenerPorId(Long id);
    Medicamento guardar(Medicamento medicamento);
    void eliminar(Long id);
}