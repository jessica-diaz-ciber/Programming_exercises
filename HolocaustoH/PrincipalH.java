package HolocaustoH;
import java.util.Scanner;

public class PrincipalH {
	public static void main(String[] args) {
		int accion = -1;
		Scanner in = new Scanner(System.in);
		
		
		// ----- PROPIEDADES --------------	
		ObjetoJuego puerta_entrada = new ObjetoJuego(); Posicion pIn = new Posicion(0,5);  puerta_entrada.setPos(pIn); puerta_entrada.setLetra_mapa('/'); 
		ObjetoJuego puerta_salida = new ObjetoJuego(); Posicion pOut = new Posicion(Habitacion.ALTO-1,19);  puerta_salida.setPos(pOut); puerta_salida.setLetra_mapa('/'); 
		Jugador jugador = new Jugador(); Posicion pJ = new Posicion(1,5); jugador.setPos(pJ); jugador.setLetra_mapa('J');
		Hadron hadron = new Hadron();  Posicion pH = new Posicion(3,5); hadron.setPos(pH); hadron.setLetra_mapa('H');  
		// ---- CONSTRUCCION DE CLASE -----
		Habitacion habInicial = new Habitacion(pIn, pOut, jugador);
		habInicial.setObjetoJuego(puerta_entrada); habInicial.setObjetoJuego(puerta_salida);
		habInicial.setObjetoJuego(jugador); habInicial.setObjetoJuego(hadron); 
		
		// ---- GETTERS Y SETTERS -----
		//habInicial.setPuerta_entrada(puerta_entrada);
		//habInicial.setPuerta_salida(puerta_salida);
		//
		//habInicial.setJugador(jugador);
		
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
					break;
				
			}
		}
	}

}
