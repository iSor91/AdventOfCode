file = open('input', 'r')

lines = file.readlines()

start = [0, lines[0].index('S')]
print(start)

splitters = [[i, j] for i in range(0, len(lines))
             for j in range(0, len(lines[0].strip())) if lines[i][j] == '^']

print(splitters)

beams = [start[1]]
split_count = 0

for l in range(1, len(lines)):
    level_splitters = list(filter(lambda x: x[0] == l, splitters))
    new_beams = []
    for b in beams:
        hit_splitters = list(filter(lambda x: x[1] == b, level_splitters))
        if (len(hit_splitters) == 0):
            new_beams.append(b)
        else:
            split_count += 1
            new_beams.append(b+1)
            new_beams.append(b-1)
    beams = set(new_beams)
    print(beams)

print(split_count)
