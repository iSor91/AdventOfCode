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

def update_mapping(source_touple, seed_range_map, affected_ones):
    finished = False
    while(not finished):
        mapping_to_seed_range = {seed_range_map[s][-1]:s for s in seed_range_map}
        print()
        print(source_touple)
        print(seed_range_map)
        print(f"recalculated: {mapping_to_seed_range}")
        i=0
        for seed in mapping_to_seed_range:
            i+=1
            seed_sects = (sections(seed, source_touple))
            affected = seed_sects[0]
            if(len(affected) == 0 or affected[0][1] == 0):
                print(f"{seed} no affected")
                continue

            diff = affected[0][0]-source_start
            new_dest = (dest_start+diff, affected[0][1])

            not_affected = seed_sects[1]
            history = seed_range_map.pop(mapping_to_seed_range[seed])
            extended_history=[s for s in history]
            extended_history.append(new_dest)
            affected_ones[new_dest]=extended_history

            if(len(not_affected) == 0):
                print(f"{seed} no not affected")
                continue

            print(f"splitting {seed} to {affected} and {not_affected}")

            for not_a in not_affected :
                if (not_a[1]!=0):
                    extended_history2=[s for s in history]
                    extended_history2.append(not_a)
                    seed_range_map[not_a]=extended_history2
            break
        print(i, len(mapping_to_seed_range))
        if(i >= (len(mapping_to_seed_range)-1)):
            finished=True

with open('data') as input:
    seeds = input.readline().strip()
    seed_list = list(map(lambda x: int(x), re.split(": | ", seeds)[1:]))
    seed_ranges = [(seed_list[i], seed_list[i+1]) for i in range(0,len(seed_list),2)]

    seed_map = {s: [s] for s in seed_list}
    seed_range_map = {s: [s] for s in seed_ranges}

    mapping_to_seed = {}
    mapping_to_seed_range = {}
    affected_ones={}

    for l in input.readlines():
        line=l.strip()
        if(re.match(r"\s+", l)):
            continue

        elif(not re.match(r"^\d+.*", l)):
            mapping_to_seed = {seed_map[s][-1]:s for s in seed_map}
            for a in affected_ones:
                seed_range_map[a]=affected_ones[a]
            affected_ones={}

        else:
            [dest_start, source_start, range_len]=list(map(lambda x: int(x), re.split(" ", line)))

            in_range_seeds = list(filter(lambda x: (x>=source_start and x<source_start+range_len), mapping_to_seed))
            for seed in in_range_seeds:
                diff=seed-source_start
                dest=dest_start+diff
                seed_map[mapping_to_seed[seed]].append(dest)

            # print(dest_start, source_start, range_len)

            source_touple = (source_start, range_len)
            update_mapping(source_touple, seed_range_map, affected_ones)
            print(f"after: {seed_range_map}")


    print(min([seed_map[s][-1] for s in seed_map]))

    for a in affected_ones:
        seed_range_map[a]=affected_ones[a]
    print(min(list(map(lambda x: x[0], [seed_range_map[s][-1] for s in seed_range_map]))))
