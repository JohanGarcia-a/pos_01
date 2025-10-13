package controlador;

import java.util.Collections;
import java.util.List;

import modelo.AlmacenProductosDAO;
import modelo.CategoriaDAO;
import modelo.MalmacenProductos;
import modelo.Mcategoria;
import modelo.Mproveedor;
import modelo.ProveedorDAO;
import modeloGenerico.ModeloCombobox;
import vista.Panel_AlmacenProductos;

public class ControladorAlmacen extends Controlador<MalmacenProductos> {

	private ProveedorDAO proveedorDAO;
	private Panel_AlmacenProductos panelAlmacen;
	private CategoriaDAO categoriaDAO;

	public ControladorAlmacen(AlmacenProductosDAO almacenDAO, ProveedorDAO proveedorDAO, Panel_AlmacenProductos vista,
			CategoriaDAO categoriaDAO) {
		super(almacenDAO, vista);
	
		this.proveedorDAO = proveedorDAO;
		this.panelAlmacen = vista;
		this.categoriaDAO = categoriaDAO;
		panelAlmacen.actualizarColoresTabla();
		cargarProveedores();
		cargarCategorias();

		this.panelAlmacen.addModifcarMinimoStok(e -> modificarStock());
	}

	@Override
	public void buscar() {
		String textoBusqueda = vista.getTbuscar();
		if (textoBusqueda.isEmpty()) {
			vista.mostrarError("El campo de búsqueda está vacío.");
			return;
		}

		AlmacenProductosDAO almacenDAO = (AlmacenProductosDAO) this.modelo;
		MalmacenProductos productoEncontrado = null;

		// 1. Lógica específica: Primero intentamos buscar por código (String)
		productoEncontrado = almacenDAO.buscarPorCodigo(textoBusqueda);

		if (productoEncontrado == null) {
			try {
				int id = Integer.parseInt(textoBusqueda);

				productoEncontrado = almacenDAO.buscarPorID(id);
			} catch (NumberFormatException e) {

			}
		}

		// 3. Mostramos el resultado final
		if (productoEncontrado != null) {
			vista.mostrarEntidades(Collections.singletonList(productoEncontrado));
			vista.mostrarMensaje("Producto encontrado.");
		} else {
			vista.mostrarError("No se encontró ningún producto con ese código o ID.");
		}
	}

	private void cargarProveedores() {
		List<Mproveedor> listaProveedores = proveedorDAO.ObtenerTodo();
		ModeloCombobox.cargarComboBox(panelAlmacen.getComboProveedor(), listaProveedores);
	}

	private void cargarCategorias() {
		List<Mcategoria> listaCategorias = categoriaDAO.ObtenerTodo();
		ModeloCombobox.cargarComboBox(panelAlmacen.getComboCategoria(), listaCategorias);
	}

	public void modificarStock() {
		if (panelAlmacen.getChecMinStok().isSelected()) {
			panelAlmacen.getTminStok().setEnabled(true);
			panelAlmacen.getLminStok().setEnabled(true);
		} else {
			panelAlmacen.getTminStok().setEnabled(false);
			panelAlmacen.getLminStok().setEnabled(false);
		}
	}

}