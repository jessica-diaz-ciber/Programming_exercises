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


//private String nombre; 
//private Posicion pos; 

// ---- GETTERS Y SETTERS -----
//public void setNombre(String nombre) {this.nombre = nombre;}
//public String getNombre() { return nombre; } 
//public void setPos(Posicion pos) { this.pos = pos; }
//public Posicion getPos() { return pos; }

// ---- FUNCIONES --------------
//public void movY(int X) { 
//	if (this.pos.getPosX() == 0 || this.pos.getPosX() == Habitacion.ANCHO ){ this.pos.setPosX(this.pos.getPosX());	}
//	else this.pos.setPosX(this.pos.getPosX() + X );}

//public void movX(int Y) { 
//	if (this.pos.getPosY() == 0 || this.pos.getPosY() == Habitacion.ALTO ){ this.pos.setPosY(this.pos.getPosY());	}
//	else this.pos.setPosY(this.pos.getPosY() + Y);}


