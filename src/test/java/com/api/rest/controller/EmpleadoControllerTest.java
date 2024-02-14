package com.api.rest.controller;

import com.api.rest.model.Empleado;
import com.api.rest.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest//probar controladores
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Test para guardar empleado")
    @Test
    void testGuardarEmpleado() throws Exception{
        //given
        Empleado empleado = Empleado.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));
        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @DisplayName("Test para listar empleado")
    @Test
    void testListarEmpleados() throws Exception{
        //given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Ramirez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ramirez").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Ramirez").email("a@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);
        //when
        ResultActions response = mockMvc.perform(get("/api/empleados"));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));
    }

    @DisplayName("Test para buscar empleado por id")
    @Test
    void testEmpleadoPorId() throws Exception{
        //given
        Empleado empleado = Empleado.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
        given(empleadoService.getEmpleadoById(empleado.getId())).willReturn(Optional.of(empleado));
        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", empleado.getId()));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is("Pepe")));
    }

    @DisplayName("Test empleado no encontrado")
    @Test
    void testEmpleadoNoEncontrado() throws Exception{
        //given
        Empleado empleado = Empleado.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
        given(empleadoService.getEmpleadoById(empleado.getId())).willReturn(Optional.empty());
        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", empleado.getId()));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Test actualizar empleado")
    @Test
    void testActualizarEmpleado() throws Exception {
        //given
        Empleado empleadoGuardado = Empleado.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
        Empleado empleadoActualizado = Empleado.builder()
                .id(2L)
                .nombre("Raul")
                .apellido("Ramirez")
                .email("j33@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoGuardado.getId())).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoGuardado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleadoActualizado.getEmail())));
    }

    @DisplayName("Test actualizar empleado no encontrado")
    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception {
        //given
        Empleado empleadoGuardado = Empleado.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
        Empleado empleadoActualizado = Empleado.builder()
                .id(2L)
                .nombre("Raul")
                .apellido("Ramirez")
                .email("j33@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoGuardado.getId())).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoGuardado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("Test eliminar empleado")
    @Test
    void testEliminarEmpleado() throws Exception {
        //given
        long empleadoId = 1;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);
        //when
        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}",empleadoId));
        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
