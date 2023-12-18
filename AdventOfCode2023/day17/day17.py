#max 3 blocks in the same direction
#the crucible can't turn around
#either go straight, left or right

move_map={}
heat_loss_map={}

def substract(t1, t2):
    return (t2[0]-t1[0],t2[1]-t1[1])

def add(t1, t2):
    return tuple(map(sum, zip(t1,t2)))

def create_step(current, dir, length):
    pos=add(current[0],dir)
    if(pos[0] >= 0 and pos[0] < rows and pos[1] >= 0 and pos[1] < cols and length+1 <= 10):
        new_pos_heat_loss=heat_loss_matrix[pos[0]][pos[1]]
        new_step=(pos, dir, length+1, new_pos_heat_loss)
        sum_heat_loss = heat_loss_map[current] + new_pos_heat_loss
        if(new_step not in heat_loss_map or heat_loss_map[new_step] > sum_heat_loss):
            move_map[new_step] = current
            heat_loss_map[new_step] = sum_heat_loss
            return new_step
    return None

def next_steps(current):
    dir=current[1]
    dir1=(dir[1], dir[0])
    dir2=(-dir[1], -dir[0])
    same_dir=create_step(current, dir, current[2])
    change_dir1=create_step(current, dir1, 0) if current[2] >= 4 else None
    change_dir2=create_step(current, dir2, 0) if current[2] >= 4 else None
    return[x for x in [same_dir, change_dir1, change_dir2] if x != None]

with open('data') as input1:
    lines=input1.readlines()
    rows=len(lines)
    cols=len(lines[0].strip())

    heat_loss_matrix=[[int(x) for x in l.strip()] for l in lines]

    start = ((0,0),(0,1),0,0)
    heat_loss_map[start] = 0
    finish=(rows-1,cols-1)

    to_visit=[]
    to_visit.append(start)
    visited=[]
    current=start

    while(len(to_visit) != 0 and (current[0] != finish or current[2] < 4)):
        current=to_visit[0]
        print(current, heat_loss_map[current])
        to_visit.remove(current)
        visited.append(current)

        next_possible_steps=next_steps(current)
        for next_step in list(filter(lambda x: x not in visited, next_possible_steps)):
            to_visit.append(next_step)

        to_visit.sort(key=lambda x: heat_loss_map[x] * 1000000 + 1000 * (rows-x[0][0]) + (cols-x[0][1]))

    print('press a button')
    input()
    end_states=[x for x in visited if x[0]==finish and x[2] >= 4]
    end_states.sort(key=lambda x: heat_loss_map[x])
    print(heat_loss_map[end_states[0]])
