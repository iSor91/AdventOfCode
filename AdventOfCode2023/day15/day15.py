# char -> ascii -> x17 -> mod 256

from functools import reduce
import re

def hash(a):
    asciis=[ord(c) for c in a]
    return reduce(lambda acc,c: ((acc + c) * 17)%256, asciis, 0) 

def convert_instruction(instruction):
    parts = list(map(lambda x: '-1' if x == '' else x, re.split("-|=", instruction)))
    return (hash(parts[0]), parts[0], int(parts[1]))

boxes={i:{} for i in range(256)}
with open('data') as input:
    instructions=[l for line in input.readlines() for l in line.strip().split(',')]
    full_hashes=[hash(a) for a in instructions]
    # print(full_hashes)
    print(sum(full_hashes))

    converted = list(map(convert_instruction, instructions))
    # print(converted)
    for c in converted:
        if(c[2] < 0):
            # print(f"removing {c[1]} from box {c[0]}")
            if(c[0] not in boxes):
                continue
            if(c[1] in boxes[c[0]]):
                boxes[c[0]].pop(c[1])
        else:
            # print(f'adding {c[2]} {c[1]} to box {c[0]}')
            boxes[c[0]][c[1]]=c[2]
            # print(boxes[c[0]])

    sum2=0
    for k in boxes:
        if(len(boxes[k]) != 0):
            for i,slot in enumerate(boxes[k]):
                sum2+=((i+1)*boxes[k][slot] * (k+1))

    print(sum2)
                

