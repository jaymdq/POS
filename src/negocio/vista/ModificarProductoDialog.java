package negocio.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;

import database.DataBaseManager;
import negocio.logica.Producto;

public class ModificarProductoDialog extends AbsOperacionProductoDialog {

	// Variables

	private static final long serialVersionUID = 1L;
	private int selectedRow;

	// Constructors

	public ModificarProductoDialog(TableDialog tableDialog, int selectedRow) {
		super(tableDialog);
		this.selectedRow = selectedRow;

		Producto producto = productos.get(selectedRow);

		if (producto != null){

			// Cargar los datos del producto
			txtCodigo.setText(producto.getCodigo());

			Integer rubroID = producto.getRubro();

			String rubro = DataBaseManager.getInstancia().getRubroByID(rubroID);
			for (int i = 0; i < rubrosComboBox.getItemCount(); i++){
				if (rubrosComboBox.getItemAt(i).equals(rubro)){
					rubrosComboBox.setSelectedIndex(i);
					break;
				}
			}		

			txtDescripcion.setText(producto.getDescripcion());
			txtPrecioCosto.setText(producto.getPrecioCosto().toString());
			txtPrecioVenta.setText(producto.getPrecioVenta().toString());
		}
	}

	// Getters and Setters

	// Methods


	@Override
	protected void establecerOperacion() {
		// Dialogo
		setTitle("Modificar Producto");		

		// Botón
		operacionBtn.setText("Modificar Producto");
		((AbstractButton) operacionBtn).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificarProducto();
			}
		});		

	}

	// REV 1.0
	private void modificarProducto() {

		// Obtenemos los datos
		String codigo = txtCodigo.getText();
		String descripcion = txtDescripcion.getText();
		String rubro = (String) rubrosComboBox.getSelectedItem();

		String pCostoTexto = txtPrecioCosto.getText().replaceAll(",", "");
		String pVentaTexto = txtPrecioVenta.getText().replaceAll(",", "");
		
		Double precioCosto = (Double) Double.parseDouble(pCostoTexto); 
		Double precioVenta = (Double) Double.parseDouble(pVentaTexto);

		// Obtenemos el ID del rubro
		Integer rubroID = DataBaseManager.getInstancia().getRubroID(rubro);

		// Obtenemos el ID del producto
		Integer productoID = DataBaseManager.getInstancia().getProductoIDByAppearence(selectedRow);
		
		// Insertamos el nuevo producto
		Producto productoToAdd = new Producto(descripcion, codigo,rubroID, precioCosto, precioVenta);
		boolean resultado = DataBaseManager.getInstancia().updateProducto(productoID,productoToAdd);

		if (resultado){

			// Se avisa que se agrego bien
			final WebNotification notificationPopup = new WebNotification ();
			notificationPopup.setIcon ( NotificationIcon.plus.getIcon() );
			notificationPopup.setDisplayTime ( 5000 );
			notificationPopup.setContent("Producto Modificado");    
			NotificationManager.showNotification ( notificationPopup );
		}

		cerrarDialog();
	}






}
