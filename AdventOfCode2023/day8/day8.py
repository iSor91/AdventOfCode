import re
from math import lcm
from itertools import cycle

node_map={}
node_finishes={}

def solve(start, end):
    with open('data') as input:
        instructions = input.readline().strip()
        print(instructions)
        input.readline()
        current_nodes = []
        for l in input.readlines():
            nodes = re.findall(r"([A-Z0-9]{3})", l.strip())
            node_map[nodes[0]]={'name': nodes[0], 'L': nodes[1], 'R': nodes[2]}
            if(nodes[0].endswith(start)):
                current_nodes.append(nodes[0])

        print(current_nodes)
        for node in current_nodes:
            steps = 0
            finished_nodes = []
            curr = node

            for i,inst in cycle(enumerate(instructions)):
                steps += 1
                next_nodes = []
                curr = node_map[curr][inst]
                next_nodes.append(curr)
                if(curr.endswith(end)):
                    already_contained = list(filter(lambda x: (x['icnt'] == i and x['node'] == curr), finished_nodes))
                    if(len(already_contained) > 0):
                        break
                    finished_nodes.append({'icnt': i, 'step': steps, 'node': curr})
                current_nodes=next_nodes
            node_finishes[node] = finished_nodes

    first_node_finishes = list(node_finishes.values())[0]

    for node_finish in first_node_finishes:
        finishes = []
        for node in node_finishes:
            curr_finishes = node_finishes[node]
            matching_finish = list(filter(lambda x : node_finish['icnt'] == x['icnt'], curr_finishes))
            if(len(matching_finish) >= 1):
                finishes.append(matching_finish)

        print(lcm(*[x[0]['step'] for x in finishes]))

solve('AAA','ZZZ')
solve('A','Z')
