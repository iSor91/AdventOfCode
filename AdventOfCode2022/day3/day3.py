values_lc={
    chr(c): c-ord('a')+1 for c in range(ord('a'), ord('z')+1)
}
values_uc = {
    chr(s): s-ord('A')+27 for s in range(ord('A'), ord('Z')+1)
}

values = (values_lc | values_uc)

common_sum = 0
common_sum_2 = 0
with open('day3') as input:
    lines = input.readlines()

    cleared = []
    for l in lines: 
        cleared.append(l.replace('\n',''))

    print(cleared)
    for l in cleared:
        items = list(l)
        if('\n' in items):
            items.remove('\n')
        i1 = items[0:int(len(items)/2)]
        i2 = items[int(len(items)/2):]
        c1 = set(i1)
        c2 = set(i2)
        # print(l, i1, i2)
        common_item = c1.intersection(c2).pop()
        common_sum += values[common_item] 
    
    for i in range(0, len(cleared) - 2, 3):
        i1 = set(cleared[i])
        i2 = set(cleared[i+1])
        i3 = set(cleared[i+2])
        common_sum_2 += values[i1.intersection(i2, i3).pop()]



print(common_sum)
print(common_sum_2)
