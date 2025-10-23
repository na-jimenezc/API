// DashboardServiceImpl.java
package com.api.api.service.serviceImplementation;

import com.api.api.dto.*;
import com.api.api.model.*;
import com.api.api.repository.*;
import com.api.api.service.serviceInterface.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final TratamientoRepository tratamientoRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final MascotaRepository mascotaRepository;

    public DashboardServiceImpl(TratamientoRepository tratamientoRepository,
                               MedicamentoRepository medicamentoRepository,
                               VeterinarioRepository veterinarioRepository,
                               MascotaRepository mascotaRepository) {
        this.tratamientoRepository = tratamientoRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public DashboardDTO getDashboardData() {
        // Obtener todos los datos necesarios
        List<Tratamiento> tratamientos = tratamientoRepository.findAllWithRelations();
        List<Medicamento> medicamentos = medicamentoRepository.findAll();
        List<Veterinario> veterinarios = veterinarioRepository.findAll();
        List<Mascota> mascotas = mascotaRepository.findAll();

        // Procesar los datos para el dashboard
        return procesarDatosDashboard(tratamientos, medicamentos, veterinarios, mascotas);
    }

    private DashboardDTO procesarDatosDashboard(List<Tratamiento> tratamientos, 
                                               List<Medicamento> medicamentos,
                                               List<Veterinario> veterinarios,
                                               List<Mascota> mascotas) {
        // Filtrar tratamientos del último mes
        LocalDate haceUnMes = LocalDate.now().minusMonths(1);
        List<Tratamiento> tratamientosUltimoMes = tratamientos.stream()
                .filter(t -> t.getFecha() != null && !t.getFecha().isBefore(haceUnMes))
                .collect(Collectors.toList());

        // Mapa para acumular por medicamento
        Map<String, TratamientoPorMedicamentoDTO> acumulador = new HashMap<>();
        double ventasTotales = 0;
        double costoTotales = 0;

        for (Tratamiento t : tratamientosUltimoMes) {
            Medicamento medicamento = t.getMedicamento();
            String nombreMedicamento = medicamento != null ? medicamento.getNombre() : "Sin nombre";

            TratamientoPorMedicamentoDTO dto = acumulador.getOrDefault(nombreMedicamento, 
                new TratamientoPorMedicamentoDTO(nombreMedicamento, 0));

            int cantidad = t.getCantidadUsada();
            dto.setCantidad(dto.getCantidad() + cantidad);

            double venta = medicamento != null ? medicamento.getPrecioVenta() * cantidad : 0;
            double costo = medicamento != null ? medicamento.getPrecioCompra() * cantidad : 0;

            ventasTotales += venta;
            costoTotales += costo;

            acumulador.put(nombreMedicamento, dto);
        }

        List<TratamientoPorMedicamentoDTO> tratamientosPorMedicamento = new ArrayList<>(acumulador.values());

        // Top 3 tratamientos
        List<TopTratamientoDTO> top3Tratamientos = acumulador.values().stream()
            .sorted((a, b) -> b.getCantidad() - a.getCantidad())
            .limit(3)
            .map(dto -> new TopTratamientoDTO(dto.getMedicamento(), dto.getCantidad()))
            .collect(Collectors.toList());

        // Veterinarios
        long veterinariosActivos = veterinarios.stream().filter(v -> v.getActivo() == 1).count();
        long veterinariosInactivos = veterinarios.size() - veterinariosActivos;

        // Mascotas
        long totalMascotas = mascotas.size();
        long mascotasActivas = mascotas.stream().filter(m -> m.getActivo() != null && m.getActivo()).count();

        // Convertir lista de veterinarios a DTOs
        List<VeterinarioDTO> todosVeterinarios = veterinarios.stream()
            .map(v -> new VeterinarioDTO(
                v.getId(),
                v.getNombre(),
                v.getEspecialidad(),
                v.getNombreUsuario(),
                v.getImagen(),
                v.getActivo(),
                v.getConsultas()
            ))
            .collect(Collectors.toList());

        return new DashboardDTO(
            tratamientosUltimoMes.size(),
            tratamientosPorMedicamento,
            (int) veterinariosActivos,
            (int) veterinariosInactivos,
            (int) totalMascotas,
            (int) mascotasActivas,
            ventasTotales,
            Math.max(ventasTotales - costoTotales, 0),
            top3Tratamientos,
            todosVeterinarios
        );
    }
}