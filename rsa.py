#!/usr/bin/python3

def es_primo(number):
  for i in range(2,number):
    if (number % i) == 0:
      return False
  return True

def get_e(n,t):
	numbers = []
	valid_numbers = []
	for i in range(1,t):
		numbers.append(i)
		for number in numbers:
			if es_primo(number) and number not in valid_numbers:
				valid_numbers.append(number)
	posibles_llaves = []
	for valid_number in valid_numbers:
		si = "n"
		if t % valid_number != 0 and n % valid_number != 0:
			si = input(f"Llave actual [{valid_number}]. ¿Quieres otra (y/n)?: ")
			if si == "y":
				continue
			else:
				break
	return valid_number

def get_d(t,e):
	posibles = []
	init = 0
	end = 20
	while len(posibles) < 2:
		for d in range(init, end):
			if e * d % t == 1:  
				posibles.append(d)
		else:
			init += 20
			end += 20
	return posibles[1]

p = int(input("introduce un numero primo: "))
q = int(input("introduce otro numero primo: "))
n = p * q
t = n - p - q + 1 
e = get_e(n,t) 
d = get_d(t,e)

print("------------------------------")
print(f"p = {p}")
print(f"q = {q}")
print(f"n = {n}")
print(f"t = {t}")
print(f"e = {e}")
print(f"d = {d}")
