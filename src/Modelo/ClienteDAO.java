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
import Vista.Panel_Cliente;

public class ClienteDAO implements BaseDAO<Mcliente> {
	Panel_Cliente vistaClientes = new Panel_Cliente();

	@Override
	public Mcliente buscarPorID(int id) {
		String sql = "SELECT Cid,NombreC,NumeroTel FROM TablaClientes WHERE Cid=?";
		Mcliente clienteEncontrado = null;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					clienteEncontrado = new Mcliente(rs.getInt("Cid"), rs.getString("NombreC"),
							rs.getString("NumeroTel"));
				}

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaClientes, "Error al buscar el cliente: " + e.toString());
		}
		return clienteEncontrado;
	}

	@Override
	public List<Mcliente> ObtenerTodo() {
		List<Mcliente> clientes = new ArrayList<>();

		String sql = "SELECT Cid,NombreC,NumeroTel FROM TablaClientes";

		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {

				clientes.add(new Mcliente(rs.getInt("Cid"), rs.getString("NombreC"), rs.getString("NumeroTel")));

			}

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(vistaClientes, e.toString());

		}
		return clientes;
	}

	@Override
	public boolean agregar(Mcliente entidad) {
		String sql = "INSERT INTO TablaClientes(NombreC,NumeroTel) VALUES (?,?)";
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
			JOptionPane.showMessageDialog(vistaClientes, "Cliente no agregado: " + e.toString());

		}

		return Exito;
	}

	@Override
	public boolean modificar(Mcliente entidad) {

		String sql = "UPDATE TablaClientes SET NombreC=?, NumeroTel=? WHERE Cid=?";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getNumTel());
			ps.setInt(3, entidad.getid());

			if (ps.executeUpdate() > 0) {
				exito = true;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaClientes, "Error al modificar " + e.toString());
		}

		return exito;
	}

	@Override
	public boolean borrar(int id) {

		String sql = "DELETE FROM TablaClientes WHERE Cid=? ";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			if (ps.executeUpdate() > 0) {
				exito = true;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(vistaClientes, "Error al borrar " + e.toString());

		}

		return exito;
	}

}
