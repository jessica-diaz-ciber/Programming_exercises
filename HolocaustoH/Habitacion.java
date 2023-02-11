package HolocaustoH;

public class Habitacion {
	
	// Elementos 
	// --- Estaticas -> ANCHO Y ALTO. 
	// --- Propiedades -> Puertas de entrada y salida(Posicion), Jugador(Posicion)
	// --- Funciones 
	//      ------> Comprobar si la posicion dada coincide con una puerta
	
	
	// ----- PROPIEDADES --------------
	public static final int ANCHO=24;
	public static final int ALTO=6;
	
	//private Jugador jugador;
	private Personaje jugador;
	private Posicion puertaEntrada; 
	private Posicion puertaSalida;
	private ObjetoJuego ObjetosJuego[] = new ObjetoJuego[20];
	private int NumObjetos = 0;
	
	// ---- CONSTRUCCION DE CLASE -----
	public Habitacion(Posicion puerta_entrada, Posicion puerta_salida, Personaje jugador) {
		this.puertaEntrada=puerta_entrada;
		this.puertaSalida=puerta_salida;
		this.jugador=jugador;}
	
	// ---- GETTERS Y SETTERS -----
	//public void setJugador(Jugador jugador) { this.jugador = jugador;}
	//public void setPersonaje(Personaje p) {}
	//public void setPuerta_entrada(Posicion puertaEntrada) { this.puertaEntrada = puertaEntrada;} public Posicion getPuerta_entrada() { return puertaEntrada;}
	//public void setPuerta_salida(Posicion puertaSalida) { this.puertaSalida = puertaSalida;} public Posicion getPuerta_salida() { return puertaSalida;}
	
	public void setObjetoJuego(ObjetoJuego obj) { ObjetosJuego[NumObjetos] = obj; NumObjetos++;}
	public ObjetoJuego getObjetoJuego(int ObjPos){ return ObjetosJuego[ObjPos];}
	
	// ----- FUNCIONES --------------
	public int hayObjeto(Posicion p) {
		for (int i=0;i<NumObjetos;i++) {
			ObjetoJuego obj=ObjetosJuego[i];
			Posicion objPosicion=obj.getPos();
			if(p.es_igual(objPosicion)) return i;}
		return -1;}
	

	public Boolean es_una_puerta(Posicion pos) { 
		// El metodo es_igual es para no hacer if p.PosX || p.PosY tanto por puerta entrada como de salida.
		if (pos.es_igual(getPuerta_entrada()) || pos.es_igual(getPuerta_salida())) return true; 
		else return false;}

	public boolean es_un_jugador(Posicion pos) {
		if(pos.es_igual(jugador.getPos())) return true;
		return false;}

}
