import sys 
class cube:
    def __init__(self, top_left):
        self.top_left = top_left
        self.sides = [i for i in range(6)]
    
    def __str__(self):
        return f"{self.top_left} - {self.sides}"
    
    __repr__ = __str__

    def __eq__(self, other):
        return self.top_left[0] == other.top_left[0] and self.top_left[1] == other.top_left[1] and self.top_left[2] == other.top_left[2]
    
    def __hash__(self):
        return hash(self.top_left)
    
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

def get_dir(side):
    match side:
        case 0: return (-1,0,0)
        case 3: return (1,0,0)
        case 1: return (0,-1,0)
        case 4: return (0,1,0)
        case 2: return (0,0,-1)
        case 5: return (0,0,1)

with open('day18') as input:
    cubes = [cube([int(c) for c in l.strip().split(',')]) for l in input]


#calculate inner cube count
possible_inner_cubes = set()

check_size = 2
for c in cubes:
    x,y,z = c.top_left
    for i in range(-check_size,check_size+1):
        for j in range(-check_size,check_size+1):
            for k in range(-check_size,check_size+1):
                possible_inner_cubes.add(cube((x+i,y+j,z+k)))

for c in possible_inner_cubes:
    print(c)

possible_inner_cubes = list(filter(lambda c: c not in cubes, possible_inner_cubes))
for c in possible_inner_cubes:
    for i in range(len(cubes)):
        hamming_distance = c.calculat_hamming(cubes[i])
        checksum = sum([abs(hamming_distance[k]) for k in range(3)])
        total_dist = sum([10**k * hamming_distance[k] for k in range(3)])
        if(checksum == 1):
            c.remove_side(get_side(total_dist))
        
        if(len(c.sides) == 0):
            break

possible_inner_cubes.sort(key=lambda c: len(c.sides))

to_visit = {c.top_left: c for c in possible_inner_cubes}
inner_cubes = {c.top_left: c for c in possible_inner_cubes}
inner_pockets = []
outer_cube = (-10,-10,-10)

def check_cube(current, pocket_cubes):
    pocket_cubes.append(current.top_left)
    to_visit.pop(current.top_left)

    for side in current.sides:
        nx,ny,nz = get_dir(side)
        cx,cy,cz = current.top_left
        neighbor = (nx+cx, ny+cy, nz+cz)
        # print(current, neighbor)
        if(neighbor in to_visit):
            check_cube(to_visit[neighbor], pocket_cubes)
            pass # check the same on the neighbor
        elif(neighbor in pocket_cubes):
            pass # already checked neighbor
        else:
            pocket_cubes.append(outer_cube)
            return pocket_cubes
            pass # the neighbor is not in the possible inner cubes, thus it is outside of the obsidian, this is not an inner pocket
    return pocket_cubes

while(to_visit):
    pocket_cubes = []
    current = to_visit[list(to_visit.keys())[0]]
    resp = check_cube(current, pocket_cubes)
    if(not any(c == outer_cube for c in resp)):
        inner_pockets.append([inner_cubes[c] for c in resp])
    # inner_pockets.append(list(filter(lambda c: c != outer_cube, resp)))

for p in inner_pockets:
    print(p)

#calculate full surface including inner cubes
for i in range(len(cubes)):
    for j in range(i+1, len(cubes)):
        hamming_distance = cubes[i].calculat_hamming(cubes[j])
        checksum = sum([abs(hamming_distance[k]) for k in range(3)])
        total_dist = sum([10**k * hamming_distance[k] for k in range(3)])
        if(checksum == 1):
            removed_a = cubes[i].remove_side(get_side(total_dist))
            removed_b = cubes[j].remove_side(get_side(-1 * total_dist))
            # if(removed_a or removed_b):
            #     print(f"{cubes[i]} removed {get_side(total_dist)}", end= ' ')
            #     print(f"{cubes[j]} removed {get_side(-1 * total_dist)} - {hamming_distance} - {total_dist}")

cubes.sort(key=lambda c: len(c.sides))
# for c in cubes:
#     print(c)

surface = sum(list(map(lambda c: len(c.sides), cubes)))
print(surface)
print(surface - sum(list(sum(list(map(lambda c: 6-len(c.sides), i))) for i in inner_pockets)))

#3210 too high
#have to increase the inner cube check count, but it fails with recursion error on 2