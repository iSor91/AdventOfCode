# boat starts with 0 mm/ms
# each ms holding the button adds 1 mm/ms
# i don't think that slowing down shall be calculated

import re
reg1=":\\s+|\\s+"

reg2=":\\s+"

def solve(reg):

    with open ('data') as input:
        times=list(map(lambda x: int(x), list(map(lambda x: x.replace(' ', ''), re.split(reg, input.readline().strip())[1:]))))
        distances=list(map(lambda x: int(x), list(map(lambda x: x.replace(' ', ''), re.split(reg, input.readline().strip())[1:]))))
        print(times, distances)

        result=1

        for i in range(len(times)):
            curr_time = times[i]
            curr_dist = distances[i]
            winnable=0
            for i in range(curr_time):
                s = curr_dist/(curr_time-i+1)
                if (s <= i+1 and (i+1) * (curr_time-i-1) > curr_dist):
                    winnable+=1
            result *= winnable

        print(result)



solve(reg1)
solve(reg2)
