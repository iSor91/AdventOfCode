knots_count = 10
knots = [(0,0) for i in range(knots_count)]

instructions = {
    'R': (0,1),
    'L': (0,-1),
    'U': (1,0),
    'D': (-1,0)
}

tail_pos = [(0,0)]

def add(t1,t2):
    return tuple(t1[i] + t2[i] for i in range(len(t1)))

def sub(t1,t2):
    return tuple(t1[i] - t2[i] for i in range(len(t1)))

def one(t):
    return tuple(int(t[i]/abs(t[i])) if t[i] != 0 else 0 for i in range(len(t)))

with open('day9') as input:
    commands = [[l.split()[0], int(l.split()[1])] for l in input]
    for c in commands:
        cu = instructions[c[0]]
        for i in range(c[1]):
            knots[0] = add(knots[0], cu)
            for j in range(knots_count-1):
                diff = sub(knots[j], knots[j+1])
                if(any(map(lambda x: abs(x)>1,diff))):
                    print(f"update needed {knots[j]} {knots[j+1]}")
                    move = one(add((0,0), diff))
                    print(f" change: {move}")
                    knots[j+1] = add(knots[j+1], move)

            tail_pos.append(knots[knots_count-1])

print(len(set(tail_pos)))