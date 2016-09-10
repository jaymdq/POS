package negocio.vista;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.alee.laf.table.WebTable;

import negocio.logica.Item;
import negocio.logica.Venta;

public class VentasTableModel extends AbstractTableModel {

	// Variables

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"","PRODUCTO", "CANTIDAD", "PRECIO UNITARIO", "TOTAL"};
	public final Object[] longValues = {Icon.class,"", 1, "", ""};
	private Venta venta;

	// Constructors

	public VentasTableModel(Venta venta){
		this.venta = venta;
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
		return venta.getCantidadProductos();
	}

	@Override
	public String getColumnName ( int col )
	{
		return columnNames[ col ];
	}

	@Override
	public Object getValueAt ( int row, int col )
	{

		Item item = venta.getItem(row);
		switch(col){
		case 0 :{
			try {
				return new ImageIcon(ImageIO.read(getClass().getResource("/resources/erase.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		case 1 :{
			return item.getProducto().getDescripcion();
		}
		case 2 :{
			return item.getCantidad();
		}
		case 3 :{
			return "$" + item.getProducto().getPrecioVenta();
		}
		case 4 :{
			return "$" + item.getPrecioTotal();
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
		return col == 0;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		try{
			Integer nuevaCantidad = (Integer) aValue;
			if (nuevaCantidad < 1)
				return;
			venta.getItem(rowIndex).setCantidad(nuevaCantidad);
			this.fireTableCellUpdated(rowIndex, columnIndex);
			this.fireTableCellUpdated(rowIndex, 3);
		} catch (Exception e){ return;}
	}

	public Venta clear(){
		this.venta= new Venta();
		this.fireTableDataChanged();
		return this.venta;
	}

	public void removeRow(int modelRow) {
		venta.removeProducto(modelRow);
		
		this.fireTableRowsDeleted(modelRow, modelRow);
		
	}
}