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
	if (max > 0) printf("\n [*] La linea mas larga es: %s", longest);} // Si este maximo es mayor que 0 se printea

void ejercicio8(){ 
    int binsearch(int x,int v[],int n){
		int low = 0; int high = n -1; int mid;
    	while(low<=high){ // En cada vuelta la linea se va partiendo en dos y cogiendo la mitad donde este.
       		mid=(low+high)/2;
        	if(x<v[mid]) high=mid-1;
        	else if(x>v[mid]) low=mid+1;
        	else return mid;}} 
	printf("\n [Ejercicio chorra 8]: Buscar numero en una lista ordenada de numeros: \n");
	int lista_ordenada[]={1,6,90,120,220,469,875,675}; int num=121; int pos_encontrado = -1;
    pos_encontrado=binsearch(num,lista_ordenada,8);
    if(pos_encontrado>-1) printf("La posicion del numero %d es %d\n",num,pos_encontrado);
    else printf("Numero no encontrado");}
	// se suman los indices de los extremos para sacar el tamaño del array y se divide entre 2 para sacar el medio
	
void ejercicio9(){
	int shellshort(int v[], int num_elem){
		int gap,i,j,temp;
		for (gap=num_elem/2; gap>0; gap/=2) // 9/2 -> gap=4. Gap parte la lista en mitades cada vez mas chicas 
			for (i=gap; i<num_elem; i++) //  de v[4] a v[8] -> 6,120,469,220,875
				for (j=i-gap; (j>=0) && (v[j]>v[j+gap]) ;j-=gap){ // 2 = 6-4; 675 > 469?;
					temp=v[j]; v[j]=v[j+gap]; v[j+gap]=temp;}} // se intercambian estos dos  si 675 > 469
	printf("\n [Ejercicio chorra 9]: Ordenando una lista de numeros: \n");
	int lista []= {3,1,675,90,6,120,469,220,875}; int tamaño = sizeof(lista)/sizeof(lista[0]); int i;
	shellshort(lista, tamaño); for (i = 0; i < tamaño; i++) printf("%d ", lista[i]);}

void ejercicio10(){
	void swap(int *px, int *py){ int temp = *px; *px = *py; *px = temp; }
	int num1=20; int num2=35; swap(&num1, &num2);
	printf("\n [Ejercicio muy chorra 10]: Intercambiando con punteros los numeros 20 y 35: %d, %d\n", num1, num2);
	}

void ejercicio11(){
	void str_cpy(char *s, char *t){
		//int i=0; while ((s[i]=t[i])!='\0'); i++;} -> Obsoleto, no aprovecha los punteros
		while ((*s=*t)!='\0'){ s++; t++;}}
	int str_cmp(char *s, char *t){
		//for (int i=0; s[i]==t[i]; i++)  if(s[i]=='\0') return 0;
		for (*s == *t; s++; t++) if(*s=='\0') return 0;
		return *s - *t;}

	printf("\n [Ejercicio chorra 11]: Strcpy\n");
	char origen[] = "Paco tiene un coche amarillo"; char copia[30];
	str_cpy(copia, origen); printf("\n [*] Frase copiada -> %s; %d \n", copia, str_cmp(copia, origen));}

// Utilizar librerias propias:
// Tenemos la libreria creada gestorbuffer.c
//#define ALLOCSIZE 1000000
//static char allocbuff[ALLOCSIZE]; // ALLOCSIZE es el final el buffer o sea  allocbuff[1000000] de tamaño.
//static char *allocp = allocbuff; // *allocp apunta al principio de allocbuff.
//char * alloc(int n){ // Devuelve un puntero char a una reserva de tamaño "n"
//    if (allocbuff + ALLOCSIZE) - allocp >n ){ // si el espacio disponible es mayor que lo que solicito
//        allocp += n; return allocp - n;}  // actualiza el puntero de espacio libre (con menos obviamente)
//    else return 0;}
//void afree(char *p){
//    if (p >= allocbuff && p < allocbuff + ALLOCSIZE) allocp = p; } // liberar la memoaria.

// Que luego se define en gestorbuffer.h
// #define ALLOCSIZE 1000000
// char * alloc(int n);
// char afree(char *p);

// Y en el programa.c se puede meter con #include "gestorbuffer.h" y se pueden usar sus funciones pues.
// Luego se tienen que compilar todo en un solo programa -> gcc programa.c gestorbuffer.c -o programa

int main (){
//	ejercicio1();
//	ejercicio2();
//	ejercicio3();
//	ejercicio4();
//	ejercicio5();
//	ejercicio7();
//	ejercicio8();
//	ejercicio9();
//	ejercicio10();
	ejercicio11();
    return(0);}
