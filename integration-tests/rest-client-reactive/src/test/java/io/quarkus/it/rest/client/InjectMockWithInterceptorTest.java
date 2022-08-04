package io.quarkus.it.rest.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.it.rest.client.main.FaultToleranceOnInterfaceClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import io.smallrye.faulttolerance.api.CircuitBreakerMaintenance;

@QuarkusTest
public class InjectMockWithInterceptorTest {
    @Inject
    CircuitBreakerMaintenance cb;

    @InjectMock
    FaultToleranceOnInterfaceClient mock;

    @BeforeEach
    public void setUp() {
        cb.resetAll();
        when(mock.hello()).thenReturn("MockHello");
    }

    @AfterEach
    public void tearDown() {
        cb.resetAll();
    }

    @Test
    void shouldMockClient() {
        assertThat(mock.hello()).isEqualTo("MockHello");
    }

    @Test
    void shouldMockClientInTheApp() {
        RestAssured.with().post("/call-with-fault-tolerance-on-interface")
                .then()
                .statusCode(200)
                .body(Matchers.equalTo("MockHello"));
    }
}