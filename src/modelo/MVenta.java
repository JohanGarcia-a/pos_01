package modelo;

import java.util.Date;
import java.util.List;

import modeloGenerico.Entidad;

import java.util.ArrayList;

public class MVenta implements Entidad {
	private int id;
	private int clienteId;
	private int empleadoId;
	private Date fecha;
	private double total;

	private String nombreCliente;
	private String nombreEmpleado;

	private List<MVentaDetalle> detalles;

	public MVenta() {
		this.detalles = new ArrayList<>();
	}

	public void agregarDetalle(MVentaDetalle detalle) {
		this.detalles.add(detalle);
	}

	public List<MVentaDetalle> getDetalles() {
		return detalles;
	}

	@Override
	public Object[] toTableRow() {

		return new Object[] { this.id, this.fecha, this.nombreCliente, this.nombreEmpleado, this.total };
	}

	public int getClienteId() {
		return clienteId;
	}

	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}

	public int getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(int empleadoId) {
		this.empleadoId = empleadoId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getTotal() {
		double total = 0.0;
		for (MVentaDetalle detalle : detalles) {
			total += detalle.getSubtotal();
		}
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public void setDetalles(List<MVentaDetalle> detalles) {
		this.detalles = detalles;
	}

	public void quitarDetalle(int indice) {
		if (indice >= 0 && indice < detalles.size()) {
			detalles.remove(indice);
		}
	}

	@Override
	public int getid() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setid(int id) {
		this.id = id;

	}
}