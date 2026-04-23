package org.geoazul.view.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static modules.LoadInitParameter.*;
 
public class ExecuteShellComand {
	
	
 
	public void mainTest() {
 
		ExecuteShellComand obj = new ExecuteShellComand();
 
		String domainName = "google.com";
 
		//in mac oxs
		String command = "ping -c 3 " + domainName;
 
		//in windows
		//String command = "ping -n 3 " + domainName;
 
		String output = obj.executeCommand(command);
 
 
	}
	
	
	
	
	
	
	public String preprocessGnss(String fileName, String glebaId, String fileDestination) {
		 
		ExecuteShellComand obj = new ExecuteShellComand();
 
		String antena = "TRM57970.00";
		String observer = "GNSS Observer";
		String vendor = "Trimble";
		String header = "GeoEJB 1.4";
		String customer = "GeoEJB.com.br";
		String rn = "TRM57970.00";
		String rt = "TRIMBLE 5700";
		String an = "1";
		String week = "";
		String os = "M";
 
		//in mac oxs
		String command = "teqc  -O.at \"TRM57970.00\" -O.o \"GNSS Observer\"  -O.ag \"Trimble\" -O.r \"GeoEJB 3.0\" -O.o \"Tischer\"  -O.rn \"4906165501\" -O.rt \"TRIMBLE 5700\" -O.an \"1\" -O.s \"M\"    +meta  " 
				+ fileName ; 
		//in windows
		
		
		//String command = "ping -n 3 " + domainName;
 
		String output = obj.executeCommand(command);
		
		
		
		
	     // temp file
	     File outFile = new File(fileDestination);
	      
	

	     // output         
	     FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     PrintWriter out = new PrintWriter(fos);
    
	       out.println(output);
	   
	     
	     
	     
	    out.flush();
	    out.close();

	    
	    
	   
		
		
		
 
		return output;
 
	}
 
	private String executeCommand(String command) {
 
		StringBuffer output = new StringBuffer();
 
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));
 
                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return output.toString();
 
	}
 
}
