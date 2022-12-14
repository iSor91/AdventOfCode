task = 1

rock = '#'
sand = 'o'
empty = '.'

sand_start = (500, 0)

slice = {}
columns = {}

max_y = 0
offset = 2

def calculate_rocks(coordinates):
    global min_x, max_x, max_y
    for c in coordinates:
        for i in range(len(c)-1):
            startx, starty = c[i]
            endx, endy = c[i+1]

            tmp_max_y = max(starty, endy)

            max_y = tmp_max_y if tmp_max_y > max_y else max_y

            x_diff = endx - startx
            y_diff = endy - starty
            l = max(abs(x_diff), abs(y_diff))
            x_step = x_diff / l
            y_step = y_diff / l

            for j in range(l):
                coor = (int(startx + x_step * j), int(starty + y_step * j))
                slice[coor] = rock
                add_to_column(coor)

        last = (c[-1][0], c[-1][1])
        slice[last] = rock
        add_to_column(last)

def print_slice():

    x_values = list(map(lambda x: x[0], slice.keys()))
    min_x = min(x_values)
    max_x = max(x_values)
    for y in range(max_y + offset+1):
        for x in range(min_x - offset, max_x + offset):
            if((x,y) in slice):
                print(slice[(x,y)], end='')
            elif(y == max_y+offset and task == 2):
                print(rock, end='')
            else:
                print(empty, end='')
        print()

def step_sand(s):
    # print(s)
    ns = has_stop(s)
    cs = (ns[0],ns[1]-1)

    ns1 = (ns[0]-1, ns[1])
    ns2 = (ns[0]+1, ns[1])
    if(ns1 not in slice and (ns1[1] != max_y + offset or task == 1)):
        return step_sand(ns1)
    if(ns2 not in slice and (ns2[1] != max_y + offset or task == 1)):
        return step_sand(ns2)
    return cs

def has_stop(s):
    occupied = columns[s[0]] if s[0] in columns else []
    stoppers = list(filter(lambda x: x[0] == s[0] and x[1] > s[1], occupied))
    if(task == 2):
        stoppers.append((s[0], max_y + 2))
    stoppers.sort(key=lambda x: x[1])
    # print(stoppers)
    return stoppers[0]

def add_to_column(s):
    if(s[0] not in columns):
        columns[s[0]] = []
    columns[s[0]].append(s)
    

def day14(file):
    with open(file) as input:
        coordinates = [[[int(c) for c in l.strip().split()[i].split(',')] for i in range(0, len(l.strip().split()), 2)] for l in input]
        calculate_rocks(coordinates)
        i = 0
        while(True):
            try:
                final=step_sand(sand_start)
                slice[final] = sand
                add_to_column(final)
                print(i, end='\r')
                i+=1
                if(final == sand_start):
                    print('lyuk betomve')
                    break
            except BaseException:
                break
        
        print_slice()
        

day14('day14')
# print(slice)
print(len(list(filter(lambda x: x == sand, slice.values()))))

