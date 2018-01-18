package cps.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Enumeration;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import cps.entities.StatusReport;
import cps.entities.enums.ParkingSpotCondition;

// TODO: Auto-generated Javadoc
/**
 * The Class Pdf_Builder.
 * Used for building a pdf according to a status report.
 */
public class Pdf_Builder {
	
	/** The path. */
	private String path;
	
	/**
	 * Instantiates a new pdf builder.
	 *
	 * @param path the path
	 */
	public Pdf_Builder(String path)
	{
		this.path=path;
	}
	
	/**
	 * Builds the PDF.
	 *
	 * @param statusReport the status report
	 * @return true, if successful
	 */
	public boolean build(StatusReport statusReport)
	{
		try
		{
			int size = statusReport.getTable().size();
			
			int pageWidth = 580;
			
			int pageHeight = 760;
			
			Enumeration<String> parkingLotNames = statusReport.getTable().keys();
			
			PDDocument doc = new PDDocument();
			
			for(int i = 0; i < size; i++)
			{
				String name = parkingLotNames.nextElement();
				
				int parkingLotTotal = statusReport.getTable().get(name).size();
				
				int parkingLotwidth = parkingLotTotal / 9;
				
				BufferedImage bufferedImage = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_ARGB);
				
				Graphics graphics = bufferedImage.createGraphics();
				
				graphics.setFont(new Font("Arial", Font.BOLD, 10));
				
				graphics.setColor(Color.green);
			      
				graphics.drawString("green ", 30, 700);
			      
				graphics.setColor(Color.blue);
			      
				graphics.drawString("blue ", 30, 710);
			      
				graphics.setColor(Color.red);
			      
				graphics.drawString("red ", 30, 720);
			      
				graphics.setColor(Color.black);
			      
				graphics.drawString("is a free parking spot", 62, 700);
				
				graphics.drawString("is an occupied parking spot", 56, 710);
				
				graphics.drawString("is a disabled parking spot", 51, 720);
				
				if(i == 0)
				{
					graphics.setFont(new Font("Arial", Font.BOLD, 24));
				
					graphics.setColor(Color.black);
				
					graphics.drawString("Status Report", 220, 25);
				}
				
				graphics.setFont(new Font("Arial", Font.BOLD, 16));
				
				graphics.setColor(Color.black);
			
				graphics.drawString(name, 30, 60);
				
				int x_floor = 30;
				
				int y_floor = 90;
				
				int x_parking_start = 120;
				
				int y_parking_start = 220;
				
				for(int j = 0; j < 3; j++)
				{
					int x_parking = x_parking_start;
					
					int y_parking = y_parking_start + (j*200);
					
					int floor = j + 1;
					
					String floor_name = "Floor " + floor + ":";
					
					graphics.setFont(new Font("Arial", Font.BOLD, 12));
					
					graphics.setColor(Color.black);
				
					graphics.drawString(floor_name, x_floor, y_floor);
					
					for(int l = 0; l < 3; l++)
					{
						for(int k = 0; k < parkingLotwidth; k++)
						{
							graphics.setColor(Color.black);
						
							graphics.drawRect(x_parking, y_parking, 30, 30);
							
							int index = k + (l*parkingLotwidth) + (j*parkingLotwidth*3);
							
							ParkingSpotCondition spotCond = statusReport.getTable().get(name).get(index);
							
							if(spotCond.equals(ParkingSpotCondition.Free))
							{
								graphics.setColor(Color.green);
							}
							else if(spotCond.equals(ParkingSpotCondition.Occupied))
							{
								graphics.setColor(Color.blue);
							}
							else if(spotCond.equals(ParkingSpotCondition.Disabled))
							{
								graphics.setColor(Color.red);
							}
							
							graphics.fillRect(x_parking + 1, y_parking + 1, 29, 29);
							
							x_parking += 50;
						}
						
						x_parking -= (50 * (parkingLotwidth));
						
						y_parking -= 50;
					}
					
					y_floor+=200;
				}
				String path_png = path + "\\status_report_" + name + ".PNG";
				
				File file = new File(path_png);
				
				ImageIO.write(bufferedImage, "PNG", file);
				
				PDImageXObject pdImage = PDImageXObject.createFromFile(path_png, doc);
				
				PDPage blankPage = new PDPage();
				
				doc.addPage( blankPage );
				
				PDPage page = doc.getPage(i);
				
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				
		    	contentStream.drawImage(pdImage, 15, 15);
		    	
		    	contentStream.close();
		    	
		    	file.delete();
			}
			
			String path_pdf = path + "\\status_report_" + LocalDate.now().toString() + ".pdf";

			doc.save(path_pdf);
		
			doc.close();
			
			return true;
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
			return false;
		}
	}
}
