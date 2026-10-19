package com.jayant.ambulanceassignmentservice.Strategy;

import com.jayant.ambulanceassignmentservice.Entity.Ambulance;
import com.jayant.ambulanceassignmentservice.Entity.Emergency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmbulanceSelectionStrategyTest {

    private final AmbulanceSelectionStrategy strategy = new AmbulanceSelectionStrategy();

    // ---------- helpers ----------

    private Emergency buildEmergency(Double lat, Double lon, Integer patientCount) {
        Emergency emergency = new Emergency();
        emergency.setEmergencyId(1L);
        emergency.setLatitude(lat);
        emergency.setLongitude(lon);
        emergency.setPatientCount(patientCount);
        return emergency;
    }

    private Ambulance buildAmbulance(Long id, String status, Integer capacity, Double lat, Double lon) {
        Ambulance ambulance = new Ambulance();
        ambulance.setAmbulanceId(id);
        ambulance.setStatus(status);
        ambulance.setPatientCapacity(capacity);
        ambulance.setLatitude(lat);
        ambulance.setLongitude(lon);
        return ambulance;
    }

    // ---------- validation: emergency ----------

    @Test
    void selectAmbulance_whenEmergencyNull_shouldThrow() {
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", 4, 20.0, 85.0));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> strategy.selectAmbulance(null, list));

        assertTrue(ex.getMessage().contains("Emergency must not"));
    }

    @Test
    void selectAmbulance_whenEmergencyLatitudeNull_shouldThrow() {
        Emergency emergency = buildEmergency(null, 85.0, 2);
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", 4, 20.0, 85.0));

        assertThrows(IllegalArgumentException.class,
                () -> strategy.selectAmbulance(emergency, list));
    }

    @Test
    void selectAmbulance_whenEmergencyLongitudeNull_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, null, 2);
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", 4, 20.0, 85.0));

        assertThrows(IllegalArgumentException.class,
                () -> strategy.selectAmbulance(emergency, list));
    }

    @ParameterizedTest
    @CsvSource({
            "91.0, 85.0",
            "-91.0, 85.0",
            "20.0, 181.0",
            "20.0, -181.0"
    })
    void selectAmbulance_whenEmergencyCoordinatesInvalid_shouldThrow(double lat, double lon) {
        Emergency emergency = buildEmergency(lat, lon, 2);
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", 4, 20.0, 85.0));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> strategy.selectAmbulance(emergency, list));

        assertEquals("Invalid emergency coordinates", ex.getMessage());
    }

    // ---------- validation: ambulance list ----------

    @Test
    void selectAmbulance_whenListNull_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.selectAmbulance(emergency, null));
    }

    @Test
    void selectAmbulance_whenListEmpty_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.selectAmbulance(emergency, new ArrayList<>()));
    }

    // ---------- filtering ----------

    @Test
    void selectAmbulance_whenNoAmbulanceAvailable_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);
        List<Ambulance> list = List.of(buildAmbulance(1L, "BUSY", 4, 20.0, 85.0));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> strategy.selectAmbulance(emergency, list));

        assertEquals("No suitable ambulance available", ex.getMessage());
    }

    @Test
    void selectAmbulance_whenCapacityTooSmall_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, 5);
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", 2, 20.0, 85.0));

        assertThrows(IllegalStateException.class,
                () -> strategy.selectAmbulance(emergency, list));
    }

    @Test
    void selectAmbulance_whenAmbulanceCapacityNull_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", null, 20.0, 85.0));

        assertThrows(IllegalStateException.class,
                () -> strategy.selectAmbulance(emergency, list));
    }

    @Test
    void selectAmbulance_whenPatientCountNull_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, null);
        List<Ambulance> list = List.of(buildAmbulance(1L, "AVAILABLE", 4, 20.0, 85.0));

        assertThrows(IllegalStateException.class,
                () -> strategy.selectAmbulance(emergency, list));
    }

    // ---------- selection ----------

    @Test
    void selectAmbulance_shouldReturnNearestSuitableAmbulance() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);

        Ambulance far = buildAmbulance(1L, "AVAILABLE", 4, 21.0, 86.0);
        Ambulance near = buildAmbulance(2L, "AVAILABLE", 4, 20.01, 85.01);
        Ambulance nearestButBusy = buildAmbulance(3L, "BUSY", 4, 20.0, 85.0);
        Ambulance nearestButSmall = buildAmbulance(4L, "AVAILABLE", 1, 20.0, 85.0);

        Ambulance result = strategy.selectAmbulance(emergency,
                List.of(far, nearestButBusy, nearestButSmall, near));

        assertSame(near, result);
    }

    @Test
    void selectAmbulance_shouldMatchStatusIgnoringCase() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);
        Ambulance ambulance = buildAmbulance(1L, "available", 4, 20.01, 85.01);

        Ambulance result = strategy.selectAmbulance(emergency, List.of(ambulance));

        assertSame(ambulance, result);
    }

    @Test
    void selectAmbulance_shouldSkipAmbulancesWithNullCoordinates() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);

        Ambulance nullLat = buildAmbulance(1L, "AVAILABLE", 4, null, 85.0);
        Ambulance nullLon = buildAmbulance(2L, "AVAILABLE", 4, 20.0, null);
        Ambulance valid = buildAmbulance(3L, "AVAILABLE", 4, 21.0, 86.0);

        Ambulance result = strategy.selectAmbulance(emergency, List.of(nullLat, nullLon, valid));

        assertSame(valid, result);
    }

    @ParameterizedTest
    @CsvSource({
            "91.0, 85.0",
            "-91.0, 85.0",
            "20.0, 181.0",
            "20.0, -181.0"
    })
    void selectAmbulance_shouldSkipAmbulancesWithInvalidCoordinates(double lat, double lon) {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);

        Ambulance invalid = buildAmbulance(1L, "AVAILABLE", 4, lat, lon);
        Ambulance valid = buildAmbulance(2L, "AVAILABLE", 4, 21.0, 86.0);

        Ambulance result = strategy.selectAmbulance(emergency, List.of(invalid, valid));

        assertSame(valid, result);
    }

    @Test
    void selectAmbulance_whenAllAmbulancesHaveInvalidCoordinates_shouldThrow() {
        Emergency emergency = buildEmergency(20.0, 85.0, 2);

        Ambulance nullCoords = buildAmbulance(1L, "AVAILABLE", 4, null, null);
        Ambulance outOfRange = buildAmbulance(2L, "AVAILABLE", 4, 95.0, 200.0);

        assertThrows(IllegalStateException.class,
                () -> strategy.selectAmbulance(emergency, List.of(nullCoords, outOfRange)));
    }

    // ---------- calculateDistance ----------

    @Test
    void calculateDistance_samePoint_shouldBeZero() {
        double distance = strategy.calculateDistance(20.0, 85.0, 20.0, 85.0);

        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void calculateDistance_oneDegreeLatitude_shouldBeAbout111Km() {
        double distance = strategy.calculateDistance(0.0, 0.0, 1.0, 0.0);

        assertEquals(111.195, distance, 0.01);
    }

    @Test
    void calculateDistance_shouldBeSymmetric() {
        double d1 = strategy.calculateDistance(20.0, 85.0, 21.0, 86.0);
        double d2 = strategy.calculateDistance(21.0, 86.0, 20.0, 85.0);

        assertEquals(d1, d2, 0.0001);
    }
}