package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import ModeloGenerico.BaseDAO;
import Vista.Panel_Proveedor;

public class ProveedorDAO implements BaseDAO<Mproveedor> {
	Panel_Proveedor vistaProveedor = new Panel_Proveedor();

	@Override
	public Mproveedor buscarPorID(int id) {
		String sql = "SELECT Pid,NombreP,NumeroTel FROM TablaProveedores WHERE Pid=?";
		Mproveedor proveedorEncontrado = null;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					proveedorEncontrado = new Mproveedor(rs.getInt("Pid"), rs.getString("NombreP"),
							rs.getString("NumeroTel"));
				}

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaProveedor, "Error al buscar el Proveedor: " + e.toString());
		}
		return proveedorEncontrado;
	}

	@Override
	public List<Mproveedor> ObtenerTodo() {
		List<Mproveedor> clientes = new ArrayList<>();

		String sql = "SELECT Pid,NombreP,NumeroTel FROM TablaProveedores";

		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {

				clientes.add(new Mproveedor(rs.getInt("Pid"), rs.getString("NombreP"), rs.getString("NumeroTel")));

			}

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(vistaProveedor, e.toString());

		}
		return clientes;
	}

	@Override
	public boolean agregar(Mproveedor entidad) {
		String sql = "INSERT INTO TablaProveedores(NombreP,NumeroTel) VALUES (?,?)";
		boolean Exito = false;

		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getNumTel());

			if (ps.executeUpdate() > 0) {

				try (ResultSet idGenerado = ps.getGeneratedKeys()) {
					if (idGenerado.next()) {
						entidad.setid(idGenerado.getInt(1));
						Exito = true;
					}

				}

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaProveedor, "Proveedor no agregado: " + e.toString());

		}

		return Exito;
	}

	@Override
	public boolean modificar(Mproveedor entidad) {

		String sql = "UPDATE TablaProveedores SET NombreP=?, NumeroTel=? WHERE Pid=?";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getNumTel());
			ps.setInt(3, entidad.getid());
			if (ps.executeUpdate() > 0) {
				exito = true;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaProveedor, "Error al modificar " + e.toString());
		}

		return exito;
	}

	@Override
	public boolean borrar(int id) {

		String sql = "DELETE FROM TablaProveedores WHERE Pid=? ";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			if (ps.executeUpdate() > 0) {
				exito = true;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaProveedor, "Error al borrar " + e.toString());

		}

		return exito;
	}

}
