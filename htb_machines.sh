#!/bin/bash

# ORIGINAL AUTHOR: S4VITAR
# I ADDED THE -W PARAMETER AND SHORTENED A BIT OF CODE, MY AESTHETIC IS ALSO DIFFERENT.

# Variables Globales -------------------------- #

tit="\e[7m"
end="\033[0m"
red="\e[0;31m\033[1m"
grn="\e[0;32m\033[1m"
yllw="\e[0;33m\033[1m"
enc="\033[0m\e[0m"

main_url="https://htbmachines.github.io/bundle.js"
file='/usr/local/bin/utilities/web.js'
tmp_file='/usr/local/bin/utilities/$tmp_file'

# --------------------------------------------- #

function salir(){
	echo -e "\n\n${red}[!] Saliendo${enc}"
	tput cnorm && exit 1
}

trap salir INT

# --------------------------------------------- #  

declare -i param_count=0   
separador="\n --------------------------------------------"
declare -i flag_diff=0
declare -i flag_so=0


function ayuda(){
	echo -e "\n --- ${tit}USO${end} ------------------------------------ \n "
	echo -e "   ${yllw}[ -h ] -> Panel de ayuda ${enc}"
	echo -e "   ${yllw}[ -u ] -> Descargar archivos necesarios ${enc}"
	echo -e "   ${yllw}[ -m ] -> Buscar por nombre de maquina ${enc}"
	echo -e "   ${yllw}[ -i ] -> Buscar por IP ${enc}"
	echo -e "   ${yllw}[ -h ] -> Buscar por dificultad ${enc}"
	echo -e "   ${yllw}[ -o ] -> Buscar por S.O ${enc}"
	echo -e "   ${yllw}[ -s ] -> Buscar por skill ${enc}"
	echo -e "   ${yllw}[ -w ] -> Listar vulns ${enc}"
	echo -e "${separador}\n"
}

function busca_ip(){
	ip="$1"
	maquina=$(cat $file | grep "$ip" -B 3 | grep "name:" | awk '{print $2}' | tr -d '",')
	buscar $maquina
}

function nivel(){
	dificultad="$1"
	result=$(cat $file | grep "dificultad: \"$dificultad\"")
	if [ "$result" ]; then
		echo -e "\n --- ${tit}DIFICULTAD: ${dificultad}${end} ------------------------------------------------ \n "
		cat $file | grep "dificultad: \"$dificultad\"" -B 5 | grep "name: " | awk '{print $2}' | tr -d '",' | column
		echo -e "----------------------------------------------------------------------------------\n"
	else
		echo -e "\n ${red} [!] Has puesto mal la dificultad, niveles disponibles ${enc}"
		echo -e "\n ||   Fácil   ||   Medio   ||   Difícil   ||   Insane   ||"
	fi
}

function busca_so(){
    so="$1"
    result=$(cat $file | grep "so: \"$so\"" )
    if [ "$result" ]; then
        echo -e "\n --- ${tit}SO: ${so}${end} ------------------------------------------------ \n"
        cat $file | grep "so: \"Linux\"" -B 5 | grep "name: " | awk '{print $2}' | tr -d '",' | column
		echo -e "----------------------------------------------------------------------------------\n"
    else
        echo -e "\n ${red} [!] Has puesto mal es Sistema Operativo, hay solo Linux o Windows ${enc}"
    fi
}

function busca_so_diff(){
	dificultad=$1
	so=$2
	result=$(cat $file | grep "so: \"$so\"" -C 5 | grep "dificultad: \"$dificultad\"" )
	if [ "$result" ]; then
		echo -e "\n --- ${tit}SO: ${so} Y  DIFICULTAD: ${dificultad}${end} --------------------------- \n"
		cat $file | grep "so: \"$so\"" -C 5 | grep "dificultad: \"$dificultad\"" -B 5 | grep "name: " | awk '{print $2}' | tr -d '",' | column
		echo -e "----------------------------------------------------------------------------------\n"
	else
		echo -e "\n ${red} [!] Algo has tenido que poner mal ${enc}"
	fi
}

function busca_skill(){
	skill=$1
	result=$(cat $file | grep "skills: " -B 6  | grep -i "$skill")
	if [ "$result" ]; then
		echo -e "\n --- ${tit}SKILL: ${skill}${end} ------------------------------------ \n"
		cat $file | grep "skills: " -B 6  | grep -i "$skill" -B 6 | grep "name: " | awk '{print $NF}' | tr -d '",' | column
		echo -e "----------------------------------------------------------------------------------\n"
	else
		echo -e "\n ${red} [!] La vuln/skill no existe, prueba la opcion -w para buscar vulns ${enc}"
	fi
}

function buscar(){
	maquina="$1"
	busqueda="$(cat $file | awk "/name: \"$maquina\"/,/resuelta:/" | grep -vE "id:|sku:|resuelta:|name:" | tr -d '",' | sed 's/^ */  > /' | sed 's/:/ ->/')"
	if [ "$busqueda" ]; then
		echo -e "\n --- ${tit}MAQUINA: ${maquina}${end} ------------------------ \n "
		echo -e "\n$busqueda"
		echo -e "${separador}\n"
	else
		echo -e "\n ${red} [!] La maquina no existe bro :V ${enc}"
	fi
}

function busca_vulns(){
	vuln="$1"
	echo -e "\n --- ${tit}VULNS RELACIONADAS CON: ${vuln}${end} ------------- \n "
	cat $file | grep "skills: " | tr -d '"' | sed "s/skills://" | sed 's/^  */ - /g' |  sed 's/ - /\n/g' | sed '/^ *$/d' | tr -d "," | sed 's/] /]\n/g'| sed 's/) [A-Z]/)\n/g' | sort -u | grep -i "$vuln"
}

function actualizar(){
	echo -e "\n ## ${tit} COMPROBANDO ACTUALIZACIONES ${end} ####### \n "
	tput civis
	if [ ! -f $file ]; then
		echo -e "\n ${tit}[*] Descargando/actualizando archivos necesarios${end} ---"
		sleep 2
		curl -s "${main_url}" > $file || echo -e "\n${red}[!] Ha habido un problema con el servidor${enc}"
		cat $file | js-beautify | sponge $file
		echo -e "\n${yllw} [!] Todos los archivos ya han sido descargadors ...${enc}"
		echo -e "${separador}\n"
	else
		curl -s ${main_url} > $tmp_file
		cat $tmp_file | js-beautify | sponge $tmp_file
		if [ $(diff <(md5sum $file | awk '{print $1}') <(md5sum $tmp_file |  awk '{print $1}') | wc -l) -eq 0 ]; then
			echo -e "\n ${yllw}[!] No hay actualizaciones${enc}"
			rm -rf $tmp_file
		else
			mv $tmp_file $file
			echo -e "\n${yllw} [!] El archivo ha sido  actualizado${enc}"
		fi
	fi
	tput cnorm
}



# --------------------------------------------- #

while getopts "m:ui:hd:o:s:w:" arg; do
	case $arg in
		m) maquina="$OPTARG"; let param_count+=1;;
		u) let param_count+=2;;
		i) ip="$OPTARG"; let param_count+=3;;
		d) dificultad="$OPTARG"; flag_diff=1; let param_count+=4;;
		o) so="$OPTARG"; flag_so=1; let param_count+=5;;
		s) skill="$OPTARG"; let param_count+=6;;
		w) vuln="$OPTARG"; let param_count+=7;;
		h) ;;
	esac
done

if [ $param_count -eq 1 ]; then
	buscar $maquina
elif [ $param_count -eq 2 ]; then
	actualizar
elif [ $param_count -eq 3 ]; then
    	busca_ip $ip	
elif [ $param_count -eq 4 ]; then
    	nivel $dificultad
elif [ $param_count -eq 5 ]; then
	busca_so $so
elif [ $param_count -eq 6 ]; then
	busca_skill "$skill"
elif [ $param_count -eq 7 ]; then
	busca_vulns "$vuln" 
elif [ $flag_diff -eq 1 ]  && [ $flag_so -eq 1 ]; then
	busca_so_diff $dificultad $so
else
	ayuda
fi

# --------------------------------------------- #
