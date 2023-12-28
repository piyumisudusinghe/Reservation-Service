package org.reservationservice.validator;

import org.apache.commons.lang3.StringUtils;
import org.enactor.commonlibrary.exception.InvalidRequestException;
import org.enactor.commonlibrary.model.ReservationCriteria;

public class BusReservationCriteriaValidator
{
    public static void validateBusAvailabilityCheckCriteria(ReservationCriteria busReservationCriteria)
    {
        if (StringUtils.isBlank(busReservationCriteria.getOrigin()))
        {
            throw new InvalidRequestException("Mandatory parameter origin is not specified");
        }
        if (StringUtils.isBlank(busReservationCriteria.getDestination()))
        {
            throw new InvalidRequestException("Mandatory parameter destination is not specified");
        }
        if ( busReservationCriteria.getPaxCount() == null )
        {
            throw new InvalidRequestException("Mandatory parameter passenger count is not specified");
        }
        if ( busReservationCriteria.getPaxCount() <= 0 )
        {
            throw new InvalidRequestException("Mandatory parameter passenger count should be a positive number");
        }
        if ( busReservationCriteria.getVehicleType() == null )
        {
            throw new InvalidRequestException("Mandatory parameter vehicle type is not specified");
        }
        if ( StringUtils.isBlank(busReservationCriteria.getVehicleNum()))
        {
            throw new InvalidRequestException("Mandatory parameter vehicle number is not specified");
        }
        if ( busReservationCriteria.getDate() == null)
        {
            throw new InvalidRequestException("Mandatory parameter travelling date is not specified");
        }
        if ( busReservationCriteria.getTurn() == null || busReservationCriteria.getTurn() <= 0 )
        {
            throw new InvalidRequestException("Mandatory parameter turn of the vehicle within the day is not specified");
        }
    }
}
