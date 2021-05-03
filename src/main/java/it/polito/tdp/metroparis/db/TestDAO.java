package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.metroparis.model.Fermata;

public class TestDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = DBConnect.getConnection();
			connection.close();
			System.out.println("Connection Test PASSED");
			
			MetroDAO dao = new MetroDAO() ;
			
			//System.out.println(dao.getAllFermate()) ;
			//System.out.println(dao.getAllLinee()) ;
			
			
			List<Fermata> f = new ArrayList<>(dao.getAllFermate());
			System.out.println(dao.getAllConnessioni(f));
			
			
			
		} catch (Exception e) {
			throw new RuntimeException("Test FAILED", e);
		}
	}

}
