#!/bin/bash

# Martingala -> apostar todo el rato a lo mismo, pero la siguiente vez el doble para que haya un 50%
# de recuperar y recuperes los jugado + un margen de beneficio.

# Labouvhere inverso
# [1 2 3 4]   (10$) > sumar los extremos (1 + 4 = 5) -> 5$ a impar   
# Si perdemos quitamos los extremos [2 3] (5$) al impar
# Si perdemos otra vez es otra secuancia [1 2 3 4] -> 5$ a impar 
# Si ganas se te paga el doble de lo apostado (o sea 10$), el beneficio va al final [1 2 3 4 5] (15$)
# Ahora son 6$ a aspostar al impar > (5 + 1 = $6) Pierdo -> [2 3 4] (6$)
# He ganado otra vez $6 -> [2 3 4 6] $8
# Gano -> [2 3 4 6 8] $10 > Gano [2 3 4 6 8 10] $12 > Gano [2 3 4 6 8 10 12] 14$ > Pierdo [3 4 6 8 10] 13$
# Gano -> [3 4 6 8 10 13] 16$
# Si llegas a beneficio de 50$ se resetea pero juegas con el dinero de la casa (los 50$)

function help(){
	echo -e "[*] Modo de uso"
	echo -e " -t Tecnica (martingala = 0 /labouchere = 1)"
	echo -e " -m Dinero a apostar"
	exit 1
}

function ctrl_c(){
	tput cnorm
	exit 1
}
trap ctrl_c INT
declare -i index=0

# -- Header/footer ----------------------- #

function footer(){
	money=$1
	ronda=$2
	mala_racha=$3
	dinero_maximo=$4
	
	echo -e "\n [*] Fin de partida!"
	echo -e "\t> Has jugado: $ronda rondas"
	echo -e "\t> Numero maximo de cagadas consecutivas: [$mala_racha]"
	echo -e "\t> Tienes una deuda de: $(echo $money | tr -d '-')\$"
	echo -e "\t> Has llegado a tener: $dinero_maximo\$"
	tput cnorm
	exit 1
}

# ---------------------------------------- #

function martingala(){
	money=$1
	echo -e "\n > Dinero: $money\$"
	echo -n " > ¿Cuanto dinero vas a apostar?: " && read initial_bet
	echo -n " > ¿Que deseas {par/impar}?: " && read par_impar
	echo -e "Apuesta > $initial_bet\$ / $par_impar / MARTINGALA"
	tput civis
	ronda=0
	mala_racha=""
	dinero_maximo=$money
	
	backup_bet=$initial_bet
	function ganas(){
		reward=$(($initial_bet*2))
		money=$(($money+$reward))
		initial_bet=$backup_bet
		let ronda+=1
		mala_racha=""
		if [ $money -gt $dinero_maximo ]; then
			dinero_maximo=$money
		fi
	}	
	function pierdes(){
		initial_bet=$(($initial_bet*2))
		let ronda+=1
		let mala_racha+=1
	}
	while true; do
		money=$(($money-$initial_bet))
		# Acabas de apostar: $initial_bet\$. Dinero total: $money\$"
		random_number="$(($RANDOM % 37))"
		if [ $money -ge 0 ]; then
			if [ "$(($random_number % 2))" -eq 0 ]; then
				if [ $random_number -eq 0 ]; then
					pierdes
				else
					if [ "$par_impar" == "par" ]; then
						ganas
					else
						pierdes
					fi
				fi
			else
				if [ $par_impar == "impar" ]; then 
					ganas
				else
					pierdes
				fi
			fi
		else
			footer $money $ronda $mala_racha $dinero_maximo
		fi
	done
	tput cnorm
}

# ----LA BOUCHERE------------------------- #

function labouchere(){
	money=$1
	bet=5
	echo -e "\n > Dinero: $money\$"
	echo -n " > ¿Que deseas {par/impar}?: " && read par_impar
	echo -e "Apuesta > $bet\$ / $par_impar / LABOUCHERE (INVERSA)"
	declare -a array=(1 2 3 4)
	echo -e "Secuencia -> [${array[@]}]\n"
	tput civis
	ronda=0
	renew=$(($money + 50)) # Almacena dinero que si es alzanzado hara que reseteemos la secuencia
	mala_racha=""
	dinero_maximo=$money

	function ganas(){
		array+=($bet)
		array=(${array[@]})	
		echo -e "\tGanas. Secuencia -> [${array[@]}]\n"
		reward=$(( $bet * 2))
		bak_money=$money
		money=$(($money+$reward))
		mala_racha=""
		if [ $money -gt $dinero_maximo ]; then
			dinero_maximo=$money
		fi

	}	
	function pierdes(){
		unset array[0]
		unset array[-1] 2>/dev/null
		array=(${array[@]})
		if [ ${#array[@]} -eq 0 ]; then 
			echo -e "\tPierdes. Secuencia -> [1 2 3 4]\n"
		else
			echo -e "\tPierdes. Secuencia -> [${array[@]}]\n"
		fi
		let mala_racha+=1
	}
	while true; do
		money=$(($money-$bet))
		if [ ${#array[@]} -gt 1 ]; then 
			bet=$(( ${array[0]} + ${array[-1]} ))
		elif [ ${#array[@]} -eq 1 ]; then
			bet=${array[0]}
		else # 0
			array=(1 2 3 4)
		fi

		if [ $money -gt $renew ]; then
			echo -e "\t > Nuestro dinero ha superado el tope de $renew asi que restableceremos la secuencia"
			array=(1 2 3 4)
			let renew+=50
			echo -e "\t > El tope se ha restablecido a $renew"
		elif [ $money -lt $(($renew - 50)) ]; then
			let renew-=100
			echo -e "\t > Hemos llegado a un minimo critico, reajustamos el tope a $renew"	
		fi
		let ronda+=1	
		echo -e "[Ronda $ronda] Acabas de apostar: $bet\$. Dinero total: $money\$"
		
		random_number="$(($RANDOM % 37))"
		echo "> Ha salido $random_number"
		if [ $money -ge 0 ]; then
			if [ "$(($random_number % 2))" -eq 0 ]; then
				if [ $random_number -eq 0 ]; then
					pierdes
				else
					if [ "$par_impar" == "par" ]; then
						ganas
					else
						pierdes
					fi
				fi
			else
				if [ $par_impar == "impar" ]; then 
					ganas
				else
					pierdes
				fi
			fi
		else
			footer $money $ronda $mala_racha $dinero_maximo
		fi
	done
	tput cnorm
}



# ---------------------------------------- #

while getopts "m:t:h" arg; do
	case $arg in
		m) money=$OPTARG;;
		t) tecnique=$OPTARG;;
		h) ;;
	esac
done

if [ $money ] && [ $tecnique ]; then
	echo -e "Dinero Inicial > ${money}"
	echo -e "Tecnica empleada > ${tecnique}"
	if [ $tecnique -eq 0 ]; then
		martingala $money
	elif [ $tecnique -eq 1 ]; then
		labouchere $money
	else
		help
	fi
else
 	help
fi



