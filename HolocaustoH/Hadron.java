package HolocaustoH;

public class Hadron extends Personaje{
	private boolean visible=true;

	public Hadron() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean cambio_visible() {
		if(visible) visible=false;
		else visible=true;
		return this.visible;
	}
}
