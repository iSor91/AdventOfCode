
import re

def extend(line, base, arrangement_findings):
    l = []
    # print('extend', arrangement_findings)
    for b in base:
        for a in arrangement_findings:
            if((len(b) == 0 or a[0] > b[-1][1]) and (a[0] == 0 or line[a[0]-1] != '#') and (a[1] == len(line) or line[a[1]] != '#')):
                l.append([*b, a])
    return l

def solve(springs, arrangments):
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
    current = [[]]
    for a in range(len(arrangement_findings)):
        # print(a, current, end=' ')
        current = extend(springs, current, arrangement_findings[a])
        # print(current)
    for i in current:
        if(i not in valid):
            valid.append(i)

    broken_springs=[i for i in range(len(springs)) if springs[i] == '#']
    true_valid=[]
    for v in valid:
        broken=[i for x in v for i in range(*x)]
        if(all([i in broken for i in broken_springs])):
            true_valid.append(valid)
        # print(broken)
        # for i in range(len(springs)):
        #     if(i not in broken):
        #         print('.', end='')
        #     else:
        #         print('#', end='')
        # print()
    # print()
    # print(valid)

    print('VALID', len(true_valid))
    return len(true_valid)

sum1=0
sum2=0
with open('data') as input:
    
    for l in input.readlines():
        [s, a] = l.strip().split(" ")
        print(s, a)

        res1=solve(s,a)
        sum1+=res1

        es = f"{s}?"
        res2=solve(es, a)
        es = f"?{s}?"
        res3=solve(es, a)
        es = f"?{s}"
        res4=solve(es, a)
        res = res2*res3*res3*res3*res4
        print(res1, res2, res)
        sum2+=res


print(sum1)
print(sum2)




        

