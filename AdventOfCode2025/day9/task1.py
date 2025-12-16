file = open('input', 'r')

lines = file.readlines()

coordinates = [list(map(lambda x: int(x), line.strip().split(',')))
               for line in lines]


rectangles = []

for i in range(0, len(coordinates)):
    for j in range(i+1, len(coordinates)):
        rectangles.append([i, j,
                           (abs(coordinates[i][0] - coordinates[j][0])+1)
                           * (abs(coordinates[i][1] - coordinates[j][1]) + 1)])

rectangles.sort(key=lambda x: x[2], reverse=True)
for c in coordinates:
    print(c)
print()
for r in rectangles:
    print(r)

print(rectangles[0][2])
