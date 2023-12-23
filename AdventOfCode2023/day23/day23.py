import collections


lines = None
with open ('data') as f:
    lines = f.readlines()


path = {(i,j): lines[i][j] for i in range(len(lines)) for j in range(len(lines[i]))}

start = (0,1)
bot = len(lines)-1
rig = len(lines[0])-1
finish_test = (19, 19)
finish_data = (135, 127)
finish = finish_data


print(start, finish)

forced_dirs = {
    '>': (0,1),
    '^': (-1,0),
    '<': (0,-1),
    'v': (1,0)
}

def add_tuple(t1,t2):
    x=(t1[0]+t2[0], t1[1]+t2[1])
    return x if x[0] >= 0 and x[0]<=bot and x[1]>=0 and x[1]<=rig else t1

def get_neighbours(current):
    # if(path[current] in '<>v^'):
    #     return [add_tuple(current, forced_dirs[path[current]])]
    neighbours = []

    left=add_tuple(current, forced_dirs['<'])
    if(left!=current and path[left]  != '#'):
        neighbours.append(left)

    right=add_tuple(current, forced_dirs['>'])
    if(right!=current and path[right] != '#'):
        neighbours.append(right)

    top=add_tuple(current, forced_dirs['^'])
    if(top!=current and path[top] != '#'):
        neighbours.append(top)

    bottom=add_tuple(current, forced_dirs['v'])
    if(bottom!=current and path[bottom] != '#'):
        neighbours.append(bottom)

    return neighbours

to_check = []
to_check.append([start])
checked = []
current=start
cnt = 0
while(len(to_check) != 0):
    cnt+=1
    current = to_check[0]
    to_check.remove(current)
    print(cnt, len(current), len(to_check), len(checked))

    neighbours = get_neighbours(current[-1])
    for n in neighbours:
        if(n not in current):
            next_step = [*current, n]
            if(n == finish):
                checked.append(next_step)
            elif(next_step not in to_check):
                to_check.append(next_step)

    # to_check.sort(key=lambda x: len(x), reverse=True)

checked = list(map(lambda x: len(x), list(filter(lambda x: x[-1]==finish, checked))))
checked.sort(reverse=True)
longest_path = checked[0]
print(longest_path)

# for i in range(rig):
#     for j in range(bot):
#         if((i,j) in longest_path):
#             print('O', end='')
#         elif((i,j) in path and path[(i,j)] !='#'):
#             print(path[(i,j)], end='')
#         else:
#             print(' ', end='')
#     print()
