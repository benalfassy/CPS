package clientServerCPS;

// These consts are used in the server side to determine what method should be called.

// For each const there should be one method in the ServerRequestHandler with the exact name.

public class ClientServerConsts
{
    public static final int PORT = 5555;
    
    public static final String RegisterFullMembership = "RegisterFullMembership";
    
    public static final String AddCustomerIfNotExists = "AddCustomerIfNotExists";
    
    public static final String RegisterPartialMembership = "RegisterPartialMembership";
    
    public static final String GetFullMembership = "GetFullMembership";
    
    public static final String GetCustomer = "GetCustomer";
    
    public static final String GetPartialMembership = "GetPartialMembership";
    
    public static final String GetReservation = "GetReservation";
    
    public static final String Reservation = "Reservation";
    
    public static final String GetEmployee = "GetEmployee";
    
    public static final String AddParkinglot = "AddParkinglot";
    
    public static final String GetAllParkinglots = "GetAllParkinglots";
    
    public static final String AddComplaint = "AddComplaint";
    
    public static final String GetAllActiveComplaints = "GetAllActiveComplaints";
    
    public static final String CloseComplaint = "CloseComplaint";
}
