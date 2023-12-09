def get_diffs(l):
    return list(map(lambda x: x[1]-x[0], zip(l,l[1:])))

with open('data') as input:
    lines=[list(map(lambda x: int(x), l.strip().split(' '))) for l in input.readlines()]

    extrapolated=[]
    for i,l in enumerate(lines):
        diff=get_diffs(l)
        extrapolates=[l]
        while(not all(list(map(lambda x: x==0, diff)))):
            extrapolates.append(diff)
            diff=get_diffs(diff)
        print(extrapolates)
        steps=len(extrapolates)-1
        for i in range(1,steps+1):
            extrapolates[steps-i].append(extrapolates[steps-i][-1] + extrapolates[steps-i+1][-2])
            extrapolates[steps-i].append(extrapolates[steps-i][0] - extrapolates[steps-i+1][-1])

        print(extrapolates)
        extrapolated.append(extrapolates[0])

    print(sum([e[-2] for e in extrapolated]))
    print(sum([e[-1] for e in extrapolated]))
        
