class place:

    def __init__(self, height, c):
        self.c = c
        self.place = None
        self.height = height if height != -14 and height != -28 else (0 if height != -28 else ord('z') - ord('a'))
        self.is_start = height == -28
        self.is_end = height == 0
        self.neighbors = []
        self.can_reach = []

        self.reachable_in = -1
    
    def __str__(self):
        return f"{self.place} {self.height} - {self.reachable_in}"
    
    __repr__ = __str__

with open('day12') as map:
    height_map = [[place(ord(c) - ord('a'),c) for c in list(l.strip())] for l in map]
    place_dict = {}
    
for i in range(len(height_map)):
    for j in range(len(height_map[i])):
        current = height_map[i][j]
        current.place = (i,j)
        current.neighbors = [(i-1,j), (i+1,j), (i,j-1), (i,j+1)]
        place_dict[(i,j)] = current

for p in place_dict:
    current = place_dict[p]
    for n in current.neighbors:
        if(n in place_dict):
            reachable = place_dict[n]
            if(reachable.height - current.height >= -1) :
                current.can_reach.append(reachable)


start_places = list(filter(lambda p: p.is_start, place_dict.values()))
end_places = list(filter(lambda p: p.is_end, place_dict.values()))
print(start_places, end_places)
current = start_places[0]
current.reachable_in = 0

all_places = list(place_dict.keys())
to_visit = [current]
visited = []

while(not current.is_end):
    # print(current)
    for n in current.can_reach:
        new_path_length = current.reachable_in + 1
        if(n.reachable_in == -1 or new_path_length < n.reachable_in):
            n.reachable_in = new_path_length
        if(n not in visited and n not in to_visit):
            to_visit.append(n)
    visited.append(current)
    to_visit.remove(current)
    all_places.remove(current.place)
    # print(to_visit)
    to_visit.sort(key=lambda x: x.reachable_in)
    if(not to_visit):
        break
    current = to_visit[0]

# print(all_places)
print(current)

for i in range(len(height_map)):
    for j in range(len(height_map[0])):
        if((i,j) in all_places):
            print(place_dict[(i,j)].c, end='')
        else:
            print('.', end='')
    print()