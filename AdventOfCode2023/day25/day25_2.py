lines = None
with open('data_updated') as f:
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


start = 'xvh'
sink = 'gfh'

def calculate(start):
    to_check = []
    to_check.append(start)
    checked = []
    length = 0

    while (len(to_check) != 0):
        current = to_check[0]
        length+=1

        to_check.remove(current)
        for n in connection_map[current]:
            if(n not in to_check and n not in checked):
                to_check.append(n)
        checked.append(current)

    return length


print(calculate(start))
print(calculate(sink))
