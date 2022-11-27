def decision(num):
    # return num > 2 #2nd task
    return False #1st task
    
with open('day5.txt') as file:
    lines = [int(line.rstrip()) for line in file]

    instruction = 0
    steps = 0
    while(instruction < len(lines)):
        next_i = lines[instruction]
        # print(f"{instruction} : {next_i}")
        # print(lines)
        lines[instruction] = next_i + (-1 if decision(next_i) else 1)
        instruction = instruction + next_i
        steps+=1

    print()
    print(steps)
    # print(lines)
