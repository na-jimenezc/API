package com.api.api.service.serviceInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.api.api.model.Medicamento;

public interface MedicamentoService {
    List<Medicamento> obtenerTodos();
    Medicamento obtenerPorId(Long id);
    Medicamento guardar(Medicamento medicamento);
    void eliminar(Long id);
    int importarDesdeExcel(InputStream in, boolean replace) throws IOException;
    default int importarDesdeExcel(InputStream in) throws IOException {
        return importarDesdeExcel(in, false);
    }
}