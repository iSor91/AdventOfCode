lines = None
with open ('data') as f:
    lines = f.readlines()

start = (0,1)
bot = len(lines)-1
rig = len(lines[0].strip())-1

finish = (bot,rig-1)

choke_points=[]
choke_points.append(start)
choke_points.append(finish)

finish_test = (19, 19)
finish_data = (135, 127)
pre_finish=finish_data
post_start=(5, 9)

path = {(i,j): lines[i][j] for i in range(len(lines)) for j in range(len(lines[i])) if lines[i][j] != '#'}

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
    if(left!=current and left in path):
        neighbours.append(left)

    right=add_tuple(current, forced_dirs['>'])
    if(right!=current and right in path):
        neighbours.append(right)

    top=add_tuple(current, forced_dirs['^'])
    if(top!=current and top in path):
        neighbours.append(top)

    bottom=add_tuple(current, forced_dirs['v'])
    if(bottom!=current and bottom in path):
        neighbours.append(bottom)

    return neighbours


for p in path:
    if(len(get_neighbours(p)) > 2):
        choke_points.append(p)

print(len(choke_points), choke_points)



def get_neighbouring_choke_points(current):
    local_to_check = [] 
    local_to_check.append(current)
    local_distances = {}
    local_distances[current]=0
    local_checked = []
    neighbour_choke_points={}
    while(len(local_to_check)!=0):
        local_current = local_to_check[0]
        local_to_check.remove(local_current)
        neighbours = get_neighbours(local_current)
        for n in neighbours:
            if(n in choke_points and n != current):
                neighbour_choke_points[n]=local_distances[local_current]+1
            elif(n not in local_checked and n not in local_to_check):
                local_to_check.append(n)
                local_distances[n]=local_distances[local_current]+1
        local_checked.append(local_current)
    return neighbour_choke_points



choke_point_map = {}

for c in choke_points:
    choke_point_map[c] = get_neighbouring_choke_points(c)
    print(c, choke_point_map[c])

input()

longest_path = 0

cnt = 0

def calculate(current):
    global longest_path 
    global cnt
    cnt+=1

    last = current[-1]
    neighbours = choke_point_map[last]
    for n in neighbours:
        next_step = [*current, n]
        if(n not in current):
            if (n == pre_finish and len(next_step) > 30):
                length = (sum([*[choke_point_map[next_step[i]][next_step[i+1]] for i in range(len(next_step)-1)], choke_point_map[finish][pre_finish]]))
                print(cnt, length, longest_path)
                if(length > longest_path):
                    longest_path = length
                        # choke_point_map[pre_finish][finish] 
            else:
                calculate(next_step)

calculate([start])
