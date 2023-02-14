#include <stdio.h>
#include <stdlib.h>

void ejercicio1(){
	printf("\n [Ejercicio chorra 1]: Conversion de Celsius a Farenheit: \n");
	for (int far=0; far <= 100; far += 20){
		printf("%3dºC -> %3.1fºF \n",far, (5.0/9.0)*(far -32.0)); }}

void ejercicio2(){
	printf("\n [Ejercicio chorra 2]: Repetidor de caracteres: \n");
	int c= getchar();
	while ( c != '.' ){ printf("%c",c); c = getchar();}}

void ejercicio3(){
    printf("\n [Ejercicio chorra 3]: Contador de caracteres: \n");
    long nc = 0;   printf("    > Frase: "); 
    while ( getchar() != '\n') nc ++ ;   
	printf("    > Numero de caracteres: %ld\n", nc); }

void ejercicio4(){
    printf("\n [Ejercicio chorra 4]: Analizador de texto: \n");
	int c=0; int nl=1; int nw=0; int nc=0;
	# define IN 1
	# define OUT 0
	int state = OUT;
	while ((c=getchar())!=EOF){ // EOF = Ctrl + D
		nc++;
		if (c =='\n') nl++;
		if (c ==' ' || c=='\n' || c=='\t' ) state=OUT;
		else if(state==OUT){ nw++; state=IN;}} 
	printf("\n [*] Numero de lineas: %d\tcaracteres: %d\tpalabras: %d\n", nl, nc, nw);}

void ejercicio5(){
	printf("\n [Ejercicio chorra 5]: Contador de caracteres con arrays: \n");
	int c = 0; int nwhite = 0; int nother = 0;
	int ndigit[10]; for (int i=0; i<10;i++) ndigit[i]=0;   // Lista de numeros del 1 al 10
	while ((c=getchar())!=EOF){ 
		if(c>='0' && c<='9') ++ndigit[c-'0'];  //al restarle 0 a los caracteres ascii se trasnforma en int.
		else if (c ==' ' || c=='\n' || c=='\t' ) nwhite++;
		else nother++; }
	printf("\n [*] Digitos: "); for (int i=0; i<10;i++) printf("%d, ", ndigit[i]);
	printf("\t Espacios blancos: %d,\totros: %d\n",nwhite, nother);}

void ejercicio6(){
	printf("\n [Ejercicio chorra 6]: Potencias y funciones: \n");
	int power(int base, int numero){
		int resultado = 1;
		for(int i =1;i <=numero; i++)  resultado = resultado * base;
		return resultado;}
	for (int i=0; i<10; i++) printf("| %5d | %5d | %7d |\n", power(2,i), power(3,i), power(4,i));}

void ejercicio7(){
    printf("\n [Ejercicio chorra 7]: Arrays de texto: \n");
	# define MAXLINE 1000
	
	int get_line(char line[], int maxline){
		// Esta funcion lee con getchar() caracteres que meten en "linea", 
		// len es indice de caracter de "linea" pero a su vez su tamaño.
		int c,len; 
		for (len=0; (len < maxline - 1) && ((c = getchar())!= EOF) && (c!='\n'); len++) 
			line[len] = c; // De 1 hasta el tamaño tope - 1/EOF/caracter "\n", lee un caracter y ponlo en la linea
		if (c == '\n') { line[len] = c; len++; } // Si el caracter es salto de linea  ponlo y cuentalo tambien.
		line[len]='\0'; //  Ultimo caracter -> "\0" (fin de lectura) -> por eso maxline - 1
		return len;} 

	void copy(char to[], char from[]){
		int len = 0; while (( to[len] = from[len]) != '\0') len++;} 
		// Mueve cada caracter hasta el de final ('\0') de la linea actual a la mas larga

	char line[MAXLINE]; char longest[MAXLINE]; int len; int max = 0; 
	while ( (len = get_line(line, MAXLINE)) > 0 )// getline(linea actual, cuanto debe leer) -> int tamaño_linea
		if (len > max){ // Si se supera el maximo
			max = len; copy(longest, line);} // Se actualiza este maximo y la linea se copia en longest
	if (max > 0) printf("\n La linea mas larga es: %s", longest);} // Si este maximo es mayor que 0 se printea

int main (){
	ejercicio1();
//	ejercicio2();
//	ejercicio3();
//	ejercicio4();
//	ejercicio5();
	ejercicio7();
    return(0);}
