
init_state = []
move_actions = []

stacks = {}

with open('day5') as initial:
    move = False
    for l in initial:
        if(move):
            move_actions.append(l)
        elif(l == '\n'):
            move=True
        elif(not move):
            init_state.append(l)

    stack_cnt_row = init_state[-1].strip()
    for i in range(0,int(stack_cnt_row[-1])):
        # print(f"creating crane {i}")
        stacks[i+1]=[]

    for level in init_state[:-1]:
        # print(level)
        k = 1
        for i in range(1,len(list(level)), 4):
            crate = level[i]
            if(crate != ' '):
                stacks[k].insert(0,crate)
            k+=1
    print(stacks)

    for command in move_actions:
        _,c,_,f,_,t = command.split()
        c,f,t=map(int, [c,f,t])
        print(c,f,t)

        stacks[t].append(stacks[f].pop())
        for i in range(c-1):
            # stacks[t].append(stacks[f].pop())
            stacks[t].insert(len(stacks[t])-i-1, stacks[f].pop())
        print(stacks)
    

    print("".join([s[1][-1] for s in stacks.items()]))

    


# print(init_state, move_actions)