#t = 30
#valve_flow_rate = 0 #pressure/minute if open
# tunnels = [] # each valve can be accessed from other valve via tunnels

#what is the most pressure i can release under 30 minutes
#every valves are closed
#got to valve -1m open valve -1m
#flow rate * remaining minute added to sum

first_valve = 'AA'

valve_open_t = 1
follow_tunnel_t = 1

name = 'name'
flow = 'flow'
time_to_reach = 'time_to_reach'
tunnels = 'tunnels'

def find_paths(valves, current):
    to_visit = [current]
    visited = []
    paths = {current: 0}
    while(True):
        for next in valves[current][tunnels]:
            if(next not in paths or paths[next] > paths[current] + follow_tunnel_t):
                paths[next] = paths[current] + follow_tunnel_t
            if(next not in to_visit and next not in visited):
                to_visit.append(next)
        visited.append(current)
        to_visit.remove(current)
        to_visit.sort(key = lambda x: paths[x])
        if(not to_visit):
            break
        current = to_visit[0]
    return paths

def process_file(file):
    with open(file) as input:
        lines = [l.strip().split(';') for l in input]
        # print(lines)

        valves = {l[0].split()[1]: {flow:int(l[0].split()[4].split('=')[1]),tunnels: list(map(lambda x: x.replace(',', ''), l[1].split()[4:]))} for l in lines}
        return valves

def calculate(t, valves, current, opened):
    # print(t)
    paths = find_paths(valves, current)
    # print(paths)
    next_steps = {}
    for p in paths:
        next_steps[p] = {name: p, time_to_reach: paths[p], flow: (t-paths[p]-1) * valves[p][flow]}
    
    steps = list(filter(lambda x: x[name] not in opened and x[flow] != 0, list(next_steps.values())))
    steps.sort(key=lambda x: -1 * x[flow])

    pressures = [[]]
    for i in range(len(steps)):
        next = steps[i]
        if(t > next[time_to_reach]):
            new_opened = opened.copy()
            current = next[name]
            new_opened.append(current)
            
            remaining_time = t
            remaining_time -= next[time_to_reach]
            remaining_time -= 1
            sublists = calculate(remaining_time, valves, current, new_opened)

            rp = next[flow]
            for l in sublists:
                l.insert(0,rp)
                pressures.append(l)

    return pressures
    

def day16(file):
    valves = process_file(file)

    released_pressure = calculate(30, valves, first_valve, [])
    rps = list(map(lambda x: sum(x), released_pressure))
    rps.sort()
    print(rps[-1])

        




day16('day16')