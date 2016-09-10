package negocio.logica;
public class Item {

	// Variable
	private Producto producto;
	private Integer cantidad;	
	
	// Constructors
	
	public Item(){
		
	}

	public Item(Producto producto, Integer cantidad) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	// Getters and Setters
	
	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	// Methods
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item){
			Item otroItem = (Item) obj;
			if (this.getProducto().equals(otroItem.getProducto()))
				return true;
		}
		return false;
	}

	public Double getPrecioTotal() {
		return producto.getPrecioVenta() * cantidad;
	}
	
}
