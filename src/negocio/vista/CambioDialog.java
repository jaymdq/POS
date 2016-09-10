package negocio.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import negocio.logica.app.MainApp;

public class CambioDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFormattedTextField montoTxt;
	private MainApp parent;

	public CambioDialog(MainApp mainApp) {
		this.parent = mainApp;
		setTitle("Calcular Cambio");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 150);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblInsertarMonto = new JLabel("Insertar monto");
			lblInsertarMonto.setFont(new Font("Tahoma", Font.BOLD, 24));
			contentPanel.add(lblInsertarMonto, BorderLayout.NORTH);
		}
		{
			montoTxt = new JFormattedTextField(NumberFormat.getNumberInstance());
			montoTxt.setHorizontalAlignment(SwingConstants.LEFT);
			montoTxt.setFont(new Font("Tahoma", Font.PLAIN, 24));
			montoTxt.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
						cerrar();
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						ok();
				}
				@Override
				public void keyTyped(KeyEvent e) {
					char ch = e.getKeyChar();
					String txt = montoTxt.getText();
					if (!isNumber(ch) && !isValidSignal(ch, txt) && !validatePoint(ch,txt)  && ch != '\b') {
						e.consume();
					}
				}

				private boolean isNumber(char ch){
					return ch >= '0' && ch <= '9';
				}

				private boolean isValidSignal(char ch, String txt){
					if( (txt == null || "".equals(txt.trim()) ) && ch == '-'){
						return true;
					}

					return false;
				}

				private boolean validatePoint(char ch, String txt){
					if(ch != '.' || txt.contains(".")){
						return false;
					}

					if(txt == null || "".equals(txt.trim())){
						montoTxt.setText("0.");
						return false;
					}else if("-".equals(txt)){
						montoTxt.setText("-0");
					}

					return true;
				}
			});
			contentPanel.add(montoTxt, BorderLayout.SOUTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Aceptar");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ok();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cerrar();
					}
				});
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void cerrar() {
		parent.terminaVenta = false;
		this.setVisible(false);
	}

	protected void ok() {
		
		if (montoTxt.getText().isEmpty()){
			this.setVisible(false);
			return;
		}
		Double monto = Double.parseDouble(montoTxt.getText().replaceAll(",", ""));
		parent.lblTotalCambio.setText(String.format("%.2f",monto - parent.venta.getTotal()));
		this.setVisible(false);
		
	}
	
	@Override
	public void setVisible(boolean b) {
		this.montoTxt.setText("");
		super.setVisible(b);		
	}

}
