# Simples

De la tabla videogames_games, ordenar por año y mostrar el nombre del juego que fue publicado por Nintendo en el año 1995 en adelante, que no se llame “Donkey Kong 2”, que vendiera entre cero y 0,32 en Europa, entre cero y 0,31 en America, y entre cero y 0,3 en otros territorios.
```sql
SELECT name, year, platform_code FROM videogames_games WHERE year >= 1995 AND publisher = 'Nintendo'
AND na_sales BETWEEN 0 And 0.31 AND eu_sales BETWEEN 0 And 0.32 
AND jp_sales BETWEEN 0 And 0.3 and other_sales BETWEEN 0 And 0.3 and name NOT LIKE '%Kong%';
```
star_wars_characters. Nombre de los personajes de Star Wars que tengan los ojos distintos a rojo y que sean de los mundos Chandrila, Stewjon o Tatooine. Añadir un nuevo campo “tamaño” que nos diga si es “grande” cuando la altura  sea mayor o igual a 200 y “pequeño” si es menor de 200. Por último ordenar por altura de menor a mayor.
```sql
SELECT name, homeworld, eye_color, height,
CASE WHEN height >= 200 then 'Grande' ELSE 'Pequeño' END as tamaño
FROM star_wars_characters_2 WHERE eye_color <> 'yellow' AND
homeworld IN ('Chandrila','Stewjon','Tatooine')
```
De la tabla fortune, queremos ver sólo las 100 primeras compañías según el rango, sin incluir a Citigroup, viendo sólo las compañías dentro del sector financiero; industria diversified financials y crearemos un nuevo campo de rentabilidad en el que divida a las compañías entre rentables y no rentables si sus beneficios son mayores o menores a 1000.
```sql
SELECT company, rank, revenue,
CASE WHEN revenue >= 1000 THEN 'rentables' ELSE 'no rentables'
END AS rentabilidad FROM fortune 
WHERE company <> 'Citigroup' and sector = 'Financials' AND industry = 'Diversified Financials' AND rank <= 100
ORDER BY rank ASC
```
En la tabla gobierno_paro, filtrar por el mes de ‘Enero de 2018’ y el paro de hombres y mujeres de entre 25 y 45 años entre 1100 y 20 y ordenar los municipios con esos filtros de mayor paro resgistrado (del total) a menor. Por último, añadir una nueva columna llamada ‘Comparación de paro’, agrupando el número total de parados en: mucho si es mayor que 1000, medio si es mayor que 500 y poco si es por debajo o igual a 500.
```sql
SELECT  paro_hombre_edad_25__45, paro_mujer_edad_25__45, municipio, total_paro_registrado,
	CASE WHEN total_paro_registrado > 1000 THEN 'mucho' WHEN total_paro_registrado < 500 THEN 'poco' ELSE 'medio'
	END AS ' comparacion de paro' FROM gobierno_paro
WHERE mes = 'Enero de 2018' AND paro_hombre_edad_25__45 BETWEEN 20 and 1100
AND paro_mujer_edad_25__45 BETWEEN 20 and 1100 order by total_paro_registrado DESC 
```
En la tabla star_wars_characters, buscar el personaje que sea de un planeta que comience por la letra T y que no termine por la letra d, que mida más de 200 y que no sea un droide. Pista: el caracter comodin es %.
```sql
SELECT name, homeworld, species, height FROM star_wars_characters_2 
WHERE homeworld LIKE "T%" AND homeworld  NOT LIKE "%d" AND
species <> 'Droid' AND height > 200
```
En la tabla videogames_games, queremos ver los nombres de los juegos de platform_code ‘DS’ y años ordenados por año, con las ventas en las tres regiones principales (NA, EU y JP). 
Teniendo en cuenta sólo estas regiones (ignorar other_sales), queremos determinar en qué región ha vendido mejor cada juego. Para ello, generar un campo nuevo best_selling_region cuyo valor sea ‘NA’, si NA_sales es mayor que EU_sales y que JP_sales; ‘EU’ en caso de que EU_sales sea mayor que NA_sales y JP_sales o ‘JP’ en el el tercer caso.
```sql
SELECT name, year, na_sales, jp_sales, eu_sales,
CASE  WHEN na_sales > jp_sales AND na_sales > eu_sales then 'NA'
    WHEN eu_sales > na_sales AND eu_sales > jp_sales then 'EU' ELSE 'JP' 
END as Best_Selling_Region FROM videogames_games WHERE platform_code = 'DS' ORDER BY year  asc;
```
Cuales son las películas en USA, ordenados de forma descendente por duración en la tabla de imdb_movies. añadir el nuevo campo ‘’SON MUY LARGAS’’ a las que duren  de 120 minutos a 200  minutos y ‘’son cortas’’ las que duren menos 120.
```sql
SELECT movie_title, duration, 
CASE  WHEN duration > 200 then 'Larguisimas'
	WHEN duration BETWEEN 120 AND 200 then 'Muy largas' ELSE 'Son cortas' 
END as Duracion FROM imdb_movies WHERE country = 'USA' ORDER BY duration desc LIMIT 10;
```
De la tabla gobierno_paro, cuál es la cifra de mujeres de 45 años que estaban en paro, donde la cifra de parados sin empleo anterior era cero, durante el mes de marzo de 2018, ordenado por municipios por órden alfabético descendente.
```sql
SELECT paro_mujer_edad___45, municipio FROM gobierno_paro
WHERE mes = 'Marzo de 2018' AND paro_sin_empleo_anterior = 0
ORDER BY municipio DESC
```
Las 50 películas a color de USA, desde el año 2000 a 2010 con un campo nuevo llamado 'Calificación según Facebook' donde se evidencien tres variables: si tiene menos de 5.000 likes es 'Regular'; si tiene entre 5.000 y 10.000 likes es 'Buena' y si tiene más de 10.000 likes es 'Muy Buena'.
```sql
SELECT movie_title, title_year,
CASE  WHEN movie_facebook_likes < 5000 then 'Regular' WHEN movie_facebook_likes > 10000 then 'Muy Buena'
    ELSE 'Son cortas'  END as 'Calificación según Facebook'
FROM imdb_movies WHERE color = 'Color' AND  country = 'USA' AND title_year BETWEEN 2000 AND 2010
```
De la tabla star_wars_characters_2. Nombre de los personajes de Star Wars no humanos que midan menos de 200 y pesen más de 50, nacidos entre el año 20 y 60 y ordenados de más viejo a más joven. Añadir un nuevo campo “Rol” que nos diga si es un “Lord Sith” si la especie es Gungan, si no, que diga que es un “Parguela”.
```sql
SELECT name, birth_year, CASE  WHEN species = 'Gungan' THEN 'Lord Sith' ELSE 'Parguelas' END AS 'ROL'
FROM star_wars_characters_2 WHERE height < 200 and mass > 50 AND birth_year BETWEEN 20 AND 60 ORDER BY birth_year DESC
```
Imad: De la tabla star wars, crea una columna llamada ‘Lado de la Fuerza’ donde ‘Lado luminoso’  corresponda al personaje que mide menos de 200, es rubio y de ojos azules, por otro lado donde ‘Lado Oscuro’ corresponde al personaje que mide mas de 200, no tiene pelo y ojos amarillos, los dos personajes han nacido en el 42 y son humanos. Tras la columna de ‘Lado de la Fuerza’ tiene que ir la columna con el nombre de los dos personajes.
```sql
select case
	WHEN height < 200 AND eye_color = 'blue' AND hair_color = 'blond' then 'Lado luminoso'
    WHEN height > 200 AND eye_color = 'yellow' AND hair_color = 'none' THEN'Lado Oscuro'
END AS 'Lado de la Fuerza', name FROM star_wars_characters_2 WHERE species = 'Human' AND birth_year = 42
```
Alejandro: De la tabla star_wars_characters_2, muestrame una tabla donde aparezcan los personajes en orden alfabético, sin que salga ‘BB8’ y crea una campo llamado Heterocromia, tendrá que decir ‘Si’ si el personaje tiene heterocromía, ’No’ si no lo tiene y ‘desconocido’ si no hay información.
```sql
select case WHEn skin_color LIKE '%,%' THEN 'SI' WHEN skin_color = 'none' THEN 'Desconocido' ELSE 'NO'
END AS 'Heterocromia', name FROM star_wars_characters_2  where name <> 'BB8' ORDER By name
```
-------------
# Agregaciones
Cuentame cuantas peliculas habia en cada pais y muestra solo los paises por orden descendiente que tengan mas de 20 peliculas
```sql
SELECT COUNT(movie_title), country FROM imdb_movies GROUP BY country HAVING COUNT(movie_title) > 20 ORDER BY  COUNT(movie_title) desc
```
Agregar las siguientes métricas para todos los países africanos, (average gross income per capita, total population, number of countries) Tabla: world_health_org 
```sql
SELECT
	ROUND(avg(CASE WHEN continent = 'Africa' THEN gross_income_per_capita END),2) AS africa_AVG_GDP,
	SUM(CASE WHEN continent = 'Africa' THEN population_in_thousands END) AS total_population,
	COUNT (CASE WHEN continent = 'Africa' THEN country END) AS count_countries
FROM world_health_org;
```
Calcular número de personajes según planeta (homeworld). Evitar personajes sin información sobre planeta de origen. Tabla: star_wars_characters (`star_wars_characters`)
```SQL
SELECT homeworld, COUNT(name) AS count_characters FROM star_wars_characters
GROUP BY homeworld ORDER BY COUNT(name) DESC
```
Calcular el total de salario percibido por cada actor en todas las películas. Omitir películas sin data sobre salario. Tabla: james_bond (`sepe-sql-256409.SQL_Basics.james_bond`)
```SQL
SELECT actor, ROUND(SUM(Bond_Actor_Salary),1) as bond_salary FROM james_bond
WHERE Bond_Actor_Salary IS NOT NULL  GROUP BY 1 ORDER BY bond_salary DESC --en sqlite: bond_actor_salary != 'NA'
```
¿Podemos asegurar que las películas de acción tienen de media mejor valoración que el resto de películas? Extraer total de películas y media de IMDB score para películas de acción vs. el resto (de forma conjunta).
```sql
SELECT CASE WHEN gender LIKE '%Action%' THEN 'Action Movies' ELSE 'Rest of Movies' END as Genero,
COUNT(movie_title) as Total_movies, ROUND(AVG(imdb_score),2) as AVG_imdb_score FROM imdb_movies GROUP BY 1
```
Calcular la facturación (box office) según director. Filtrar por aquellos directores que hayan generado más de 1500 en el total de facturación (todas las películas). Tabla: james_bond
```sql
SELECT Director, ROUND(SUM(Box_Office)) as total_box_office FROM jamesbond GROUP BY 1
HAVING total_box_office>1500 ORDER BY total_box_office DESC
```
Calcular número total de álbumes según sub metal genre, filtrar por aquellos subgéneros con al menos 10 álbumes. Tabla: rolling_top_albums
```sql
SELECT  Sub_Metal_Genre , COUNT(Album) as count_albums FROM rolling_top_albums GROUP BY 1
HAVING count_albums>=10 ORDER BY count_albums DESC
```
¿Cuántos artistas hay incluídos en el dataset cuyo nombre incluye las palabras ‘god’, ‘death’ or ‘black’?
Tabla: rolling_top_albums
```sql
SELECT CASE
  WHEN upper(Artist) LIKE '%DEATH%' THEN 'DEATH' WHEN upper(Artist) LIKE '%BLACK%' THEN 'BLACK'
  WHEN upper(Artist) LIKE '%GOD%' THEN 'GOD' END AS artist_keyword,
COUNT(DISTINCT Artist) AS count_artist FROM `rolling_top_albums` WHERE artist_keyword != 'NULLL' GROUP BY 1 ORDER BY 2 DESC
```
Extraer media mensual del la cotización (open rate) y volumen de operación (volumen USD) del bitcoin desde el año 2016. Tabla: bitcoin_daily_rates
```sql
SELECT EXTRACT(MONTH FROM Date) as month, EXTRACT(YEAR FROM Date) as year,
  ROUND(AVG(Open),2) as AVG_open_rate, ROUND(AVG(Volume_USD)) as AVG_volume_USD
FROM Basics.bitcoin_daily_rates WHERE EXTRACT(YEAR FROM Date) >= 2016 GROUP BY 1,2 ORDER BY year,month
```

```sql
SELECT strftime('%m', Date) as month, strftime ('%Y', Date) as year,
  ROUND(AVG(Open),2) as AVG_open_rate, ROUND(AVG(Volume_USD)) as AVG_volume_USD
FROM bitcoin_daily_rates_formatdate WHERE  year >= '2016' GROUP BY 2,1
```
¿Cuál fue la semana con el valor mayor de cotización? Utilizar cotización high. Tabla: amazon_stocks
```sql
SELECT date, strftime('%W', date) AS num_semana, max(high) AS max_rate FROM amazon_stocks_formatdate
```
Calcular el total de sesiones según canal para octubre de 2019. Crear métricas
específicas agregadas para cada dispositivo. Tabla: google_analytics
```sql
SELECT 
	channelgrouping as Canal,
    sum(CASE WHEN devicecategory = 'mobile' THEN sessions end) AS 'mobile_sesions',
    sum(CASE WHEN devicecategory = 'desktop' THEN sessions end) AS 'desktop_sesions',
    sum(CASE WHEN devicecategory = 'tablet' THEN sessions end) AS 'tablet_sesions',
    sum(sessions) AS total
from google_analytics_formatdate where strftime('%Y-%m', date) = '2019-10'
group by channelgrouping
```
- sqlite `EXTRACT(year FROM date)=2019 AND EXTRACT(month FROM date)=10`
¿Cuántas películas duran menos de 60 minutos?; ¿Cuántas entre 60 y 100? Y ¿Cuántas más de 100?
```sql
SELECT 
	count(case when duration < 60 THEN movie_title END) AS duracion_menos_60,
	count(CASE WHEN duration between 60 and  100 THEN movie_title END) AS duracion_entre_60_y_100,
	count(CASE WHEN duration > 100 THEN movie_title END) AS duracion_mas_100
FROM imdb_movies
```
¿Cuál sería el día de menos cotización en una tendencia alcista en el año 2018? ¿Y la media ese mismo día?
```sql
SELECT date, min(high) AS cotizacion, -- -día de menos cotización 
ROUND((hight+low+open+close)/4,2) AS media_diaria -- -¿Y la media ese mismo día?
FROM bitcoin_daily_rates_formatdate WHERE strftime('%Y', date) = '2018' -- -en el año 2018?
```
Mostrar el conteo de las películas relacionadas con los géneros (Action, Crime, Comedy, Drama, Romance), indicando la película con mayor número de votos en cada caso (num_voted_users).
```sql
SELECT -- - SOLUCION CON SUBQUERYS QUE ME ARROJO CHATGPT Y QUE QUEDA FETEN
    COUNT(CASE WHEN gender LIKE '%Action%' THEN 1 END) AS 'Total Action',
    (SELECT movie_title FROM imdb_movies WHERE gender LIKE '%Action%' ORDER BY num_voted_users DESC LIMIT 1) AS 'Top Action',
     -- - igual con los otros generos
FROM imdb_movies;
```

```sql
SELECT CASE -- -SOLUCION DE ALBERTO
	WHEN gender LIKE '%Action%' THEN 'Action', -- -igual con el resto Crime, Comedy, Drama, Romance
	ELSE 'Otras' -- -Para los de genero NULL
END AS 'generos', COUNT(movie_title), MAX(num_voted_users) AS Max_numero de votos, movie_title 
FROM imdb_movies GROUP BY 1;
```
Mostrar el número de personajes que tienen el mismo color de ojos (eye_color) y el planeta de origen (homeworld). No mostrar color de ojos desconocidos (unknown) ni planetas sin datos/nombre (NA).
```sql
select count(name) as num_personajes, eye_color, homeworld FROM star_wars_characters_2
GROUP BY 2,3 HAVING eye_color != 'unknowm' AND homeworld != 'NA' AND num_personajes > 1 ORDER BY 1 DESC
```

Identificar y calcular el presupuesto de aquellas películas de James Bond que
fueron dirigidas por John Glen y protagonizadas por Timothy Dalton.
`select film,actor,director from jamesbond WHERE actor != 'Timothy Dalton' AND director = 'John Glen'`

¿Cuál es el monto de los créditos otorgados y no otorgados según el Status personal?
```sql
select personal_status, 
	sum(CASE WHEN class = 'good' THEN credit_amount END) AS Otorgado,
    sum(CASE WHEN class = 'bad' THEN credit_amount END) AS NO_Otorgado
from LoanData GROUP BY 1
```
Obtén un listado de las películas de acción con actor protagonista con más de 10000 likes en Facebook y cuyas películas hayan sido valoradas con al menos un 8 en imdb. Todo ello con fechas anteriores a 2012.
```sql
select movie_title, gender, actor_1_facebook_likes, imdb_score, title_year FROM imdb_movies
WHERE gender LIKE '%Action%'  AND actor_1_facebook_likes > 10000 AND imdb_score > 8 AND title_year < 2012 GROUP BY 3
```
Queremos saber cuáles son las 20 películas y género al que pertenecen, con mayor presupuesto y con mayor beneficio
`SELECT movie_title, gender, budget, gross FROM imdb_movies ORDER BY budget DESC LIMIT 20 -- -order by gross`

# Subquerys
Los directores de pelis de James bond han trabajado en promedio en dos peliculas cada uno
```sql
WITH tabla_1 AS
	(SELECT director, count(film) as num_pelis FROM jamesbond GROUP BY director)
SELECT avg(num_pelis) from tabla_1
```
En google_analythic indica para septiembre que canal ha sido el mas usado en cada dia
```sql
WITH tabla1 AS
	(SELECT date, channelgrouping AS canal, SUM(sessions) as total_sessions 
	FROM google_analytics_formatdate where strftime('%m', date) = '09' GROUP BY channelgrouping, date)
SELECT date, canal, max(total_sessions) AS maximo_sesiones from tabla1 GROUP BY date
```
Mostrar el conteo de las películas relacionadas con los géneros (Action, Crime, Comedy, Drama, Romance), indicando la película con mayor número de votos en cada caso (num_voted_users).
```sql
SELECT 
    COUNT(CASE WHEN gender LIKE '%Action%' THEN 1 END) AS 'Total Action',
    (SELECT movie_title FROM imdb_movies WHERE gender LIKE '%Action%' ORDER BY num_voted_users DESC LIMIT 1) AS 'Top Action',
     -- - igual con los otros generos
FROM imdb_movies;
```

# Joins
Extraer consolas discontinuadas y su fecha de lanzamiento (first retail availability). Agregar ventas globales de videojuegos publicados desde el año 2000. Ordenar según ventas totales de videojuegos (descendiente).
Query1 -> `SELECT console_name, platform_code, first_retail_availability from videogames_consoles WHERE discontinued <> ''`
Query2 -> `SELECT platform_code, round(sum(na_sales + eu_sales + jp_sales + other_sales),2) AS total_ventas from videogames_games where year > 2000 group by 1`
```sql
select vc.console_name, vc.platform_code, vc.first_retail_availability, 
round(sum(vg.na_sales + vg.eu_sales + vg.jp_sales + vg.other_sales),2) AS ventas_globales FROM videogames_consoles
AS vc INNER JOIN videogames_games AS vg ON vc.platform_code = vg.platform_code 
WHERE vc.discontinued <> '' AND vg.year >= 2000 GROUP BY vc.console_name
```