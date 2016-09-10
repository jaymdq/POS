package negocio.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.AbstractButton;

import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;

import database.DataBaseManager;
import negocio.logica.Producto;

public class AgregarProductoDialog extends AbsOperacionProductoDialog {

	// Variables

	private static final long serialVersionUID = 1L;

	// Constructors

	public AgregarProductoDialog(TableDialog tableDialog) {
		super(tableDialog);
	}

	// Getters and Setters

	// Methods


	@Override
	protected void establecerOperacion() {
		// Dialogo
		setTitle("Agregar Producto");		
		
		// Botón
		operacionBtn.setText("Agregar Producto");
		((AbstractButton) operacionBtn).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarNuevoProducto();
			}
		});
		
		txtCodigo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				chequearQueElCodigoNoExista();
			}
		});
		
	}
	
	// REV 1.0
	private void agregarNuevoProducto() {

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

		// Insertamos el nuevo producto
		Producto productoToAdd = new Producto(descripcion, codigo,rubroID, precioCosto, precioVenta);
		boolean resultado = DataBaseManager.getInstancia().insertProducto(productoToAdd);

		if (resultado){
			// Se avisa que se agrego bien
			final WebNotification notificationPopup = new WebNotification ();
            notificationPopup.setIcon ( NotificationIcon.plus.getIcon() );
            notificationPopup.setDisplayTime ( 5000 );
            notificationPopup.setContent("Producto agregado");    
            NotificationManager.showNotification ( notificationPopup );
		}

		cerrarDialog();
	}




	

}
