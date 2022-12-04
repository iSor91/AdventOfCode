with open('day3') as input:
    values = { chr(c): c-ord('a')+(1 if chr(c).islower() else 59) for c in range(ord('A'), ord('z')+1) }
    cleared = [l.replace('\n','') for l in input]
    items = [list(map(int,map(lambda a: values[a], list(l)))) for l in cleared]
    print(sum(set(i[0:int(len(i)/2)]).intersection(set(i[int(len(i)/2):])).pop() for i in items))
    print(sum(values[set(cleared[i]).intersection(cleared[i+1], cleared[i+2]).pop()] for i in range(0, len(cleared) - 2, 3)))

