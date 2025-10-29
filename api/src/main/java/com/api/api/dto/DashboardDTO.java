package com.api.api.dto;

import java.util.List;

public class DashboardDTO {
    private int totalTratamientosUltimoMes;
    private List<TratamientoPorMedicamentoDTO> tratamientosPorMedicamento;
    private int veterinariosActivos;
    private int veterinariosInactivos;
    private int totalMascotas;
    private int mascotasActivas;
    private double ventasTotales;
    private double gananciasTotales;
    private List<TopTratamientoDTO> top3Tratamientos;
    private List<VeterinarioDTO> todosVeterinarios;

    public DashboardDTO() {}

    public DashboardDTO(int totalTratamientosUltimoMes, 
                       List<TratamientoPorMedicamentoDTO> tratamientosPorMedicamento,
                       int veterinariosActivos, int veterinariosInactivos,
                       int totalMascotas, int mascotasActivas,
                       double ventasTotales, double gananciasTotales,
                       List<TopTratamientoDTO> top3Tratamientos,
                       List<VeterinarioDTO> todosVeterinarios) {
        this.totalTratamientosUltimoMes = totalTratamientosUltimoMes;
        this.tratamientosPorMedicamento = tratamientosPorMedicamento;
        this.veterinariosActivos = veterinariosActivos;
        this.veterinariosInactivos = veterinariosInactivos;
        this.totalMascotas = totalMascotas;
        this.mascotasActivas = mascotasActivas;
        this.ventasTotales = ventasTotales;
        this.gananciasTotales = gananciasTotales;
        this.top3Tratamientos = top3Tratamientos;
        this.todosVeterinarios = todosVeterinarios;
    }

    public int getTotalTratamientosUltimoMes() { return totalTratamientosUltimoMes; }
    public void setTotalTratamientosUltimoMes(int totalTratamientosUltimoMes) { this.totalTratamientosUltimoMes = totalTratamientosUltimoMes; }

    public List<TratamientoPorMedicamentoDTO> getTratamientosPorMedicamento() { return tratamientosPorMedicamento; }
    public void setTratamientosPorMedicamento(List<TratamientoPorMedicamentoDTO> tratamientosPorMedicamento) { this.tratamientosPorMedicamento = tratamientosPorMedicamento; }

    public int getVeterinariosActivos() { return veterinariosActivos; }
    public void setVeterinariosActivos(int veterinariosActivos) { this.veterinariosActivos = veterinariosActivos; }

    public int getVeterinariosInactivos() { return veterinariosInactivos; }
    public void setVeterinariosInactivos(int veterinariosInactivos) { this.veterinariosInactivos = veterinariosInactivos; }

    public int getTotalMascotas() { return totalMascotas; }
    public void setTotalMascotas(int totalMascotas) { this.totalMascotas = totalMascotas; }

    public int getMascotasActivas() { return mascotasActivas; }
    public void setMascotasActivas(int mascotasActivas) { this.mascotasActivas = mascotasActivas; }

    public double getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(double ventasTotales) { this.ventasTotales = ventasTotales; }

    public double getGananciasTotales() { return gananciasTotales; }
    public void setGananciasTotales(double gananciasTotales) { this.gananciasTotales = gananciasTotales; }

    public List<TopTratamientoDTO> getTop3Tratamientos() { return top3Tratamientos; }
    public void setTop3Tratamientos(List<TopTratamientoDTO> top3Tratamientos) { this.top3Tratamientos = top3Tratamientos; }

    public List<VeterinarioDTO> getTodosVeterinarios() { return todosVeterinarios; }
    public void setTodosVeterinarios(List<VeterinarioDTO> todosVeterinarios) { this.todosVeterinarios = todosVeterinarios; }
}