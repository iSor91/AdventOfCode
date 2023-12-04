import math
import re
with open('data') as input:
    lines=input.readlines()
    card_count={x: 1 for x in range(1,len(lines)+1)}
    sum=[0,0]
    for l in lines:
        parts=[item.split() for item in  [row.strip() for row in re.split('\\||:', l)]]
        [number, matches]=[int(parts[0][1]), [x for x in parts[2] if x in parts[1]]]

        for m in range(1,len(matches)+1):
            card_count[number+m]+=card_count[number]

        sum[0]+=int(math.pow(2, len(matches)-1))
        sum[1]+=card_count[number]

    print(sum[0])
    print(sum[1])

