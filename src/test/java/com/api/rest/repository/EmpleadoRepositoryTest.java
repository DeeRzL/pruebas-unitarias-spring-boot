package com.api.rest.repository;

//import static org.aspectj.core.api.Assertions.assertThat;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;

import com.api.rest.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest//prueba entidades y repos
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado(){
        //given - dado o condicion previa o config
        Empleado empleado1 = Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();

        //when - accion o el comportamiento
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);

        //then - verificar la salida
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);

    }

    @DisplayName("Test para listar empleados")
    @Test
    void testListarEmpleados(){
        //given - dado o condicion previa o config
        Empleado empleado1 = Empleado.builder()
                .nombre("Julen")
                .apellido("Oliva")
                .email("j2@gmail.com")
                .build();

        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);
        //when - accion o el comportamiento
        List<Empleado> listaEmpleados = empleadoRepository.findAll();
        //then - verificar la salida
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);

    }

    @DisplayName("Test para obtener empleado por id")
    @Test
    void testObtenerEmpleadoPorId(){
        //given - dado o condicion previa o config
        empleadoRepository.save(empleado);
        //when - accion o el comportamiento
        //Optional<Empleado> empleadoPorId  = empleadoRepository.findById(empleado.getId());
        Empleado empleadoPorId = empleadoRepository.findById(empleado.getId()).get();
        //then - verificar la salida
        assertThat(empleadoPorId).isNotNull();
    }

    @DisplayName("Test para actualizar empleado")
    @Test
    void testActualizarEmpleado(){
        //given
        empleadoRepository.save(empleado);
        //when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("c34@gmail.com");
        empleadoGuardado.setNombre("raul");
        empleadoGuardado.setApellido("yanguas");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);
        //that
        assertThat(empleadoActualizado.getEmail()).isEqualTo("c34@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("raul");
        assertThat(empleadoActualizado.getApellido()).isEqualTo("yanguas");
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    void testEliminarEmpleado(){
        //given
        empleadoRepository.save(empleado);
        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());
        //then
        assertThat(empleadoOptional).isEmpty();
    }

}
