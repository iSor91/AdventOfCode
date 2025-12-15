file = open('input', 'r')

lines = file.readlines()

empty_line = lines.index('\n')

ranges = [l.strip() for l in lines[0:empty_line]]

ingredients = [l.strip() for l in lines[empty_line+1:len(lines)]]


print(ranges)
print(ingredients)

final_ranges = []
for r in ranges:
    boundaries = [int(x) for x in r.split('-')]
    new_range = [boundaries[0], boundaries[1]]
    new_ranges = []
    for fr in final_ranges:
        if (fr[0] <= boundaries[0] and fr[1] >= boundaries[0]):
            new_range[0] = fr[0]
        if (fr[0] <= boundaries[1] and fr[1] >= boundaries[1]):
            new_range[1] = fr[1]

        if (fr[1] < boundaries[0] or fr[0] > boundaries[1]):
            new_ranges.append(fr)

    new_ranges.append(new_range)
    final_ranges = new_ranges

print(new_ranges)

fresh = 0
for r in new_ranges:
    fresh += r[1]-r[0]+1

print(fresh)
