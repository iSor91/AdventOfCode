from math import prod

file = open('input', 'r')

lines = file.readlines()

groups = []

for line in range(0, len(lines)-1):
    group = [int(x) for x in lines[line].strip().split()]
    groups.append(group)


operations = lines[-1].strip().split()

print(groups)
print(operations)

result = 0
for i in range(0, len(operations)):
    if (operations[i] == '*'):
        result += prod([g[i] for g in groups])
    if (operations[i] == '+'):
        result += sum([g[i] for g in groups])

print(result)
