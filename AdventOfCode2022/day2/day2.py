

played={'A': 1, 'B':2, 'C': 3, 'X': 1, 'Y': 2, 'Z': 3}

win=[-2, 1]
sum = 0
sum2 = 0
with open('day2') as day2:
    for l in day2:
        chose = (l.split())

        elf = played[chose[0]]
        me = played[chose[1]]

        sum += me + ( 6 if me - elf in win else 3 if me == elf else 0)

        if(me == 1):
            my = (elf + 1) % 3 + 1
        if(me == 2):
            my = elf
        if(me == 3):
            my = (elf) % 3 + 1
        sum2+=my
        sum2+=(me-1)*3

print(sum)
print(sum2)
