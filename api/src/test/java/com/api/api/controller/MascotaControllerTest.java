package com.api.api.controller;

import java.util.List;

import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.api.api.dto.MascotaCreateDTO;
import com.api.api.model.Cliente;
import com.api.api.model.Mascota;
import com.api.api.service.serviceInterface.MascotaService;
import com.api.api.dto.MascotaUpdateDTO;
import com.api.api.service.serviceInterface.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//PRUEBAS UNITARIAS -> COMPONENTE ESPECÍFICO
//PRUEBA DE INTEGRACIÓN -> UN COMPONENTE SE UTILIZAN OTROS COMPONENTES || SE SIMULAN LOS LLAMADOS HTTP

@WebMvcTest(controllers = MascotaController.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @SuppressWarnings("removal")
    @MockBean //Crea espacio de memoria
    private MascotaService mascotaService;

 @SuppressWarnings("removal")
    @MockBean //Crea espacio de memoria
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    //Test para obtener las mascotas
    @Test
    public void MascotaController_ListarMascotas_listaMascotas() throws Exception {
        Mascota mascota1 = new Mascota("Firulais", "Criollo", 3, "Perro","Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        mascota1.setId(1L);

        Mascota mascota2 = new Mascota("Michi", "Persa", 2, "Gato","Alergias", 4.2, "/assets/images/michi.jpg", "Estable", true);
        mascota2.setId(2L);

        when(mascotaService.obtenerTodasMascotas()).thenReturn(List.of(mascota1, mascota2));

        mockMvc.perform(get("/api/mascotas").contentType("aplication/json"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Firulais"))
                .andExpect(jsonPath("$[1].nombre").value("Michi"));
    }

    //Test oara crear una nueva mascota con el DTO
    @Test
    public void MascotaController_CrearMascota_Mascota() throws Exception {
        MascotaCreateDTO dto = new MascotaCreateDTO();
        dto.setNombre("Firulais");
        dto.setRaza("Criollo");
        dto.setEdad(3);
        dto.setTipo("Perro");
        dto.setEnfermedad("Ninguna");
        dto.setPeso(25.4);
        dto.setFotoURL("/assets/images/firulais.jpg");
        dto.setEstado("Sano");
        dto.setActivo(true);
        dto.setClienteId(1L);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Mascota mascotaGuardada = new Mascota("Firulais", "Criollo", 3, "Perro",
                "Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        mascotaGuardada.setId(10L);
        mascotaGuardada.setCliente(cliente);

        when(clienteService.obtenerClientePorId(1L)).thenReturn(cliente);
        when(mascotaService.registrarMascota(any(Mascota.class))).thenReturn(mascotaGuardada);

        mockMvc.perform(post("/api/mascotas").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(10)).andExpect(jsonPath("$.nombre").value("Firulais"));
    }

    //Test para ver una mascota por su id
    @Test
    public void MascotaController_verDetalleMascota_MascotaId() throws Exception {
        Mascota mascota = new Mascota("Firulais", "Criollo", 3, "Perro","Ninguna", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        mascota.setId(1L);

        when(mascotaService.obtenerMascotaPorId(1L)).thenReturn(mascota);

        mockMvc.perform(get("/api/mascotas/1")).andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Firulais")).andExpect(jsonPath("$.tipo").value("Perro"));
    }

    //Test para actualizar una mascota
    @Test
    public void MascotaController_actualizarMascota_Mascota() throws Exception {
        MascotaUpdateDTO dto = new MascotaUpdateDTO();
        dto.setNombre("Firulais Modificado");
        dto.setTipo("Perro");
        dto.setRaza("Criollo");
        dto.setEnfermedad("Sano");
        dto.setFotoURL("/assets/images/firulais.jpg");
        dto.setActivo(true);

        Mascota actualizada = new Mascota("Firulais Modificado", "Criollo", 3, "Perro",
                "Sano", 25.4, "/assets/images/firulais.jpg", "Sano", true);
        actualizada.setId(1L);

        when(mascotaService.actualizarMascota(eq(1L),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(actualizada);

        mockMvc.perform(put("/api/mascotas/1").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Firulais Modificado"));
    }

    //Test para eliminar una mascota
    @Test
    public void MascotaController_eliminarMascota_200() throws Exception {
        Mockito.doNothing().when(mascotaService).eliminarMascotaHard(1L);
        mockMvc.perform(delete("/api/mascotas/1")).andExpect(status().isOk());
    }

    //Test para desactivar una mascota
    @Test
    public void MascotaController_desactivarMascota_200() throws Exception {
        Mockito.doNothing().when(mascotaService).desactivarMascota(1L);
        mockMvc.perform(delete("/api/mascotas/1/desactivar")).andExpect(status().isOk());
    }

}
