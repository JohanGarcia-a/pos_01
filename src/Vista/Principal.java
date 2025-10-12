package Vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import Controlador.Controlador;
import Controlador.ControladorAlmacen;
import Controlador.ControladorVenta;
import Modelo.AlmacenProductosDAO;
import Modelo.CategoriaDAO;
import Modelo.ClienteDAO;
import Modelo.EmpleadoDAO;
import Modelo.MalmacenProductos;
import Modelo.Mcategoria;
import Modelo.Mcliente;
import Modelo.Mempleado;
import Modelo.ProveedorDAO;
import Modelo.VentaDAO;
import Modelo.Mproveedor;
import javax.swing.ImageIcon;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panelContenido;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	public Principal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicia maximizado

		JPanel contentPane = new JPanel(new BorderLayout(5, 5));
		setContentPane(contentPane);

		// --- Panel de Cabecera (Norte) ---
		JPanel panelCabecera = new JPanel();
		panelCabecera.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelCabecera.setPreferredSize(new Dimension(0, 45));
		contentPane.add(panelCabecera, BorderLayout.NORTH);

		// --- Panel de Botones del Menú (Oeste) ---
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelBotones.setPreferredSize(new Dimension(180, 0));
		contentPane.add(panelBotones, BorderLayout.WEST);
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
		panelBotones.add(Box.createRigidArea(new Dimension(0, 15))); // Espaciado superior

		// --- Panel Principal de Contenido (Centro) ---
		panelContenido = new JPanel(new BorderLayout(0, 0));
		panelContenido.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panelContenido, BorderLayout.CENTER);

		// --- Creación de Botones del Menú ---
		agregarBotonMenu(panelBotones, "Clientes");
		agregarBotonMenu(panelBotones, "Proveedores");
		agregarBotonMenu(panelBotones, "Categorias");
		agregarBotonMenu(panelBotones, "Empleados");
		agregarBotonMenu(panelBotones, "Almacen/Productos");
		agregarBotonMenu(panelBotones, "Ventas");
	}

	private void agregarBotonMenu(JPanel panel, String nombre) {

		JToggleButton boton = new JToggleButton(nombre);
		boton.setFont(new Font("Calisto MT", Font.BOLD, 13));

		boton.setMaximumSize(new Dimension(160, 40));
		boton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonGroup.add(boton); // Agrupa los botones para que solo uno esté seleccionado
		panel.add(boton);
		panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado entre botones

		boton.addActionListener(e -> {
			// Lógica para cambiar de panel según el botón presionado
			if ("Clientes".equals(nombre)) {
				boton.setIcon(new ImageIcon("D:\\isaia\\Pictures\\Cliente.png"));
				// 1. Instanciar los componentes específicos del módulo
				ClienteDAO clienteModelo = new ClienteDAO();
				Panel_Cliente clienteVista = new Panel_Cliente();

				// 2. Instanciar el controlador genérico, especificando el tipo <Cliente>
				new Controlador<Mcliente>(clienteModelo, clienteVista);

				// 3. Cargar el panel en la ventana principal
				cargarPanel(clienteVista);
			} else if ("Proveedores".equals(nombre)) {
				boton.setIcon(new ImageIcon("D:\\isaia\\Pictures\\proveedor.png"));
				ProveedorDAO proveedorModelo = new ProveedorDAO();
				Panel_Proveedor proveedorVista = new Panel_Proveedor();

				new Controlador<Mproveedor>(proveedorModelo, proveedorVista);

				cargarPanel(proveedorVista);
			} else if ("Categorias".equals(nombre)) {
				boton.setIcon(new ImageIcon("D:\\isaia\\Pictures\\categorias.png"));
				CategoriaDAO categoriaModelo = new CategoriaDAO();
				Panel_Categoria vistaCategorias = new Panel_Categoria();

				new Controlador<Mcategoria>(categoriaModelo, vistaCategorias);

				cargarPanel(vistaCategorias);
			} else if ("Empleados".equals(nombre)) {
				boton.setIcon(new ImageIcon("D:\\isaia\\Pictures\\empleados.png"));
				EmpleadoDAO empleadoModelo = new EmpleadoDAO();
				Panel_Empleado empleadoVista = new Panel_Empleado();

				new Controlador<Mempleado>(empleadoModelo, empleadoVista);

				cargarPanel(empleadoVista);
			}

			else if ("Almacen/Productos".equals(nombre)) {
				boton.setIcon(new ImageIcon("D:\\isaia\\Pictures\\almacen.png"));

				// 1. Crea los DAOs que necesitas
				AlmacenProductosDAO productoModelo = new AlmacenProductosDAO();
				ProveedorDAO proveedorModelo = new ProveedorDAO();
				CategoriaDAO categoriaModelo = new CategoriaDAO();
				Panel_AlmacenProductos productoVista = new Panel_AlmacenProductos();

				// 3. Usa el nuevo ControladorAlmacen y pásale todo lo necesario
				new ControladorAlmacen(productoModelo, proveedorModelo, productoVista, categoriaModelo);

				cargarPanel(productoVista);

			} else if ("Ventas".equals(nombre)) {

				VentaDAO ventaModelo = new VentaDAO();
				AlmacenProductosDAO productoModelo = new AlmacenProductosDAO();
				ClienteDAO clienteModelo = new ClienteDAO();
				EmpleadoDAO empleadoModelo = new EmpleadoDAO();

				// 2. Instanciar la vista
				Panel_Venta ventaVista = new Panel_Venta();

				// 3. Instanciar el controlador específico
				new ControladorVenta(ventaModelo, productoModelo, clienteModelo, empleadoModelo, ventaVista);

				// 4. Cargar el panel
				cargarPanel(ventaVista);
			}

		});
	}

	public void cargarPanel(JPanel panel) {
		panelContenido.removeAll();
		panelContenido.add(panel, BorderLayout.CENTER);
		panelContenido.revalidate();
		panelContenido.repaint();
	}
}