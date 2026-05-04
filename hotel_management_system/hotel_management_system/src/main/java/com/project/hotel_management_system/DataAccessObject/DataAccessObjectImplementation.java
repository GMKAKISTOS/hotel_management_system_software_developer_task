package com.project.hotel_management_system.DataAccessObject;

import com.project.hotel_management_system.Classes.Customers;
import com.project.hotel_management_system.Classes.Reservations;
import com.project.hotel_management_system.Classes.Rooms;
import com.project.hotel_management_system.Classes.RoomType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class DataAccessObjectImplementation implements ApplicationDataAccessObject
{
    private EntityManager entitymanager;

    @Autowired
    public DataAccessObjectImplementation(EntityManager entitymanager)
    {
        this.entitymanager = entitymanager;
    }

    @Override
    @Transactional
    public void addRoomType(RoomType roomtype)
    {
        entitymanager.persist(roomtype);
    }

    @Override
    public List<Rooms> findAllRooms()
    {
        TypedQuery<Rooms> typedquery = entitymanager
                .createQuery("FROM Rooms", Rooms.class);

        return typedquery.getResultList();
    }

    @Override
    public RoomType findRoomTypeById(int id) {
        return entitymanager.find(RoomType.class, id);
    }

    @Override
    @Transactional
    public void addRoom(Rooms rooms)
    {
        entitymanager.persist(rooms);
    }

    @Override
    public List<RoomType> findAllRoomTypes()
    {
        TypedQuery<RoomType> typedquery = entitymanager
                .createQuery("FROM RoomType", RoomType.class);

        return typedquery.getResultList();
    }

    @Override
    @Transactional
    public void addCustomer(Customers customers){entitymanager.persist(customers);}

    @Override
    public List<Customers> findAllCustomers()
    {
        TypedQuery<Customers> typedquery = entitymanager
                .createQuery("FROM Customers", Customers.class);

        return typedquery.getResultList();
    }

    @Override
    public Rooms findRoomsById(int id) {
        return entitymanager.find(Rooms.class, id);
    }

    @Override
    public Customers findCustomersById(int id) {
        return entitymanager.find(Customers.class, id);
    }

    @Override
    @Transactional
    public void addReservations(Reservations reservations)
    {
        entitymanager.persist(reservations);
    }

    @Override
    public List<Reservations> findAllReservations()
    {
        TypedQuery<Reservations> typedquery = entitymanager
                .createQuery("FROM Reservations", Reservations.class);

        return typedquery.getResultList();
    }

    @Override
    public Reservations findReservationsById(int id) {
        return entitymanager.find(Reservations.class, id);
    }

    @Override
    @Transactional
    public void deleteReservationsById(int id)
    {
        Reservations reservations = entitymanager.find(Reservations.class, id);

        if(reservations != null) {
            entitymanager.remove(reservations);
        }
    }

    @Override
    public List<Rooms> findAllFreeRooms()
    {
        TypedQuery<Rooms> typedquery = entitymanager
                .createQuery(
                        "SELECT r FROM Rooms r WHERE NOT EXISTS (SELECT NULL FROM Reservations res WHERE r.roomId = res.roomId)",
                        Rooms.class
                );

        return typedquery.getResultList();
    }

    @Override
    public List<Reservations> findAllReservationsBasedOnCustomers(int id)
    {
        TypedQuery<Reservations> typedquery = entitymanager
                .createQuery(
                        "SELECT r FROM Reservations r WHERE r.customerId = :customerId1",
                        Reservations.class
                );

        typedquery.setParameter("customerId1", id);

        return typedquery.getResultList();
    }

    @Override
    public List<Reservations> findAllReservationsBasedOnDates(Date date1, Date date2)
    {
        TypedQuery<Reservations> typedquery = entitymanager
                .createQuery(
                        "SELECT r FROM Reservations r WHERE r.startDate >= :startDate1 AND r.endDate <= :endDate1",
                        Reservations.class
                );

        typedquery.setParameter("startDate1", date1);
        typedquery.setParameter("endDate1", date2);

        return typedquery.getResultList();
    }

}
