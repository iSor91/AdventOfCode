
active_objects={}
[south, east]=[0,0]

beams = [{"pos": (0,-1), "dir": (0,1), }]
start = []
start.append(((0,0), (0,1)))
energized = set()

def get_new_dir(dir, t):
    if(t == '-'):
        if(dir[0] != 0):
            return [(0,1), (0,-1)]
        return [dir]
    if(t == '|'):
        if(dir[1] != 0):
            return [(1,0), (-1,0)]
        return [dir]
    return []

def get_new_beam(beam, east, south):
    new_pos = tuple(map(sum,zip(beam["pos"],beam["dir"])))
    if(new_pos[0] >= south or new_pos[1] >= east or new_pos[0] < 0 or new_pos[1] < 0):
        return []
    if(new_pos not in active_objects):
        new_dir = beam['dir']
        return [{"pos": new_pos, "dir": new_dir}]
    if(active_objects[new_pos] == '/'):
        new_dir = (-beam['dir'][1], -beam['dir'][0])
        return [{"pos": new_pos, "dir": new_dir}]
    if(active_objects[new_pos] == '\\'):
        new_dir = (beam['dir'][1], beam['dir'][0])
        return [{"pos": new_pos, "dir": new_dir}]
    else:
        new_beams=[]
        for d in get_new_dir(beam["dir"], active_objects[new_pos]):
            # print(beam, new_pos, d)
            if((new_pos, d) not in start):
                start.append((new_pos, d))
                new_beams.append({"pos": new_pos, "dir": d})
    return new_beams


with open('data') as input:
    lines=input.readlines()
    active_objects = {(i,j):t for i,line in enumerate(lines) for j,t in enumerate([l for l in line.strip()]) if t!='.'}
    south = len(lines)
    east = len(lines[0].strip())

    while(len(beams) > 0):
        new_beams = []
        for b in beams:
            energized.add(b["pos"])
            for new_beam in get_new_beam(b, east, south):
                # print(beam)
                new_beams.append(new_beam)
        beams = new_beams
        print(len(beams))

    for i in range(south):
        for j in range(east):
            print(active_objects[(i,j)] if (i,j) in active_objects else '#' if (i,j) in energized else ' ', end='')
        print()

    print(len(energized) - 1)

