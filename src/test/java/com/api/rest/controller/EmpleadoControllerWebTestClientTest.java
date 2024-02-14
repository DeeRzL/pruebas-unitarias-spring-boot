package com.api.rest.controller;

import com.api.rest.model.Empleado;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerWebTestClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("Test guardar empleado")
    @Test
    @Order(1)
    void testGuardarEmpleado() {
        //given
        Empleado empleado = Empleado.builder()
                .id(3L)
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@email.com")
                .build();
        //when
        webTestClient.post().uri("http://localhost:8080/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleado)
                .exchange() //envia request
        //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(empleado.getId())
                .jsonPath("$.nombre").isEqualTo(empleado.getNombre())
                .jsonPath("$.apellido").isEqualTo(empleado.getApellido())
                .jsonPath("$.email").isEqualTo(empleado.getEmail());
    }

    @DisplayName("Test obtener empleado por id")
    @Test
    @Order(2)
    void testEmpleadoPorId() {
        webTestClient.get().uri("http://localhost:8080/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.nombre").isEqualTo("Pepe")
                .jsonPath("$.apellido").isEqualTo("Lopez")
                .jsonPath("$.email").isEqualTo("p12@email.com");
    }

    @DisplayName("Test listar empleados ")
    @Test
    @Order(3)
    void testListarEmpleados() {
        webTestClient.get().uri("http://localhost:8080").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[1].nombre").isEqualTo("Pepe")
                .jsonPath("$[1].apellido").isEqualTo("Lopez")
                .jsonPath("$[1].email").isEqualTo("p12@email.com")
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @DisplayName("Test obtener listado de empleados ")
    @Test
    @Order(4)
    void testObtenerListadoDeEmpleados() {
        webTestClient.get().uri("http://localhost:8080").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .consumeWith(response ->{
                    List<Empleado> empleados = response.getResponseBody();
                    Assertions.assertEquals(2, empleados.size());
                    Assertions.assertNotNull(empleados);
                });
    }

    @DisplayName("Test actualizar empleado")
    @Test
    @Order(5)
    void testActualizarEmpleado(){
        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Pepe")
                .apellido("Castillo")
                .email("ckk2@gmail.com")
                .build();

        webTestClient.put().uri("http://localhost:8080/api/empleados/2")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleadoActualizado)
                .exchange() //emvia el request

                //then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(6)
    void testEliminarEmpleado(){
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .hasSize(2);

        webTestClient.delete().uri("http://localhost:8080/api/empleados/2")
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .hasSize(1);

        webTestClient.get().uri("http://localhost:8080/api/empleados/1").exchange()
                .expectStatus().is4xxClientError();
    }
}
