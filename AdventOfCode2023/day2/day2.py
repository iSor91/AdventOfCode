import re
import math

colors=['red', 'green', 'blue']
content = [12, 13, 14]

sum=0
power=0

with open('data') as game_reveals:
    for l in game_reveals.readlines():
        if(ma:=re.match(r'Game (\d+)', l)):
            game_num=ma.group(1)
        else:
            raise ValueError(f"Game is not find in {l}")
        possible = True
        max=[0,0,0]
        for reveal in l.split(";"):
            current=[0,0,0]
            for i,c in enumerate(colors):
                if(m:=re.search(fr"(\d+) {c}", reveal)):
                    current[i]=int(m.group(1))
                    if(current[i]>max[i]):
                        max[i]=current[i]

            if(any([a>b for a,b in zip(current, content)])):
                possible = False

        if(possible):
            sum = sum + int(game_num)
        power = power+(math.prod(max))

print(sum)
print(power)
