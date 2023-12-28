package org.reservationservice.service;

import org.enactor.commonlibrary.model.ApiResponse;
import org.enactor.commonlibrary.model.BusReservationCriteria;
import org.enactor.commonlibrary.model.VehicleType;
import org.enactor.commonlibrary.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.reservationservice.model.BusKey;
import org.reservationservice.model.ReservationResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest
{
    @Spy
    @InjectMocks
    private ReservationService busReservationService;



    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testReserveTickets_ValidReservation_Success()
    {

        BusReservationCriteria validCriteria = getReservationCriteria();
        BusKey validBusKey = new BusKey(validCriteria);

        assertEquals(40,ReservationService.seatAvailabilityMap.get(validBusKey).getAvailableSeats());
        ApiResponse<ReservationResult> response1 = busReservationService.reserveTickets(validCriteria);

        assertNotNull(response1);
        assertEquals(200, response1.getStatus());
        assertEquals(Constant.SUCCESS, response1.getMessage());
        assertNotNull(response1.getData());
        assertEquals(40-validCriteria.getPaxCount(),ReservationService.seatAvailabilityMap.get(validBusKey).getAvailableSeats());

        int availableSeats2 = ReservationService.seatAvailabilityMap.get(validBusKey).getAvailableSeats();
        ApiResponse<ReservationResult> response2 = busReservationService.reserveTickets(validCriteria);

        assertNotNull(response2);
        assertEquals(200, response2.getStatus());
        assertEquals(Constant.SUCCESS, response2.getMessage());
        assertNotNull(response2.getData());
        assertEquals(availableSeats2-validCriteria.getPaxCount(),ReservationService.seatAvailabilityMap.get(validBusKey).getAvailableSeats());

    }

    @Test
    void testReserveTickets_NotEnoughAvailableSeats_Success()
    {
        BusReservationCriteria highPaxCountCriteria = getReservationCriteria();
        BusKey validBusKey = new BusKey(highPaxCountCriteria);
        int availableSeats2 = ReservationService.seatAvailabilityMap.get(validBusKey).getAvailableSeats();

        highPaxCountCriteria.setPaxCount(availableSeats2 + 1);
        ApiResponse<ReservationResult> response = busReservationService.reserveTickets(highPaxCountCriteria);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("Not enough available seats.", response.getMessage());
    }

    @Test
    void testReserveTickets_with_invalid_criteria()
    {
        BusReservationCriteria invalidCriteria = getReservationCriteria();
        invalidCriteria.setTurn(3);

        ApiResponse<ReservationResult> response = busReservationService.reserveTickets(invalidCriteria);

        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals(Constant.FAILED, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getError());
        assertEquals("Invalid reservation details are provided",response.getError().getErrorMessage());
    }

    @Test
    void testReserveTickets_ExceptionOccurred_Failure()
    {
        BusReservationCriteria criteria = getReservationCriteria();
        criteria.setOrigin("");
        ApiResponse<ReservationResult> response = busReservationService.reserveTickets(criteria);

        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals(Constant.FAILED, response.getMessage());
        assertNotNull(response.getError());
    }

    @Test
    void getSeatAvailabilities() {
    }

    private BusReservationCriteria getReservationCriteria()
    {
        BusReservationCriteria criteria = new BusReservationCriteria();
        criteria.setVehicleType(VehicleType.BUS);
        criteria.setVehicleNum("1");
        criteria.setOrigin("A");
        criteria.setDestination("D");
        criteria.setDate(LocalDate.of(2024,01,01));
        criteria.setTurn(1);
        criteria.setPaxCount(3);
        return criteria;
    }

}