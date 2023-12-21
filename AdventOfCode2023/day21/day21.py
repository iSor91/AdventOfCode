def add_tuple(t1, t2):
    return (t1[0]+t2[0], t1[1]+t2[1])

def sub_tuple(t1, t2):
    return (abs(t1[0]-t2[0]), abs(t1[1]-t2[1]))

def get_neighbors(current, plots):
    neighbors = [add_tuple(current, (0,1)), add_tuple(current,(1,0)), add_tuple(current, (0,-1)), add_tuple(current, (-1,0))]
    return [x for x in neighbors if x in plots]

def get_plots():
    with open('data') as f:
        lines=f.readlines()
        plots=[(i,j) for i,l in enumerate(lines) for j in range(len(l)) if l[j] in ['.','S']]
        start=[(i,j) for i,l in enumerate(lines) for j in range(len(l)) if l[j] == 'S'][0]
        right = len(lines[0])
        bottom = len(lines)

        return plots, right, bottom, start


step_count = 65
odd_reachable = []
even_reachable = []

plots, right, bottom, start = get_plots()

to_visit=[]
to_visit.append((start,0))
visited = {}

while(len(to_visit)!=0):
    current=list(to_visit)[0]
    pos = current[0]
    step = current[1]
    to_visit.remove(current)
    neighbors = get_neighbors(pos, plots)
    to_visit_pos = [x[0] for x in to_visit] 
    for n in neighbors:
        next_step=step+1
        if(n not in to_visit_pos and n not in visited):
            to_visit.append((n,next_step))
        if(next_step % 2 == 1 and n not in odd_reachable):
            odd_reachable.append(n)
        elif(next_step % 2 == 0 and n not in even_reachable):
            even_reachable.append(n)
        if (n in visited and next_step < visited[n]):
            visited[n]=next_step
    visited[pos]=step
    to_visit.sort(key=lambda x: x[1])


# print(to_visit)
# print(visited)


accessible = [p[0] for p in list(filter(lambda x: x[1] <= step_count and (step_count-x[1]) % 2 == 1, visited.items()))]

# for i in range(bottom):
#     for j in range(right-1):
#         if((i,j) in accessible):
#             print('.', end='')
#             pass
#         elif((i,j) not in accessible and (i,j) in even_reachable):
#             print('X', end='')
#         elif((i,j) in plots):
#             print(' ', end='')
#         else:
#             print('#', end='')
#     print()


extension = 202301
print(len(accessible))

all_squares_wo_center = (extension*extension*2)-(extension*2)
all_odd_squares = int(40925290000 * len(even_reachable))
all_even_squares = int(40925694600* len(odd_reachable))

print(all_odd_squares + all_even_squares + len(accessible))
print(all_squares_wo_center * len(odd_reachable) + len(accessible))
print(all_squares_wo_center * len(even_reachable) + len(accessible))
