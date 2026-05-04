package com.project.hotel_management_system.Classes;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    @Column(name = "room_number")
    private int roomNumber;
    @Column(name = "room_type_id")
    private int roomTypeId;

    public Rooms(){}

    public Rooms(int roomNumber, int roomTypeId)
    {
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public String toString() {
        return "roomId : " + getRoomId() + "\n" +
                "roomNumber : " + getRoomNumber() + "\n" +
                "roomTypeId : " + getRoomTypeId() + "\n";
    }
}
