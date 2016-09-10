package negocio.logica;

public class Producto {

	// Variables
	
	private String descripcion;
	private String codigo;
	private Integer rubro;
	private Double precioCosto;
	private Double precioVenta;
	
	// Constructors
	
	public Producto(){
		
	}

	public Producto(String descripcion, String codigo, Integer rubro, Double precioCosto, Double precioVenta) {
		super();
		this.descripcion = descripcion;
		this.codigo = codigo;
		this.rubro = rubro;
		this.precioCosto = precioCosto;
		this.precioVenta = precioVenta;
	}

	// Getters and Setters
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getRubro() {
		return rubro;
	}

	public void setRubro(Integer rubro) {
		this.rubro = rubro;
	}

	public Double getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(Double precioCosto) {
		this.precioCosto = precioCosto;
	}

	public Double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}
	
	// Methods
	
	public String getSQLValues(){
		String out = "";
		out += "'" + getCodigo() + "', ";
		out += "'" + getDescripcion() + "', ";
		out += getRubro() + ", ";
		out += getPrecioCosto() + ", ";
		out += getPrecioVenta();
		return out;
	}
	
	@Override
	public String toString() {
		
		return getCodigo() + " | " + getDescripcion();
	}
}
