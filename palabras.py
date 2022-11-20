#!/usr/bin/python3

import itertools
import collections

#Globales
letras = []
combination = []


#Conseguir la lista de letras
print("\n [*] Este script buscará todas las combinaciones de caracteres ¡SIN repetir!")
print("    > cuando no quiera meter mas caracteres, escriba \"stop\"")
print("    > mete de 3 a 7 caracteres \n")

usuario = None
while usuario != "stop" and len(letras) < 7:
	usuario = input(" * Introduce un caracter: ")
	if len(usuario) > 1 and usuario != "stop":
		print("    > Mete solo uno por favor.")
	else:
		letras.append(usuario)
try:
	letras.remove("stop")
except:
	pass
print(f"La lista de caracteres es -> {letras}")

diccionario = dict(zip(letras,map(lambda x: letras.count(x),letras)))

#Funcion combinatoria
for n in range(3, len(letras) + 1):
	palabras_validas = []
	print(f"Combinaciones con {n} letras")
	combination = list(itertools.product(letras, repeat=n))
	lista = ["".join(i) for i in combination]
	unq = set(lista)
	for palabra in unq:
		diccionario_salida = collections.Counter(palabra)
		if diccionario.items() >= diccionario_salida.items():
			palabras_validas.append(palabra)
	print(palabras_validas)
