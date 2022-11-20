#!/usr/bin/Python3

import subprocess
import re
from tabulate import tabulate
import sys

valor_euro = float(0.88)

header = "$$$$$$$$$  [BITCOINxii].py  $$$$$$$$$$"
print(header)


def function(command):
	proc = subprocess.Popen(["{}" .format(command), ""], stdout=subprocess.PIPE, shell=True)
	(out,err) = proc.communicate()
	out = out.split()
	list = []
	for i in out:
		i = i.decode('utf-8')
		list.append(i)
	return list

url = "https://www.blockchain.com/es/btc/unconfirmed-transactions"
adress_url = "https://www.blockchain.com/btc/address/"
trans_url = "https://www.blockchain.com/btc/tx/"

#Saca el grueso de la página (html to text)

curled = "curl -s {} | html2text" .format(url)
list = function(curled)


def filter_func(command):
	p = subprocess.Popen(["{}" .format(command)], stdout=subprocess.PIPE, stdin=subprocess.PIPE, stderr=subprocess.PIPE)
	out, err = p.communicate(input=list.encode())
	print(out)

def filter(pattern, list):
	array = []
	for i in list:
		if re.match(pattern,i):
			array.append(i)
	return array

def inspect():
	hash = sys.argv[1]
	url = trans_url + hash
	curled = "curl -s {} | html2text" .format(url)
	list = function(curled)
	list = list[80:]
	fecha_indx = list.index('Date') + 1
	status_indx = list.index('Status') + 1
	print("\n [Transaccion] --> {}" .format(sys.argv[1]))
	print("------------------------------------------------------------------------------------------")
	print(" A fecha de {} ->  Transaccion = {}" .format(list[fecha_indx], list[status_indx]))

	input_indx = list.index('Input') + 1
	print("    <Entradas totales: +{}" .format(list[input_indx]))
	output_indx = list.index('Output') + 1
	print("    >Salidas totales: -{}" .format(list[output_indx]))

	#Cuenta_origen
	print("\n [Origen]")
	from_indx = list.index('From') + 1
	to_indx = list.index('To')
	inputs = list[from_indx:to_indx]
	n=3
	output = [inputs[i:i + n] for i in range(0, len(inputs), n)]
	for i in output:
		print("  < {} --> {}{}" .format(i[0], i[1], i[2]))
	#Cuenta destino
	print("\n [Destinatarios]")
	start_indx = list.index('To') + 1
	end_indx = list.index('This')
	inputs2 = list[start_indx:end_indx]
	output2 = [inputs2[i:i + n] for i in range(0, len(inputs2), n)]
	for w in output2:
		print("  > {} --> {}{}" .format(w[0], w[1], w[2]))
	print("\n")

def adress():
	adress = sys.argv[1]
	url = adress_url + adress
	curled = "curl -s {} | html2text" .format(url)
	list = function(curled)
	init_indx = list.index('This') + 1
	finish_indx = list.index('Show') - 1
	list = list[init_indx:finish_indx]
	#Transacciones
	trans_indx =list.index('transacted') + 1
	#Recibidos
	received = list.index('received') + 4
	received_euros = str(list.index('received') + 2)
	received_euros = conversor2(received_euros)
	#Enviados
	sent = list.index('sent') + 4
	sent_euros = str(list.index('received') + 2)
	sent_euros = conversor2(sent_euros)
	#Pasta
	money = str(list.index('is') + 3)
	money = conversor2(money)

	print("\n [Adress] --> {}" .format(sys.argv[1]))
	print("-------------------------------------------------------------------------------------------")
	print("  [Saldo total de la cuenta] --> {}€" .format(money))
	print("  [CAntidad de transacciones realizadas] --> {} \n" .format(list[trans_indx]))
	print("  <[Dinero recibido] : {} BTC  --> + {}€" .format(list[received], received_euros))
	print("  >[Dinero gastado] : {} BTC  --> - {}€" .format(list[sent], sent_euros))


def conversor(dolares):
	dolares = [x.replace(".", "") for x in dolares]
	dolares = [x.replace(",", ".") for x in dolares]
	dolares = [float(x) for x in dolares]
	euros = [x * valor_euro for x in dolares]
	euros = [round(x,2) for x in dolares]
	return euros

def conversor2(x):
	dolares = x.replace(".", "")
	dolares = x.replace(",", ".")
	euros = float(x)
	euros = euros * valor_euro
	euros = round(euros,2)
	return euros


def main():
	#Hashes
	p_hashes = r".*\w{60,65}.*"
	hashes = filter(p_hashes, list)
	#Tiempo
	p_tiempo = r"\d{2}:\d{2}"
	time = filter(p_tiempo, list)
	#Bitcoins
	p_bitcoins = r"\d{1,6}\.\d{8}"
	bitcoins = filter(p_bitcoins, list)
	#Dolares
	p_dolares = r".*[$]"
	dolares = filter(p_dolares, list)
	dolares = [x.replace("Â US$", " ") for x in dolares]
	euros = conversor(dolares)

	#total
	total = []

	for i in hashes:
		n_indx = hashes.index(i)
		trans = []
		d = ","
		action = i + d + time[n_indx] + d + bitcoins[n_indx] + d + str(euros[n_indx]) + "€"
		trans = action.split(",")
		total.append(trans)

	head = ["Hash", "Hora", "Bitcoins", "Euros"]
	print(tabulate(total, headers=head, tablefmt="pretty"))
	return hashes

if len(sys.argv) == 1:
	main()

elif len(sys.argv) == 2:
	if len(sys.argv[1]) > 60:
		inspect()
	if len(sys.argv[1]) > 20 and len(sys.argv[1]) < 50:
		adress()
