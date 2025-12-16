file = open('input', 'r')

lines = file.readlines()

coordinates = [list(map(lambda x: int(x), line.strip().split(',')))
               for line in lines]


def f(first, second):
    if (first[0] == second[0]):
        return [(first[0], i) for i in range(
            min(first[1], second[1]),
            max(first[1], second[1])+1
        )]
    if (first[1] == second[1]):
        return [(i, first[1]) for i in range(
            min(first[0], second[0]),
            max(first[0], second[0])+1
        )]


sides = []

for i in range(1, len(coordinates)):
    first = coordinates[i-1]
    second = coordinates[i]
    for s in f(first, second):
        sides.append(s)

for s in f(coordinates[-1], coordinates[0]):
    sides.append(s)

sides = set(sides)

max_y = max([x[0] for x in sides])
max_x = max([x[1] for x in sides])

print(max_x, max_y)

full_coord = [(i, j) for i in range(0, max_y+2) for j in range(0, max_x+2)]

visit = [(0, 0)]
visited = []
while (len(visit) != 0):
    print(len(full_coord))
    current = visit[0]
    visit.remove(current)
    if (current not in sides):
        full_coord.remove(current)
        print(current, 'out')
        unfiltered = [(current[0]+i, current[1]+j)
                      for i in range(-1, 2) for j in range(-1, 2)]
        neighbours = list(filter(
            lambda x: x != current and x in full_coord and x not in visited,
            unfiltered))

        for n in neighbours:
            visit.append(n)
    visited.append(current)
    visit = list(set(visit))


# print(full_coord)
#
# for j in range(0, max_x+2):
#     for i in range(0, max_y+2):
#         if ((i, j) in full_coord):
#             print('#', end='')
#         else:
#             print(' ', end='')
#     print()


rectangles = []

for i in range(0, len(coordinates)):
    for j in range(i+1, len(coordinates)):
        rectangles.append([i, j,
                           (abs(coordinates[i][0] - coordinates[j][0])+1)
                           * (abs(coordinates[i][1] - coordinates[j][1]) + 1)])

rectangles.sort(key=lambda x: x[2], reverse=True)

# for r in rectangles:


print(rectangles[0][2])
