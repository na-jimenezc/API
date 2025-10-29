package com.api.api.controller;

import com.api.api.dto.VeterinarioCreateDTO;
import com.api.api.dto.VeterinarioUpdateDTO;
import com.api.api.model.Veterinario;
import com.api.api.service.serviceInterface.VeterinarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = VeterinarioController.class)
@ActiveProfiles("test")
public class VeterinarioControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private VeterinarioService veterinarioService;

    @Autowired
    private ObjectMapper objectMapper;
    private Veterinario v(long id, String nombre) {
        Veterinario vet = new Veterinario();
        vet.setId(id);
        vet.setNombre(nombre);
        vet.setEspecialidad("Dermatología");
        vet.setNombreUsuario("user" + id);
        vet.setContrasenia("secret");
        vet.setImagen("avatar.png");
        vet.setActivo(1);
        vet.setConsultas(0);
        return vet;
    }
    @Test
    public void listar_ok() throws Exception {
        when(veterinarioService.buscar(null, null, null))
                .thenReturn(List.of(v(1, "Dra. Ana"), v(2, "Dr. Juan")));

        mockMvc.perform(get("/api/veterinarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Dra. Ana")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Dr. Juan")));
    }
    @Test
    public void getById_ok() throws Exception {
        when(veterinarioService.obtenerVeterinarioPorId(1L)).thenReturn(v(1, "Dra. Ana"));

        mockMvc.perform(get("/api/veterinarios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Dra. Ana")));
    }
    @Test
    public void crear_ok() throws Exception {
        VeterinarioCreateDTO dto = new VeterinarioCreateDTO();
        dto.setNombre("Dra. López");
        dto.setEspecialidad("Dermatología");
        dto.setNombreUsuario("vet10");
        dto.setContrasenia("pass123");
        dto.setImagen("avatar10.png");

        Veterinario persisted = v(10, "Dra. López");
        when(veterinarioService.guardarVeterinario(any(Veterinario.class))).thenReturn(persisted);

        mockMvc.perform(post("/api/veterinarios")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()) // tu controlador retorna ok(guardado)
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.nombre", is("Dra. López")));
    }
    @Test
    public void actualizar_ok() throws Exception {
        VeterinarioUpdateDTO dto = new VeterinarioUpdateDTO();
        dto.setNombre("Dr. Max");
        dto.setEspecialidad("Dermatología");
        dto.setNombreUsuario("maxuser");
        dto.setContrasenia("newpass");
        dto.setImagen("max.png");

        Veterinario existente = v(1, "Dra. Ana");

        Veterinario actualizado = v(1, "Dr. Max");
        actualizado.setEspecialidad("Dermatología");
        actualizado.setNombreUsuario("maxuser");
        actualizado.setContrasenia("newpass");
        actualizado.setImagen("max.png");
        actualizado.setActivo(1);

        when(veterinarioService.obtenerVeterinarioPorId(1L)).thenReturn(existente);
        when(veterinarioService.guardarVeterinario(any(Veterinario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/veterinarios/{id}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.nombre", is("Dr. Max")))
            .andExpect(jsonPath("$.especialidad", is("Dermatología")));
    }
    @Test
    public void patchEstado_ok() throws Exception {
        Veterinario after = v(3, "Dr. Patch");
        after.setActivo(0);

        when(veterinarioService.actualizarEstado(3L, 0)).thenReturn(after);

        mockMvc.perform(patch("/api/veterinarios/{id}/estado", 3L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("activo", 0))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.activo", is(0)));
    }

    @Test
    public void eliminar_ok() throws Exception {
        doNothing().when(veterinarioService).eliminarVeterinario(7L);

        mockMvc.perform(delete("/api/veterinarios/{id}", 7L))
                .andExpect(status().isOk());
    }
}