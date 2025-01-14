package io.quarkus.gradle.devmode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;

import com.google.common.collect.ImmutableMap;

@Disabled
@org.junit.jupiter.api.Tag("failsOnJDK19")
public class MultiModuleKotlinProjectDevModeTest extends QuarkusDevGradleTestBase {

    @Override
    protected String projectDirectoryName() {
        return "multi-module-kotlin-project";
    }

    @Override
    protected String[] buildArguments() {
        return new String[] { "clean", ":web:quarkusDev" };
    }

    protected void testDevMode() throws Exception {

        assertThat(getHttpResponse())
                .contains("ready")
                .contains("quarkusmm")
                .contains("org.acme")
                .contains("1.0.0-SNAPSHOT");

        assertThat(getHttpResponse("/hello")).contains("howdy");

        replace("domain/src/main/kotlin/com/example/quarkusmm/domain/CustomerServiceImpl.kt",
                ImmutableMap.of("return \"howdy\"", "return \"modified\""));

        assertUpdatedResponseContains("/hello", "modified");
    }
}
