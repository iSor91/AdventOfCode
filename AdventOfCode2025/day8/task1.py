from math import sqrt

file = open('input', 'r')

lines = file.readlines()

coordinates = [list(map(lambda x: int(x), line.strip().split(',')))
               for line in lines]

pairs = []
for i in range(0, len(coordinates)):
    first = coordinates[i]
    for j in range(i+1, len(coordinates)):
        second = coordinates[j]
        pairs.append([i, j, sqrt(pow(first[0]-second[0], 2) +
                                 pow(first[1]-second[1], 2) +
                                 pow(first[2]-second[2], 2))])

pairs.sort(key=lambda x: x[2])

circuits = []

for i in range(0, 1000):
    p = pairs[i]
    print(p)
    first_circuit = next(filter(lambda x: p[0] in x, circuits), None)
    second_circuit = next(filter(lambda x: p[1] in x, circuits), None)

    not_affected_circuits = list(
        filter(lambda x: x != first_circuit and x != second_circuit, circuits))

    new_circuits = []

    if (first_circuit is None and second_circuit is None):
        new_circuits.append([p[0], p[1]])

    elif (first_circuit == second_circuit):
        continue

    elif (first_circuit is None and second_circuit is not None):
        second_circuit.append(p[0])
        new_circuits.append(second_circuit)

    elif (first_circuit is not None and second_circuit is None):
        first_circuit.append(p[1])
        new_circuits.append(first_circuit)

    elif (first_circuit != second_circuit):
        for box in second_circuit:
            first_circuit.append(box)
        new_circuits.append(first_circuit)

    for c in not_affected_circuits:
        new_circuits.append(c)

    circuits = new_circuits
    print('circuits', circuits)


print(circuits)

circuits.sort(key=lambda x: len(x), reverse=True)

print(len(circuits[0]), len(circuits[1]), len(circuits[2]))
print(len(circuits[0]) * len(circuits[1]) * len(circuits[2]))
