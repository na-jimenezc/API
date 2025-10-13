package com.api.api.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.api.api.service.serviceInterface.MascotaService;

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

}
