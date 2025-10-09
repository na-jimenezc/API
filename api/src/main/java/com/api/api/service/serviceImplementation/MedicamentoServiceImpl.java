package com.api.api.service.serviceImplementation;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.*;
import java.text.Normalizer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public int importarDesdeExcel(InputStream in, boolean replace) throws IOException {
        try (Workbook wb = WorkbookFactory.create(in)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) return 0;

            DataFormatter fmt = new DataFormatter();

            // --- cabecera ---
            Row header = sheet.getRow(sheet.getFirstRowNum());
            if (header == null) return 0;

            Map<String,Integer> idx = new HashMap<>();
            for (Cell c : header) {
                String k = norm(fmt.formatCellValue(c));
                if (!k.isEmpty()) idx.put(k, c.getColumnIndex());
            }

            boolean conId = idx.containsKey("ID");

            // nombres a conservar (normalizados) para el replace
            Set<String> keepNames = new HashSet<>();

            int count = 0;
            int start = sheet.getFirstRowNum() + 1;
            for (int r = start; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Long id            = getLong(fmt, row, idx.get("ID"));
                String nombre      = getString(fmt, row, idx.get("NOMBRE"));
                Long precioCompra  = getLong(fmt, row, idx.get("PRECIO_COMPRA"));
                Long precioVenta   = getLong(fmt, row, idx.get("PRECIO_VENTA"));
                Integer disp       = getInt(fmt, row, idx.get("UNIDADES_DISPONIBLES"));
                Integer vend       = getInt(fmt, row, idx.get("UNIDADES_VENDIDAS"));

                if (isBlank(nombre)) continue;
                keepNames.add(nombre.trim().toLowerCase());

                Medicamento med;
                if (conId && id != null) {
                    med = medicamentoRepository.findById(id).orElseGet(Medicamento::new);
                    med.setId(id);
                } else {
                    // si no usas findByNombreIgnoreCase, basta con buscar todos y filtrar (menos eficiente)
                    med = medicamentoRepository.findAll().stream()
                            .filter(m -> m.getNombre()!=null
                                && m.getNombre().equalsIgnoreCase(nombre))
                            .findFirst()
                            .orElseGet(Medicamento::new);
                }

                med.setNombre(nombre);
                if (precioCompra != null) med.setPrecioCompra(precioCompra);
                if (precioVenta  != null) med.setPrecioVenta(precioVenta);
                if (disp         != null) med.setUnidadesDisponibles(disp);
                if (vend         != null) med.setUnidadesVendidas(vend);

                medicamentoRepository.save(med);
                count++;
            }

            // --- eliminar los que no están en el Excel ---
            if (replace) {
                List<Medicamento> actuales = medicamentoRepository.findAll();
                for (Medicamento m : actuales) {
                    String n = m.getNombre()==null ? "" : m.getNombre().trim().toLowerCase();
                    if (!keepNames.contains(n)) {
                        medicamentoRepository.delete(m);
                    }
                }
            }
            return count;
        }
    }

    // ---------- Helpers robustos ----------
    private static String norm(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")   // quita acentos
                .replace(" ", "_")          // espacios -> _
                .toUpperCase();
        // alias más tolerantes
        if (t.startsWith("PRECIO_V")) {
            if (t.contains("COMPRA")) return "PRECIO_COMPRA";
            if (t.contains("VENTA"))  return "PRECIO_VENTA";
        }
        if (t.startsWith("UNIDADES_DISP")) return "UNIDADES_DISPONIBLES";
        if (t.startsWith("UNIDADES_VEND")) return "UNIDADES_VENDIDAS";
        return t;
    }
    private static boolean isBlank(String s) { return s == null || s.isBlank(); }

    private static String getString(DataFormatter fmt, Row row, Integer col) {
        if (col == null) return null;
        return safe(fmt.formatCellValue(row.getCell(col)));
    }
    private static Long getLong(DataFormatter fmt, Row row, Integer col) {
        if (col == null) return null;
        return parseLong(fmt.formatCellValue(row.getCell(col)));
    }
    private static Integer getInt(DataFormatter fmt, Row row, Integer col) {
        if (col == null) return null;
        return parseInt(fmt.formatCellValue(row.getCell(col)));
    }
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    private static Long parseLong(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            return Long.parseLong(s.replaceAll("[^0-9\\-]", "")); // tolera $, comas, espacios
        } catch (NumberFormatException e) { return null; }
    }
    private static Integer parseInt(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            return Integer.parseInt(s.replaceAll("[^0-9\\-]", ""));
        } catch (NumberFormatException e) { return null; }
    }
}