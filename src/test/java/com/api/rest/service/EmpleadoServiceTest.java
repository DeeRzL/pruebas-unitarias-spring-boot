package com.api.rest.service;

import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.model.Empleado;
import com.api.rest.repository.EmpleadoRepository;
import com.api.rest.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//import static org.awaitility.Awaitility.given;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {
    //simula repo

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setup() {
        empleado = Empleado.builder()
                .id(2L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
    }

    @DisplayName("Test para guardar empleado")
    @Test
    void testGuardarEmpleado() {
        //gvn
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);
        //then
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para guardar empleado con throw exception")
    @Test
    void testGuardarEmpleadoConThrowException() {
        //gvn
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));
        //when
        assertThrows(ResourceNotFoundException.class,() -> {
            empleadoService.saveEmpleado(empleado);
        });
        //then
        verify(empleadoRepository,never()).save(any(Empleado.class));
    }

    @DisplayName("Test para listar empleados")
    @Test
    void testListarEmpleados() {
        //given
        Empleado empleado1 = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Oliva")
                .email("jj22@email.com")
                .build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));
        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();
        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para listar coleccion empleados vacia")
    @Test
    void testListarColeccionEmpleadosVacia() {
        //gv
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());
        //whn
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();
        //th
        //assertThat(listaEmpleados).isNull();
        assertThat(listaEmpleados.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener empleado por id")
    @Test
    void testObtenerEmpleadoPorId() {
        //given
        given(empleadoRepository.findById(2L)).willReturn(Optional.of(empleado));
        //when
        Empleado empleadoEncontrado = empleadoService.getEmpleadoById(empleado.getId()).get();
        //then
        assertThat(empleadoEncontrado).isNotNull();
    }

    @DisplayName("Test para actualizar un empleado ")
    @Test
    void testActualizarEmpleado() {
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("javier@gmail.com");
        //when
        empleadoService.updateEmpleado(empleado);
        //then
        assertThat(empleado.getEmail()).isEqualTo("javier@gmail.com");
    }

    @DisplayName("Test para eliminar un empleado ")
    @Test
    void testEliminarEmpleado() {
        //given
        Long empleadoId = 2L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);
        //when
        empleadoService.deleteEmpleado(empleadoId);
        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }
}
