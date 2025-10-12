package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Vista.Panel_Cliente;

public class conexion {

	public static Connection getConexion() {
		Panel_Cliente vista = new Panel_Cliente();
		String url = "jdbc:sqlserver://JOAHAN:1433;" + "database=MyPos;" + "user=sa;" + "password=3312;"
				+ "trustServerCertificate=true";

		try {

			Connection con = DriverManager.getConnection(url);

			return con;

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(vista, e.toString());

			return null;
			//
			
		}
	}
}