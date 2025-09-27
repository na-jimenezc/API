package com.api.api.dto;

public class MedicamentoDTO {
    private Long id;
    private String nombre;
    private float precioCompra;
    private float precioVenta;
    private int unidadesDisponibles;
    private int unidadesVendidas;

    public MedicamentoDTO() {}

    public MedicamentoDTO(Long id, String nombre, float precioCompra, float precioVenta, 
                         int unidadesDisponibles, int unidadesVendidas) {
        this.id = id;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.unidadesDisponibles = unidadesDisponibles;
        this.unidadesVendidas = unidadesVendidas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public float getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(float precioCompra) { this.precioCompra = precioCompra; }

    public float getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(float precioVenta) { this.precioVenta = precioVenta; }

    public int getUnidadesDisponibles() { return unidadesDisponibles; }
    public void setUnidadesDisponibles(int unidadesDisponibles) { this.unidadesDisponibles = unidadesDisponibles; }

    public int getUnidadesVendidas() { return unidadesVendidas; }
    public void setUnidadesVendidas(int unidadesVendidas) { this.unidadesVendidas = unidadesVendidas; }
}