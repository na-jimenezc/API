// com.api.api.config.ExcelBootstrap.java
package com.api.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.boot.CommandLineRunner;

import com.api.api.service.serviceInterface.MedicamentoService;

@Configuration
public class ExcelBootstrap {

  @Bean
  CommandLineRunner importarExcelInicial(MedicamentoService service) {
    return args -> {
      try {
        var res = new ClassPathResource("data/MEDICAMENTOS_VETERINARIA.xlsx");
        if (!res.exists()) {
          System.out.println("[ExcelBootstrap] No se encontró el recurso data/MEDICAMENTOS_VETERINARIA.xlsx");
          return;
        }
        // Evita el “existe pero vacío”
        if (res.contentLength() <= 0) {
          System.out.println("[ExcelBootstrap] El recurso existe pero está vacío: data/MEDICAMENTOS_VETERINARIA.xlsx");
          return;
        }
        try (var in = res.getInputStream()) {
          int filas = service.importarDesdeExcel(in);
          System.out.println("[ExcelBootstrap] Importados: " + filas);
        }
      } catch (Exception e) {
        System.out.println("[ExcelBootstrap] No se pudo importar Excel: " + e.getMessage());
      }
    };
  }
}
