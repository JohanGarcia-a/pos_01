package modelo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modeloGenerico.BaseDAO;

public class AlmacenProductosDAO implements BaseDAO<MalmacenProductos> {

	@Override
	public MalmacenProductos buscarPorID(int id) {
		// --- MODIFICACIÓN AQUÍ: Añadido p.StockMinimo a la consulta ---
		String sql = "SELECT p.*, tp.NombreP, tc.Nombre AS NombreCategoria " + "FROM TablaAlmacen_Productos p "
				+ "INNER JOIN TablaProveedores tp ON p.ProveedorID = tp.Pid "
				+ "INNER JOIN TablaCategorias tc ON p.CategoriaID = tc.Cid " + "WHERE p.Pid=?";

		MalmacenProductos productoEncontrado = null;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// --- MODIFICACIÓN AQUÍ: Se pasa el nuevo campo al constructor ---
					productoEncontrado = new MalmacenProductos(rs.getInt("Pid"), rs.getString("Nombre"),
							rs.getString("Descripcion"), rs.getDouble("Precio"), rs.getString("Codigo"),
							rs.getInt("Cantidad"), rs.getString("Ruta_Imagen"), rs.getInt("CategoriaID"),
							rs.getString("NombreCategoria"), rs.getInt("ProveedorID"), rs.getString("NombreP"),
							rs.getInt("StockMinimo"));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al buscar el producto: " + e.toString());
		}
		return productoEncontrado;
	}

	public MalmacenProductos buscarPorCodigo(String codigo) {
		// --- MODIFICACIÓN AQUÍ: Añadido p.StockMinimo a la consulta ---
		String sql = "SELECT p.*, tp.NombreP, tc.Nombre AS NombreCategoria " + "FROM TablaAlmacen_Productos p "
				+ "INNER JOIN TablaProveedores tp ON p.ProveedorID = tp.Pid "
				+ "INNER JOIN TablaCategorias tc ON p.CategoriaID = tc.Cid " + "WHERE p.Codigo=?";
		MalmacenProductos productoEncontrado = null;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, codigo);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// --- MODIFICACIÓN AQUÍ: Se pasa el nuevo campo al constructor ---
					productoEncontrado = new MalmacenProductos(rs.getInt("Pid"), rs.getString("Nombre"),
							rs.getString("Descripcion"), rs.getDouble("Precio"), rs.getString("Codigo"),
							rs.getInt("Cantidad"), rs.getString("Ruta_Imagen"), rs.getInt("CategoriaID"),
							rs.getString("NombreCategoria"), rs.getInt("ProveedorID"), rs.getString("NombreP"),
							rs.getInt("StockMinimo"));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al buscar por código: " + e.toString());
		}
		return productoEncontrado;
	}

	public List<MalmacenProductos> buscarPorNombre(String nombre) {
		// --- MODIFICACIÓN AQUÍ: Añadido p.StockMinimo a la consulta ---
		String sql = "SELECT p.*, tp.NombreP, tc.Nombre AS NombreCategoria " + "FROM TablaAlmacen_Productos p "
				+ "INNER JOIN TablaProveedores tp ON p.ProveedorID = tp.Pid "
				+ "INNER JOIN TablaCategorias tc ON p.CategoriaID = tc.Cid " + "WHERE p.Nombre LIKE ?";

		List<MalmacenProductos> productosEncontrados = new ArrayList<>();

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, "%" + nombre + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					// --- MODIFICACIÓN AQUÍ: Se pasa el nuevo campo al constructor ---
					MalmacenProductos producto = new MalmacenProductos(rs.getInt("Pid"), rs.getString("Nombre"),
							rs.getString("Descripcion"), rs.getDouble("Precio"), rs.getString("Codigo"),
							rs.getInt("Cantidad"), rs.getString("Ruta_Imagen"), rs.getInt("CategoriaID"),
							rs.getString("NombreCategoria"), rs.getInt("ProveedorID"), rs.getString("NombreP"),
							rs.getInt("StockMinimo"));
					productosEncontrados.add(producto);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al buscar por nombre: " + e.toString());
		}
		return productosEncontrados;
	}

	@Override
	public List<MalmacenProductos> ObtenerTodo() {
		List<MalmacenProductos> productos = new ArrayList<>();
		// --- MODIFICACIÓN AQUÍ: Añadido p.StockMinimo a la consulta ---
		String sql = "SELECT p.*, tp.NombreP, tc.Nombre AS NombreCategoria " + "FROM TablaAlmacen_Productos p "
				+ "INNER JOIN TablaProveedores tp ON p.ProveedorID = tp.Pid "
				+ "INNER JOIN TablaCategorias tc ON p.CategoriaID = tc.Cid";

		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				// --- MODIFICACIÓN AQUÍ: Se pasa el nuevo campo al constructor ---
				productos.add(
						new MalmacenProductos(rs.getInt("Pid"), rs.getString("Nombre"), rs.getString("Descripcion"),
								rs.getDouble("Precio"), rs.getString("Codigo"), rs.getInt("Cantidad"),
								rs.getString("Ruta_Imagen"), rs.getInt("CategoriaID"), rs.getString("NombreCategoria"),
								rs.getInt("ProveedorID"), rs.getString("NombreP"), rs.getInt("StockMinimo")));
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener productos: " + e.toString());
		}
		return productos;
	}

	@Override
	public boolean agregar(MalmacenProductos entidad) {
		// --- MODIFICACIÓN AQUÍ: Añadida la columna StockMinimo y un '?' ---
		String sql = "INSERT INTO TablaAlmacen_Productos(Nombre,Descripcion,Precio,Codigo,Cantidad,Ruta_Imagen,ProveedorID,CategoriaID,StockMinimo) VALUES (?,?,?,?,?,?,?,?,?)";
		boolean exito = false;
		try (Connection con = conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setBigDecimal(3, BigDecimal.valueOf(entidad.getPrecio()));
			ps.setString(4, entidad.getCodigo());
			ps.setInt(5, entidad.getCantidad());
			ps.setString(6, entidad.getRuta());
			ps.setInt(7, entidad.getProveedorId());
			ps.setInt(8, entidad.getCategoriaId());
			ps.setInt(9, entidad.getStockMinimo()); // --- MODIFICACIÓN AQUÍ: Se añade el nuevo parámetro ---

			if (ps.executeUpdate() > 0) {
				exito = true;
			}
		} catch (SQLException e) {
			System.err.println("Producto no agregado: " + e.toString());
		}
		return exito;
	}

	@Override
	public boolean modificar(MalmacenProductos entidad) {
		// --- MODIFICACIÓN AQUÍ: Añadido StockMinimo = ? ---
		String sql = "UPDATE TablaAlmacen_Productos SET Nombre=?, Descripcion=?, Precio=?, Codigo=?, Cantidad=?, Ruta_Imagen=?, ProveedorID=?, CategoriaID=?, StockMinimo=? WHERE Pid=?";
		boolean exito = false;

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setDouble(3, entidad.getPrecio());
			ps.setString(4, entidad.getCodigo());
			ps.setInt(5, entidad.getCantidad());
			ps.setString(6, entidad.getRuta());
			ps.setInt(7, entidad.getProveedorId());
			ps.setInt(8, entidad.getCategoriaId());
			ps.setInt(9, entidad.getStockMinimo()); // --- MODIFICACIÓN AQUÍ: Se añade el nuevo parámetro ---
			ps.setInt(10, entidad.getid()); // --- MODIFICACIÓN AQUÍ: El ID ahora es el parámetro 10 ---

			if (ps.executeUpdate() > 0) {
				exito = true;
			}
		} catch (SQLException e) {
			System.err.println("Error al modificar producto: " + e.toString());
		}
		return exito;
	}

	@Override
	public boolean borrar(int id) {
		String sql = "DELETE FROM TablaAlmacen_Productos WHERE Pid=?";
		boolean exito = false;
		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			if (ps.executeUpdate() > 0) {
				exito = true;
			}
		} catch (SQLException e) {
			System.err.println("Error al borrar producto: " + e.toString());
		}
		return exito;
	}
}