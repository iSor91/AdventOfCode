from itertools import groupby

rocks=[]
rolling=[]

south = 0
east = 0

def drw(max_r, max_l, nr):
    for i in range(max_r):
        for j in range(max_l):
            if(i,j) in rocks:
                print('#', end='')
            elif(i,j) in nr:
                print('O', end='')
            else:
                print('.', end='')
        print()
    print()

def cycle(new_rolling) :
    rolling=[roll for roll in new_rolling]
    nr = []
    for r in rocks:
        moved=list(filter(lambda x: x[1]==r[1] and x[0] > r[1], rolling))
        for i,roll in enumerate(moved):
            nr.append((r[0]+i+1, roll[1]))
            rolling.remove(roll)
    for k,g in groupby(rolling, lambda x: x[1]):
        to_edge=list(g)
        for i,roll in enumerate(to_edge):
            nr.append((i, roll[1]))

    drw(south, east, nr)

    return nr

with open('test') as input:

    for i,l in enumerate(input.readlines()):
        rocks.append([(i,j) for j,x in enumerate(l) if x == '#'])
        rolling.append([(i,j) for j,x in enumerate(l) if x == 'O'])
        south=i
        east = len(l)-1

    south+=1
    rocks = [item for row in rocks for item in row]
    rolling = [item for row in rolling for item in row]
    new_rolling = [roll for roll in  rolling]

    drw(south, east, rolling)
    for i in range(5):
        print(i)
        # new_rolling=[roll for roll in cycle(new_rolling)]
        # drw(south, east, new_rolling)


    print(sum(list(map(lambda x: south-x[0]+1, new_rolling))))
