package com.api.api.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.api.model.Tratamiento;

@Repository
public interface DashboardRepository extends JpaRepository<Tratamiento, Long> {
    @Query("""
        select count(t)
        from Tratamiento t
        where t.fecha >= :start and t.fecha < :end
    """)
    long countTratamientosBetween(@Param("start") LocalDate start,
                                  @Param("end")   LocalDate end);

    @Query("""
        select m.nombre, sum(t.cantidadUsada)
        from Tratamiento t
        join t.medicamento m
        where t.fecha >= :start and t.fecha < :end
        group by m.nombre
    """)
    List<Object[]> rawTratamientosPorMedicamentoBetween(@Param("start") LocalDate start,
                                                        @Param("end")   LocalDate end);

    @Query("""
        select m.nombre, sum(t.cantidadUsada)
        from Tratamiento t
        join t.medicamento m
        where t.fecha >= :start and t.fecha < :end
        group by m.nombre
        order by sum(t.cantidadUsada) desc
    """)
    List<Object[]> rawTopTratamientosBetween(@Param("start") LocalDate start,
                                             @Param("end")   LocalDate end);

    @Query("""
        select coalesce(sum(coalesce(m.precioVenta, 0) * t.cantidadUsada), 0)
        from Tratamiento t
        join t.medicamento m
        where t.fecha >= :start and t.fecha < :end
    """)
    BigDecimal ventasTotalesBetween(@Param("start") LocalDate start,
                                    @Param("end")   LocalDate end);

    @Query("""
        select coalesce(sum(coalesce(m.precioCompra, 0) * t.cantidadUsada), 0)
        from Tratamiento t
        join t.medicamento m
        where t.fecha >= :start and t.fecha < :end
    """)
    BigDecimal costosTotalesBetween(@Param("start") LocalDate start,
                                    @Param("end")   LocalDate end);

    /* --------- Totales para tarjetas del dashboard --------- */

    // Veterinario.activo == int
    @Query("select count(v) from Veterinario v where v.activo = 1")
    long countVeterinariosActivos();

    @Query("select count(v) from Veterinario v where v.activo <> 1")
    long countVeterinariosInactivos();

    // Mascota.activo == Boolean
    @Query("select count(m) from Mascota m")
    long countMascotasTotales();

    @Query("select count(m) from Mascota m where m.activo = true")
    long countMascotasActivas();
}
