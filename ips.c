#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

// compilar gcc: añadir -lm: gcc ips.c -o ips -lm

int main(){
    void calc(int index, int octet, int mask, int baits[], int ip[]){
    int diff; int rangos; int interval; int net; int broad; int hosts;
    diff = mask - index; 
    rangos = baits[diff]; 
    interval = octet / rangos; 
    net = rangos * interval;
    broad = (rangos * (interval + 1)) - 1;
    hosts = pow(2,(32 - mask)); 
    if (octet == ip[1]){ 
        printf("\n > La direccion de red es %d.%d.0.0. Rangos de  %d en %d", ip[0], net, rangos, rangos);
        printf("\n > La gateway es %d.%d.0.1", ip[0], net );
        printf("\n > La broadcast es %d.%d.255.255", ip[0], broad);
        printf("\n > Hosts: %d.%d.0.2 <-> %d.%d.255.254 = %d",ip[0], net, ip[0], broad, hosts );}
    else if (octet == ip[2]) {
        printf("\n > La direccion de red es %d.%d.%d.0. Rangos de  %d en %d", ip[0],ip[1], net, rangos, rangos);
        printf("\n > La gateway es %d.%d.%d.1", ip[0], ip[1], net );
        printf("\n > La broadcast es %d.%d.%d.255", ip[0], ip[1], broad);
        printf("\n > Hosts: %d.%d.%d.2 <-> %d.%d.%d.254 = %d",ip[0],ip[1], net, ip[0],ip[1], broad, hosts );}
    else if (octet == ip[3]) { // 24
        printf("\n > La direccion de red es %d.%d.%d.%d. Rangos de  %d en %d", ip[0],ip[1], ip[2], net, rangos, rangos);
        printf("\n > La gateway es %d.%d.%d.%d", ip[0], ip[1],ip[2], net + 1 );
        printf("\n > La broadcast es %d.%d.%d.%d", ip[0], ip[1], ip[2], broad);
        printf("\n > Hosts: %d.%d.%d.%d <-> %d.%d.%d.%d = %d",ip[0],ip[1],ip[2], net+2, ip[0],ip[1],ip[2], broad-1, hosts );}
    }


    printf("Ingrese una direccion IP: ");   
    char str[12]; scanf("%s", str);  int i=0; int a;     int index; int octet;
    char delim[] = "."; char *octetos[5]; int noctetos[5];
    char *ptr = strtok(str, delim);
    if(ptr != NULL){ while(ptr != NULL){ octetos[i] = ptr; i++; ptr = strtok(NULL, delim);}}
    for (a = 0; a < 4; a++){ noctetos[a] = atoi(octetos[a]);}
    int mask; printf("Ingrese una mascara de subred: /"); scanf("%d", &mask);
     // Ya tenemos la IP y la mascara
    int indexes[3] = {8, 16, 24}; int baits[9] = {256,128,64,32,16,8,4,2,1};
     // Ver el tipo de mascara
    if (mask >= 8 && mask < 16){
        index = indexes[0]; octet = noctetos[1];  calc(index, octet, mask, baits, noctetos);
    } else if ( mask >= 16 && mask < 24){
        index = indexes[1]; octet = noctetos[2]; calc(index, octet, mask, baits, noctetos);
    } else if (mask >= 24){
        index = indexes[2]; octet = noctetos[3]; calc(index, octet, mask, baits, noctetos);}
    return 0;}
