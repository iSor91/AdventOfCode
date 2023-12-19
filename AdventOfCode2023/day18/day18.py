dir_map_1 = {
        "R": (0,1),
        "D": (1,0),
        "L": (0,-1),
        "U": (-1,0)
    }
dir_map = {
        0: (0,1),
        1: (1,0),
        2: (0,-1),
        3: (-1,0)
    }

corners = []

def mul_tuple(t, n):
    return (t[0]*n,t[1]*n)

def add_tuple(t1, t2):
    return tuple(map(sum,zip(t1,t2)))

with open ('test') as f:

    current = (0,0)
    lines=f.readlines()
    for l in lines:
        [dir, length, color] = l.strip().split(' ')

        instruction = color[2:-1]
        # dir = dir_map[int(instruction[-1])]
        # length = int(instruction[:-1], 16)
        print(instruction, dir, length)
        step=mul_tuple(dir_map_1[dir],int(length))
        print(step)
        corners.append(add_tuple(current, step))
    print(corners)


    top=min(map(lambda t: t[0], corners))
    bottom=max(map(lambda t: t[0], corners))
    left=min(map(lambda t: t[1], corners))
    right=max(map(lambda t: t[1], corners))
    span=[top,bottom,left,right]
    print(span, (bottom-top) * (right-left))
