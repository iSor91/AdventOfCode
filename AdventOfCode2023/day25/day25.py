lines = None
with open('data') as f:
    lines=f.readlines()

connection_map = {} 
for l in lines:
    [comp1, connections]=l.strip().split(":")
    connection_list = [c.strip() for c in connections.strip().split(" ")]
    if(comp1 not in connection_map):
        connection_map[comp1]=[]
    for c in connection_list:
        if (c not in connection_map):
            connection_map[c]=[]

        connection_map[comp1].append(c)
        connection_map[c].append(comp1)

flow_map = {}



def calculate(source):
    to_check = []
    to_check.append(source)
    distance = {}
    distance[source] = 0
    checked = []

    while (len(to_check) != 0):
        current = to_check[0]
        to_check.remove(current)
        for c in connection_map[current]:
            if(c not in distance or distance[current] + 1 < distance[c]):
                distance[c] = distance[current]+1
            if(c not in to_check and c not in checked):
                to_check.append(c)
        checked.append(current)

    furthest = max(list(map(lambda x: x[1], distance.items())))

    sink = list(filter(lambda x: x[1] == furthest, distance.items()))[0][0]

    print(source, sink)

    to_check = []
    to_check.append([source])

    while(len(to_check)!=0):
        current = to_check[0]
        to_check.remove(current)
        last = current[-1]
        current_dist = distance[last]
        for n in connection_map[last]:
            if(n == sink):
                new_path = [*current, n]
                for i in range(1, len(new_path)):
                    edge = [new_path[i-1], new_path[i]]
                    edge.sort()
                    edge = str(edge)
                    if(edge not in flow_map):
                        flow_map[edge] = 0
                    flow_map[edge] += 1
                continue
            if(distance[n] > current_dist and n not in current):
                to_check.append([*current, n])



for c in connection_map:
    calculate(c)

items = list(flow_map.items())
items.sort(key = lambda x: x[1])

#connections to severe are the three max used ones
for i in items:
    print(i)
