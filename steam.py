import requests
import re
import pdb
import json

lista_juegos = []
print("Introduce la url de un videojuego tal que asi:")
print(" ")
print("URL original de STEAM [https://store.steampowered.com/app/337000/Deus_Ex_Mankind_Divided/]" )
print("Parte a introducir: [337000/Deus_Ex_Mankind_Divided/]")
print("Cuando no quieras meter mas di 'salir'")

respuesta = None
while (respuesta != "salir"):
    respuesta = input("Di un juego -> ")
    lista_juegos.append(respuesta)
lista_juegos.pop()

contador = 1
for juego in lista_juegos:
    url = "https://store.steampowered.com/app/" + juego
    req = requests.get(url)
    nombre = re.findall(r'app/.*?/(.*?)/', url)[0]
    try:
        desarollador = re.findall(r'<a href="https://store.steampowered.com/developer/(.*?)?"',req.text)[0].split("?")[0]
    except:
        desarollador = re.findall(r'<a href="https://store.steampowered.com/search/(.*?)">', req.text)[1].split("?")[1].split("=")[1].split("&")[0]
    fecha = re.findall(r'<b>.*?</b>(.*?)<br>',req.text)[2]
    generos = [i.split("/")[1].replace("%20"," ") for i in re.findall(r'</a><a href="https://store.steampowered.com/tags/(.*?)"', req.text)]
    juego = {"nombre":nombre,
            "desarollador":desarollador,
            "fecha de lanzamiento":fecha,
            "generos": generos}

    juego = json.dumps(juego, indent = 4)
    print(f"[*] Juego nº {contador} -> {juego}")
    contador += 1

