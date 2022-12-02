

played={'A': 1, 'B':2, 'C': 3, 'X': 1, 'Y': 2, 'Z': 3}

lose=[2,-1]
win=[-2, 1]
sum = 0
sum2 = 0
with open('day2') as day2:
    for l in day2:
        chose = (l.split())
        sum+=played[chose[1]]

        elf = played[chose[0]]
        me = played[chose[1]]
        if(me - elf in win):
            sum+=6
        elif(me == elf):
            sum+=3

        if(me == 1):
            my = (elf + 1) % 3 + 1
            sum2+=my
        if(me == 2):
            my = elf
            sum2+=my
            sum2+=3
        if(me == 3):
            my = (elf) % 3 + 1
            sum2+=my
            sum2+=6
        print(my, me, elf)

print(sum)
print(sum2)
