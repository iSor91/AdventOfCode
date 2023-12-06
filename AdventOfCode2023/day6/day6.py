# boat starts with 0 mm/ms
# each ms holding the button adds 1 mm/ms
# i don't think that slowing down shall be calculated

import re


with open ('data2') as input:
    times=list(map(lambda x: int(x), re.split(":\\s+|\\s+", input.readline().strip())[1:]))
    distances=list(map(lambda x: int(x), re.split(":\\s+|\\s+", input.readline().strip())[1:]))
    print(times, distances)

    result=1

    for i in range(len(times)):
        curr_time = times[i]
        curr_dist = distances[i]

        required_speeds = []
        for i in range(curr_time):
            required_speeds.append(curr_dist/(curr_time-i+1))

        print(required_speeds)

        sadf=[s for i,s in enumerate(required_speeds) if s <= i+1 and (i+1) * (curr_time-i-1) > curr_dist]
        print(sadf)
        result *= len(sadf)
        print()

    print(result)



