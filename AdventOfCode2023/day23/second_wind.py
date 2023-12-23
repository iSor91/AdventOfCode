lines = None
with open ('data') as f:
    lines = f.readlines()


path = {(i,j): lines[i][j] for i in range(len(lines)) for j in range(len(lines[i])) if lines[i][j] != '#'}

start = (0,1)
bot = len(lines)-1
rig = len(lines[0].strip())-1
finish = (bot,rig-1)

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

choke_points=[]
choke_points.append(start)
choke_points.append(finish)

finish_test = (19, 19)
finish_data = (135, 127)

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

to_check = []
to_check.append({'p':[start],'l':0})
checked = []
cnt = 0
while(len(to_check) != 0):
    cnt+=1
    current = to_check[0]
    to_check.remove(current)
    print(cnt, current['l'], len(to_check), len(checked))
    # print(current)

    neighbours = choke_point_map[current['p'][-1]]
    for n in neighbours:
        if(n not in current['p']):
            next_step = {'p': [*current['p'], n], 'l': current['l'] + neighbours[n]}
            if(n == finish_data):
                checked.append(next_step)
            elif(next_step not in to_check):
                to_check.append(next_step)


checked.sort(key=lambda x: x['l'])
print(checked[-1]['l'] + choke_point_map[finish][finish_data])
