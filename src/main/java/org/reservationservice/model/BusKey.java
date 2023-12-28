package org.reservationservice.model;

import org.enactor.commonlibrary.model.ReservationCriteria;

import java.time.LocalDate;
import java.util.Objects;

public class BusKey
{
    private String vehicleType;

    private String vehicleNum;
    private LocalDate date;
    private Integer turn;

    public BusKey()
    {

    }

    public BusKey(String vehicleType, String vehicleNum, LocalDate date, Integer turn)
    {
        this.vehicleType = vehicleType;
        this.vehicleNum = vehicleNum;
        this.date = date;
        this.turn = turn;
    }

    public BusKey(ReservationCriteria busReservationCriteria)
    {
        this.vehicleType = busReservationCriteria.getVehicleType().getCode();
        this.vehicleNum = busReservationCriteria.getVehicleNum();
        this.date = busReservationCriteria.getDate();
        this.turn = busReservationCriteria.getTurn();
    }

    public LocalDate getDate()
    {
        return date;
    }
    public int getTurn()
    {
        return turn;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public String getVehicleNum()
    {
        return vehicleNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusKey busKey = (BusKey) o;
        return Objects.equals(vehicleType, busKey.vehicleType) &&
                Objects.equals(vehicleNum, busKey.vehicleNum) &&
                Objects.equals(date, busKey.date) &&
                Objects.equals(turn, busKey.turn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleType, vehicleNum, date, turn);
    }
}
