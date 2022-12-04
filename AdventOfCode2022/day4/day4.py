def to_range(a):
    splitted = a.split('-')
    # return range(int(splitted[0]), int(splitted[1]))
    return [int(splitted[0]), int(splitted[1])] 

def full_overlap(e0, e1):
    return (e0[0] >= e1[0] and e0[1]<=e1[1]) or (e0[1] >= e1[1] and e0[0]<=e1[0])

def overlap(e0, e1):
    return (e0[0] >= e1[0] and e0[0]<=e1[1]) or (e0[1] >= e1[0] and e0[1]<=e1[1]) or (e1[0] >= e0[0] and e1[0]<=e0[1]) or (e1[1] >= e0[0] and e1[1]<=e0[1]) 


with open('day4', 'r') as input:
    elf_pairs = [s.replace('\n','').split(',') for s in input]
    converted = [list(map(to_range, a)) for a in elf_pairs]

    print(sum([full_overlap(a[0], a[1]) for a in converted]))
    print(sum([overlap(a[0], a[1]) for a in converted]))