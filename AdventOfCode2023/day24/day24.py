
import re


lines = None
with open ('data') as f:
    lines=f.readlines()

hail_stones = [[[int(y.strip()) for y in x.split(',')] for x in l.strip().split('@')] for l in lines ]

print(hail_stones)

equations = []
for ht in hail_stones:
    [px,py,pz]=ht[0]
    [vx,vy,vz]=ht[1]
    m = vy/vx
    b = py - m * px
    equations.append([m, b, px, py, vx, vy])

min = 200000000000000
max = 400000000000000

crossed = []
for eq in range(len(equations)):
    m1,b1,p=equations[eq][0],equations[eq][1], equations[eq][2:]
    # print(f"y={m1}x + {b1}")
    for eq2 in range(eq + 1, len(equations)):
        m2,b2,p2=equations[eq2][0],equations[eq2][1], equations[eq2][2:]
        print(p, 'vs', p2)
        # print(f"looking for {m1}x+{b1} = {m2}x+{b2}")
        m = m1-m2
        b = b2-b1
        if(m==0):
            print('No solution')
        else:
            x=b/m
            y1=m1*x+b1
            y2=m2*x+b2
            # print(f"x shall be {x}")
            # print(f"y1 is {y1}")
            # print(f"y2 is {y2}")
            elapsed_time1=(x-p[0])/p[2]
            # print(f"elapsed time A: {elapsed_time1}")
            elapsed_time2=(x-p2[0])/p2[2]
            # print(f"elapsed time B: {elapsed_time2}")
            if(elapsed_time1 < 0):
                print("Crossed in the past for A")
                continue
            if(elapsed_time2 < 0):
                print("Crossed in the past for B")
                continue
            if(x >= min and x <= max and y1 >= min and y1 <= max):
                crossed.append((eq, eq2))
            else:
                print("OUTSIDE OF AREA")

print(crossed)
print(len(crossed))

