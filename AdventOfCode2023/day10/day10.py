
start = None
pipes={}
loop=[]
expanded={}
expanded_loop=[]

def expand(current):
    typ=current['typ']
    curr=current['pos']
    if(typ == '|'):
        return [(curr[0]-1, curr[1]), curr, (curr[0]+1, curr[1])]
    if(typ == '-'):
        return [(curr[0], curr[1]-1), curr, (curr[0], curr[1]+1)]
    if(typ == 'L'):
        return [(curr[0]-1, curr[1]), curr, (curr[0], curr[1]+1)]
    if(typ == 'F'):
        return [(curr[0], curr[1]+1), curr, (curr[0]+1, curr[1])]
    if(typ == 'J'):
        return [(curr[0]-1, curr[1]), curr, (curr[0], curr[1]-1)]
    if(typ == '7'):
        return [(curr[0], curr[1]-1), curr, (curr[0]+1, curr[1])]
    if(typ == 'S'):
        all=[(curr[0]+i, curr[1]+j) for i in range(-1,2) for j in range(-1, 2)]
        return all
    return[curr]

def get_neighbours(current):
    typ=current['typ']
    curr=current['pos']
    if(typ == '|'):
        return [(curr[0]-1, curr[1]), (curr[0]+1, curr[1])]
    if(typ == '-'):
        return [(curr[0], curr[1]-1), (curr[0], curr[1]+1)]
    if(typ == 'L'):
        return [(curr[0]-1, curr[1]), (curr[0], curr[1]+1)]
    if(typ == 'F'):
        return [(curr[0], curr[1]+1), (curr[0]+1, curr[1])]
    if(typ == 'J'):
        return [(curr[0]-1, curr[1]), (curr[0], curr[1]-1)]
    if(typ == '7'):
        return [(curr[0], curr[1]-1), (curr[0]+1, curr[1])]
    return[curr]

def next_pipe(prev, current):
    return [list(filter(lambda p: p!= prev, pipes[current]))[0], current]

def is_inner(current):
    list(filter(lambda x: x[0]==current[0] and x[1] > current[1], expanded_loop))

with open('data') as input:
    max = (0,0)
    lines = input.readlines()

    for i,l in enumerate(lines):
        for j,p in enumerate(l.strip()):
            expanded[(i,j)] = expand({'typ':p, 'pos':(i*3+1,j*3+1)})
            if(p == 'S'):
                start=(i,j)
            else:
                neighbours=get_neighbours({'typ':p, 'pos':(i,j)})
                # print(i,j,p, neighbours)
                pipes[(i,j)]=neighbours
            max = (i,j)
    
    [left, right] = list(map(lambda x: x[0], filter(lambda x: start in x[1],pipes.items())))
    steps = 1
    l_prev = start
    r_prev = start

    loop.append(start)
    expanded_loop.append(expanded[start])


    while (left != right):
        # print(left == right)
        # print(left,right)
        loop.append(left)
        loop.append(right)
        expanded_loop.append(expanded[left])
        expanded_loop.append(expanded[right])
        left, l_prev = next_pipe(l_prev, left)
        right, r_prev = next_pipe(r_prev, right)
        steps+=1

    loop.append(left)
    expanded_loop.append(expanded[left])

    expanded_flat_loop = [item for row in expanded_loop for item in row]

    expanded_outer=[]

    check = []
    check.append((0,0))
    checked = []
    while(len(check) != 0):
        curr=check.pop()
        right=(int(curr[0]),int(curr[1]+1))
        down=(int(curr[0]+1), int(curr[1]))
        left=(int(curr[0]),int(curr[1]-1))
        up=(int(curr[0]-1), int(curr[1]))
        # print(checked, curr, right, down)
        print(len(checked), len(check))
        if(right not in expanded_flat_loop and curr[1] < max[1]*3+3 and right not in checked):
            check.append(right)
        if(down not in expanded_flat_loop and curr[0] < max[0]*3+3 and down not in checked):
            check.append(down)
        if(left not in expanded_flat_loop and curr[1] > 0 and left not in checked):
            check.append(left)
        if(up not in expanded_flat_loop and curr[0]>0 and up not in checked):
            check.append(up)
        checked.append(curr)


    # for i in range(max[0]*3+3):
    #     curr = False
    #     switch=False
    #     prev_in_loop = False
    #     for j in range(max[1]*3+3):
    #         if(switch and (i,j) not in expanded_flat_loop):
    #             expanded_inner.append((i,j))

    #         if(not prev_in_loop and (i,j) in expanded_flat_loop):
    #             switch=not switch

    #         if((i,j) not in expanded_flat_loop):
    #             prev_in_loop = False
    #         elif((i,j) in expanded_flat_loop):
    #             prev_in_loop = True


    inner_count=0
    for i in range(max[0]*3+3):
        for j in range(max[1]*3+3):
            if((i,j) in expanded_flat_loop):
                print('*', end='')
            elif((i,j) not in checked):
                if(i%3==1 and j%3==1):
                    inner_count+=1
                    print('X', end='')
                else:
                    print('I', end='')
            else:
                print('.', end='')
        print()

    print(steps)
    print(inner_count)

