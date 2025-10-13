package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import modeloGenerico.BaseDAO;
import vista.Panel_Empleado;

public class EmpleadoDAO implements BaseDAO<Mempleado> {
	Panel_Empleado vistaEmpleado = new Panel_Empleado();

	@Override
	public Mempleado buscarPorID(int id) {
		String sql = "SELECT Eid,NombreE,NumeroTel,Rol FROM TablaEmpleados WHERE Eid=?";
		Mempleado empleadoEncontrado = null;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					empleadoEncontrado = new Mempleado(rs.getInt("Eid"), rs.getString("NombreE"),
							rs.getString("NumeroTel"), rs.getString("Rol"));
				}

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaEmpleado, "Error al buscar el empleado: " + e.toString());
		}
		return empleadoEncontrado;
	}

	@Override
	public List<Mempleado> ObtenerTodo() {
		List<Mempleado> empleados = new ArrayList<>();

		String sql = "SELECT Eid,NombreE,NumeroTel,Rol FROM TablaEmpleados";

		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {

				empleados.add(new Mempleado(rs.getInt("Eid"), rs.getString("NombreE"), rs.getString("NumeroTel"),
						rs.getString("Rol")));

			}

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(vistaEmpleado, e.toString());

		}
		return empleados;
	}

	@Override
	public boolean agregar(Mempleado entidad) {
		String sql = "INSERT INTO TablaEmpleados(NombreE,NumeroTel,Rol) VALUES (?,?,?)";
		boolean Exito = false;

		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getNumTel());
			ps.setString(3, entidad.getRol());

			if (ps.executeUpdate() > 0) {

				try (ResultSet idGenerado = ps.getGeneratedKeys()) {
					if (idGenerado.next()) {
						entidad.setid(idGenerado.getInt(1));
						Exito = true;
					}

				}

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaEmpleado, "Empleado no agregado: " + e.toString());

		}

		return Exito;
	}

	@Override
	public boolean modificar(Mempleado entidad) {

		String sql = "UPDATE TablaEmpleados SET NombreE=?, NumeroTel=?, Rol=? WHERE Eid=?";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getNumTel());
			ps.setString(3, entidad.getRol());
			ps.setInt(4, entidad.getid());

			if (ps.executeUpdate() > 0) {
				exito = true;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaEmpleado, "Error al modificar " + e.toString());
		}

		return exito;
	}

	@Override
	public boolean borrar(int id) {

		String sql = "DELETE FROM TablaEmpleados WHERE Eid=? ";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			if (ps.executeUpdate() > 0) {
				exito = true;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaEmpleado, "Error al borrar " + e.toString());

		}

		return exito;
	}

}
