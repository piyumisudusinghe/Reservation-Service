package org.reservationservice.validator;

import org.enactor.commonlibrary.exception.InvalidRequestException;
import org.enactor.commonlibrary.model.ReservationCriteria;
import org.enactor.commonlibrary.model.VehicleType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BusReservationCriteriaValidatorTest {

    @Test
    void validateBusAvailabilityCheckCriteria_invalidVehicleType() {
        ReservationCriteria criteria = getBusReservationCriteria(null, "1", "A", "D", LocalDate.of(2024, 01, 01), 3, 1);
        assertInvalidRequestException("Mandatory parameter vehicle type is not specified", criteria);
    }

    @Test
    void validateBusAvailabilityCheckCriteria_invalidVehicleNum() {
        ReservationCriteria criteria = getBusReservationCriteria(VehicleType.BUS, " ", "A", "D", LocalDate.of(2024, 01, 01), 3, 1);
        assertInvalidRequestException("Mandatory parameter vehicle number is not specified", criteria);
    }

    @Test
    void validateBusAvailabilityCheckCriteria_invalidOrigin() {
        ReservationCriteria criteria = getBusReservationCriteria(VehicleType.BUS, "1", " ", "D", LocalDate.of(2024, 01, 01), 3, 1);
        assertInvalidRequestException("Mandatory parameter origin is not specified", criteria);
    }

    @Test
    void validateBusAvailabilityCheckCriteria_invalidDest() {
        ReservationCriteria criteria = getBusReservationCriteria(VehicleType.BUS, "1", "A", " ", LocalDate.of(2024, 01, 01), 3, 1);
        assertInvalidRequestException("Mandatory parameter destination is not specified", criteria);
    }

    @Test
    void validateBusAvailabilityCheckCriteria_invalidDate() {
        ReservationCriteria criteria = getBusReservationCriteria(VehicleType.BUS, "1", "A", "D", null, 3, 1);
        assertInvalidRequestException("Mandatory parameter travelling date is not specified", criteria);
    }

    @Test
    void validateBusAvailabilityCheckCriteria_invalidPaxCount() {
        ReservationCriteria criteria = getBusReservationCriteria(VehicleType.BUS, "1", "A", "D", LocalDate.of(2024, 01, 01), 0, 1);
        assertInvalidRequestException("Mandatory parameter passenger count should be a positive number", criteria);
    }

    @Test
    void validateBusAvailabilityCheckCriteria_invalidTurn() {
        ReservationCriteria criteria = getBusReservationCriteria(VehicleType.BUS, "1", "A", "D", LocalDate.of(2024, 01, 01), 3, 0);
        assertInvalidRequestException("Mandatory parameter turn of the vehicle within the day is not specified", criteria);
    }

    private ReservationCriteria getBusReservationCriteria(VehicleType vehicleType, String vehicleNum, String origin, String destintaion, LocalDate date, Integer paxCount, Integer turn) {
        ReservationCriteria reservationCriteria = new ReservationCriteria();
        reservationCriteria.setVehicleType(vehicleType);
        reservationCriteria.setVehicleNum(vehicleNum);
        reservationCriteria.setDestination(destintaion);
        reservationCriteria.setOrigin(origin);
        reservationCriteria.setDate(date);
        reservationCriteria.setPaxCount(paxCount);
        reservationCriteria.setTurn(turn);
        return reservationCriteria;
    }

    private void assertInvalidRequestException(String expectedMessage, ReservationCriteria criteria) {
        try {
            BusReservationCriteriaValidator.validateBusAvailabilityCheckCriteria(criteria);
            fail("Expected InvalidRequestException, but no exception was thrown");
        } catch (InvalidRequestException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
