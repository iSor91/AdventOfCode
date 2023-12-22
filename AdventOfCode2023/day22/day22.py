import collections
from typing import Counter


lines=[]
with open('data') as f:
    lines=f.readlines()

cubes=list(map(lambda x: (x[0].split(','), x[1].split(',')),[l.strip().split('~') for l in lines]))
processed_cubes = []
for c in cubes:
    processed_cubes.append(list((zip(*c))))


print(max(map(lambda x: int(x[0][1]), processed_cubes)))
print(max(map(lambda x: int(x[1][1]), processed_cubes)))
print(max(map(lambda x: int(x[2][1]), processed_cubes)))

processed_cubes.sort(key=lambda x: int(x[2][1]))

top={}
cube_index = 0
analyzed_cubes = {}
for cube in processed_cubes:
    x=list(map(int,cube[0]))
    x_range=range(x[0], x[1]+1)
    y=list(map(int,cube[1]))
    y_range=range(y[0], y[1]+1)
    z=list(map(int,cube[2]))

    #x, y, z, [what it holds]=on it, [what holds it]=below it
    analyzed_cubes[cube_index]=(x,y,z,[],[])

    possible_supports=list(filter(lambda x: x[0] in [(i,j) for i in x_range for j in y_range], top.items()))
    # print(possible_supports)
    tops=list(map(lambda x: x[1], possible_supports))
    top_level=0
    supports=[]
    if(len(tops)!=0):
        top_level=max(map(lambda x: x[0], tops))
        supports=list(map(lambda y: y[1], filter(lambda x: x[1][0] == top_level, possible_supports)))
    # print(cube,tops, top_level, supports)
    diff=z[0]-1-top_level #diff to the highest possible touching point from the current bottom
    new_z_0 = top_level+1
    new_z_1 = z[1]-diff
    for i in x_range:
        for j in y_range:
            pos = (i,j)
            print(pos, z)
            # print(top[pos][0], z[0])
            top[pos]=(new_z_1, cube_index)

    for support in set(supports):
        analyzed_cubes[cube_index][4].append(support[1])
        analyzed_cubes[support[1]][3].append(cube_index)
        analyzed_cubes[cube_index][2][0] = new_z_0
        analyzed_cubes[cube_index][2][1] = new_z_1
    # print(top)
    cube_index+=1
    print()

do_not_disintegrate=set()
for c in analyzed_cubes:
    print(c, analyzed_cubes[c])
    current_supported_by=analyzed_cubes[c][4]
    if(len(current_supported_by) == 1):
        do_not_disintegrate.add(current_supported_by[0])

print(len(analyzed_cubes.keys()) - len(do_not_disintegrate))

def start_chain(current):
    print(f"Starting to check {current}")
    to_check=[]
    disintegrated=analyzed_cubes[current]
    for sub in disintegrated[3]:
        to_check.append(sub)
    checked=[]
    checked.append(current)
    falling=[]
    falling.append(current)

    while(len(to_check) != 0):
        cube_index=to_check[0]
        # print(f"Checking {cube_index} from check list {to_check}")
        to_check.remove(cube_index)
        cube=analyzed_cubes[cube_index]
        if(all([c in falling for c in cube[4]])):
            # print(f"{cube_index} would fall because {cube[4]} are all in {falling}")
            if(cube_index not in falling):
                falling.append(cube_index)
            for sub_cube_index in cube[3]:
                if(sub_cube_index not in checked and sub_cube_index not in to_check):
                    to_check.append(sub_cube_index)
        checked.append(cube_index)
        to_check.sort(key=lambda x: analyzed_cubes[x][2][0])

    # print(f"Disintegrating {current} results in {falling} which is {len(falling)-1}")
    return len(falling)-1

all_falling=0
for c in analyzed_cubes:
    all_falling+=start_chain(c)

print(f"All possible single disintegration sum is {all_falling}")
