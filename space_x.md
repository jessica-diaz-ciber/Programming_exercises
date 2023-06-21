
---------------
Jessica Diaz
6/21/2023 

---------------

## 1. RECOGIDA DE DATOS

Recogemos los datos de la API en json y luego creamos de ahí un Dataframe. Seleccionamos solo las columnas que nos interesan
```python
import requests, datetime
import pandas as pd
import numpy as np

pd.set_option('display.max_columns', None); pd.set_option('display.max_colwidth', None) # pandas muestre el df completo 
resp = requests.get("https://api.spacexdata.com/v4/launches/past").json() # sacamos el json del get a la API
data = pd.json_normalize(resp) # creamos un datarame, podemos ver las 5 primeras filas (data.head(5) )
data = data[['rocket', 'payloads','success', 'launchpad', 'cores', 'flight_number', 'date_utc' ]]
```
--------------------------------------------------------------------------------
## 2. LIMPIEZA DE DATOS 

En algunas columnas hay mas de un dato (en cores y en payloads), como es dará problemas al exportarlo a una SQL, dejamos solo
el primer dato de esa columna. Formateamos la fecha para que sea digerible por pandas.
```python
data = data[data['cores'].map(len)==1]; data = data[data['payloads'].map(len)==1]; # Quitar datos de mas de un valor en x columna
data['cores'] = data['cores'].map(lambda x : x[0]) # reemplaza cada lista de 'cores' por su primer elemento.
data['payloads'] = data['payloads'].map(lambda x : x[0]) # igual con payloads
data['date'] = pd.to_datetime(data['date_utc']).dt.date # formateamos la fecha
```
------------------------------------------------------------

## MAS RECOGIDA DE DATOS
Recogemos mas datos de endpoints de la API y con funciones, metemos esos datos en nuevas listas

```python
LaunchSite = []; Longitude = []; Latitude = [] # launchpad (getLaunchSite)
BoosterVersion = [] # rocket (getBoosterVersion)
Payload = []; Customers= []; PayloadMass = []; Orbit = []  # payload 
Outcome = []; Flights = []; GridFins = []; Reused = []; Legs = []; LandingPad = [] # cores
Block = []; Mission_Outcome = []; ReusedCount = []; Serial = []

def getBoosterVersion(data): #Definimos la función
	for x in data['rocket']: # Iteramos por cada fila de la columna rocket
		if x: # si no esta vacia
			response = requests.get("https://api.spacexdata.com/v4/rockets/"+str(x)).json()
			BoosterVersion.append(response['name']) #Añadimos a BoosterVersion el valor con key 'name'
  
def getLaunchSite(data):
	for x in data['launchpad']:
		if x:
			resp = requests.get("https://api.spacexdata.com/v4/launchpads/"+str(x)).json()
			Longitude.append(resp['longitude']); Latitude.append(resp['latitude']); LaunchSite.append(resp['name']);

def getPayloadData(data):
	for x in data['payloads']:
		if x:
			resp = requests.get("https://api.spacexdata.com/v4/payloads/"+str(x)).json()
			Payload.append(resp['name']); Customers.append(resp['customers'])
			PayloadMass.append(resp['mass_kg']); Orbit.append(resp['orbit'])

def getCoreData(data):
	for core in data['cores']:
		if core['core'] != None:
			response = requests.get("https://api.spacexdata.com/v4/cores/"+core['core']).json()
			Block.append(response['block']); ReusedCount.append(response['reuse_count']); Serial.append(response['serial'])
		else:
			Block.append(None); ReusedCount.append(None); Serial.append(None) 
		Outcome.append(str(core['landing_success'])+' '+str(core['landing_type']))
		Flights.append(core['flight']); GridFins.append(core['gridfins']);Reused.append(core['reused'])
		Legs.append(core['legs']); LandingPad.append(core['landpad'])

getBoosterVersion(data); getCoreData(data); getLaunchSite(data); getPayloadData(data)
```
Exportamos esos datos junto a los del dataframe anterior en un nuevo diccionario, del que creamos un dataframe

```python
launch_dict = {'FlightNumber': list(data['flight_number']), 'Date': list(data['date']), 'BoosterVersion':BoosterVersion, 'PayloadMass':PayloadMass, 'Payload':Payload, 'Orbit':Orbit, 'LaunchSite':LaunchSite, 'LandingOutcome':Outcome, 'Flights':Flights, 'GridFins':GridFins, 'Reused':Reused, 'Legs':Legs, 'LandingPad':LandingPad, 'Block':Block, 'Customers': Customers, 'Mission_Outcome':list(data['success']), 'ReusedCount':ReusedCount, 'Serial':Serial, 'Longitude': Longitude, 'Latitude': Latitude}
launch_data = pd.DataFrame(data=launch_dict) # creamos dataframe
```
Limpiamos esos datos (dataframe con solo datos del Falcon9, reajuste de columns, rellenar valores vacios con valores promedio...)
Luego exportamos eso a un csv que podamos tratar por ejemplo con SQL
```python
data_falcon9 = launch_data[launch_data['BoosterVersion'] == "Falcon 9"] # dataframe con los Falcon 9 solo
data_falcon9.FlightNumber = list(range(1, data_falcon9.shape[0]+1)) # reajustar la columna FlightNumber
data_falcon9.isnull().sum() # vemos que muchos PayloadMass estan vacios asi que los rellenamos con los valores medios
mean_payload_mass = data_falcon9['PayloadMass'].mean()
data_falcon9['PayloadMass'] = data_falcon9['PayloadMass'].replace(np.nan, mean_payload_mass)
data_falcon9['Customers'] = data_falcon9['Customers'].map(lambda x: ','.join(x)) # que Customers no sea lista
data_falcon9.to_csv('dataset_part_1.csv', index=False) # Exportar todo a un csv final
```
--------------------------------------------------------------

## MODELOS
Con los datos que tenemos, podemos hacer modelos que nos revelen información

```python
df=pd.read_csv("./dataset_part_1.csv")
df['LaunchSite'].value_counts() # número de lanzamientos en cada plataforma.
df['Orbit'].value_counts() # igual para orbitas
```
Filtramos de los aterrizajes los fallidos (None y False) y lo exportamos a una lista "Class" donde si es un aterrizaje fallido ponemos 0 y si es exitoso 1. Esa lista sera una nueva columna
```python
landing_outcomes = df['LandingOutcome'].value_counts() # vemos si aterrizo o no y donde
bad_outcomes = set(filter(lambda key: 'False' in key or 'None' in key, landing_outcomes.keys())) # aterrizajes mal
df['Class'] = df["LandingOutcome"].apply(lambda x: 0 if x in bad_outcomes else 1).tolist() # 0 si mal, uno si bien
df.to_csv('dataset_part_2.csv', index=False) # Exportar todo a un csv final
```

Las 10 primeras columans del CSV (dataset_part_2.csv) parecen

|FlightNumber|Date      |BoosterVersion|PayloadMass      |Payload                  |Orbit|LaunchSite  |LandingOutcome|Flights|GridFins|Reused|Legs |LandingPad|Block|Customers |Mission_Outcome|ReusedCount|Serial|Longitude  |Latitude  |Class|
|------------|----------|--------------|-----------------|-------------------------|-----|------------|--------------|-------|--------|------|-----|----------|-----|----------|---------------|-----------|------|-----------|----------|-----|
|1           |2010-06-04|Falcon 9      |8191.079109589042|Dragon Qualification Unit|LEO  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |SpaceX    |True           |0          |B0003 |-80.577366 |28.5618571|0    |
|2           |2012-05-22|Falcon 9      |525.0            |COTS Demo Flight 2       |LEO  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |NASA(COTS)|True           |0          |B0005 |-80.577366 |28.5618571|0    |
|3           |2013-03-01|Falcon 9      |677.0            |CRS-2                    |ISS  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |NASA (CRS)|True           |0          |B0007 |-80.577366 |28.5618571|0    |
|4           |2013-09-29|Falcon 9      |500.0            |CASSIOPE                 |PO   |VAFB SLC 4E |False Ocean   |1      |False   |False |False|          |1.0  |MDA       |True           |0          |B1003 |-120.610829|34.632093 |0    |
|5           |2013-12-03|Falcon 9      |3170.0           |SES-8                    |GTO  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |SES       |True           |0          |B1004 |-80.577366 |28.5618571|0    |
|6           |2014-01-06|Falcon 9      |3325.0           |Thaicom 6                |GTO  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |Thaicom   |True           |0          |B1005 |-80.577366 |28.5618571|0    |
|7           |2014-04-18|Falcon 9      |2296.0           |CRS-3                    |ISS  |CCSFS SLC 40|True Ocean    |1      |False   |False |True |          |1.0  |NASA (CRS)|True           |0          |B1006 |-80.577366 |28.5618571|1    |
|8           |2014-07-14|Falcon 9      |1316.0           |Orbcomm-OG2-M1           |LEO  |CCSFS SLC 40|True Ocean    |1      |False   |False |True |          |1.0  |Orbcomm   |True           |0          |B1007 |-80.577366 |28.5618571|1    |
|9           |2014-08-05|Falcon 9      |4535.0           |AsiaSat 8                |GTO  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |AsiaSat   |True           |0          |B1008 |-80.577366 |28.5618571|0    |
|10          |2014-09-07|Falcon 9      |4428.0           |AsiaSat 6                |GTO  |CCSFS SLC 40|None None     |1      |False   |False |False|          |1.0  |AsiaSat   |True           |0          |B1011 |-80.577366 |28.5618571|0    |





Vemos con graficas los datos estrcturados de manera que nos diga donde tienen mas tasa de exito los aterrizajes
```python
df2 = df[['Orbit', 'LandingOutcome']]; df2.groupby(['Orbit', 'LandingOutcome']).size() # Df de las orbitas
df3 = df[['Orbit', 'Class']]; df3.groupby(['Orbit','Class']).size() # df de orbitas y si sale bien o mal
```
El modelo que mas se adapta es df4, que calcula el porcentaje de exito por sitio a lo largo del tiempo
```python
df4 = df[['LaunchSite', 'Class']]; df4.groupby(['LaunchSite','Class']).size() # df de sitios y si sale bien o mal
	totales = df4.groupby('LaunchSite')['Class'].size() # contar todos los lanzamientos de cada sitio
	validos = df4[df4['Class'] == 1].groupby('LaunchSite')['Class'].size() # filtrar solo los buenos
	porcentaje_exito_por_sitio = validos / totales # porcentaje de exito por sitio
porcentaje_exito_por_sitio
```
Si vemos las 5 primeras entradas de df4 vemos que estas dos columnas LaunchSite y Class
```
LaunchSite    Class
CCSFS SLC 40  0        23
              1        70
KSC LC 39A    0         5
              1        44
VAFB SLC 4E   0         3
              1        23
```
Y el haber claculado el porcentaje de exito nos muestra que:

|LaunchSite|
|----------|
|CCSFS SLC 40    0.752688|
|KSC LC 39A      0.897959|
|VAFB SLC 4E     0.884615|

El sitio con mas tasa de exito es por tanto 'KSC LC 39A'

--------------------------

## DATA VISUALIZATION
Visualizamos en una gráfica los datos correlacionados para sacar conclusiones

```python
import matplotlib.pyplot as plt
import seaborn as sns

sns.catplot(y="PayloadMass", x="FlightNumber", hue="Class", data=df, aspect = 2)
plt.xlabel("Flight Number",fontsize=20); plt.xticks(range(0, 170, 8)); plt.ylabel("Pay load Mass (kg)",fontsize=20)
plt.show()

sns.catplot(y="LaunchSite", x="FlightNumber", hue="Class", data=df, aspect = 2)
plt.xlabel("Flight Number",fontsize=20); plt.xticks(range(0, 170, 8)); plt.ylabel("Lugar de lanzamiento",fontsize=20)
plt.show()

sns.catplot(y="PayloadMass", x="LaunchSite", hue="Class", data=df, aspect = 2)
plt.xlabel("Lugar de lanzamiento",fontsize=20); plt.ylabel("Masa de carga (kg)",fontsize=20)
plt.show()

sns.catplot(y="Orbit", x="FlightNumber", hue="Class", data=df, aspect = 2)
plt.xlabel("Flight Number",fontsize=20); plt.xticks(range(0, 170, 8)); plt.ylabel("Tipo de Orbita",fontsize=20)
plt.show()

sns.catplot(y="PayloadMass", x="Orbit", hue="Class", data=df, aspect = 2)
plt.xlabel("Tipo de Orbita",fontsize=20); plt.ylabel("Masa de carga (kg)",fontsize=20)
plt.show()
```
