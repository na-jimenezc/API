package com.api.api.dto;

public class TratamientoDTO {
    private Long id;
    private String fecha;
    private int cantidadUsada;
    private MedicamentoDTO medicamento;
    private VeterinarioDTO veterinario;

    public TratamientoDTO() {
    }

    public TratamientoDTO(int cantidadUsada, String fecha, Long id, MedicamentoDTO medicamento, VeterinarioDTO veterinario) {
        this.cantidadUsada = cantidadUsada;
        this.fecha = fecha;
        this.id = id;
        this.medicamento = medicamento;
        this.veterinario = veterinario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(int cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public MedicamentoDTO getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(MedicamentoDTO medicamento) {
        this.medicamento = medicamento;
    }

    public VeterinarioDTO getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(VeterinarioDTO veterinario) {
        this.veterinario = veterinario;
    }
}