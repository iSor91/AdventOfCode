

file = open ('day2.txt')
values = [[int(i) for i in a.split()] for a in file.readlines()]
for v in values:
    v.sort()

for v in values:
    print(v)

sum = 0 
for l in values:
    sum += (max(l) - min(l))

print(sum)


def calculate(l):
    for i in l:
        for j in reversed(l):
            if(j%i == 0 and i != j):
                print(j, i)
                return j/i


sum2 = 0
for l in values:
    sum2+=calculate(l)

print(sum2)
