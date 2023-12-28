package org.reservationservice.model;

import org.enactor.commonlibrary.model.TicketPrice;

import java.util.List;

public class ReservationResult
{
    private String ticketNo;
    private List<Seat> seats;
    private Journey journey;
    private TicketPrice price;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public TicketPrice getPrice() {
        return price;
    }

    public void setPrice(TicketPrice price) {
        this.price = price;
    }
}
