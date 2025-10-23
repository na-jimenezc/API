
package com.api.api.dto;

public class TopTratamientoDTO {
    private String medicamento;
    private int unidadesVendidas;

    public TopTratamientoDTO() {}

    public TopTratamientoDTO(String medicamento, int unidadesVendidas) {
        this.medicamento = medicamento;
        this.unidadesVendidas = unidadesVendidas;
    }

    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }

    public int getUnidadesVendidas() { return unidadesVendidas; }
    public void setUnidadesVendidas(int unidadesVendidas) { this.unidadesVendidas = unidadesVendidas; }
}