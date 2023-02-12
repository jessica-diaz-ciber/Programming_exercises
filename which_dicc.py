#!/usr/bin/python3

import sys
import subprocess
try:
    from tabulate import tabulate
except ModuleNotFoundError:
    print("Install tabulate please [sudo pip install tabulate]")
    exit(1)
import re

arguments = ["domains", "subdomains", "chars", "wordpress"]

def help():
    funciones = [["domains", "http//web.com --> http//web.something.com   "],
                 ["subdomains", "http//web.com --> http//subdomain.web.com"],
                 ["chars", "Characters like: @ % &...                   "],
                 ["wordpress", "Plugins 'and' themes 'for' wordpress"]]

    head = ["Argument", "Definition"]
    print(tabulate(funciones, headers=head, tablefmt="pretty"))
    print("[USE] ------> python3 " + sys.argv[0] + " argument")
    print("  ---> Example: python3 " + sys.argv[0] + " domains")
    print("[list of arguments] -> {}" .format(arguments))



def param(arguments):
    param = sys.argv[1]
    if (param in arguments):
        return param
    else:
        help()
        exit(1)


def finder(parameter):
    proc = subprocess.Popen(["/usr/bin/find /usr/share/SecLists  -name *{}*" .format(parameter), ""], stdout=subprocess.PIPE, shell=True)
    (out,err) = proc.communicate()
    out = out.split()
    return out


def lines():
    line = "-"
    print(line * 100)


if len(sys.argv) == 1:
    help()

def installed():
    print("You don have the SecList diccionary... let's intall it")
    print("The SecLits is a big thins, it might take some minutes")
    print("If it says [permission denied], run it as sudo")
    try:
        proc = subprocess.check_call(["/usr/bin/which git", ""], shell=True)
        Repo_Path = "https://github.com/danielmiessler/SecLists.git"
        subprocess.Popen(['git', 'clone', str(Repo_Path), '/usr/share/SecLists/' ])
        print("SecList installed in /usr/share/ directory")
        exit(1)
    except subprocess.CalledProcessError as Error:
        print("Install [sudo apt install git]")
        exit(1)
  
if len(sys.argv) == 2:
    parameter = param(arguments)
    paths = finder(parameter)
    if paths != []:
        lines()
        print(" Parameter -->  {} " .format(parameter))
        lines()
        for i in paths:
            i = i.decode('utf-8')
            if not re.match(r".*vscode.*", i):
                print("   {}" .format(i))
        lines()
    else:
        installed()
