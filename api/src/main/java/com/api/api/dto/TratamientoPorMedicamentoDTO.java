package com.api.api.dto;

public class TratamientoPorMedicamentoDTO {
  private String medicamento;
  private int cantidad;

  public TratamientoPorMedicamentoDTO() {}

  public TratamientoPorMedicamentoDTO(String medicamento, int cantidad) {
    this.medicamento = medicamento;
    this.cantidad = cantidad;
  }

  public TratamientoPorMedicamentoDTO(String medicamento, long cantidad) {
    this.medicamento = medicamento;
    this.cantidad = (int) cantidad;
}

  public String getMedicamento() { return medicamento; }
  public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
  public int getCantidad() { return cantidad; }
  public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}