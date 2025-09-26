package com.api.api.service.serviceImplementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.api.model.Medicamento;
import com.api.api.repository.MedicamentoRepository;
import com.api.api.service.serviceInterface.MedicamentoService;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    @Override
    public Medicamento obtenerPorId(Long id) {
        return medicamentoRepository.findById(id).orElse(null);
    }

    @Override
    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public void eliminar(Long id) {
        medicamentoRepository.deleteById(id);
    }
}