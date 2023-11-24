package org.lessons.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

	private static final String url = "jdbc:mysql://localhost:3306/db-nations";
	private static final String user = "root";
	private static final String pws = "";

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		System.out.print("Filtra i paesi per nome: ");
		String strNameCountry = in.nextLine();
		
		try (Connection con = DriverManager.getConnection(url, user, pws)) {  
			  
			  final String sql = " SELECT countries.country_id AS 'countryId', countries.name AS 'countryName', regions.name AS 'regionsName', continents.name AS 'continentsName' "
				  			   + " FROM countries "
				  			   + " JOIN regions "
				  			   + " ON countries.region_id = regions.region_id "
				  			   + " JOIN continents "
				  			   + " ON regions.continent_id = continents.continent_id "
				  			   + " WHERE countries.name LIKE ? "
				  			   + " ORDER BY countries.name "
				  			   + " ; ";		  
			  
			  try(PreparedStatement ps = con.prepareStatement(sql)){
				  
				  ps.setString(1, "%" + strNameCountry + "%");
				  
			    try(ResultSet rs = ps.executeQuery()){
			    	
			    	while(rs.next()) {
			    		
			    		int id = rs.getInt(1);
			    		String countryName = rs.getString(2);
			    		String regionsName = rs.getString(3);
			    		String continentsName = rs.getString(4);
			    		
			    		System.out.println("[" + id + "] " + countryName + " - " + regionsName + " - " + continentsName + "\n");;
			    	}
			    }
			  }
			} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
		
	}

}
