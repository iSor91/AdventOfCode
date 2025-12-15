file = open('input', 'r')

lines = file.readlines()

start = [0, lines[0].index('S')]
print(start)

splitters = [[i, j] for i in range(0, len(lines))
             for j in range(0, len(lines[0].strip())) if lines[i][j] == '^']

print(splitters)

beams = [[start[1], 1]]

for l in range(1, len(lines)):
    level_splitters = list(filter(lambda x: x[0] == l, splitters))
    new_beams = []
    for b in beams:
        hit_splitters = list(filter(lambda x: x[1] == b[0], level_splitters))
        if (len(hit_splitters) == 0):
            new_beams.append(b)
        else:
            new_beams.append([b[0]+1, b[1]])
            new_beams.append([b[0]-1, b[1]])

    grouped_beams = {}
    for b in new_beams:
        if (grouped_beams.get(b[0]) is not None):
            grouped_beams[b[0]] = grouped_beams[b[0]] + b[1]
        else:
            grouped_beams[b[0]] = b[1]

    beams = set(grouped_beams.items())
    print(l, beams)

print(sum([b[1] for b in beams]))
