package com.project.hotel_management_system.Classes;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "customers")
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "sex")
    private char sex;
    @Column(name = "date_birth")
    private Date dateBirth;
    @Column(name = "card_number_id")
    private String cardNumberId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;

    public Customers(){}

    public Customers(String firstName, String lastName, char sex, Date dateBirth, String cardNumberId, String phone, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.dateBirth = dateBirth;
        this.cardNumberId = cardNumberId;
        this.phone = phone;
        this.email = email;

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getCardNumberId() {
        return cardNumberId;
    }

    public void setCardNumberId(String cardNumberId) {
        this.cardNumberId = cardNumberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "\ncustomerId : " + getCustomerId() + "\n" +
                "firstName : " + getFirstName() + "\n" +
                "lastName : " + getLastName() + "\n" +
                "sex : " + getSex() + "\n" +
                "dateBirth : " + getDateBirth() +  "\n" +
                "cardNumberId : " + getCardNumberId() + "\n" +
                "phone : " + getPhone() + "\n" +
                "email : " + getEmail() + "\n";
    }
}
