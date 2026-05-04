package com.project.hotel_management_system.Classes;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservations
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "total_room_cost")
    private BigDecimal totalRoomCost;
    @Column(name = "create_date")
    @CreationTimestamp
    private Timestamp createDate;
    @Column(name = "room_id")
    private int roomId;
    @Column(name = "customer_id")
    private int customerId;

    public Reservations(){}

    public Reservations(Date start_date, Date end_date, int room_id, int customer_id)
    {
        this.startDate = start_date;
        this.endDate = end_date;
        this.roomId = room_id;
        this.customerId = customer_id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalRoomCost() {
        return totalRoomCost;
    }

    public void setTotalRoomCost(BigDecimal totalRoomCost) {
        this.totalRoomCost = totalRoomCost;
    }

    public Timestamp  getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp  createDate) {
        this.createDate = createDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "reservationId : " + getReservationId() + "\n" +
                "startDate : " + getStartDate()  + "\n" +
                "endDate : " + getEndDate() + "\n" +
                "totalRoomCost : " + getTotalRoomCost() + "\n" +
                "createDate : " + getCreateDate() + "\n" +
                "roomId : " + getRoomId() + "\n" +
                "customerId : " + getCustomerId() + "\n";
    }
}
