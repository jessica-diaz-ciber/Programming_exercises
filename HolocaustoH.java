import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Posicion {	
	private int PosX;  private int PosY;
	
	public Posicion() {  this.PosX = 0; this.PosY = 0;}
	public Posicion(int posx, int posy) {  this.PosX=posx; this.PosY=posy;	}
	
	public void setPosX(int posX) { PosX = posX; } public int getPosX() { return PosX; }
	public void setPosY(int posY) { PosY = posY;} public int getPosY() { return PosY; }
	public Boolean es_igual(Posicion pos) { 
		if (pos.PosX==this.PosX && pos.PosY==this.PosY) return true; else return false;}
	
}



class ObjetoJuego {
	private String nombre; private Posicion pos; private char letra_mapa = ' ';
	public ObjetoJuego() {}
	public String getNombre() {return nombre;} 	public void setNombre(String nombre) {this.nombre = nombre;}
	public Posicion getPos() {return pos;} public void setPos(Posicion pos) {this.pos = pos;}
	public char getLetra_mapa() {return letra_mapa;} 
	public void setLetra_mapa(char letra_mapa) {this.letra_mapa = letra_mapa;}
}

class Personaje extends ObjetoJuego{
	public void movY(int X) {
		Posicion pos =super.getPos();
		if (pos.getPosX() == 0 || pos.getPosX() == Habitacion.ANCHO ){ pos.setPosX(pos.getPosX());	}
		else pos.setPosX(pos.getPosX() + X );}

	public void movX(int Y) {
		Posicion pos =super.getPos();
		if (pos.getPosY() == 0 || pos.getPosY() == Habitacion.ALTO ){ pos.setPosY(pos.getPosY());	}
		else pos.setPosY(pos.getPosY() + Y);}
}

class Jugador extends Personaje{
	public Jugador() {}}

class Hadron extends Personaje{
	private boolean visible=true;
	public Hadron() {}
	public boolean cambio_visible() {
		if(visible) visible=false;
		else visible=true;
		return this.visible;}}

class Item extends ObjetoJuego{
	private int peso; public Item() {}
	public int getPeso() { return peso;} public void setPeso(int peso) { this.peso = peso;}}

class Habitacion {
	public static final int ANCHO=24; public static final int ALTO=6;

	private Personaje jugador;
	private Posicion puertaEntrada; private Posicion puertaSalida;
	private ObjetoJuego ObjetosJuego[] = new ObjetoJuego[20]; private int NumObjetos = 0;

	public Habitacion(Posicion puerta_entrada, Posicion puerta_salida, Personaje jugador) {
		this.puertaEntrada=puerta_entrada; this.puertaSalida=puerta_salida; this.jugador=jugador;}

	public void setObjetoJuego(ObjetoJuego obj) { ObjetosJuego[NumObjetos] = obj; NumObjetos++;}
	public ObjetoJuego getObjetoJuego(int ObjPos){ return ObjetosJuego[ObjPos];}

	public int hayObjeto(Posicion p) {
		for (int i=0;i<NumObjetos;i++) {
			ObjetoJuego obj=ObjetosJuego[i];
			Posicion objPosicion=obj.getPos();
			if(p.es_igual(objPosicion)) return i;} 
			return -1;}
}

class Juego {
	final static int LANZAR_DADO=1; final static int SALIR=0;

	public static void pintar_habitacion(Habitacion hab) {
		for (int fila=0; fila<Habitacion.ALTO; fila++){
			for (int columna=0; columna<Habitacion.ANCHO; columna++){
				Posicion actual= new Posicion(fila,columna);
				int posObj = hab.hayObjeto(actual);
				if (posObj != -1) {
					ObjetoJuego objJ = hab.getObjetoJuego(posObj);
					System.out.print(objJ.getLetra_mapa());}

				else if (columna==0 || columna==Habitacion.ANCHO-1) System.out.print("|");
				else if (fila==0 || fila==Habitacion.ALTO-1) System.out.print("=");
				else System.out.print(" ");
				if (columna==Habitacion.ANCHO-1) System.out.println();}}}

	public static void pintar_menu(){
		System.out.println("HolocaustoHadron ------------");
		System.out.println(" [1] Tirar dado");
		System.out.println(" [0] Salir");
		System.out.println("-----------------------------");}

	public static int lanzar_dado() {
		int dado = ThreadLocalRandom.current().nextInt(1,6);
		System.out.println(" [*] Dado lanzado: "+dado); return dado;}
	}

public class HolocaustoH {
	public static void main(String[] args) {
		int accion = -1;
		Scanner in = new Scanner(System.in);
		
		// ----- PROPIEDADES --------------	
		ObjetoJuego puerta_entrada = new ObjetoJuego(); Posicion pIn = new Posicion(0,5);  
		puerta_entrada.setPos(pIn); puerta_entrada.setLetra_mapa('/'); 
		ObjetoJuego puerta_salida = new ObjetoJuego(); Posicion pOut = new Posicion(Habitacion.ALTO-1,19);  
		puerta_salida.setPos(pOut); puerta_salida.setLetra_mapa('/'); 
		Jugador jugador = new Jugador(); Posicion pJ = new Posicion(1,5); 
		jugador.setPos(pJ); jugador.setLetra_mapa('J');
		Hadron hadron = new Hadron();  Posicion pH = new Posicion(3,5); 
		hadron.setPos(pH); hadron.setLetra_mapa('H');  
		
		// ---- GETTERS Y SETTERS -----
		Habitacion habInicial = new Habitacion(pIn, pOut, jugador);
		habInicial.setObjetoJuego(puerta_entrada); habInicial.setObjetoJuego(puerta_salida);
		habInicial.setObjetoJuego(jugador); habInicial.setObjetoJuego(hadron); 
		
		
		// ----- FUNCIONES --------------
		jugador.setNombre("Antonio"); System.out.println("Bienvenido " + jugador.getNombre());
		while (accion != 0) {
			Juego.pintar_habitacion(habInicial);
			Juego.pintar_menu();
			accion = in.nextInt();
			switch (accion) { 
				case Juego.LANZAR_DADO:
					int numMov = Juego.lanzar_dado();
					System.out.println("[MOV DCHA/IZQ] > Movimientos restantes: "+numMov);
					int numColumnas = in.nextInt(); // no dejar que se pase de movimientos
					while (numColumnas > numMov) { 
						System.out.println("[!] No tienes tantos movimientos"); 
						System.out.println("[MOV DCHA/IZQ] > Movimientos restantes: "+numMov);
						numColumnas = in.nextInt(); break;}
					jugador.movX(numColumnas); 
					if (numColumnas < 0 ) numMov=numMov+numColumnas; 
					else numMov=numMov-numColumnas;
					if (numMov > 0) {
						System.out.println("[MOV ARRIBA/ABAJO] > Movimientos restantes: "+numMov);
						int numFilas = in.nextInt(); 
						while (numFilas > numMov) { 
							System.out.println("[!] No tienes tantos movimientos"); 
							System.out.println("[MOV ARRIBA/ABAJO] > Movimientos restantes: "+numMov);
							numFilas = in.nextInt(); break;}
						jugador.movY(numFilas); 
						if (numColumnas < 0 ) numMov=numMov+numFilas; 
						else numMov=numMov-numFilas;}
					
					else System.out.println("[!] No hay mas movimientos disponibles");
					break;
				case Juego.SALIR:
					System.out.println("[!] Saliendo...");
					System.exit(1);
					break;}
			}}}
