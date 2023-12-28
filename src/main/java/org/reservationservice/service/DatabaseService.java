package org.reservationservice.service;

import org.enactor.commonlibrary.model.Bus;
import org.enactor.commonlibrary.model.DayTurn;
import org.enactor.commonlibrary.model.SeatAvailability;
import org.reservationservice.model.BusKey;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DatabaseService
{
    private static boolean dataInserted = false;

    public static synchronized void insertData()
    {

        if(!dataInserted)
        {
            BusKey key1 = new BusKey("B","1",LocalDate.of(2024,01,01),1);
            BusKey key2 = new BusKey("B","1",LocalDate.of(2024,01,01),2);
            BusKey key3 = new BusKey("B","1",LocalDate.of(2024,01,02),1);
            BusKey key4 = new BusKey("B","1",LocalDate.of(2024,01,02),2);
            BusKey key5 = new BusKey("B","1",LocalDate.of(2024,01,03),1);
            BusKey key6 = new BusKey("B","1",LocalDate.of(2024,01,03),2);
            BusKey key7 = new BusKey("B","1",LocalDate.of(2024,01,04),1);
            BusKey key8 = new BusKey("B","1",LocalDate.of(2024,01,04),2);
            BusKey key9 = new BusKey("B","1",LocalDate.of(2024,01,05),1);
            BusKey key10 = new BusKey("B","1",LocalDate.of(2024,01,05),2);
            BusKey key11 = new BusKey("B","1",LocalDate.of(2024,01,06),1);
            BusKey key12 = new BusKey("B","1",LocalDate.of(2024,01,06),2);

            ReservationService.seatAvailabilityMap.put(key1,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key2,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key3,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key4,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key5,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key6,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key7,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key8,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key9,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key10,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key11,getSeatAvailability());
            ReservationService.seatAvailabilityMap.put(key12,getSeatAvailability());

            dataInserted = true;
        }
    }

    private BusKey getBusKey(String vehicleType, String vehicleNum, LocalDate date, Integer turn)
    {
        BusKey busKey = new BusKey(vehicleType,vehicleNum,date,turn);
        return busKey;
    }

    private static SeatAvailability getSeatAvailability()
    {
        SeatAvailability seatAvailability = new SeatAvailability();
        seatAvailability.setTotalSeats(40);
        seatAvailability.setAvailableSeats(40);
        Bus bus = getBus(40,1,4,"A","B",getPriceList(),getDayTurnMap());
        seatAvailability.setVehicle(bus);
        return seatAvailability;
    }

    private static Bus getBus(int noOfSeats, int vehicleNum, int seatsPerRow, String origin, String destination, Map<String, Double> priceList, Map<Integer, DayTurn> dayTurnMap) {

        Bus bus = new Bus(noOfSeats, vehicleNum, seatsPerRow, origin, destination, priceList, dayTurnMap);
        return bus;
    }

    private static Map<String,Double> getPriceList()
    {
        Map<String,Double> priceList = new HashMap<>();
        priceList.put("AB",50.00);
        priceList.put("AC",100.00);
        priceList.put("AD",150.00);
        priceList.put("BC",50.00);
        priceList.put("BD",100.00);
        priceList.put("CD",50.00);
        priceList.put("BA",50.00);
        priceList.put("CA",100.00);
        priceList.put("DA",150.00);
        priceList.put("CB",50.00);
        priceList.put("DB",100.00);
        priceList.put("DC",50.00);
        return priceList;
    }

    private static Map<Integer,DayTurn> getDayTurnMap()
    {
        Map<Integer,DayTurn> dayTurnHashMap = new HashMap<>();
        dayTurnHashMap.put(1,getDayTurn(1,8,0,0,5));
        dayTurnHashMap.put(2,getDayTurn(2,14,0,0,5));
        return dayTurnHashMap;
    }

    private static DayTurn getDayTurn(int turn, int startHour, int startMin, int startSec, int noOfHours)
    {
        LocalTime depart = LocalTime.of(startHour, startMin, startSec);
        LocalTime arrival = LocalTime.of(startHour + noOfHours, startMin, startSec);
        DayTurn dayTurn = new DayTurn(turn,arrival,depart);
        return  dayTurn;
    }
}
