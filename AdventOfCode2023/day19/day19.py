from functools import reduce
import re

workflows = {}
parts = [] 

with open('data') as asdf:
    lines = asdf.readlines()
    proc_lines=[[x for x in re.split('\\{|\\}', l.strip()) if x != ''] for l in lines]

    for l in proc_lines:
        if(len(l) == 2):
            steps = []
            # it is a workflow
            for step in l[1].split(','):
                check = None
                destination = None
                condition = None
                if(':' in step):
                    [condition, destination]=step.split(':')
                    [prop, value]=re.split('<|>', condition)
                    if('<' in condition):
                        check = {'c': lambda a,b: a < b, 'p': prop, 'v': int(value), 'o': '<'}
                    elif('>' in condition):
                        check = {'c': lambda a,b: a > b, 'p': prop, 'v': int(value), 'o': '>'}
                else:
                    destination=step
                steps.append({'dest': destination, 'check': check, 'raw': condition})
            workflows[l[0]] = steps
            pass
        if(len(l) == 1):
            #it is a part
            values=re.split(',|=', l[0])
            parts.append({values[i]: int(values[i+1]) for i in range(0,len(values),2)})
            pass


accepted = 0

for p in parts:
    stage = 'in'
    while(stage not in ['R', 'A']):
        for step in workflows[stage]:
            check = step['check']
            if(check == None or check['c'](p[check['p']],check['v'])):
                stage = step['dest']
                break
    print(p, stage)

    if(stage == 'A'):
        accepted+=sum(p.values())

print(accepted)

w = workflows['in']
subres = []

#max 4000
start_bound = {x: (1,4001) for x in 'xmas'}

def split_tuple(t, v, operator):
    addition = 0 if operator == '<' else 1
    tuple1=(t[0], v + addition)
    tuple2=(v + addition, t[1])
    return [tuple1, tuple2] if operator == '<' else [tuple2, tuple1]

def calc(available, workflow, steps):
    unused = {}

    paths = []
    for step in workflow:
        current_available = {b: unused[b] if b in unused else available[b] for b in available}
        check=step['check']
        if(check != None):
            p = check['p']
            v = check['v']
            t = current_available[p]
            splitted=split_tuple(t, v, check['o'])
            current_available[p]=splitted[0]
            unused[p]=splitted[1]

        new_steps = [s for s in steps]
        new_steps.append(step['dest'])

        print(new_steps, current_available)
        if(step['dest'] in ['A','R']):
            prod = 1
            for value in current_available:
                length = (current_available[value][1]-current_available[value][0])
                print(value, current_available[value], length)
                prod=prod*length
            paths.append({'path': new_steps, 'combination': prod, 'bound': current_available})
            print(prod)
        else:
            for p in calc(current_available, workflows[step['dest']], new_steps):
                paths.append(p)

    return paths



all_paths = calc(start_bound, w, ['in'])

checkup = {x: [i for i in range(1, 4001)] for x in 'xmas'}
for p in [a for a in all_paths]:
    print(p)
    # for i in p['bound']:
    #     t=p['bound'][i]
    #     for j in range(t[0], t[1]):
    #         if(j in checkup[i]):
    #             checkup[i].remove(j)
sums=sum([a['combination'] for a in all_paths])
print(sums)
sums=sum([a['combination'] for a in all_paths if a['path'][-1] == 'A'])
print(sums)

# print(checkup)
