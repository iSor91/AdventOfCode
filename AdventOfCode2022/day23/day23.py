# elves are on the plain
# they are trying to figure out where to move
# if there are no elves on the adjacent fields, the elves don't move
# they try to move to N, S, W, E
# they consider each direction in order, if one fits, they propose to go there
# a direction is valid if there is no elf in any of the direction - e.g. no elf is on the adjacent NE, N, and NW
# after each proposed a move, they try to move. if two or more wants to move to the same place, no one moves there, they stay at their place.
# after the move turn, the consideration directions move in round robin, one so from NSWE -> SWEN

# how many empty fields are on round 10 in the smallest rectangle that can contain each elf?

directions = [
    [(-1,-1), (-1,0), (-1,1)],  #NORTH
    [(1,-1), (1,0), (1,1)],  #SOUTH
    [(-1,-1), (0,-1), (1,-1)],  #WEST
    [(-1,1), (0,1), (1,1)]   #EAST
]

elves = {}
with open('day23') as input:
    lines = [list(l.strip()) for l in input]

    id = 0
    for y in range(len(lines)):
        line = lines[y]
        for x in range(len(line)):
            if(line[x] == '#'):
                elves[id] = (y,x)
                id+=1


def check_positions(current_positions, current_pos, start_dir_index):
    valid_dirs = []
    for i in range(len(directions)):
        check_dirs = [(d[0] + current_pos[0], d[1]+ current_pos[1]) for d in directions[(i + start_dir_index)%len(directions)]]
        if(not any(c in current_positions for c in check_dirs)):
            valid_dirs.append(check_dirs[1])
    return valid_dirs

def get_ranges(elves):
    positions = list(elves.values())
    max_x = max(positions, key=lambda e: e[1])[1]
    max_y = max(positions, key=lambda e: e[0])[0]
    min_x = min(positions, key=lambda e: e[1])[1]
    min_y = min(positions, key=lambda e: e[0])[0]
    return min_x, max_x, min_y, max_y

def print_elves(elves):
    offset = 0
    x1,x2,y1,y2 = get_ranges(elves)
    elves_ids = {elves[e]: e for e in elves }
    for y in range(y1-offset,y2+offset+1):
        for x in range(x1-offset,x2+offset+1):
            # if(x==0 and y ==0):
            #     print(0,end='')
            if ((y,x) in elves.values()):
                # print(elves_ids[(y,x)], end='')
                print('#', end='')
            else:
                print('.', end='')
        print()

i = 0
while(True):
    print(f"STEP {i}")
    change = 0
    elves_update = {}
    proposes = {}
    current_positions = {elves[id]: id for id in elves}
    for e in elves:
        propose = check_positions(current_positions, elves[e], i%len(directions))
        # print(e, elves[e],  propose)
        if(propose and len(propose) != 4):
            if(propose[0] not in proposes):
                proposes[propose[0]] = []
            proposes[propose[0]].append(e)
            change+=1
        else:
            elves_update[e] = elves[e]

    for p in proposes:
        if(len(proposes[p]) > 1):
            for e in proposes[p]:
                elves_update[e] = elves[e]
        else:
            elves_update[proposes[p][0]] = p

    elves.clear()
    elves = elves_update
    if(change == 0):
        break
    i+=1
    # print(f"changes = {change}")
    # print_elves(elves)
    # print()

print(i)
x1,x2,y1,y2 = get_ranges(elves)
print(x1, x2, y1, y2, (x2-x1+1) * (y2-y1+1) - len(elves))