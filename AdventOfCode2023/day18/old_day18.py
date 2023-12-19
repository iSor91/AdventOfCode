
trench_edges = []
trenche_filling = []
edge_colors = {}
up=0
left=0
right=0
down=0

dir_map = {
        'R': (0,1),
        'L': (0,-1),
        'U': (-1,0),
        'D': (1,0)
        }


def add_tuple(t1, t2):
    return tuple(map(sum,zip(t1,t2)))

def neighbours(t):
    return [
            add_tuple(t, (-1,0)),
            add_tuple(t, (0,1)),
            add_tuple(t, (1,0)),
            add_tuple(t, (0,-1))
        ]

def fill_trenche():
    to_visit = []
    to_visit.append((1,1))
    while (len(to_visit) != 0):
        fill=to_visit.pop()
        for nf in neighbours(fill):
            if(nf not in trenche_filling and nf not in to_visit and nf not in trench_edges):
                to_visit.append(nf)
        trenche_filling.append(fill)

print('calculating trench edge')
with open ('data') as f:

    current = (0,0)
    lines=f.readlines()
    for l in lines:
        [dir, length, color] = l.strip().split(' ')
        # print(dir, length, color, current)

        dir_coord = dir_map[dir]
        for i in range(int(length)):
            current = add_tuple(current, dir_coord)
            trench_edges.append(current)
            edge_colors[current]=color
        if(current[1] < left):
            left=current[1]-1
        if(current[0] < up):
            up=current[0]-1
        if(current[0] > down):
            down=current[0]+1
        if(current[1] > right):
            right=current[1]+1

print('calculating trench interior')
fill_trenche()

print(len(trench_edges), '+', len(trenche_filling), '=', len(trench_edges) + len(trenche_filling))
        

# print(up,left,down,right)
# for i in range(up,down):
#     for j in range(left,right):
#         if((i,j) in trenche_filling):
#             print('8', end='')
#         elif((i,j) in trench_edges):
#             print('#', end='')
#         else:
#             print(' ', end='')
#     print()



