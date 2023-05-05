package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service

public class HotelManagementService {
    @Autowired
    HotelManagementRepository hotelManagementRepository;
    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName() == null || hotel == null){
            return  "FAILURE";
        }
        if(hotelManagementRepository.getHotelDb().containsKey(hotel.getHotelName())){
            return  "FAILURE";
        }

        hotelManagementRepository.addHotel(hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        hotelManagementRepository.addUser(user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        HashMap<String, Hotel> hotelDb = hotelManagementRepository.getHotelDb();
        int maxFacilities = 0;
        String resultHotelName = "";
        for (String hotelName : hotelDb.keySet()){
            Hotel hotel = hotelDb.get(hotelName);

            if(hotel.getFacilities().size() > maxFacilities){
                maxFacilities = hotel.getFacilities().size();
                resultHotelName = hotelName;
            }
            else if(hotel.getFacilities().size() == maxFacilities){
                if(hotelName.compareTo(resultHotelName) < 0){
                    resultHotelName = hotelName;
                }
            }
        }
        return resultHotelName;
    }

    public int bookARoom(Booking booking) {
        hotelManagementRepository.addBooking(booking);

        Hotel hotel = hotelManagementRepository.getHotelDb().get(booking.getHotelName());
        if(hotel.getAvailableRooms() < booking.getNoOfRooms()){
            return -1;
        }

        int totalAmount = booking.getNoOfRooms() * hotel.getPricePerNight();

        booking.setAmountToBePaid(totalAmount);
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());

        hotelManagementRepository.addBookingCount(booking.getBookingAadharCard());

        hotelManagementRepository.addHotel(hotel);

        return totalAmount;
    }

    public int getBookings(Integer aadharCard) {
        return hotelManagementRepository.getNoOfBookingByUser().get(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelManagementRepository.getHotelDb().get(hotelName);
        List<Facility> hotelFacilities = hotel.getFacilities();

        for(int i=0;i<newFacilities.size();i++){
            Facility facility = newFacilities.get(i);
            if(!hotelFacilities.contains(facility)){
                hotelFacilities.add(facility);
            }
        }

        hotel.setFacilities(hotelFacilities);
        hotelManagementRepository.addHotel(hotel);
        return hotel;
    }
}
