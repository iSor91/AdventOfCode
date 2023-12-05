import math
import re

#creates new sections from t1 - seed and t2 dest tuples
def sections(seed, dest):
    seed_start=seed[0]
    dest_start=dest[0]
    seed_end=seed_start + seed[1]
    dest_end=dest_start + dest[1]

    result = []
    if(seed_end < dest_start or seed_start > dest_end):
        result.append([])
        result.append([seed])
        return result
    if(seed_end <= dest_end and seed_start >= dest_start):
        result.append([seed])
        result.append([])
        return result

    if (seed_start<=dest_start):
        if(seed_end<=dest_end):
            result.append([(dest_start, seed_end-dest_start)])
            result.append([(seed_start, dest_start-seed_start)])

        if(seed_end>dest_end):
            result.append([dest])
            result.append([(seed_start, dest_start-seed_start), (dest_end, seed_end-dest_end)])

    if (seed_start>dest_start):
        if(seed_end>dest_end):
            result.append([(seed_start, dest_end-seed_start)])
            result.append([(dest_end, seed_end-dest_end)])

    return result

def update_mapping(source_touple, seed_range_map):
    print(source_touple)
    finished = False
    while(not finished):
        mapping_to_seed_range = {seed_range_map[s][-1]:s for s in seed_range_map}
        print()
        print(seed_range_map)
        print(f"recalculated: {mapping_to_seed_range}")
        for seed in mapping_to_seed_range:
            seed_sects = (sections(seed, source_touple))
            affected = seed_sects[0]
            if(len(affected) == 0):
                continue
            print(f"{source_touple} affected {seed}")
            history = seed_range_map.pop(mapping_to_seed_range[seed])

            diff = affected[0][0]-source_start
            extended_history=[s for s in history]
            extended_history.append((dest_start+diff, affected[0][1]))
            seed_range_map[affected[0]]=extended_history
            not_affected = seed_sects[1]
            for not_a in not_affected:
                seed_range_map[not_a]=history
            if(len(not_affected) > 0 and len(affected) > 0): 
                break
        else:
            finished = True

with open('test') as input:
    seeds = input.readline().strip()
    seed_list = list(map(lambda x: int(x), re.split(": | ", seeds)[1:]))
    seed_ranges = [(seed_list[i], seed_list[i+1]) for i in range(0,len(seed_list),2)]

    seed_map = {s: [s] for s in seed_list}
    seed_range_map = {s: [s] for s in seed_ranges}

    mapping_to_seed = {}
    mapping_to_seed_range = {}

    for l in input.readlines():
        line=l.strip()
        if(re.match(r"\s+", l)):
            continue

        elif(not re.match(r"^\d+.*", l)):
            mapping_to_seed = {seed_map[s][-1]:s for s in seed_map}

        else:
            [dest_start, source_start, range_len]=list(map(lambda x: int(x), re.split(" ", line)))

            in_range_seeds = list(filter(lambda x: (x>=source_start and x<source_start+range_len), mapping_to_seed))
            for seed in in_range_seeds:
                diff=seed-source_start
                dest=dest_start+diff
                seed_map[mapping_to_seed[seed]].append(dest)

            # print(dest_start, source_start, range_len)

            source_touple = (source_start, range_len)
            update_mapping(source_touple, seed_range_map)
            print(f"after: {seed_range_map}")


    print(min([seed_map[s][-1] for s in seed_map]))
    print(seed_range_map)
