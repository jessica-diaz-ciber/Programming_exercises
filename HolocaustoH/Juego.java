package HolocaustoH;
import java.util.concurrent.ThreadLocalRandom;

public class Juego {
	final static int LANZAR_DADO=1;
	final static int SALIR=0;
	
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
		System.out.println(" [*] Dado lanzado: "+dado);
		return dado;}
}



// PONERLE COORDENADAS
	//for (int fila=-1; fila<Habitacion.ALTO; fila++){
		//for (int columna=0; columna<=Habitacion.ANCHO; columna++){
			//else if (fila==-1 && columna=Habitacion.ANCHO) System.out.print(columna);
			//else if (columna==Habitacion.ANCHO && fila!=-1) System.out.print(fila);
