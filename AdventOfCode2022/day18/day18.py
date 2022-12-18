import sys 
class cube:
    def __init__(self, top_left):
        self.top_left = top_left
        self.sides = [i for i in range(6)]
    
    def __str__(self):
        return f"{self.top_left}"
    
    __repr__ = __str__
    
    def calculat_hamming(self, other):
        return [self.top_left[i] - other.top_left[i] for i in range(3)]
    
    def remove_side(self, to_remove):
        if(to_remove in self.sides):
            self.sides.remove(to_remove)
            return True
        return False
    
def get_side(remove):
    match remove:
        case 1: return 0
        case -1: return 3
        case 10: return 1
        case -10: return 4
        case 100: return 2
        case -100: return 5
    
    return -1
        


with open('day18') as input:
    cubes = [cube([int(c) for c in l.strip().split(',')]) for l in input]


with open('out', 'w') as out:
    original_out = sys.stdout
    sys.stdout = out
    for i in range(len(cubes)):
        for j in range(i+1, len(cubes)):
            hamming_distance = cubes[i].calculat_hamming(cubes[j])
            checksum = sum([abs(hamming_distance[k]) for k in range(3)])
            total_dist = sum([10**k * hamming_distance[k] for k in range(3)])
            if(checksum == 1):
                removed_a = cubes[i].remove_side(get_side(total_dist))
                removed_b = cubes[j].remove_side(get_side(-1 * total_dist))
                if(removed_a or removed_b):
                    print(f"{cubes[i]} removed {get_side(total_dist)}", end= ' ')
                    print(f"{cubes[j]} removed {get_side(-1 * total_dist)} - {hamming_distance} - {total_dist}")

cubes.sort(key=lambda c: len(c.sides))
# for c in cubes:
#     print(c)

sys.stdout = original_out
surface = sum(list(map(lambda c: len(c.sides), cubes)))
print(surface)
