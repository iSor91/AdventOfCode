# shapes in order of falling:

# ####

# .#.
# ###
# .#.

# ..#
# ..#
# ###

# #
# #
# #
# #

# ##
# #


# chamber 7 units wide
# shapes appear at 
# - left 2 spaces from the left
# - bottom 3 spaces above the highest rock point / or bottom if there is none yet

# first pushed by a jet of hot air by one unit -> if not possible, it doesn't happen
# then falls down one unit -> if not possible, it stops, new rock appears

def add(a, b):
    return (a[0] + b[0], a[1] + b[1])

def print_cavern(top, occupied, shape=[]):
    for i in range(top,1):
        for j in range(7):
            if(i in occupied and (i,j) in occupied[i]):
                print('#', end='')
            elif((i,j) in shape):
                print('@', end='')
            else:
                print('.',end='')
        print()
    
    print()

def update_shape_to_current_pos(shape_top_left, shape_current):
    return [add(shape_top_left, shape_part) for shape_part in shape_current]


shapes = {
    0: [(0,0), (0,1), (0,2), (0,3)],
    1: [(0,1),(1,0),(1,1),(1,2),(2,1)],
    2: [(0,2),(1,2),(2,0),(2,1),(2,2)],
    3: [(0,0),(1,0),(2,0),(3,0)],
    4: [(0,0),(0,1),(1,0),(1,1)]
}

direction = {
    '<': (0, -1),
    '>': (0,  1)
}

with open('day17') as input:
    directions = list(map(lambda x: direction[x], [list(l.strip()) for l in input][0]))

occupied_row = {
    0: [(0,i) for i in range(7)]
}

rock_cnt = 1000000000000
j = 0

dir_to_rocks={}
for i in range(rock_cnt):
    # print(i, end='\r')
    top = min(occupied_row.keys())

    dir_to_rock=(j%len(directions), i%len(shapes))
    if(dir_to_rock in dir_to_rocks and i%len(shapes)==0):
        previous_state = dir_to_rocks[dir_to_rock]
        print(i, previous_state, top)
        height_diff = previous_state['top'] - top
        print(f"height diff = {height_diff}")
        rock_diff = i - previous_state['cnt']
        print(f"rock diff = {rock_diff}")
        rock_remaining = rock_cnt - previous_state['cnt']
        print(f"rocks remaining after first match = {rock_remaining}")
        same_iteration = rock_remaining//rock_diff
        print(f"iteration count to fill = {same_iteration}")
        add_these_yet = rock_remaining - same_iteration * rock_diff
        print(f"add these many rocks yet: {add_these_yet}")
        rocks_to_add = list(filter(lambda x: dir_to_rocks[x]['cnt'] == add_these_yet + previous_state['cnt'], dir_to_rocks))
        rock_to_add = list(map(lambda x: dir_to_rocks[x], rocks_to_add))[0]
        print(rock_to_add)
        print(same_iteration * height_diff - (rock_to_add['top']))

        # 1577207973640 not good
        # 1577207975413 >
        # 1577207977186
        # 1577207979848 ??
        # 1577207980269 <

        break
    dir_to_rocks[dir_to_rock]={'top': top, 'cnt': i}

    can_fall = True
    shape_current = shapes[i % 5]
    shape_top_left = (top-4-max(list(map(lambda x: x[0], shape_current))), 2)
    # print(top)
    # print(update_shape_to_current_pos(shape_top_left, shape_current))
    # print_cavern(top, occupied_row)

    while(can_fall):
        # print_cavern(shape_top_left[0], occupied_row, )
        dir = directions[j%len(directions)]
        can_move = True
        for shape_part in shape_current:
            shape_part_now = add(shape_top_left, shape_part)
            y,x = add(shape_part_now, dir)
            if((x < 0 or x > 6) or (y in occupied_row and (y,x) in occupied_row[y])):
                can_move = False
                break
        if(can_move):
            # print(dir, shape_current)
            shape_top_left = add(shape_top_left, dir)
        # else:
        #     print(f"no move {dir}")
        j+=1

        for shape_part in shape_current:
            shape_part_now = add(shape_top_left, shape_part)
            y,x = add(shape_part_now, (1,0))
            if(y in occupied_row and (y,x) in occupied_row[y]):
                can_fall = False
        if(not can_fall):
            for shape_part in shape_current:
                y,x = add(shape_top_left, shape_part)
                if(y not in occupied_row):
                    occupied_row[y] = []
                occupied_row[y].append((y,x))

        else:
            shape_top_left = add(shape_top_left, (1,0))


# top = min(occupied_row.keys())
# print_cavern(top, occupied_row)
print()

