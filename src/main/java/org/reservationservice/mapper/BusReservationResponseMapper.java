package org.reservationservice.mapper;

import org.enactor.commonlibrary.model.*;
import org.enactor.commonlibrary.util.Constant;
import org.reservationservice.model.*;

import java.util.ArrayList;
import java.util.List;

public class BusReservationResponseMapper
{
    private static int reservationCounter = 0;

    /**
     * This method is used to map the bus reservation response
     *
     * @param seatAvailability object which contains seat availability data
     * @param busReservationCriteria reservation criteria
     * @return mapped response
     */
    public static ReservationResult mapBusReservationResult(SeatAvailability seatAvailability, ReservationCriteria busReservationCriteria)
    {
        Bus bus = (Bus) seatAvailability.getVehicle();
        int startSeatNo = seatAvailability.getTotalSeats() - seatAvailability.getAvailableSeats();
        BusReservationResult busReservationResult = new BusReservationResult();
        busReservationResult.setJourney(mapJourney(bus, busReservationCriteria));
        busReservationResult.setSeats(mapSeats(startSeatNo,busReservationCriteria.getPaxCount(), bus));
        busReservationResult.setPrice(mapPrice(bus, busReservationCriteria));
        busReservationResult.setTicketNo(generateReservationNumber(busReservationCriteria));
        return busReservationResult;
    }

    /**
     * This method is used to generate a unique reservation number for the reservation
     *
     * @param reservationCriteria reservation criteria
     * @return the unique string which created for the reservation
     */
    private static String generateReservationNumber(ReservationCriteria reservationCriteria)
    {
        return reservationCriteria.getVehicleType().getCode() + reservationCriteria.getOrigin() + reservationCriteria.getDestination() + generateReservationNumber();

    }

    /**
     * This method is used to get number in increasing order to include in reservation number
     * @return integer
     */
    private static synchronized int generateReservationNumber()
    {
        return ++reservationCounter;
    }

    /**
     * This method is used to map the price object in the response
     *
     * @param vehicle vehicle object
     * @param busReservationCriteria reservation criteria
     * @return mapped TicketPrice object
     */
    private static TicketPrice mapPrice(Vehicle vehicle, ReservationCriteria busReservationCriteria)
    {
        Bus bus = (Bus)vehicle;
        TicketPrice ticketPrice = new TicketPrice();
        String key = busReservationCriteria.getOrigin() + busReservationCriteria.getDestination();
        ticketPrice.setUnitPrice(bus.getPriceList()  != null ? bus.getPriceList().get(key) : 0 );
        ticketPrice.setCurrency(Constant.LKR);
        ticketPrice.setValue(ticketPrice.getUnitPrice() * busReservationCriteria.getPaxCount());
        return ticketPrice;
    }

    /**
     * This method is used to map the node with journey information in the response
     *
     * @param vehicle vehicle object
     * @param busReservationCriteria reservation criteria
     * @return mapped journey node
     */
    private static Journey mapJourney(Vehicle vehicle, ReservationCriteria busReservationCriteria)
    {
        Bus bus = (Bus)vehicle;
        Journey journey = new Journey();
        if(bus.getDayTurnMap() != null &&  bus.getDayTurnMap().get(busReservationCriteria.getTurn()) != null)
        {
            DayTurn dayTurn = bus.getDayTurnMap().get(busReservationCriteria.getTurn());
            journey.setDepartTime(dayTurn.getDepartTime().toString());
            journey.setArrivalTime(dayTurn.getArrivalTime().toString());
            journey.setOrigin(busReservationCriteria.getOrigin());
            journey.setDestination(busReservationCriteria.getDestination());
            journey.setDate(busReservationCriteria.getDate().toString());
        }
        return journey;
    }

    /**
     * This method is used to map the seat details in the response
     *
     * @param startSeatNo starting number of the available seat
     * @param noOfSeats no of seats requested by the user
     * @param vehicle vehicle object
     * @return mapped seat list
     */
    private static List<Seat> mapSeats(int startSeatNo, int noOfSeats, Vehicle vehicle)
    {
        List<Seat> seats = new ArrayList<>();
        int seatNo = startSeatNo;
        for(int s = 0; s < noOfSeats; s ++)
        {
            Seat seat = mapSeat(seatNo, getRowNo(seatNo, vehicle));
            seatNo = seatNo + 1;
            seats.add(seat);
        }
        return seats;
    }

    /**
     * This method is used to get the row number of the seat(1A,1B,1C,1D...10A,10B,10C,10D)
     *
     * @param seatNo number of the seat
     * @param vehicle vehicle object
     * @return the string with row number
     */
    private static String getRowNo(int seatNo, Vehicle vehicle)
    {
        Bus bus = (Bus)vehicle;
        int letterIndex = (seatNo - 1) % bus.getSeatsPerRow();
        char letter = (char) ('A' + letterIndex);
        int groupNumber = ((seatNo - 1) / bus.getSeatsPerRow())+ 1;
        return groupNumber + String.valueOf(letter);
    }

    /**
     * This method is used to map the Seat object
     *
     * @param seatNo number of the seat(1,2,3,...38,39,40)
     * @param rowNo row number (1A,1B,1C...)
     * @return mapped Seat object
     */
    private static Seat mapSeat(int seatNo, String rowNo)
    {
        Seat seat = new Seat();
        seat.setSeatNo(seatNo);
        seat.setRowNo(rowNo);
        return seat;
    }
}
