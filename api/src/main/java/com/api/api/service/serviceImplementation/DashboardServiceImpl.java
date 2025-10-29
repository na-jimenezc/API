package com.api.api.service.serviceImplementation;

import com.api.api.dto.*;
import com.api.api.model.Veterinario;
import com.api.api.repository.DashboardRepository;
import com.api.api.repository.VeterinarioRepository;
import com.api.api.service.serviceInterface.DashboardService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final VeterinarioRepository veterinarioRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository,
                                VeterinarioRepository veterinarioRepository) {
        this.dashboardRepository = dashboardRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    private LocalDate startDefault() { return LocalDate.now().minusDays(30); }
    private LocalDate endDefault()   { return LocalDate.now().plusDays(1); }

    @Override
    public DashboardDTO getDashboardData() {
        LocalDate start = startDefault();
        LocalDate end   = endDefault();

        long totalTratamientos = dashboardRepository.countTratamientosBetween(start, end);

        var porMedRaw = dashboardRepository.rawTratamientosPorMedicamentoBetween(start, end);
        List<TratamientoPorMedicamentoDTO> porMedicamento = porMedRaw.stream()
            .map(r -> new TratamientoPorMedicamentoDTO((String) r[0], ((Number) r[1]).longValue()))
            .collect(Collectors.toList());

        var topRaw = dashboardRepository.rawTopTratamientosBetween(start, end);
        List<TopTratamientoDTO> top3 = topRaw.stream()
            .map(r -> new TopTratamientoDTO((String) r[0], ((Number) r[1]).longValue()))
            .limit(3)
            .collect(Collectors.toList());

        BigDecimal ventasDb = dashboardRepository.ventasTotalesBetween(start, end);
        BigDecimal costosDb = dashboardRepository.costosTotalesBetween(start, end);

        double ventas    = ventasDb  != null ? ventasDb.doubleValue()  : 0.0;
        double costos    = costosDb  != null ? costosDb.doubleValue()  : 0.0;
        double ganancias = Math.max(ventas - costos, 0.0);

        int vetsAct = (int) dashboardRepository.countVeterinariosActivos();
        int vetsInac= (int) dashboardRepository.countVeterinariosInactivos();
        int mascTot = (int) dashboardRepository.countMascotasTotales();
        int mascAct = (int) dashboardRepository.countMascotasActivas();

        List<VeterinarioDTO> todosVet = veterinarioRepository.findAll().stream()
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
            (int) totalTratamientos,
            porMedicamento,
            vetsAct,
            vetsInac,
            mascTot,
            mascAct,
            ventas,
            ganancias,
            top3,
            todosVet
        );
    }
}