package HolocaustoH;

public class ObjetoJuego {
	private String nombre;
	private Posicion pos;
	private char letra_mapa = ' ';
	
	public ObjetoJuego() {
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public Posicion getPos() {return pos;}
	public void setPos(Posicion pos) {this.pos = pos;}
	public char getLetra_mapa() {return letra_mapa;}
	public void setLetra_mapa(char letra_mapa) {this.letra_mapa = letra_mapa;}

}
