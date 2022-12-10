instructions = {
    'noop':{
        'timeout': 1,
        'args': 0,
        'action': lambda x, a: x
    },
    'addx': {
        'timeout': 2,
        'args': 1,
        'action': lambda x, a: x+a
    }
}

cycle = 0
x = 1

values = []
with open('day10') as input:
    commands = [l.strip() for l in input]
    for c in commands:
        args = c.split()
        instruction = instructions[args[0]]
        for j in range(instruction['timeout']):
            values.append(x)
            cycle +=1
        x = instruction['action'](x, int(args[1]) if len(args) == 2 else 0)

    interesting_cycles = [i * values[i-1] for i in range(20,221,40)]
    print(sum(interesting_cycles))

    rows = [[x for x in values[i:i+40]] for i in range(0, len(values), 40)]
    print('\n'.join([''.join(['%' if p in [r[p] - 1, r[p], r[p] + 1] else ' '  for p in range(len(r))]) for r in rows]))