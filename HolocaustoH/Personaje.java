package HolocaustoH;

public class Personaje extends ObjetoJuego{
	// Elementos 
	// --- Porpiedades -> Nombre(String) y Posicion(Posicion) -> heredados
	// --- Funciones 
	//      ------> Movimiento
	
	// ---- FUNCIONES --------------
	public void movY(int X) { 
		Posicion pos =super.getPos();
		if (pos.getPosX() == 0 || pos.getPosX() == Habitacion.ANCHO ){ pos.setPosX(pos.getPosX());	}
		else pos.setPosX(pos.getPosX() + X );}
	
	public void movX(int Y) { 
		Posicion pos =super.getPos();
		if (pos.getPosY() == 0 || pos.getPosY() == Habitacion.ALTO ){ pos.setPosY(pos.getPosY());	}
		else pos.setPosY(pos.getPosY() + Y);}
}	

public class Jugador extends Personaje{
	public Jugador() {}}

public class Hadron extends Personaje{
	private boolean visible=true;
	public Hadron() {}
	public boolean cambio_visible() {
		if(visible) visible=false;
		else visible=true;
		return this.visible;}}


