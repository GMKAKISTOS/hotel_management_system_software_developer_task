package com.project.hotel_management_system.Classes;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "room_type")
public class RoomType
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomTypeId;
    @Column(name = "name_type_room")
    private String nameTypeRoom;
    @Column(name = "room_cost")
    private BigDecimal roomCost;

    public RoomType(){}

    public RoomType(String nameTypeRoom)
    {
        this.nameTypeRoom = nameTypeRoom;
    }

    public String getNameTypeRoom() {
        return nameTypeRoom;
    }

    public void setNameTypeRoom(String nameTypeRoom) {
        this.nameTypeRoom = nameTypeRoom;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public BigDecimal getRoomCost() {
        return roomCost;
    }

    public void setRoomCost(BigDecimal roomCost) {
        this.roomCost = roomCost;
    }

    @Override
    public String toString() {
        return "roomTypeId : " + getRoomTypeId() + "\n" +
                "nameTypeRoom : " + getNameTypeRoom() + "\n" +
                "roomCost : " + getRoomCost() + "\n";
    }
}
