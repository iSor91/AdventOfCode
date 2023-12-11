

galaxies=[]
expanded_galaxies=[]
with open('data') as input:
    galaxies=[(i,j) for i,g in enumerate(input.readlines()) for j,c in enumerate(g.strip()) if c == '#']
    galaxy_rows=list(map(lambda x: x[0], galaxies))
    galaxy_cols=list(map(lambda x: x[1], galaxies))
    empty_rows = [i for i in range(max(galaxy_rows)) if i not in galaxy_rows]
    empty_cols = [i for i in range(max(galaxy_cols)) if i not in galaxy_cols]

    print(galaxies)
    print(empty_rows, empty_cols)
    for g in galaxies:
        push_rows=len(list(filter(lambda x: x<g[0], empty_rows)))*(1000000-1)
        push_cols=len(list(filter(lambda x: x<g[1], empty_cols)))*(1000000-1)
        expanded_galaxies.append((g[0]+push_rows, g[1]+push_cols))

    print(expanded_galaxies)

    galaxy_pairs=[]
    for i in range(1,len(expanded_galaxies)):
        galaxy_pairs.append(list(zip(expanded_galaxies, expanded_galaxies[i:])))


    flat_pairs = [item for row in galaxy_pairs for item in row]
    print(flat_pairs)

    distances = list(map(lambda x: abs(x[0][0] - x[1][0]) + abs(x[0][1] - x[1][1]), flat_pairs))
    print(len(distances))
    print(distances)
    print(sum(distances))

