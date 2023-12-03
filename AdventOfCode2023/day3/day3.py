import math
import re

numberRegex = r"\d+"
symbolRegex = r"[^\d\.\n]"

gear = "*"

symbols = {}
numbers = {}

with open('data') as file1:
    for i, l in enumerate(file1.readlines()):
        for f in re.finditer(symbolRegex, l):
            symbols[(i,f.start())]=f.group()

        for n in re.finditer(numberRegex, l):
            numbers[(i,n.start())]=n.group()
        
print(symbols)
print(numbers)
print()

def surround(tup):
    return [(tup[0]+i, tup[1]+j) for i in range(-1,2) for j in range(-1,2)]

gears = {s: [] for s in symbols if symbols[s]==gear}
print(gears)

sum = 0
for n in numbers:
    leng = len(numbers[n])
    numSpan = [(n[0], n[1]+i) for i in range(leng)]
    border = [surround(s) for s in numSpan]
    flattened_border = list(set([item for row in border for item in row]))
    flattened_border.sort()
    # print(flattened_border)
    containment=[(x in symbols) for x in flattened_border]
    # print(containment)
    if (any(containment)):
        sum = sum + int(numbers[n])
        # print(numbers[n])

    for g in gears:
        if (g in flattened_border):
            gears[g].append(int(numbers[n]))


print(sum)
print(gears)

gears_filtered=(list(filter(lambda x: len(x) == 2, gears.values())))

sum2=0
for g in gears_filtered:
    sum2 = sum2 + math.prod(g)

print(sum2)
