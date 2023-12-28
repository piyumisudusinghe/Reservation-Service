package org.reservationservice.service;

import org.enactor.commonlibrary.model.*;
import org.enactor.commonlibrary.util.Constant;
import org.reservationservice.mapper.BusReservationResponseMapper;
import org.reservationservice.model.BusKey;
import org.reservationservice.model.BusReservationResult;
import org.reservationservice.model.ReservationResult;
import org.reservationservice.util.ResServiceConstant;
import org.reservationservice.validator.BusReservationCriteriaValidator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationService
{
    public static final Logger logger = Logger.getLogger(ResServiceConstant.LOG_CODE);
    public static Map<BusKey, SeatAvailability> seatAvailabilityMap = new ConcurrentHashMap<>();

    static
    {
        //insert data to in memory data structures
        DatabaseService.insertData();
    }

    /**
     * This method is used to reserve ticket in the bus
     *
     * @param busReservationCriteria reservation criteria
     * @return the details of the reservation
     */
    public static ApiResponse<ReservationResult> reserveTickets(ReservationCriteria busReservationCriteria)
    {
        try
        {
            BusReservationCriteriaValidator.validateBusAvailabilityCheckCriteria(busReservationCriteria);
            BusKey busKey = new BusKey(busReservationCriteria);
            synchronized (seatAvailabilityMap)
            {
                // Check if the bus details are present in the map
                if (!seatAvailabilityMap.containsKey(busKey))
                {
                    logger.log(Level.SEVERE, () -> "Invalid bus details for the given date, origin, destination, and turn." );
                    return new ApiResponse<>(400,"FAILED", new ApiError(401,"Invalid reservation details are provided"));
                }

                int availableSeats = seatAvailabilityMap.get(busKey).getAvailableSeats();
                if (availableSeats >= busReservationCriteria.getPaxCount())
                {
                    SeatAvailability seatAvailability = seatAvailabilityMap.get(busKey);
                    seatAvailability.setAvailableSeats(availableSeats - busReservationCriteria.getPaxCount());
                    ReservationResult busReservationResult = BusReservationResponseMapper.mapBusReservationResult( seatAvailability, busReservationCriteria);
                    logger.log(Level.INFO, () -> "Seats reserved successfully." );
                    return new ApiResponse<>(200, Constant.SUCCESS,busReservationResult);
                } else
                {
                    logger.log(Level.INFO, () -> "Not enough available seats." );
                    return new ApiResponse<>(200,"Not enough available seats.", new BusReservationResult());
                }
            }
        }
        catch ( Exception exception)
        {
            logger.log(Level.SEVERE, () -> "error occurred while reserving tickets" );
            return new ApiResponse<>(400, Constant.FAILED, new ApiError(401,exception.getMessage()));
        }
    }

    /**
     * This method is used to get the details of the available seats
     *
     * @param busReservationCriteria reservation criteria
     * @return the available seats details
     */
    public static ApiResponse<SeatAvailability> getSeatAvailability(BusReservationCriteria busReservationCriteria)
    {
        BusKey busKey = new BusKey(busReservationCriteria);
        SeatAvailability seatAvailability = seatAvailabilityMap.get(busKey);
        ((Bus)seatAvailability.getVehicle()).setType("bus");
        return new ApiResponse<>(200, Constant.SUCCESS,seatAvailability);
    }

}
