package org.reservationservice.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.enactor.commonlibrary.exception.InvalidRequestException;
import org.enactor.commonlibrary.model.*;
import org.enactor.commonlibrary.util.Constant;
import org.enactor.commonlibrary.util.CustomObjectMapper;
import org.enactor.commonlibrary.util.DateUtil;
import org.enactor.commonlibrary.util.JsonMappingUtil;
import org.reservationservice.model.BusReservationRequestBody;
import org.reservationservice.model.ReservationResult;
import org.reservationservice.service.ReservationService;
import org.reservationservice.util.ResServiceConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusReservationServlet extends HttpServlet
{
    public static final Logger logger = ReservationService.logger;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            BusReservationCriteria busReservationCriteria = createBusReservationCriteriaFromReqBody(request);
            //call service method to reserve tickets
            ApiResponse<ReservationResult> apiResponse = ReservationService.reserveTickets(busReservationCriteria);
            String jsonResponse = CustomObjectMapper.getObjectMapper().writeValueAsString(apiResponse);
            response.setStatus(apiResponse.getStatus());
            response.setContentType(Constant.JSON_CONTENT_TYPE);
            response.getWriter().write(jsonResponse);

        } catch (InvalidRequestException e)
        {
            Logger.getLogger(ResServiceConstant.LOG_CODE).log(Level.SEVERE, () -> "Mandatory arguments were given in invalid formats" + e.getMessage());
            ApiResponse<ReservationResult> apiResponse = new ApiResponse<>(400,Constant.FAILED,new ApiError(400,e.getMessage()));
            String jsonResponse = JsonMappingUtil.converObjectToString(apiResponse);
            response.setContentType(Constant.JSON_CONTENT_TYPE);
            response.getWriter().write(jsonResponse);
        }catch (Exception e)
        {
            logger.log(Level.SEVERE, () -> "Error occurred while reserving the seats of the bus" + e);
            ApiResponse<ReservationResult> apiResponse = new ApiResponse<>(500,Constant.FAILED,new ApiError(500,e.getMessage()));
            String jsonResponse = JsonMappingUtil.converObjectToString(apiResponse);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(Constant.JSON_CONTENT_TYPE);
            response.getWriter().write(jsonResponse);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try
        {
            BusReservationCriteria busReservationCriteria = createBusReservationCriteriaFromReq(req);
            //call the service method to check availability
            ApiResponse<SeatAvailability> apiResponse = ReservationService.getSeatAvailability(busReservationCriteria);
            response.setStatus(apiResponse.getStatus());
            response.setContentType(Constant.JSON_CONTENT_TYPE);
            ObjectMapper objectMapper = CustomObjectMapper.getObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);
        }
        catch (InvalidRequestException e)
        {
            Logger.getLogger(ResServiceConstant.LOG_CODE).log(Level.SEVERE, () -> "Mandatory arguments were given in invalid formats" + e.getMessage());
            ApiResponse<SeatAvailability> apiResponse = new ApiResponse<>(400,Constant.FAILED,new ApiError(400,e.getMessage()));
            String jsonResponse = JsonMappingUtil.converObjectToString(apiResponse);
            response.setContentType(Constant.JSON_CONTENT_TYPE);
            response.getWriter().write(jsonResponse);
        }catch (Exception e)
        {
            logger.log(Level.SEVERE, () -> "Error occurred while getting availability  of the bus" + e);
            ApiResponse<SeatAvailability> apiResponse = new ApiResponse<>(500,Constant.FAILED,new ApiError(500,e.getMessage()));
            String jsonResponse = JsonMappingUtil.converObjectToString(apiResponse);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(Constant.JSON_CONTENT_TYPE);
            response.getWriter().write(jsonResponse);
        }
    }

    /**
     * This method is used to map the request parameters and create the availability criteria
     *
     * @param req http request
     * @return the availability criteria
     */
    private BusReservationCriteria createBusReservationCriteriaFromReq(HttpServletRequest req)
    {
        try
        {
            String origin = req.getParameter(Constant.ORIGIN);
            String destination = req.getParameter(Constant.DESTINATION);
            int paxCount = Integer.parseInt(req.getParameter(Constant.PAX_COUNT));
            String vehicleType = req.getParameter(Constant.VEHICLE_TYPE);
            String vehicleNum = req.getParameter(Constant.VEHICLE_NUM);
            int turn = Integer.parseInt(req.getParameter(Constant.TURN_OF_THE_DAY));
            String date = req.getParameter(Constant.TRAVELLING_DATE);

            BusReservationCriteria criteria = new BusReservationCriteria();
            criteria.setOrigin(origin);
            criteria.setDestination(destination);
            criteria.setPaxCount(paxCount);
            criteria.setVehicleType(VehicleType.getVehicleType(vehicleType));
            criteria.setVehicleNum(vehicleNum);
            criteria.setTurn(turn);
            criteria.setDate(DateUtil.convertStringToDate(date));
            return criteria;
        }
        catch (Exception e)
        {
            throw new InvalidRequestException("Mandatory arguments were given in invalid formats");
        }
    }

    private BusReservationCriteria createBusReservationCriteriaFromReqBody(HttpServletRequest req) throws IOException
    {

        // Read the request body
        StringBuilder requestBody = new StringBuilder();
        try (InputStream inputStream = req.getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }
        }

        try
        {
            // Parse the request body using Jackson ObjectMapper
            BusReservationRequestBody requestBodyObject = CustomObjectMapper.getObjectMapper().readValue(requestBody.toString(), BusReservationRequestBody.class);

            // Map the values to BusReservationCriteria
            BusReservationCriteria criteria = new BusReservationCriteria();
            criteria.setOrigin(requestBodyObject.getOrigin());
            criteria.setDestination(requestBodyObject.getDestination());
            criteria.setPaxCount(requestBodyObject.getPaxCount());
            criteria.setVehicleType(VehicleType.getVehicleType(requestBodyObject.getVehicleType()));
            criteria.setVehicleNum(requestBodyObject.getVehicleNum());
            criteria.setTurn(requestBodyObject.getTurn());
            criteria.setDate(requestBodyObject.getDate());

            return criteria;
        }
        catch (Exception e)
        {
            throw new InvalidRequestException("Mandatory arguments were given in invalid formats");
        }
    }
}
