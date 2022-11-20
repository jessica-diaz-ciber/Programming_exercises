#!/bin/python3
#Juego del Ahorcado
'''
- Usando paises del continente Americano
- El juego terminara si se han descubierto todas las letras del pais o el usuario se a equivocado 6 veces
'''
import random
import pdb


def find_indices(letra, secreto):
	index = -1
	while True:
		index = secreto.find(letra, index + 1)
		if index == -1:
			break
		yield index

def jugada(guiones, rep, ronda, vida, secreto):	
	while vida >= 0 or secreto != "".join(guiones):
		print(guiones)
		letra = input(f"[{ronda}]    > Dime una letra: ")
		ronda += 1
		if letra in secreto:
			indices = []
			for i in find_indices(letra, secreto):
				indices.append(i)
			if letra in guiones:
				rep = rep + 1
				try:  
					guiones[indices[rep]] = letra
				except:
					rep = rep - 1
					vida = vida - 1
					print(f"Te qeudan {vida} vidas")
			else:
				guiones[indices[0]] = letra
		if "_" not in guiones:
			print(guiones)
			print("Has ganado")
			exit()
		if letra not in secreto:
			if vida <= 1:
				print("Has perdido")
				exit()
			vida = vida - 1
			print(f"Te qeudan {vida} vidas")


paises = ['argentina','brazil','bolivia','chile','peru','ecuador','colombia','paraguay','uruguay','mexico','el salvador','costa rica','cuba','guatemala','honduras','panama','canada']
secreto = random.choice(paises)

print("[*] Adivina la plabra secreta: ")
ronda = 0
vida = 6
guiones = list(len(secreto) * '_')
rep = 0
jugada(guiones, rep, ronda, vida, secreto)

