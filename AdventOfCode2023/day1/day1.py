import re
base = 0

digits=["one","two","three","four","five","six","seven","eight","nine"]

with open("data") as file1:
    for l in file1.readlines():
        cnt = 0
        first=None
        first_i=10000
        last=None
        last_i=-1
        for c in l:
            if (re.match("[0-9]", c)):
                if(first == None):
                    first_i=cnt
                    first = c
                last=c
                last_i=cnt
            cnt+=1
        for d in digits:
            if(d in l):
                for f in re.finditer(d, l):
                    found=f.start()
                    print(d,found)
                    if(first == None or found < first_i):
                        first_i = found
                        first = digits.index(d)+1
                    if(last == None or found >= last_i):
                        last_i=found
                        last = digits.index(d)+1

        current=int(f"{first}{last}")
        print(l, current)
        base = base + (current)


print(base)
