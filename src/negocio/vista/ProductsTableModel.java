package negocio.vista;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import database.DataBaseManager;
import negocio.logica.Producto;

public class ProductsTableModel extends AbstractTableModel {

	// Variables
	
	private static final long serialVersionUID = 1L;
	private String[] columnNames = { "Código","Producto", "Rubro", "$ Costo", "$ Venta" };
	public final Object[] longValues = { "", "", "", 1.0, 1.0 };
	private ArrayList<Producto> productos;

	// Constructors

	public ProductsTableModel(){
		productos = DataBaseManager.getInstancia().getProductosList();
	}

	// Getters and Setters

	// Methods

	// REV 1.0
	@Override
	public int getColumnCount ()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount ()
	{
		return productos.size();
	}

	@Override
	public String getColumnName ( int col )
	{
		return columnNames[ col ];
	}

	@Override
	public Object getValueAt ( int row, int col )
	{
		Producto producto = productos.get(row);
		switch(col){
		case 0 :{
			return producto.getCodigo();
		}
		case 1 :{
			return producto.getDescripcion();
		}
		case 2 :{
			return DataBaseManager.getInstancia().getRubroByID(producto.getRubro());
		}
		case 3 :{
			return producto.getPrecioCosto();
		}
		case 4 :{
			return producto.getPrecioVenta();
		}
		default :
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class getColumnClass ( int c )
	{
		return longValues[ c ].getClass ();
	}

	@Override
	public boolean isCellEditable ( int row, int col )
	{
		return false;
	}

}