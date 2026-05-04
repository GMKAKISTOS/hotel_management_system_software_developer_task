package com.project.hotel_management_system;

import com.project.hotel_management_system.Classes.Customers;
import com.project.hotel_management_system.Classes.Reservations;
import com.project.hotel_management_system.Classes.Rooms;
import com.project.hotel_management_system.Classes.RoomType;
import com.project.hotel_management_system.DataAccessObject.ApplicationDataAccessObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;

import java.awt.geom.RectangularShape;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class HotelManagementSystemApplication {

    static Scanner input = new Scanner(System.in);
    static boolean checker = false;

	public static void main(String[] args) {
        SpringApplication.run(HotelManagementSystemApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandlinerunner(ApplicationDataAccessObject applicationdataaccessobject)
    {
        return runner -> {

            boolean repetation = false;
            String choice = null;
            System.out.println("\nHOTEL MANAGEMENT SYSTEM.");

            do
            {
                System.out.println("\nhotel management system.");
                System.out.print("\nPress 1 to add roomtype.");
                System.out.print("\nPress 2 to show all roomtypes.");
                System.out.print("\nPress 3 to add room.");
                System.out.print("\nPress 4 to show all rooms.");
                System.out.print("\nPress 5 to add customer.");
                System.out.print("\nPress 6 to show all customers.");
                System.out.print("\nPress 7 to add reservation.");
                System.out.print("\nPress 8 to show all reservations.");
                System.out.print("\nPress 9 to cancel a reservation.");
                System.out.print("\nPress 10 to find all free rooms.");
                System.out.print("\nPress 11 to find reservations based on customers.");
                System.out.print("\nPress 12 to find reservations based on dates.");
                System.out.println("\nPress 13 to exit from application.");

                System.out.print("\nPlease give me a choice between 1 - 13 : ");
                choice = input.nextLine();

                switch (choice) {

                    case "1":
                        createRoomType(applicationdataaccessobject);
                        break;
                    case "2":
                        showRoomTypes(applicationdataaccessobject);
                        break;
                    case "3":
                        createRoom(applicationdataaccessobject);
                        break;
                    case "4":
                        showRooms(applicationdataaccessobject);
                        break;
                    case "5":
                        createCustomer(applicationdataaccessobject);
                        break;
                    case "6":
                        showCustomers(applicationdataaccessobject);
                        break;
                    case "7":
                        createReservations(applicationdataaccessobject);
                        break;
                    case "8":
                        showReservations(applicationdataaccessobject);
                        break;
                    case "9":
                        deleteReservation(applicationdataaccessobject);
                        break;
                    case "10":
                        findFreeRooms(applicationdataaccessobject);
                        break;
                    case "11":
                        findReservationsBasedOnCustomers(applicationdataaccessobject);
                        break;
                    case "12":
                        findReservationsBasedOnDates(applicationdataaccessobject);
                        break;
                    case "13":
                        System.out.println("\nExit from application.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("\nPlease give me a choice between 1 - 13.");
                        break;
                }
            }
            while (!repetation);
        };
    }

    private void findReservationsBasedOnDates(ApplicationDataAccessObject applicationdataaccessobject)
    {
        Date date1 = null, date2 = null;
        String startdate = null, enddate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        checker = false;

        while(!checker)

        {

            System.out.print("\nPlease give me the startdate : ");
            startdate = input.nextLine();

            System.out.print("\nPlease give me the enddate : ");
            enddate = input.nextLine();

            try
            {

                date1 = formatter.parse(startdate);
                date2 = formatter.parse(enddate);

                if(date1.compareTo(date2) <= 0)
                {
                    List<Reservations> reservations = applicationdataaccessobject.findAllReservationsBasedOnDates(date1, date2);

                    if (reservations.isEmpty()) {
                        checker = false;
                        System.out.println("\nReservations with startdate : " + date1 + " and enddate "
                                + date2 + " doesn't exist!");
                        return;
                    }
                    else
                    {
                        checker = true;
                        System.out.println("\nReservations with startdate : " + date1 + " and enddate : " + date2 + " -> " +
                                reservations.toString());
                    }

                }
                else
                {
                    checker = false;
                    System.out.println("\nstartdate should be less than/equal to endate.");
                }
            } catch (ParseException exception)
            {
                checker = false;
                System.out.println("\nInvalid date format. Please use format -> (yyyy-MM-dd).");
            }
        }
    }

    private void findReservationsBasedOnCustomers(ApplicationDataAccessObject applicationdataaccessobject)
    {
        String customeridstr = null;
        int customerid;

        checker = false;

        while (!checker)
        {

            System.out.print("\nPlease give me the customerid : ");
            customeridstr = input.nextLine();

            try {

                customerid = Integer.parseInt(customeridstr);

                List<Reservations> reservations = applicationdataaccessobject.findAllReservationsBasedOnCustomers(customerid);

                if(customerid <= 0)
                {
                    return;
                }

                if (reservations.isEmpty() && customerid > 0) {
                    checker = false;
                    System.out.println("\nCustomer with id : " + customerid + " doesn't exist!");

                }
                else
                {
                    checker = true;
                    System.out.println("\nReservations of customer with id : " + customerid + " -> " +
                            reservations.toString());
                }
            } catch (NumberFormatException exception) {
                checker = false;
                System.out.println("\nIt's not a number! -> " + exception.getMessage());
            }
        }
    }

    private void findFreeRooms(ApplicationDataAccessObject applicationdataaccessobject)
    {
        List<Rooms> freerooms = applicationdataaccessobject.findAllFreeRooms();

        System.out.println("\nFree rooms : " + freerooms.toString());
    }

    private void deleteReservation(ApplicationDataAccessObject applicationdataaccessobject)
    {
        checker = false;
        String reservationidstr = null;
        int reservationid;

        while (!checker) {

            System.out.print("\nPlease give me the reservationid : ");
            reservationidstr = input.nextLine();

            try {
                reservationid = Integer.parseInt(reservationidstr);

                Reservations reservations = applicationdataaccessobject.findReservationsById(reservationid);

                if(reservationid <= 0)
                {
                    return;
                }

                if (reservations == null && reservationid > 0) {
                    checker = false;
                    System.out.println("\nReservation with id : " + reservationid + " doesn't exist!");

                }
                else {
                    checker = true;
                    applicationdataaccessobject.deleteReservationsById(reservationid);
                    System.out.println("\nReservation was cancelled successfully!");
                }
            } catch (NumberFormatException exception) {
                System.out.println("\nIt's not a number! -> " + exception.getMessage());
            }
        }
    }

    private void showReservations(ApplicationDataAccessObject applicationdataaccessobject) {
        List<Reservations> reservations = applicationdataaccessobject.findAllReservations();

        System.out.println("\nReservations : " + reservations.toString());
    }

    private void createReservations(ApplicationDataAccessObject applicationdataaccessobject)
    {
        String startdate = null, enddate = null, roomidstr = null, customeridstr = null;
        int roomid, customerid;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;

        checker = false;

        while(!checker)

        {

            System.out.print("\nPlease give me the startdate : ");
            startdate = input.nextLine();

            System.out.print("\nPlease give me the enddate : ");
            enddate = input.nextLine();

            try
            {

                date1 = formatter.parse(startdate);
                date2 = formatter.parse(enddate);

                if(date1.compareTo(date2) <= 0)
                {
                    checker = true;
                }
                else
                {
                    System.out.println("\nstartdate should be less than/equal to endate.");
                }
            }
            catch (ParseException exception)
            {
                System.out.println("\nInvalid date format. Please use format -> (yyyy-MM-dd).");
            }
        }

        checker = false;

        while (!checker)
        {
            System.out.print("\nPlease give me the roomid : ");
            roomidstr = input.nextLine();

            System.out.print("\nPlease give me the customerid : ");
            customeridstr = input.nextLine();

            try {
                roomid = Integer.parseInt(roomidstr);

                customerid = Integer.parseInt(customeridstr);

                Rooms rooms = applicationdataaccessobject.findRoomsById(roomid);

                Customers customers = applicationdataaccessobject.findCustomersById(customerid);

                if(roomid <= 0 || customerid <= 0)
                {
                    return;
                }

                if ((rooms == null && roomid > 0) || (customers == null && customerid > 0)) {
                    checker = false;
                    System.out.println("\nRoom with id : " + roomid + " doesn't exist! OR" +
                            " Customer with id : " + customerid + " doesn't exist!");

                }
                else
                {
                    checker = true;
                    Reservations reservations = new Reservations(date1, date2, roomid, customerid);
                    applicationdataaccessobject.addReservations(reservations);
                    System.out.println("\nReservation was added successfully!");
                }
            } catch (NumberFormatException exception) {
                checker = false;
                System.out.println("\nIt's not a number! -> " + exception.getMessage());
            }
            catch (DataIntegrityViolationException exception) {
                checker = false;
                System.out.println("\nInvalid errors! -> " + exception.getMessage());
            }
        }

    }

    private void showCustomers(ApplicationDataAccessObject applicationdataaccessobject) {
        List<Customers> customers = applicationdataaccessobject.findAllCustomers();

        System.out.println("\nCustomers : " + customers.toString());
    }

    private void createCustomer(ApplicationDataAccessObject applicationdataaccessobject)
    {
        String firstname = null, lastname = null, sexstr = null, datebirth = null, cardnumberid = null, phone = null, email = null;
        char sex = ' ';
        Date utildate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        checker = false;

        while (!checker) {

                System.out.print("\nPlease give me your firstname : ");
                firstname = input.nextLine();
                System.out.print("\nPlease give me your lastname : ");
                lastname = input.nextLine();

                if (!(firstname.length() <= 50) || !(lastname.length() <= 50) || firstname.matches(".*\\d.*") || lastname.matches(".*\\d.*") || !firstname.matches("^[A-Za-z]+$") || !lastname.matches("^[A-Za-z]+$")) {

                    checker = false;
                    System.out.println("\nYou passed max length of firstname and lastname OR firstname and lastname must have only characters.");


                } else {

                    checker = true;

                }
            }

        checker = false;

        while (!checker) {

            System.out.print("\nPlease give me your sex : ");
            sexstr = input.nextLine();

            if(sexstr.isBlank())
            {
                checker = true;
                sex = 'N';
                continue;
            }

            sex = sexstr.toUpperCase().charAt(0);

            if ((!(sex == 'M') && !(sex == 'F') && !(sex == 'N')) || sexstr.length() != 1) {

                checker = false;
                System.out.println("\nPlease give me a valid sex (M / F / N)");

            }
            else
            {
                checker = true;
            }
        }

        checker = false;

        while (!checker) {

            System.out.print("\nPlease give me your birthdate : ");
            datebirth = input.nextLine();

            if(datebirth.isBlank() || datebirth.equalsIgnoreCase("NULL"))
            {
                checker = true;
            }
            else {

                try {

                    checker = true;
                    utildate = formatter.parse(datebirth);

                } catch (ParseException exception) {

                    checker = false;
                    System.out.println("\nInvalid date format. Please use format -> (yyyy-MM-dd)." + exception.getMessage());

                }
            }

        }

        checker = false;

        while (!checker)
        {

                System.out.print("\nPlease give me your cardnumberid : ");
                cardnumberid = input.nextLine();

                if (!cardnumberid.matches("^[A-Z]{2}[0-9]{6}$")) {
                    checker = false;
                    System.out.println("\nYou gave me an invalid cardnumberid!");
                } else {
                    checker = true;
                }
        }

        checker = false;

        while (!checker)
        {

            System.out.print("\nPlease give me your phone : ");
            phone = input.nextLine();

            if (!phone.matches("^69[0-9]{8}$")) {
                checker = false;
                System.out.println("\nYou gave me an invalid phone!");
            }
            else{
                checker = true;
            }

        }

        checker = false;

        while (!checker)
        {
                System.out.print("\nPlease give me your email : ");
                email = input.nextLine();

                if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    checker = false;
                    System.out.println("\nYou gave me an invalid email!");
                } else {
                    checker = true;
                }
        }

         checker = false;

        try {
            checker = true;
            Customers customers = new Customers(firstname, lastname, sex, utildate, cardnumberid, phone, email);
            applicationdataaccessobject.addCustomer(customers);
            System.out.println("\nCustomer was added successfully!");
        }
        catch (DataIntegrityViolationException exception) {
            checker = false;
            System.out.println("\nInvalid errors! -> " + exception.getMessage());

        }

    }

    private void showRooms(ApplicationDataAccessObject applicationdataaccessobject) {

        List<Rooms> rooms = applicationdataaccessobject.findAllRooms();

        System.out.println("\nRooms : " + rooms.toString());
    }

    private void createRoom(ApplicationDataAccessObject applicationdataaccessobject) {
        checker = false;
        String roomnumber = null, roomntypeid = null;

        while (!checker) {

            System.out.print("\nPlease give me the roomnumber : ");
            roomnumber = input.nextLine();

            if (!roomnumber.matches("^(100|1\\d{2}|200)$")) {
                checker = false;
                System.out.println("\nRoom number must be between 100 and 200!");
            } else {

                System.out.print("\nPlease give me the roomtypeid : ");
                roomntypeid = input.nextLine();

                try {
                    int roomnumber1 = Integer.parseInt(roomnumber);

                    int roomtypeid1 = Integer.parseInt(roomntypeid);

                    RoomType roomtype = applicationdataaccessobject.findRoomTypeById(roomtypeid1);

                    if (roomtypeid1 <= 0) {
                        return;
                    }
                    if (roomtype == null && roomtypeid1 > 0) {
                        checker = false;
                        System.out.println("\nRoomtype with id : " + roomtypeid1 + " doesn't exist!");
                    } else {
                        checker = true;
                        Rooms room = new Rooms(roomnumber1, roomtypeid1);
                        applicationdataaccessobject.addRoom(room);
                        System.out.println("\nRoom was added successfully!");
                    }
                } catch (NumberFormatException exception) {
                    checker = false;
                    System.out.println("\nIt's not a number! -> " + exception.getMessage());

                } catch (DataIntegrityViolationException exception) {
                    checker = false;
                    System.out.println("\nRoom already exists! -> " + exception.getMessage());
                }
            }
        }
    }

    private void showRoomTypes(ApplicationDataAccessObject applicationdataaccessobject) {
        List<RoomType> roomTypes = applicationdataaccessobject.findAllRoomTypes();

        System.out.println("\nRoomtypes : " + roomTypes.toString());

    }

    private void createRoomType(ApplicationDataAccessObject applicationdataaccessobject) {

        checker = false;
        String roomtypespecificnames = "^\\s*(single|double|twin|triple|superior double|family room|-1)\\s*$";
        String roomtypename = null;

        while(!checker)

        {

            System.out.print("\nPlease give me the roomtype : ");
            roomtypename = input.nextLine();

            if(roomtypename.equals("-1"))
            {
                return;
            }

            if(!roomtypename.matches(roomtypespecificnames))

            {

                checker = false;
                System.out.println("\nYou passed max length of roomtypename OR roomtypename must have only characters OR roomtypename must have only lowercase characters" +
                        " OR must specific which room you want.");

            }
            else
            {

                try
                {

                    checker = true;
                    RoomType roomtype = new RoomType(roomtypename);
                    applicationdataaccessobject.addRoomType(roomtype);
                    System.out.println("\nRoom type was added successfully!");

                }
                catch(DataIntegrityViolationException exception)
                {
                    checker = false;
                    System.out.println("\nRoom type already exists! -> " + exception.getMessage());
                }
            }
        }
    }

}
