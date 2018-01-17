package cps.entities;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ParkingSpot.
 */
public class ParkingSpot implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private int width_X;
    
    private int height_Y;
    
    private int depth_Z;
    
    /**
     * Instantiates a new parking spot.
     *
     * @param width_X
     *            the width X
     * @param height_Y
     *            the height Y
     * @param depth_Z
     *            the depth Z
     */
    public ParkingSpot(int width_X, int height_Y, int depth_Z)
    {
	this.width_X = width_X;
	this.height_Y = height_Y;
	this.depth_Z = depth_Z;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	return width_X + "," + height_Y + "," + depth_Z;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
	if (!(obj instanceof ParkingSpot)) return false;
	
	ParkingSpot parkingSpot = (ParkingSpot) obj;
	
	return (this.width_X == parkingSpot.getWidth_X() && this.height_Y == parkingSpot.getHeight_Y()
		&& this.depth_Z == parkingSpot.getDepth_Z());
    }
    
    /**
     * Parses the parking spot object to a string.
     *
     * @param str
     *            the str
     * @return the parking spot
     */
    static public ParkingSpot Parse(String str)
    {
	String[] dimentions = str.split(",");
	
	int x = Integer.parseInt(dimentions[0]);
	int y = Integer.parseInt(dimentions[1]);
	int z = Integer.parseInt(dimentions[2]);
	
	return new ParkingSpot(x, y, z);
    }
    
    /**
     * Gets the depth Z.
     *
     * @return the depth Z
     */
    public int getDepth_Z()
    {
	return depth_Z;
    }
    
    /**
     * Gets the height Y.
     *
     * @return the height Y
     */
    public int getHeight_Y()
    {
	return height_Y;
    }
    
    /**
     * Gets the width X.
     *
     * @return the width X
     */
    public int getWidth_X()
    {
	return width_X;
    }
}
