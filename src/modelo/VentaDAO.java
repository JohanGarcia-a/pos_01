package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentaDAO { // No necesita implementar BaseDAO si su lógica es muy distinta

	public boolean agregar(MVenta venta) {
		String sqlVenta = "INSERT INTO TablaVentas(ClienteID, EmpleadoID, FechaVenta, Total) VALUES (?, ?, ?, ?)";
		String sqlDetalle = "INSERT INTO TablaVentaDetalle(VentaID, ProductoID, Cantidad, PrecioUnitario, Subtotal) VALUES (?, ?, ?, ?, ?)";
		String sqlUpdateStock = "UPDATE TablaAlmacen_Productos SET Cantidad = Cantidad - ? WHERE Pid = ?";

		Connection con = null;
		boolean exito = false;

		try {
			con = conexion.getConexion();
			// ¡Iniciamos la transacción!
			con.setAutoCommit(false);

			// 1. Insertar la cabecera de la venta
			try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
				psVenta.setInt(1, venta.getClienteId());
				psVenta.setInt(2, venta.getEmpleadoId());
				psVenta.setTimestamp(3, new java.sql.Timestamp(venta.getFecha().getTime()));
				psVenta.setDouble(4, venta.getTotal());
				psVenta.executeUpdate();

				// 2. Obtener el ID de la venta que acabamos de crear
				try (ResultSet generatedKeys = psVenta.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int ventaIdGenerada = generatedKeys.getInt(1);

						// 3. Insertar cada detalle y actualizar el stock
						for (MVentaDetalle detalle : venta.getDetalles()) {
							// Insertar detalle
							try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
								psDetalle.setInt(1, ventaIdGenerada);
								psDetalle.setInt(2, detalle.getProductoId());
								psDetalle.setInt(3, detalle.getCantidad());
								psDetalle.setDouble(4, detalle.getPrecioUnitario());
								psDetalle.setDouble(5, detalle.getSubtotal());
								psDetalle.executeUpdate();
							}

							// Actualizar stock del producto
							try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdateStock)) {
								psUpdate.setInt(1, detalle.getCantidad());
								psUpdate.setInt(2, detalle.getProductoId());
								psUpdate.executeUpdate();
							}
						}
					}
				}
			}

			// 4. Si todo salió bien, confirmamos la transacción
			con.commit();
			exito = true;

		} catch (SQLException e) {
			System.err.println("Error al registrar la venta, haciendo rollback: " + e.getMessage());
			try {
				if (con != null) {
					// 5. Si algo falló, deshacemos todo
					con.rollback();
				}
			} catch (SQLException ex) {
				System.err.println("Error al hacer rollback: " + ex.getMessage());
			}
		} finally {
			try {
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
		return exito;
	}

}