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

    #north
    rolling=[roll for roll in new_rolling]
    rolling.sort(key=lambda x: x[1])
    rocks_ordered=[r for r in rocks]
    rocks_ordered.sort(key=lambda x: x[0],reverse=True)
    nr = []
    for r in rocks_ordered:
        moved=list(filter(lambda x: x[1]==r[1] and x[0] > r[0], rolling))
        for i,roll in enumerate(moved):
            nr.append((r[0]+i+1, roll[1]))
            rolling.remove(roll)

    for k,g in groupby(rolling, lambda x: x[1]):
        to_edge=list(g)
        for i,roll in enumerate(to_edge):
            nr.append((i, roll[1]))

    #west
    rolling=[roll for roll in nr]
    rolling.sort(key=lambda x: x[0])
    rocks_ordered=[r for r in rocks]
    rocks_ordered.sort(key=lambda x: x[1],reverse=True)
    nr = []
    for r in rocks_ordered:
        moved=list(filter(lambda x: x[0]==r[0] and x[1] > r[1], rolling))
        for i,roll in enumerate(moved):
            nr.append((roll[0], r[1]+i+1))
            rolling.remove(roll)

    for k,g in groupby(rolling, lambda x: x[0]):
        to_edge=list(g)
        for i,roll in enumerate(to_edge):
            nr.append((roll[0], i))

    #south
    rolling=[roll for roll in nr]
    rolling.sort(key=lambda x: x[1])
    rocks_ordered=[r for r in rocks]
    rocks_ordered.sort(key=lambda x: x[0],reverse=False)
    nr = []
    for r in rocks_ordered:
        moved=list(filter(lambda x: x[1]==r[1] and x[0] < r[0], rolling))
        for i,roll in enumerate(moved):
            nr.append((r[0]-i-1, roll[1]))
            rolling.remove(roll)

    for k,g in groupby(rolling, lambda x: x[1]):
        to_edge=list(g)
        for i,roll in enumerate(to_edge):
            nr.append((south-i-1, roll[1]))

    #east
    rolling=[roll for roll in nr]
    rolling.sort(key=lambda x: x[0])
    rocks_ordered=[r for r in rocks]
    rocks_ordered.sort(key=lambda x: x[1],reverse=False)
    nr = []
    for r in rocks_ordered:
        moved=list(filter(lambda x: x[0]==r[0] and x[1] < r[1], rolling))
        for i,roll in enumerate(moved):
            nr.append((roll[0], r[1]-i-1))
            rolling.remove(roll)

    for k,g in groupby(rolling, lambda x: x[0]):
        to_edge=list(g)
        for i,roll in enumerate(to_edge):
            nr.append((roll[0], east-i-1))

    # drw(south, east, nr)
    return nr

states={}

with open('data') as input:

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

    cycles=1000000000
    for i in range(cycles):
        new_rolling=[roll for roll in cycle(new_rolling)]
        new_rolling.sort(key=lambda x: x[0]*100 + x[1])
        key=",".join(list(map(lambda x: f"{x[0]},{x[1]}",new_rolling)))
        weight=sum(list(map(lambda x: south-x[0], new_rolling)))
        if(key not in states):
            states[key] = weight
            print(i, weight)
        else:
            first_occurence=list(states.keys()).index(key)
            index_from_first_occurence=(cycles-first_occurence-1)%(i-first_occurence)
            print(first_occurence, index_from_first_occurence, weight)
            print(states[list(states.keys())[first_occurence + index_from_first_occurence]])

            break

    # print(sum(list(map(lambda x: south-x[0]+1, new_rolling))))
