package Vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Modelo.Mempleado;

public class Panel_Empleado extends VistaGenerica {

	private JTextField Tnombre;
	private JTextField TnumCel;
	private JTextField Trol;

	public Panel_Empleado() {
		super("Empleados", new String[] { "Eid", "Nombre de Empleado", "Número Tel.", "Rol" });

	}

	@Override
	protected JPanel crearPanelCampos() {

		JPanel Panel = new JPanel();
		GridBagLayout gbl_Panel = new GridBagLayout();
		gbl_Panel.columnWeights = new double[] { 0.0, 1.0 };
		Panel.setLayout(gbl_Panel);
		GridBagConstraints gbc_Panel = new GridBagConstraints();
		gbc_Panel.insets = new Insets(5, 5, 5, 5);
		gbc_Panel.fill = GridBagConstraints.HORIZONTAL;

		// Fila 0: Nombre
		JLabel Lnombre = new JLabel("Nombre:");
		GridBagConstraints gbc_Lnombre = new GridBagConstraints();
		gbc_Lnombre.insets = new Insets(0, 0, 5, 5);
		gbc_Lnombre.gridx = 0;
		gbc_Lnombre.gridy = 0;
		gbc_Lnombre.anchor = GridBagConstraints.EAST;
		Panel.add(Lnombre, gbc_Lnombre);

		Tnombre = new JTextField(20);
		GridBagConstraints gbc_Tnombre = new GridBagConstraints();
		gbc_Tnombre.insets = new Insets(0, 0, 5, 0);
		gbc_Tnombre.gridy = 0;
		gbc_Tnombre.gridx = 1;
		gbc_Tnombre.anchor = GridBagConstraints.WEST;
		gbc_Tnombre.weightx = 1.0;
		Panel.add(Tnombre, gbc_Tnombre);

		// Fila 1: Número de Teléfono
		JLabel LnumTel = new JLabel("Numero Tel:");
		GridBagConstraints gbc_LnumTel = new GridBagConstraints();
		gbc_LnumTel.insets = new Insets(0, 0, 5, 5);
		gbc_LnumTel.gridx = 0;
		gbc_LnumTel.gridy = 1;
		gbc_LnumTel.anchor = GridBagConstraints.EAST;
		gbc_LnumTel.weightx = 0.0;
		Panel.add(LnumTel, gbc_LnumTel);

		TnumCel = new JTextField(20);
		GridBagConstraints gbc_TnumCel = new GridBagConstraints();
		gbc_TnumCel.insets = new Insets(0, 0, 5, 0);
		gbc_TnumCel.gridy = 1;

		gbc_TnumCel.gridx = 1;
		gbc_TnumCel.anchor = GridBagConstraints.WEST;
		gbc_TnumCel.weightx = 1.0;
		Panel.add(TnumCel, gbc_TnumCel);
		// Fila 2: rol
		JLabel Lrol = new JLabel("Rol:");
		GridBagConstraints gbc_Lrol = new GridBagConstraints();
		gbc_Lrol.anchor = GridBagConstraints.EAST;
		gbc_Lrol.insets = new Insets(0, 0, 5, 5);
		gbc_Lrol.gridx = 0;
		gbc_Lrol.gridy = 2;
		Panel.add(Lrol, gbc_Lrol);

		Trol = new JTextField(20);
		Trol.setText("");
		GridBagConstraints gbc_trol = new GridBagConstraints();
		gbc_trol.anchor = GridBagConstraints.WEST;
		gbc_trol.insets = new Insets(0, 0, 5, 0);
		gbc_trol.gridx = 1;
		gbc_trol.gridy = 2;
		Panel.add(Trol, gbc_trol);

		// Fila 3: Botón Limpiar
		JButton Blimpiar = new JButton("Limpiar");
		GridBagConstraints gbc_Blimpiar = new GridBagConstraints();

		gbc_Blimpiar.gridx = 1;
		gbc_Blimpiar.gridy = 3;
		gbc_Blimpiar.anchor = GridBagConstraints.EAST;
		gbc_Blimpiar.weightx = 0.0;
		gbc_Blimpiar.fill = GridBagConstraints.NONE;
		Panel.add(Blimpiar, gbc_Blimpiar);
		Blimpiar.addActionListener(e -> limpiarCampos());

		/*
		 * que -> "Sustituye" el new ActionListener() {
		 * 
		 * @Override
		 * 
		 * public void actionPerformed(ActionEvent e) {
		 * 
		 * }
		 */

		return Panel;
	}

	@Override
	public void limpiarCampos() {
		Tnombre.setText("");
		TnumCel.setText("");
		Tbuscar.setText("");
		table.clearSelection();

	}

	@Override
	protected void cargarDatosFormulario() {
		int filaSeleccionada = table.getSelectedRow();
		if (filaSeleccionada != -1) {
			String id = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
			String nombre = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
			String tpNumero = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
			String rol = modeloTabla.getValueAt(filaSeleccionada, 3).toString();

			Tbuscar.setText(id);
			Tnombre.setText(nombre);
			TnumCel.setText(tpNumero);
			Trol.setText(rol);
		}

	}

	@Override
	public Mempleado getDatosDelFormulario() {// *

		String nombre = Tnombre.getText().trim();
		String numeroTel = TnumCel.getText().trim();
		String rol = Trol.getText().trim();

		if (nombre.isEmpty()) {
			mostrarError("El campo 'Nombre' no puede estar vacío.");
			return null;
		}

		int id = filaSelect();

		return new Mempleado(id, nombre, numeroTel, rol);
	}

}
