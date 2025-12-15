from math import prod

file = open('input', 'r')

lines = file.readlines()

groups = []

for line in range(0, len(lines)-1):
    group = [x for x in list(lines[line]) if x != '\n']
    groups.append(group)

print(groups)

right = len(groups[0])

new_groups = []
new_group = []
for i in range(right-1, -1, -1):
    number = ''
    for g in range(0, len(groups)):
        n = groups[g][i]
        if (n != ' '):
            number += n

    print(number)
    if (number == ''):
        new_groups.append(new_group)
        new_group = []
    else:
        new_group.append(int(number))

new_groups.append(new_group)
print(new_groups)

operations = lines[-1].strip().split()

print(operations)

result = 0
for i in range(0, len(operations)):
    if (operations[i] == '*'):
        p = prod(new_groups[len(new_groups) - i - 1])
        result += p
    if (operations[i] == '+'):
        result += sum(new_groups[len(new_groups) - i - 1])

print(result)
