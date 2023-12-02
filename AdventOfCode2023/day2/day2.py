# red, green, or blue kubes
# random number of them in a bag

import re

content = [12, 13, 14]

games = {}

with open('data') as game_reveals:
    sum=0
    power=0
    for l in game_reveals.readlines():
        first_split=l.split(":")
        reveals=first_split[1]
        if(ma:=re.match(r'Game (\d+)', first_split[0])):
            game_num=ma.group(1)
            games[game_num] = []
        else:
            raise ValueError(f"game is not part of {first_split[0]}")
        separate_reveals = reveals.split(";")
        possible = True
        for reveal in separate_reveals:
            r=0
            g=0
            b=0
            print(reveal)
            if(m:=re.search(r"(\d+) red", reveal)):
                r=int(m.group(1))
            if(m:=re.search(r"(\d+) green", reveal)):
                g=int(m.group(1))
            if(m:=re.search(r"(\d+) blue", reveal)):
                b=int(m.group(1))

            # print(r, g, b)
            if(r > content[0] or g > content[1] or b>content[2]):
                possible = False

            games[game_num].append([r,g,b])

        if(possible):
            print(first_split[0],"is possible")

            sum = sum + int(game_num)

        r=0
        g=0
        b=0
        for game in games[game_num]:
            if(game[0] > r):
                r = game[0]
            if(game[1] > g):
                g = game[1]
            if(game[2] > b):
                b = game[2]

        power = power+(r*g*b)
        print(r*g*b)

    print(games)
    print(sum)
    print(power)

