package com.project.hotel_management_system.DataAccessObject;

import com.project.hotel_management_system.Classes.Customers;
import com.project.hotel_management_system.Classes.Reservations;
import com.project.hotel_management_system.Classes.Rooms;
import com.project.hotel_management_system.Classes.RoomType;

import java.util.Date;
import java.util.List;

public interface ApplicationDataAccessObject {

    void addRoomType(RoomType roomtype);

    List<RoomType> findAllRoomTypes();

    RoomType findRoomTypeById(int id);

    void addRoom(Rooms room);

    List<Rooms> findAllRooms();

    void addCustomer(Customers customer);

    List<Customers> findAllCustomers();

    Customers findCustomersById(int id);

    Rooms findRoomsById(int id);

    void addReservations(Reservations reservations);

    List<Reservations> findAllReservations();

    Reservations findReservationsById(int id);

    void deleteReservationsById(int id);

    List<Rooms> findAllFreeRooms();

    List<Reservations> findAllReservationsBasedOnCustomers(int id);

    List<Reservations> findAllReservationsBasedOnDates(Date date1, Date date2);
}
