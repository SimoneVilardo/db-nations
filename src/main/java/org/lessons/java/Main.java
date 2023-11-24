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
			  
			  final String sql2 = " SELECT countries.name "
				  			   + " FROM countries "
				  			   + " WHERE countries.country_id = ? "
				  			   + " ; ";	
			  
			  final String sql3 = " SELECT languages.language "
				  			   + " FROM countries "
				  			   + " JOIN country_languages "
				  			   + " ON countries.country_id = country_languages.country_id "
				  			   + " JOIN languages "
				  			   + " ON country_languages.language_id = languages.language_id "
				  			   + " WHERE countries.country_id = ? "
				  			   + " ; ";	
			  
			  final String sql4 = "SELECT country_stats.year, country_stats.population, country_stats.gdp "
			                   + " FROM countries "
			                   + " JOIN country_stats "
			                   + " ON countries.country_id = country_stats.country_id "
			                   + " WHERE countries.country_id = ? "
			                   + " ORDER BY country_stats.year DESC "
			                   + " LIMIT 1 "
			                   + " ; ";
			  
			  try(PreparedStatement ps = con.prepareStatement(sql)){
				  
				  ps.setString(1, "%" + strNameCountry + "%");
					  
				  try(ResultSet rs = ps.executeQuery()){
				    	
					  while(rs.next()) {
					    		
						  int id = rs.getInt(1);
						  String countryName = rs.getString(2);
						  String regionsName = rs.getString(3);
						  String continentsName = rs.getString(4);
						    		
						  System.out.println("[" + id + "] " + countryName + " - " + regionsName + " - " + continentsName);
						  
					  }
				  }
			  }
			  
			  System.out.print("\nInserisci l'id del paese richiesto: ");
			  String strIdPaese = in.nextLine();
			  int intIdPaese = Integer.valueOf(strIdPaese);
			  
			  try(PreparedStatement ps = con.prepareStatement(sql2)){
				  
				  ps.setInt(1, intIdPaese);
					  
				  try(ResultSet rs = ps.executeQuery()){
					  
						 while (rs.next()) {
							
							  String nomePaese = rs.getString(1);
							  
							  System.out.print("\nDettagli del Paese: " + nomePaese);
							  
						}
						  
				  }
				  
			  }
			  
			  
			  try(PreparedStatement ps = con.prepareStatement(sql3)){
				  
				  ps.setInt(1, intIdPaese);
					  
				  try(ResultSet rs = ps.executeQuery()){
					  
					  System.out.print("\nLingue parlate: ");
				    	
					  while(rs.next()) {
					    		
						  String language = rs.getString(1);
						    		
						  if (!rs.isLast()) {

	                          System.out.print(language + ", ");

	                      } else {

	                          System.out.print(language);
	                      }
						  
					  }
				  }
			  }
			  
			  
			  try(PreparedStatement ps = con.prepareStatement(sql4)){
				  
				  ps.setInt(1, intIdPaese);
					  
				  try(ResultSet rs = ps.executeQuery()){
					  
					  System.out.print("\nStatistiche Paese: \n");
				    	
					  while(rs.next()) {
					    		
						  int anno = rs.getInt(1);
						  int popolazione = rs.getInt(2);
						  String gdp = rs.getString(3);
						    		
						  System.out.println("Anno: " + anno + "\n" 
								  			+ "Popolazione: " + popolazione + "\n" 
								  			+ "GDP: " + gdp + "\n");
						  
					  }
				  }
			  }
			  
			  
			  
			} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
		
	}

}
