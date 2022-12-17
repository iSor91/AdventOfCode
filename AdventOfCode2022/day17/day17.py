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

def print_cavern(top, occupied):
    for i in range(top,1):
        for j in range(7):
            if((i,j) in occupied[i]):
                print('#', end='')
            else:
                print('.',end='')
        print()



class rock:

    def __init__(self, rocks):
        self.rocks = rocks
    
    def __str__(self):
        return ''.join(str(self.rocks))
    
    __repr__ = __str__

shapes = {
    0: rock([(0,0), (0,1), (0,2), (0,3)]),
    1: rock([(0,1),(1,0),(1,1),(1,2),(2,1)]),
    2: rock([(0,2),(1,2),(2,0),(2,1),(2,2)]),
    3: rock([(0,0),(1,0),(2,0),(3,0)]),
    4: rock([(0,0),(0,1),(1,0)])
}

direction = {
    '<': (0, -1),
    '>': (0,  1)
}

with open('day17_test') as input:
    directions = list(map(lambda x: direction[x], [list(l.strip()) for l in input][0]))

rock_cnt = 2
top = 0
occupied_row = {
    0: [(0,i) for i in range(7)]
}

occupied_col = {i:[] for i in range(7)}
print(occupied_col)

for i in range(rock_cnt):
    can_fall = True
    shape_current = shapes[i % 5]
    shape_top_left = (top-3, 2)

    print(occupied_row)
    while(can_fall):
        for shape_part in shape_current.rocks:
            shape_part_now = add(shape_top_left, shape_part)
            y,x = add(shape_part_now, (1,0))
            if(y in occupied_row and (y,x) in occupied_row[y]):
                can_fall = False
        if(not can_fall):
            print(shape_current, shape_top_left)
            for shape_part in shape_current.rocks:
                y,x = add(shape_top_left, shape_part)
                if(y not in occupied_row):
                    occupied_row[y] = []
                occupied_row[y].append((y,x))
                occupied_col[x].append((y,x))
                if(y < top):
                    top = y
        else:
            shape_top_left = add(shape_top_left, (1,0))
            print(shape_top_left)



print_cavern(top, occupied_row)

