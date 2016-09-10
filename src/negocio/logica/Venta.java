package negocio.logica;
import java.util.ArrayList;

public class Venta {

	// Variables

	private ArrayList<Item> items;

	// Constructors

	public Venta(){
		super();
		this.items = new ArrayList<Item>();
	}

	// Getters and Setters

	public void addToVenta(Item item){
		// Si un producto ya existe, entonces hay que actualizarlo
		if (items.contains(item)){
			Item itemEnVenta = items.get(items.indexOf(item));
			itemEnVenta.setCantidad(itemEnVenta.getCantidad() + item.getCantidad());
		}else{
			if (chequearItemCorrecto(item))
				items.add(item);
		}
	}
	
	public int getCantidadProductos() {
		return items.size();
	}

	// Methods	

	private boolean chequearItemCorrecto(Item item){
		boolean out = false;

		if (item.getProducto().getPrecioVenta() != null)
			out = true;

		return out;
	}

	public Double getTotal(){
		Double out = 0.0;

		for (Item item : items){
			out += item.getProducto().getPrecioVenta() * item.getCantidad();
		}

		return out;
	}

	public Producto getProducto(int row) {
		return items.get(row).getProducto();
	}

	public Item getItem(int row) {
		return items.get(row);
	}

	public void removeProducto(int row){
		this.items.remove(row);
	}
	


}
