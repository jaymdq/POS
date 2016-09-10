package negocio.vista;

import javax.swing.JDialog;

public abstract class AbsDialog extends JDialog{

	
	// Variables
	
	protected TableDialog caller; 
	private static final long serialVersionUID = 1L;
	
	// Constructors
	
	public AbsDialog(TableDialog tableDialog){
		this.caller = tableDialog;
	}

	// Getters and Setters

	// Methods

	// REV 1.0
	protected void cerrarDialog() {
		this.setVisible(false);
		caller.refreshTable();
	}
}
