package HolocaustoH;

public class Posicion {
	
	// Elementos 
	// --- Propiedades (todos int) -> PosicionX y PosicionY -> Posicion(int, int)
	// --- Funciones 
	//      ------> Comprobar si la posicion dada coincide con una puerta
	//      ------> Comprobar si la posicion dada coincide con las coordenadas de otra cosa
	
	// ----- PROPIEDADES --------------
	private int PosX; 
	private int PosY;
	
	// ---- CONSTRUCCION DE CLASE -----
	public Posicion() {  this.PosX = 0; this.PosY = 0;}
	public Posicion(int posx, int posy) {  this.PosX=posx; this.PosY=posy;	}
	
	// ---- GETTERS Y SETTERS -----
	public void setPosX(int posX) { PosX = posX; }
	public int getPosX() { return PosX; }
	public void setPosY(int posY) { PosY = posY;}
	public int getPosY() { return PosY; }
	
	// ----- FUNCIONES --------------
	public Boolean es_igual(Posicion pos) { 
		if (pos.PosX==this.PosX && pos.PosY==this.PosY) return true;
		else return false;}
	
}
