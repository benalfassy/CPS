package cps.utilities;

import java.time.LocalDate;
import java.time.LocalTime;

// TODO: Auto-generated Javadoc
/**
 * The Class CPS_Tracer.
 * This class is used to print the server logs.
 */
public class CPS_Tracer
{
    
    /**
     * Trace information.
     *
     * @param message the message
     */
    public static void TraceInformation(String message)
    {
	System.out.println();
	System.out.println("=========================================================");
	System.out.println(LocalDate.now() + "  " + LocalTime.now());
	System.out.println("Information:");
	System.out.println(message);
	System.out.println("=========================================================");
	System.out.println();
    }
    
    /**
     * Trace error.
     *
     * @param message the message
     */
    public static void TraceError(String message)
    {
	System.out.println();
	System.out.println("=========================================================");
	System.out.println(LocalDate.now() + "  " + LocalTime.now());
	System.out.println("Error:");
	System.out.println(message);
	System.out.println("=========================================================");
	System.out.println();
    }
    
    /**
     * Trace error with exception.
     *
     * @param message the message
     * @param e the e
     */
    public static void TraceError(String message, Throwable e)
    {
	System.out.println();
	System.out.println("=========================================================");
	System.out.println(LocalDate.now() + "  " + LocalTime.now());
	System.out.println("Error:");
	System.out.println(message);
	System.out.println("Exception: " + e);
	System.out.println("=========================================================");
	System.out.println();
    }
}
