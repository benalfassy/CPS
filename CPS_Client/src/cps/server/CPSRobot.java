package cps.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;

import cps.entities.AddRealTimeParkingRequest;
import cps.entities.ParkingSpot;
import cps.entities.Parkinglot;
import cps.utilities.CPS_Tracer;

// TODO: Auto-generated Javadoc
/**
 * The Class CPSRobot. Responsible of the parking spots.
 */
public class CPSRobot
{
    
    private Connection connection;
    
    private ServerRequestHandler myServerRequestHandler;
    
    @SuppressWarnings("unused")
    private class WeightedRealTimeParking
    {
	
	LocalDateTime start;
	
	LocalDateTime end;
	
	ParkingSpot parkingSpot;
	
	String carNumber;
	
	long weight;
	
	WeightedRealTimeParking(LocalDateTime start, LocalDateTime end, ParkingSpot parkingSpot, String carNumber)
	{
	    this.start = start;
	    this.end = end;
	    this.parkingSpot = parkingSpot;
	    this.carNumber = carNumber;
	    
	    this.weight = end.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	}
    };
    
    /**
     * Instantiates a new CPS robot.
     *
     * @param connection
     *            the connection
     * @param serverRequestHandler
     *            the server request handler
     */
    CPSRobot(Connection connection, ServerRequestHandler serverRequestHandler)
    {
	this.connection = connection;
	this.myServerRequestHandler = serverRequestHandler;
    }
    
    /**
     * Organize parking spots after add car.
     *
     * @param request
     *            the request
     * @throws SQLException
     *             the SQL exception
     */
    void OrginizeParkingSpotsAfterAddCar(AddRealTimeParkingRequest request) throws SQLException
    {
	CPS_Tracer.TraceInformation("Trying to orginize spots for request: \n" + request);
	
	Parkinglot parkinglot = myServerRequestHandler.GetParkinglot(request.getParkinglot()).GetResponseObject();
	
	CPS_Tracer.TraceInformation("Trying to get all the parking spots in the parking lot");
	
	PreparedStatement preparedStatement = connection.prepareStatement(
		"SELECT * FROM RealTimeParking WHERE parkingLot = ?", ResultSet.TYPE_SCROLL_SENSITIVE,
		ResultSet.CONCUR_UPDATABLE);
	
	preparedStatement.setString(1, request.getParkinglot());
	
	ResultSet resultSet = preparedStatement.executeQuery();
	
	ArrayList<WeightedRealTimeParking> parkings = new ArrayList<>();
	
	while (resultSet.next())
	{
	    parkings.add(new WeightedRealTimeParking(LocalDateTime.parse(resultSet.getString(3)),
		    LocalDateTime.parse(resultSet.getString(4)), ParkingSpot.Parse(resultSet.getString(2)),
		    resultSet.getString(5)));
	}
	
	CPS_Tracer.TraceInformation("Trying to get all the disabled parking spots");
	
	preparedStatement = connection.prepareStatement("SELECT * FROM DisabledParkingSpots WHERE parkinglotName = ?",
		ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	
	preparedStatement.setString(1, request.getParkinglot());
	
	resultSet = preparedStatement.executeQuery();
	
	ArrayList<ParkingSpot> disabledParkings = new ArrayList<>();
	
	while (resultSet.next())
	{
	    disabledParkings.add(ParkingSpot.Parse(resultSet.getString(3)));
	}
	
	CPS_Tracer.TraceInformation("Starting the sort algoritem");
	
	Collections.sort(parkings, (p1, p2) -> p1.weight - p2.weight > 0 ? 1 : -1);
	
	int iterator = 0;
	
	for (int i = 1; i <= parkinglot.getDepth(); i++)
	{
	    for (int j = 1; j <= parkinglot.getHeight(); j++)
	    {
		for (int k = 1; k <= parkinglot.getWidth(); k++)
		{
		    if (!disabledParkings.contains(new ParkingSpot(k, j, i)) && iterator < parkings.size())
		    {
			WeightedRealTimeParking oldSpot = parkings.get(iterator);
			
			oldSpot.parkingSpot = new ParkingSpot(k, j, i);
			
			iterator++;
		    }
		}
	    }
	}
	
	CPS_Tracer.TraceInformation("Updating the new spots");
	
	preparedStatement = connection
		.prepareStatement("Update RealTimeParking set parkingSpot = ? where carNumber = ?");
	
	for (WeightedRealTimeParking parking : parkings)
	{
	    preparedStatement.setString(1, parking.parkingSpot.toString());
	    preparedStatement.setString(2, parking.carNumber);
	    
	    preparedStatement.executeUpdate();
	}
	
	CPS_Tracer.TraceInformation("Finished update the spots");
    }
}
