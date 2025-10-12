package Controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import Modelo.AlmacenProductosDAO;
import Modelo.ClienteDAO;
import Modelo.EmpleadoDAO;
import Modelo.MVenta;
import Modelo.MVentaDetalle;
import Modelo.MalmacenProductos;
import Modelo.Mcliente;
import Modelo.Mempleado;
import Modelo.VentaDAO;
import Vista.Panel_Venta;

public class ControladorVenta {
	private VentaDAO ventaDAO;
	private AlmacenProductosDAO productoDAO;
	private ClienteDAO clienteDAO;
	private EmpleadoDAO empleadoDAO;

	private Panel_Venta vistaVenta;
	private MalmacenProductos productoSeleccionado;
	private MVenta ventaActual;

	public ControladorVenta(VentaDAO vDAO, AlmacenProductosDAO pDAO, ClienteDAO cDAO, EmpleadoDAO eDAO,
			Panel_Venta vista) {
		this.ventaDAO = vDAO;
		this.productoDAO = pDAO;
		this.clienteDAO = cDAO;
		this.empleadoDAO = eDAO;
		this.vistaVenta = vista;
		this.productoSeleccionado = null;
		this.ventaActual = new MVenta();

		// Cargar datos iniciales en la vista
		cargarClientesEnComboBox();
		cargarEmpleadosEnComboBox();

		// Añadir listeners a los botones de la vista
		this.vistaVenta.addBuscarProductoListener(e -> buscarProducto());
		this.vistaVenta.addAgregarCarritoListener(e -> agregarAlCarrito());
		this.vistaVenta.addFinalizarVentaListener(e -> finalizarVenta());
		this.vistaVenta.addQuitarDelCarritoListener(e -> quitarProductodelCarrito());
	}

	// En ControladorVenta.java

	private void buscarProducto() {
		String terminoBusqueda = vistaVenta.getCodigoProductoBuscado().trim();
		if (terminoBusqueda.isEmpty()) {
			vistaVenta.mostrarError("El campo de búsqueda no puede estar vacío.");
			return;
		}

		List<MalmacenProductos> productosEncontrados = new ArrayList<>();
		MalmacenProductos productoUnico = null;

		// --- NUEVA LÓGICA DE BÚSQUEDA SECUENCIAL ---

		// 1. Intenta buscar por ID si el término es un número
		try {
			int id = Integer.parseInt(terminoBusqueda);
			productoUnico = productoDAO.buscarPorID(id);
			if (productoUnico != null) {
				productosEncontrados.add(productoUnico);
			}
		} catch (NumberFormatException e) {
			// No es un número, así que no hacemos nada y continuamos.
		}

		// 2. Si no se encontró por ID, intenta buscar por Código
		if (productosEncontrados.isEmpty()) {
			productoUnico = productoDAO.buscarPorCodigo(terminoBusqueda);
			if (productoUnico != null) {
				productosEncontrados.add(productoUnico);
			}
		}

		// 3. Si sigue sin encontrarse, busca por Nombre (que puede devolver una lista)
		if (productosEncontrados.isEmpty()) {
			productosEncontrados = productoDAO.buscarPorNombre(terminoBusqueda);
		}

		// --- EL RESTO DEL CÓDIGO PERMANECE IGUAL ---
		// Esta parte ya maneja perfectamente si la lista tiene 0, 1 o muchos
		// resultados.

		if (productosEncontrados.isEmpty()) {
			vistaVenta.mostrarError("No se encontraron productos con ese ID, código o nombre.");
			this.productoSeleccionado = null;
		} else if (productosEncontrados.size() == 1) {
			MalmacenProductos producto = productosEncontrados.get(0);
			this.productoSeleccionado = producto;
			vistaVenta.mostrarDatosProducto(producto);
		} else {
			MalmacenProductos[] opciones = productosEncontrados.toArray(new MalmacenProductos[0]);
			MalmacenProductos productoElegido = (MalmacenProductos) JOptionPane.showInputDialog(vistaVenta,
					"Se encontraron varios productos, por favor elija uno:", "Seleccionar Producto",
					JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

			if (productoElegido != null) {
				this.productoSeleccionado = productoElegido;
				vistaVenta.mostrarDatosProducto(productoElegido);
			} else {
				this.productoSeleccionado = null;
			}
		}

	}

	private void agregarAlCarrito() {
		if (productoSeleccionado == null) {
			vistaVenta.mostrarError("Primero debe buscar y encontrar un producto válido.");
			return;
		}
		int cantidadAVender;
		try {
			cantidadAVender = vistaVenta.getCantidadAVender();
			if (cantidadAVender <= 0) {
				vistaVenta.mostrarError("La cantidad debe ser mayor que cero.");
				return;
			}
		} catch (NumberFormatException e) {
			vistaVenta.mostrarError("Por favor, ingrese un número válido en la cantidad.");
			return;
		}
		if (cantidadAVender > productoSeleccionado.getCantidad()) {
			vistaVenta.mostrarError("No hay suficiente stock. Disponible: " + productoSeleccionado.getCantidad());
			return;
		}

		MVentaDetalle detalle = new MVentaDetalle(productoSeleccionado.getid(), productoSeleccionado.getNombre(),
				productoSeleccionado.getDescripcion(), cantidadAVender, productoSeleccionado.getPrecio());

		ventaActual.agregarDetalle(detalle);
		vistaVenta.agregarDetalleAlCarrito(detalle);
		vistaVenta.actualizarTotalVenta(ventaActual.getTotal());
		this.productoSeleccionado = null;
	}

	private void quitarProductodelCarrito() {
		// Usamos el método que ya tienes en tu vista para obtener la fila
		int filaSeleccionada = vistaVenta.getFilaSeleccionadaCarrito();

		if (filaSeleccionada == -1) {
			// Tu validación, ¡perfecta!
			JOptionPane.showMessageDialog(vistaVenta, "Debe seleccionar un producto a eliminar de la lista.");
			return; // Salimos del método si no hay nada seleccionado
		}

		// 1. Pedir confirmación al usuario (Opcional pero recomendado)
		int confirmacion = JOptionPane.showConfirmDialog(vistaVenta,
				"¿Seguro que desea quitar este producto del carrito?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			// 2. Quitar el producto del modelo (de la lista en ventaActual)
			ventaActual.quitarDetalle(filaSeleccionada); // Necesitarás este método en tu clase MVenta

			// 3. Quitar la fila de la tabla en la vista
			vistaVenta.quitarDetalleDelCarrito(filaSeleccionada);

			// 4. Actualizar el total en la vista
			vistaVenta.actualizarTotalVenta(ventaActual.getTotal());
		}
	}

	private void finalizarVenta() {
		// --- VALIDACIONES PREVIAS ---
		if (ventaActual.getDetalles().isEmpty()) {
			vistaVenta.mostrarError("No se puede finalizar una venta sin productos en el carrito.");
			return;
		}

		int clienteId = vistaVenta.getClienteSeleccionadoId();
		int empleadoID = vistaVenta.getEmpleadoSeleccionadoId();

		if (clienteId == -1) {
			vistaVenta.mostrarError("Debe seleccionar un cliente para la venta.");
			return;
		}

		if (empleadoID == -1) {
			vistaVenta.mostrarError("Debe seleccionar un Empleado para la venta.");
			return;
		}

		ventaActual.setClienteId(clienteId);
		ventaActual.setEmpleadoId(empleadoID);
		// ventaActual.setEmpleadoId(this.idEmpleadoActual);

		ventaActual.setTotal(ventaActual.getTotal()); // Calcula y asigna el total final
		ventaActual.setFecha(new Date()); // Asigna la fecha y hora actual

		// 2. Llamar a ventaDAO.agregar(ventaActual)
		boolean exito = ventaDAO.agregar(ventaActual);

		if (exito) {
			// 3. Si tiene éxito, limpiar la vista para una nueva venta
			vistaVenta.mostrarMensaje("Venta finalizada con éxito.");
			vistaVenta.limpiarCampos(); // Limpia todos los campos del panel
			this.ventaActual = new MVenta(); // Crea un nuevo objeto de venta para la siguiente

		} else {
			// 4. Si falla, mostrar un mensaje de error
			vistaVenta.mostrarError("Ocurrió un error al guardar la venta en la base de datos.");
		}
	}

	private void cargarClientesEnComboBox() {

		List<Mcliente> clientes = clienteDAO.ObtenerTodo();

		if (clientes != null) {
			vistaVenta.cargarClientes(clientes);
		}

	}

	private void cargarEmpleadosEnComboBox() {

		List<Mempleado> empleados = empleadoDAO.ObtenerTodo();

		if (empleados != null) {
			vistaVenta.cargarEmpleados(empleados);
		}

	}
}