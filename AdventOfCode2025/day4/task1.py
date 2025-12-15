
file = open('input', 'r')
lines = file.readlines()

coordinates = [(i, j) for i in range(0, len(lines))
               for j in range(0, len(list(lines[i]))) if list(lines[i])[j] == '@']


matching_piles = 0
for c in coordinates:
    neighbours = [(c[0] + i, c[1] + j) for i in range(-1, 2)
                  for j in range(-1, 2) if (c[0] + i, c[1] + j) != c]
    # print(c, neighbours)

    piles = 0
    for n in neighbours:
        if (coordinates.__contains__(n)):
            piles += 1

    if (piles < 4):
        matching_piles += 1


print(matching_piles)
