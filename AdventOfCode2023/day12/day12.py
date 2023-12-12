
import re

def extend(line, base, arrangement_findings):
    l = []
    # print('extend', arrangement_findings)
    for b in base:
        for a in arrangement_findings:
            if((len(b) == 0 or a[0] > b[-1][1]) and (a[0] == 0 or line[a[0]-1] != '#') and (a[1] == len(line) or line[a[1]] != '#')):
                l.append([*b, a])
    return l

sum = 0

with open('data') as input:
    
    for l in input.readlines():
        [springs, arrangments] = l.strip().split(" ")
        print(springs, arrangments)

        all_substrings = [springs[i:] for i in range(len(springs))]
        arrangement_findings = []
        for a in arrangments.split(","):
            findings = set(([(f.start()+i, f.end()+i) 
                              for i,s in enumerate(all_substrings) 
                              for f in re.finditer(fr'[#?]{{{int(a)}}}', s)]))
            # print(findings)
            arrangement_findings.append(findings)
        # print(arrangement_findings)

        valid=[]
        for base in arrangement_findings[0]:
            current = [[]]
            for a in range(len(arrangement_findings)):
                # print(a, current, end=' ')
                current = extend(springs, current, arrangement_findings[a])
                # print(current)
            for i in current:
                if(i not in valid):
                    valid.append(i)

        print('VALID', len(valid))
        # print(valid)
        sum+=len(valid)

print(sum)


        

