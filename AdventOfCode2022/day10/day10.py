instructions = {
    'noop':{
        'timeout': 1,
        'args': 0,
        'action': lambda x, a: x
    },
    'addx': {
        'timeout': 2,
        'args': 1,
        'action': lambda x, a: x+sum(a)
    }
}

interesting_cycles = [i for i in range(20,221,40)]
interesting_cycle_values = []
print(interesting_cycles)
i = 0
cycle = 0
x = 1
with open('day10') as input:
    commands = [l.strip() for l in input]
    for c in commands:
        args = [c.split()[i] for i in range(len(c.split()))]
        instruction = instructions[args[0]]

        for j in range(instruction['timeout']):
            current_pixel = cycle % 40
            if(current_pixel in [x-1, x, x+1]):
                print('#', end = '')
            else:
                print('.', end = '')
            cycle +=1
            if(cycle % 40 == 39):
                print()



    #     cycle += instruction['timeout']
    #     if( i < len(interesting_cycles) and cycle+1 > interesting_cycles[i]):
    #         new_val = interesting_cycles[i] * x
    #         interesting_cycle_values.append(new_val)
    #         print(interesting_cycles[i], x, new_val, sum(interesting_cycle_values))
    #         i+=1
        arguments = list(map(int,(args[i+1] for i in range(instruction['args']))))
        x = instruction['action'](x, arguments)
    #     # print(cycle, c, x)

    # print(interesting_cycle_values, sum(interesting_cycle_values))