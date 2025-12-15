
file = open('input', 'r')
lines = file.readlines()

coordinates = [(i, j) for i in range(0, len(lines))
               for j in range(0, len(list(lines[i]))) if list(lines[i])[j] == '@']


removed_piles = 1
matching_piles = 0
while (removed_piles > 0):
    print(matching_piles)
    removed_piles = 0
    for c in coordinates:
        neighbours = [(c[0] + i, c[1] + j) for i in range(-1, 2)
                      for j in range(-1, 2) if (c[0] + i, c[1] + j) != c and coordinates.__contains__((c[0] + i, c[1] + j))]

        if (len(neighbours) < 4):
            matching_piles += 1
            removed_piles += 1
            coordinates.remove(c)


print(matching_piles)
