package ua.edu.chnu.kkn.lab6.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ua.edu.chnu.kkn.lab6.model.VectorUInt;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VectorJsonService Tests")
class VectorJsonServiceTest {
    private VectorJsonService jsonService;
    private VectorUInt testVector;

    @BeforeEach
    void setUp() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        jsonService = new VectorJsonService(gson);

        testVector = new VectorUInt(3, 5);
    }

    @Test
    @DisplayName("toJson() should serialize VectorUInt to JSON")
    void testToJson() {
        String json = jsonService.toJson(testVector);

        assertNotNull(json);
        assertTrue(json.contains("\"array\""));
        assertTrue(json.contains("\"codeError\""));
        assertTrue(json.contains("5"));
    }

    @Test
    @DisplayName("fromJson() should deserialize JSON to VectorUInt")
    void testFromJson() {
        String json = jsonService.toJson(testVector);
        VectorUInt restored = jsonService.fromJson(json);

        assertEquals(testVector.size(), restored.size());
        for (int i = 0; i < testVector.size(); i++) {
            assertEquals(testVector.get(i), restored.get(i));
        }
    }

    @Test
    @DisplayName("Round-trip serialization should preserve data")
    void testRoundTrip() {
        testVector.set(0, 10);
        testVector.set(1, 20);
        testVector.set(2, 30);

        String json = jsonService.toJson(testVector);
        VectorUInt restored = jsonService.fromJson(json);

        assertEquals(testVector.size(), restored.size());
        assertEquals(testVector.get(0), restored.get(0));
        assertEquals(testVector.get(1), restored.get(1));
        assertEquals(testVector.get(2), restored.get(2));
        assertEquals(testVector.getCodeError(), restored.getCodeError());
    }

    @Test
    @DisplayName("fromJson() with invalid JSON should throw exception")
    void testFromJsonInvalid() {
        String invalidJson = "{ invalid json }";

        assertThrows(Exception.class, () -> {
            jsonService.fromJson(invalidJson);
        });
    }
}