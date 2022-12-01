
elfs={}
calories = []
i = 0

with open('day1.txt') as input:
    for l in input:
        print(l)
        if( l == '\n'):
            elfs[i] = {'sum': sum(calories), 'calories': calories}
            calories = []
            i += 1
        else:
            calories.append(int(l))

print(elfs)

maxmap = []
for i in elfs:
    print(i)
    maxmap.append(elfs[i]['sum'])

first = max(maxmap)
print(first)
maxmap.remove(first)
second = max(maxmap)
maxmap.remove(second)
third = max(maxmap)

print(first+second+third)
