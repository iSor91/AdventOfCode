from threading import Thread

s = 'S'
b = 'B'
empty = '.'
must_be_empty = '#'

def process_reports(reports):
    sb_pairs= []
    pane = {}
    for r in reports:
        sensor, beacon = [i.replace(',','').split() for i in r.split(':')]
        sx,sy = [int(c.split('=')[1]) for c in sensor[2:]]
        bx,by = [int(c.split('=')[1]) for c in beacon[4:]]
        # print(sx,sy, bx,by)
        pane[(sx,sy)] = s
        pane[(bx,by)] = b
        sb_pairs.append([(sx,sy), (bx,by)])
    return pane, sb_pairs

def print_pane(pane):
    xs = list(map(lambda x: x[0], pane.keys()))
    ys = list(map(lambda x: x[1], pane.keys()))
    xs.sort()
    ys.sort()
    minx = xs[0] - 2
    maxx = xs[-1] + 2
    miny = ys[0] - 2
    maxy = ys[-1] + 2
    # print(minx, maxx, miny, maxy)
    for j in range(miny, maxy):
        for i in range(minx, maxx):
            if((i,j) in pane):
                print(pane[(i,j)], end='')
            else:
                print(empty, end='')
        print()

def process_pairs(pairs):
    e = []
    for p in pairs:
        s,b = p
        x = s[0] - b[0]
        y = s[1] - b[1]
        r = abs(x) + abs(y)
        # print(s, b, r)
        e.append((s,r))
    return e

def nr_of_sure_empty(pane, n):
    row = list(filter(lambda x: x[1] == n and pane[x] == must_be_empty, pane))
    return len(row)

def is_covered(p, ss):
    for s in ss:
        if(is_in_range(p,s)):
            return s
    return None

def is_in_range(p, s):
    x = s[0][0] - p[0]
    y = s[0][1] - p[1]
    return abs(x) + abs(y) <= s[1]

def fill_row(row_to_fill, distances):
    e = []
    for p in distances:
        vertical_distance = abs(p[0][0] - row_to_fill)
        if(vertical_distance <= p[1]):
            horizontal_span = p[1] - vertical_distance
            for i in range(p[0][1] - horizontal_span, p[0][1] + horizontal_span):
                e.append((row_to_fill, i))
    return e



def check_range(md, maxs = 20):
    try:
        for i in range(maxs):
            print(i, end='\r')
            j = 0
            while(j in range(maxs)):
                sensor = is_covered((i,j), md)
                if(sensor == None):
                    print(f"second solution {(i,j)} {i*maxs + j}")
                    j+=1
                else:
                    empties = sensor[0][1]-j + sensor[1] - abs(sensor[0][0] - i)
                    j+= empties + 1
    except Exception as e:
        print(f"solution: {e}")
    

def day15(file, maxx, row_to_check):
    with open(file) as input:
        reports = [l.strip() for l in input]
    pane, pairs = process_reports(reports)

    # print_pane(pane)
    # print(pairs)
    md = process_pairs(pairs)

    row = fill_row(row_to_check, md)
    print(len(set(row)))
    check_range(md, maxx)

day15('day15', 4000000, 2000000)
