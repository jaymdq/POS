package negocio.vista.filtros;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;

public class Size {

	// Variables
	private Integer width;
	private Integer height;
	private HashMap<String,Integer> map;
	private static Size instance = null;
	private Integer sizeFonts;
	
	
	// Constructors
	
	private Size(){
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		this.width = d.width;
		this.height = d.height;
		this.map = new HashMap<String,Integer>();
		
		System.out.println("Tamaño de la pantalla: [" + this.width + "/" + this.height + "]");
		if (this.width < 1366){
			sizeFonts = 2;
		}else{
			sizeFonts = 1;
		}
		
		setValues();
		
	}	
	
	// Getters and Setters
	
	private void setValues(){
		this.map.put("24_1", 24);
		this.map.put("24_2", 16);
		this.map.put("34_1", 34);
		this.map.put("34_2", 24);
		this.map.put("36_1", 36);
		this.map.put("36_2", 26);
		this.map.put("48_1", 48);
		this.map.put("48_2", 32);
		
		this.map.put("125_1", 125);
		this.map.put("125_2", 80);
		
		this.map.put("150_1", 150);
		this.map.put("150_2", 100);
		
		this.map.put("175_1", 175);
		this.map.put("175_2", 135);
		
		this.map.put("250_1", 250);
		this.map.put("250_2", 140);
	}
	
	public Integer getValue(Integer size){
		Integer out = size;
		Integer result = map.get(size+"_"+sizeFonts);
		if (result != null)
			out = result;			
		return out;
	}
	
	public Integer getMonitorWidth(){
		return this.width;
	}
	
	public Integer getMonitorHeight(){
		return this.height;
	}
	
	// Methods
	
	public static Size getInstance(){
		if (instance == null){
			instance = new Size();
		}
		return instance;
	}
	
	
}
