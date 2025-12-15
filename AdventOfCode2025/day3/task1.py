
file = open('input', 'r')
lines = file.readlines()

sum = 0

for line in lines:
    chars = list(line.strip())

    ones = 0
    tens = 0

    for c in chars:
        if (ones < int(c) or tens < ones):
            if (tens < ones):
                tens = ones
            ones = int(c)

    print(tens, ones)
    sum += tens * 10 + ones

print(sum)
