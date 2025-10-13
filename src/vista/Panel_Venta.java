package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import modelo.MVentaDetalle;
import modelo.MalmacenProductos;
import modelo.Mcliente;
import modelo.Mempleado;

import java.awt.Font; 

public class Panel_Venta extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<Mcliente> comboCliente;
	private JComboBox<Mempleado> comboEmpleado;
	private JLabel LnombreEmpleado;
	private JLabel LfechaVenta;

	private JTextField TbuscarProducto;
	private JButton BbuscarProducto;
	private JLabel LimagenProducto;
	private JTextField TnombreProducto;
	private JTextField TprecioProducto;
	private JTextField TstockProducto;
	private JTextField TcantidadAVender;
	private JButton BagregarAlCarrito;

	// --- Componentes para el "Carrito" (Detalle de la venta) ---
	private DefaultTableModel modeloTablaCarrito;
	private JTable tableCarrito;
	private JButton BquitarDelCarrito;

	// --- Componentes para el Total y acciones de la venta ---
	private JLabel LtotalVenta;
	private JButton BfinalizarVenta;
	private JButton BcancelarVenta;

	// --- Otros ---
	private String rutaImagenProducto = ""; // Para mantener la ruta de la imagen del producto actual
	private JLabel Ldescripcion;
	private JTextField Tdescripcion;

	public Panel_Venta() {
		setLayout(new BorderLayout(10, 10)); // Espaciado entre paneles principales
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen exterior

		// --- Panel Superior: Info Cliente/Empleado/Fecha ---
		JPanel panelInfoGeneral = crearPanelInfoGeneral();
		add(panelInfoGeneral, BorderLayout.NORTH);

		// --- Panel Central: Búsqueda de Productos y Carrito ---
		JPanel panelCentral = new JPanel(new GridBagLayout());

		// Panel Izquierdo: Búsqueda y detalles del producto
		JPanel panelProducto = crearPanelBusquedaProducto();
		GridBagConstraints gbc_panelProducto = new GridBagConstraints();
		gbc_panelProducto.gridx = 0;
		gbc_panelProducto.gridy = 0;
		gbc_panelProducto.weightx = 0.4; // Ocupa un 40% del ancho
		gbc_panelProducto.fill = GridBagConstraints.BOTH;
		gbc_panelProducto.insets = new Insets(0, 0, 0, 10); // Margen a la derecha
		panelCentral.add(panelProducto, gbc_panelProducto);

		// Panel Derecho: Carrito de compras
		JPanel panelCarrito = crearPanelCarrito();
		GridBagConstraints gbc_panelCarrito = new GridBagConstraints();
		gbc_panelCarrito.gridx = 1;
		gbc_panelCarrito.gridy = 0;
		gbc_panelCarrito.weightx = 0.6; // Ocupa un 60% del ancho
		gbc_panelCarrito.fill = GridBagConstraints.BOTH;
		panelCentral.add(panelCarrito, gbc_panelCarrito);

		add(panelCentral, BorderLayout.CENTER);

		// --- Panel Inferior: Total y Botones de Acción ---
		JPanel panelAccionesVenta = crearPanelAccionesVenta();
		add(panelAccionesVenta, BorderLayout.SOUTH);

		// Inicializar algunos campos
		limpiarCampos();
	}

	private JPanel crearPanelInfoGeneral() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Información de Venta"));

		panel.add(new JLabel("Cliente:"));
		comboCliente = new JComboBox<>();
		comboCliente.setPreferredSize(new Dimension(200, 25));
		panel.add(comboCliente);

		panel.add(new JLabel("Empleado:"));
		comboEmpleado = new JComboBox<>();
		comboEmpleado.setPreferredSize(new Dimension(200, 25));
		panel.add(comboEmpleado);

		panel.add(new JLabel("Fecha:"));
		LfechaVenta = new JLabel();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		LfechaVenta.setText(sdf.format(new Date()));
		panel.add(LfechaVenta);

		return panel;
	}

	private JPanel crearPanelBusquedaProducto() {
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		JPanel panel = new JPanel(gbl_panel);
		panel.setBorder(BorderFactory.createTitledBorder("Buscar Producto"));

		// Fila 0: Etiqueta "Código/Nombre:"
		JLabel LcodigoNombre = new JLabel("Código/Nombre:");
		GridBagConstraints gbc_LcodigoNombre = new GridBagConstraints();
		gbc_LcodigoNombre.insets = new Insets(5, 5, 5, 5);
		gbc_LcodigoNombre.gridx = 0;
		gbc_LcodigoNombre.gridy = 0;
		gbc_LcodigoNombre.anchor = GridBagConstraints.EAST;
		panel.add(LcodigoNombre, gbc_LcodigoNombre);

		// Fila 0: Campo de texto para buscar producto
		TbuscarProducto = new JTextField(15);
		GridBagConstraints gbc_TbuscarProducto = new GridBagConstraints();
		gbc_TbuscarProducto.insets = new Insets(5, 5, 5, 5);
		gbc_TbuscarProducto.gridx = 1;
		gbc_TbuscarProducto.gridy = 0;
		gbc_TbuscarProducto.weightx = 1.0;
		gbc_TbuscarProducto.fill = GridBagConstraints.HORIZONTAL;
		panel.add(TbuscarProducto, gbc_TbuscarProducto);

		// Fila 0: Botón "Buscar"
		BbuscarProducto = new JButton("Buscar", new ImageIcon(getClass().getResource("")));
		GridBagConstraints gbc_BbuscarProducto = new GridBagConstraints();
		gbc_BbuscarProducto.insets = new Insets(5, 5, 5, 0);
		gbc_BbuscarProducto.gridx = 2;
		gbc_BbuscarProducto.gridy = 0;
		panel.add(BbuscarProducto, gbc_BbuscarProducto);

		// Fila 1-3: Imagen del producto
		LimagenProducto = new JLabel("<html><center>Imagen<br>Producto</center></html>");
		LimagenProducto.setBorder(BorderFactory.createEtchedBorder());
		LimagenProducto.setHorizontalAlignment(SwingConstants.CENTER);
		LimagenProducto.setPreferredSize(new Dimension(220, 220)); // Tamaño fijo para la imagen
		GridBagConstraints gbc_LimagenProducto = new GridBagConstraints();
		gbc_LimagenProducto.insets = new Insets(5, 5, 5, 0);
		gbc_LimagenProducto.gridx = 0;
		gbc_LimagenProducto.gridy = 2;
		gbc_LimagenProducto.gridwidth = 3; // Ocupa 3 columnas
		gbc_LimagenProducto.gridheight = 6; // Ocupa 3 filas
		gbc_LimagenProducto.fill = GridBagConstraints.BOTH;
		panel.add(LimagenProducto, gbc_LimagenProducto);

		// Fila 4: Etiqueta "Nombre:"
		JLabel LnombreProducto = new JLabel("Nombre:");
		GridBagConstraints gbc_LnombreProducto = new GridBagConstraints();
		gbc_LnombreProducto.insets = new Insets(5, 5, 5, 5);
		gbc_LnombreProducto.gridx = 0;
		gbc_LnombreProducto.gridy = 8;
		gbc_LnombreProducto.anchor = GridBagConstraints.EAST;
		panel.add(LnombreProducto, gbc_LnombreProducto);

		// Fila 4: Campo de texto "Nombre del Producto"
		TnombreProducto = new JTextField(20);
		TnombreProducto.setEditable(false);
		GridBagConstraints gbc_TnombreProducto = new GridBagConstraints();
		gbc_TnombreProducto.insets = new Insets(5, 5, 5, 0);
		gbc_TnombreProducto.gridx = 1;
		gbc_TnombreProducto.gridy = 8;
		gbc_TnombreProducto.gridwidth = 2; // Ocupa 2 columnas
		gbc_TnombreProducto.fill = GridBagConstraints.HORIZONTAL;
		panel.add(TnombreProducto, gbc_TnombreProducto);

		Ldescripcion = new JLabel("Descripcion:");
		GridBagConstraints gbc_ldescripcion = new GridBagConstraints();
		gbc_ldescripcion.anchor = GridBagConstraints.EAST;
		gbc_ldescripcion.insets = new Insets(0, 0, 5, 5);
		gbc_ldescripcion.gridx = 0;
		gbc_ldescripcion.gridy = 9;
		panel.add(Ldescripcion, gbc_ldescripcion);

		Tdescripcion = new JTextField(20);
		Tdescripcion.setText("");
		Tdescripcion.setEditable(false);
		GridBagConstraints gbc_tdescripcion = new GridBagConstraints();
		gbc_tdescripcion.gridwidth = 2;
		gbc_tdescripcion.insets = new Insets(5, 5, 5, 0);
		gbc_tdescripcion.fill = GridBagConstraints.HORIZONTAL;
		gbc_tdescripcion.gridx = 1;
		gbc_tdescripcion.gridy = 9;
		panel.add(Tdescripcion, gbc_tdescripcion);

		// Fila 5: Etiqueta "Precio Unitario:"
		JLabel LprecioProducto = new JLabel("Precio Unitario:");
		GridBagConstraints gbc_LprecioProducto = new GridBagConstraints();
		gbc_LprecioProducto.insets = new Insets(5, 5, 5, 5);
		gbc_LprecioProducto.gridx = 0;
		gbc_LprecioProducto.gridy = 10;
		gbc_LprecioProducto.anchor = GridBagConstraints.EAST;
		panel.add(LprecioProducto, gbc_LprecioProducto);

		// Fila 5: Campo de texto "Precio del Producto"
		TprecioProducto = new JTextField(10);
		TprecioProducto.setEditable(false);
		GridBagConstraints gbc_TprecioProducto = new GridBagConstraints();
		gbc_TprecioProducto.insets = new Insets(5, 5, 5, 0);
		gbc_TprecioProducto.gridx = 1;
		gbc_TprecioProducto.gridy = 10;
		gbc_TprecioProducto.gridwidth = 2; // Ocupa 2 columnas
		gbc_TprecioProducto.fill = GridBagConstraints.HORIZONTAL;
		panel.add(TprecioProducto, gbc_TprecioProducto);

		// Fila 6: Etiqueta "Stock Disponible:"
		JLabel LstockProducto = new JLabel("Stock Disponible:");
		GridBagConstraints gbc_LstockProducto = new GridBagConstraints();
		gbc_LstockProducto.insets = new Insets(5, 5, 5, 5);
		gbc_LstockProducto.gridx = 0;
		gbc_LstockProducto.gridy = 11;
		gbc_LstockProducto.anchor = GridBagConstraints.EAST;
		panel.add(LstockProducto, gbc_LstockProducto);

		// Fila 6: Campo de texto "Stock del Producto"
		TstockProducto = new JTextField(10);
		TstockProducto.setEditable(false);
		GridBagConstraints gbc_TstockProducto = new GridBagConstraints();
		gbc_TstockProducto.insets = new Insets(5, 5, 5, 0);
		gbc_TstockProducto.gridx = 1;
		gbc_TstockProducto.gridy = 11;
		gbc_TstockProducto.gridwidth = 2; // Ocupa 2 columnas
		gbc_TstockProducto.fill = GridBagConstraints.HORIZONTAL;
		panel.add(TstockProducto, gbc_TstockProducto);

		// Fila 7: Etiqueta "Cantidad:"
		JLabel LcantidadAVender = new JLabel("Cantidad:");
		GridBagConstraints gbc_LcantidadAVender = new GridBagConstraints();
		gbc_LcantidadAVender.insets = new Insets(5, 5, 5, 5);
		gbc_LcantidadAVender.gridx = 0;
		gbc_LcantidadAVender.gridy = 12;
		gbc_LcantidadAVender.anchor = GridBagConstraints.EAST;
		panel.add(LcantidadAVender, gbc_LcantidadAVender);

		// Fila 7: Campo de texto "Cantidad a vender"
		TcantidadAVender = new JTextField(5);
		GridBagConstraints gbc_TcantidadAVender = new GridBagConstraints();
		gbc_TcantidadAVender.insets = new Insets(5, 5, 5, 0);
		gbc_TcantidadAVender.gridx = 1;
		gbc_TcantidadAVender.gridy = 12;
		gbc_TcantidadAVender.gridwidth = 2; // Ocupa 2 columnas
		gbc_TcantidadAVender.fill = GridBagConstraints.HORIZONTAL;
		panel.add(TcantidadAVender, gbc_TcantidadAVender);

		// Fila 8: Botón "Agregar al Carrito"
		BagregarAlCarrito = new JButton("Agregar al Carrito", new ImageIcon(getClass().getResource("")));
		GridBagConstraints gbc_BagregarAlCarrito = new GridBagConstraints();
		gbc_BagregarAlCarrito.insets = new Insets(5, 5, 0, 0);
		gbc_BagregarAlCarrito.gridx = 0;
		gbc_BagregarAlCarrito.gridy = 13;
		gbc_BagregarAlCarrito.gridwidth = 3; // Ocupa 3 columnas
		gbc_BagregarAlCarrito.fill = GridBagConstraints.HORIZONTAL;
		panel.add(BagregarAlCarrito, gbc_BagregarAlCarrito);

		return panel;
	}

	private JPanel crearPanelCarrito() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Productos en Carrito"));

		String[] columnasCarrito = { "ID", "Producto", "Descripcion", "Cantidad", "Precio Unitario", "Subtotal" };
		modeloTablaCarrito = new DefaultTableModel(columnasCarrito, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCarrito = new JTable(modeloTablaCarrito);
		tableCarrito.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		JScrollPane scrollPaneCarrito = new JScrollPane(tableCarrito);
		panel.add(scrollPaneCarrito, BorderLayout.CENTER);

		JPanel panelBotonesCarrito = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		BquitarDelCarrito = new JButton("Quitar del Carrito", new ImageIcon(getClass().getResource("")));
		panelBotonesCarrito.add(BquitarDelCarrito);
		panel.add(panelBotonesCarrito, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel crearPanelAccionesVenta() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEtchedBorder());

		// Panel para el total
		JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		LtotalVenta = new JLabel("Total: $0.00");
		LtotalVenta.setFont(LtotalVenta.getFont().deriveFont(Font.BOLD, 24f)); // Fuente más grande y negrita
		panelTotal.add(LtotalVenta);
		panel.add(panelTotal, BorderLayout.CENTER);

		// Panel para los botones de acción
		JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		BfinalizarVenta = new JButton("Finalizar Venta", new ImageIcon(getClass().getResource("")));
		BcancelarVenta = new JButton("Cancelar Venta", new ImageIcon(getClass().getResource("")));
		panelBotonesAccion.add(BfinalizarVenta);
		panelBotonesAccion.add(BcancelarVenta);
		panel.add(panelBotonesAccion, BorderLayout.EAST);

		return panel;
	}

	public String getCodigoProductoBuscado() {
		return TbuscarProducto.getText().trim();
	}

	public int getCantidadAVender() throws NumberFormatException {
		return Integer.parseInt(TcantidadAVender.getText().trim());
	}

	public int getFilaSeleccionadaCarrito() {
		return tableCarrito.getSelectedRow();
	}

	public void actualizarTotalVenta(double total) {
		LtotalVenta.setText(String.format("Total: $%.2f", total));
	}

	public void setNombreEmpleado(String nombre) {
		LnombreEmpleado.setText(nombre);
	}

	public JButton getBbuscarProducto() {
		return BbuscarProducto;
	}

	public JButton getBagregarAlCarrito() {
		return BagregarAlCarrito;
	}

	public JButton getBquitarDelCarrito() {
		return BquitarDelCarrito;
	}

	public JButton getBfinalizarVenta() {
		return BfinalizarVenta;
	}

	public JButton getBcancelarVenta() {
		return BcancelarVenta;
	}

	public DefaultTableModel getModeloTablaCarrito() {
		return modeloTablaCarrito;
	}

	public JTable getTableCarrito() {
		return tableCarrito;
	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public int mostrarConfirmacion(String mensaje) {
		return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Acción", JOptionPane.YES_NO_OPTION);
	}

	public String getProductoBuscadoNombre() {
		return TnombreProducto.getText();
	}

	public double getProductoBuscadoPrecio() throws NumberFormatException {
		return Double.parseDouble(TprecioProducto.getText());
	}

	public int getProductoBuscadoStock() throws NumberFormatException {
		return Integer.parseInt(TstockProducto.getText());
	}

	public String getProductoBuscadoRutaImagen() {
		return rutaImagenProducto;
	}

	public void addBuscarProductoListener(ActionListener listener) {
		BbuscarProducto.addActionListener(listener);
	}

	public void addAgregarCarritoListener(ActionListener listener) {
		BagregarAlCarrito.addActionListener(listener);
	}

	public void addQuitarDelCarritoListener(ActionListener listener) {
		BquitarDelCarrito.addActionListener(listener);
	}

	public void addFinalizarVentaListener(ActionListener listener) {
		BfinalizarVenta.addActionListener(listener);
	}

	public void addCancelarVentaListener(ActionListener listener) {
		BcancelarVenta.addActionListener(listener);
	}

	public void agregarDetalleAlCarrito(MVentaDetalle detalle) {

		modeloTablaCarrito.addRow(detalle.toTableRow());
		TbuscarProducto.setText("");
		Tdescripcion.setText("");
		LimagenProducto.setIcon(null);
		LimagenProducto.setText("");
		TnombreProducto.setText("");
		TprecioProducto.setText("");
		TstockProducto.setText("");
		TcantidadAVender.setText("");
	}

	public void limpiarCampos() {
		TbuscarProducto.setText("");
		LimagenProducto.setIcon(null);
		LimagenProducto.setText("");
		TnombreProducto.setText("");
		TprecioProducto.setText("");
		TstockProducto.setText("");
		TcantidadAVender.setText("");
		rutaImagenProducto = "";

		modeloTablaCarrito.setRowCount(0);
		LtotalVenta.setText("Total: $0.00");

		if (comboCliente.getItemCount() > 0) {
			comboCliente.setSelectedIndex(0);
		}

		if (comboEmpleado.getItemCount() > 0) {
			comboEmpleado.setSelectedIndex(0);
		}

	}

	public void mostrarDatosProducto(MalmacenProductos producto) {
		TnombreProducto.setText(producto.getNombre());
		Tdescripcion.setText(producto.getDescripcion());
		TprecioProducto.setText(String.valueOf(producto.getPrecio()));
		TstockProducto.setText(String.valueOf(producto.getCantidad()));
		TcantidadAVender.setText("1"); // Por defecto, agrega 1 unidad

		this.rutaImagenProducto = producto.getRuta();
		if (rutaImagenProducto != null && !rutaImagenProducto.isEmpty() && new File(rutaImagenProducto).exists()) {
			ImageIcon icon = new ImageIcon(rutaImagenProducto);
			Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
			LimagenProducto.setIcon(new ImageIcon(img));
			LimagenProducto.setText("");
		} else {
			LimagenProducto.setIcon(null);
			LimagenProducto.setText("");
		}
	}

	public void quitarDetalleDelCarrito(int fila) {
		modeloTablaCarrito.removeRow(fila);
	}

	public int getProductoIDEnCarritoSeleccionado() {
		int fila = tableCarrito.getSelectedRow();
		if (fila != -1) {
			return (int) modeloTablaCarrito.getValueAt(fila, 0);
		}
		return -1;
	}

	public int getClienteSeleccionadoId() {
		Mcliente cliente = (Mcliente) comboCliente.getSelectedItem();
		return (cliente != null) ? cliente.getid() : -1;
	}

	public int getEmpleadoSeleccionadoId() {
		Mempleado Empleado = (Mempleado) comboEmpleado.getSelectedItem();
		return (Empleado != null) ? Empleado.getid() : -1;
	}

	public void cargarClientes(List<Mcliente> clientes) {
		comboCliente.removeAllItems();
		for (Mcliente cliente : clientes) {
			comboCliente.addItem(cliente);
		}
		if (!clientes.isEmpty()) {
			comboCliente.setSelectedIndex(0);
		}
	}

	public void cargarEmpleados(List<Mempleado> empleados) {
		comboEmpleado.removeAllItems();
		for (Mempleado empleado : empleados) {
			comboEmpleado.addItem(empleado);
		}
		if (!empleados.isEmpty()) {
			comboEmpleado.setSelectedIndex(0);
		}
	}
}