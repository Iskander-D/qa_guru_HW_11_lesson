package guru_qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru_qa.json.Json;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTest {
    ClassLoader cl = JsonTest.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Json тест")
    void jsonTest() throws Exception {

        try (
                InputStream resource = cl.getResourceAsStream("JsonTest.json")
        ) {
            assert resource != null;
            try (InputStreamReader reader = new InputStreamReader(resource)
            ) {
                Json Json = objectMapper.readValue(reader, Json.class);
                assertThat(Json.name).isEqualTo("John");
                assertThat(Json.skill).isEqualTo("driver");
                assertThat(Json.age).isEqualTo(40);
                assertThat(Json.cars).contains("Porsche", "Maclaren", "Nissan");

            }
        }
    }
}
