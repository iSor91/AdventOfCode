import itertools

groups = [list(x[1]) for x in itertools.groupby(open('day1'), lambda x: x=='\n')]
trimmed = [list(map(lambda s: int(s.replace("\n", "")) if s != '\n' else '', s)) for s in groups]
filtered = list(filter(lambda s: all(i != '' for i in s), trimmed))
elfs = {i: {'calories': filtered[i], 'sum': sum(filtered[i])} for i in range(len(filtered))}
maxmap = [elfs[e]['sum'] for e in elfs]
maxmap.sort()
print(maxmap[-1])
print(sum(maxmap[-3:]))
