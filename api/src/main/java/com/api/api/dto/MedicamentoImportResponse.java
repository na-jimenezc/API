package com.api.api.dto;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoImportResponse {
  private int totalFilas;
  private int insertados;
  private int actualizados;
  private List<String> errores = new ArrayList<>();

  public int getTotalFilas() { return totalFilas; }
  public void setTotalFilas(int totalFilas) { this.totalFilas = totalFilas; }

  public int getInsertados() { return insertados; }
  public void setInsertados(int insertados) { this.insertados = insertados; }

  public int getActualizados() { return actualizados; }
  public void setActualizados(int actualizados) { this.actualizados = actualizados; }
  
  public List<String> getErrores() { return errores; }
  public void setErrores(List<String> errores) { this.errores = errores; }
}