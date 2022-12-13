from enum import Enum

class threestate(Enum):
    RIGHT = -1
    WRONG = 1
    UNDECIDED = 0

class level:

    def __init__(self, parent):
        self.content = []
        self.parent = parent
    
    def __str__(self):
        return f"{self.content}"

    __repr__ = __str__

def process_line(line):
    root = level(None)
    current = root
    i = 1
    while (i in range(len(line))):
        c = line[i]
        # print(c)
        if(c == '['):
            subcontent = level(current)
            current.content.append(subcontent)
            current = subcontent
            i += 1
        elif(c == ']'):
            current = current.parent
            i += 1
        elif(c == ','):
            i += 1
        else:
            next_end = line[i:].index(']')
            next_comma = line[i:].index(',') if ',' in line[i:] else len(line)
            numlen = min(next_end, next_comma)
            # print(f"{line[i:]} numlen {numlen}")
            num = int(line[i : i + numlen])
            current.content.append(num)
            # print(int(num))
            i += numlen
    return root


def is_right_order(a, b):
    al = len(a.content)
    bl = len(b.content)
    state = threestate.UNDECIDED

    if(al != 0 and bl == 0):
        return threestate.WRONG
    
    for i in range(min(al,bl)):
        ac = a.content[i]
        bc = b.content[i]
        if(type(ac) == int and type(bc) == int):
            if(ac > bc):
                return threestate.WRONG
            if(ac < bc):
                return threestate.RIGHT
        if(type(ac) == level and type(bc) == level):
            state = is_right_order(ac, bc)
        if(type(ac) == int and type(bc) == level):
            tmp = level(None)
            tmp.content.append(ac)
            state = is_right_order(tmp, bc)
        if(type(ac) == level and type(bc) == int):
            tmp = level(None)
            tmp.content.append(bc)
            state = is_right_order(ac, tmp)

        if(state != threestate.UNDECIDED):
            return state

    return threestate.WRONG if al > bl else (threestate.RIGHT if al < bl else threestate.UNDECIDED)

first_packet = level(None)
sub_packet = level(first_packet)
sub_packet.content.append(2)
first_packet.content.append(sub_packet)

second_packet = level(None)
sub2_packet = level(second_packet)
sub2_packet.content.append(6)
second_packet.content.append(sub2_packet)

last_packet = level(None)
last_packet.content.append(99999)

all_packets = [first_packet, second_packet, last_packet]
with open('day13') as input:
    lines = [l.strip() for l in input]
    right_ones = 0
    for r in range(0, len(lines), 3):
        root1 = process_line(lines[r])
        for p in range(len(all_packets)):
            if(is_right_order(root1, all_packets[p]) == threestate.RIGHT):
                print(f"inserting {root1} to {p}")
                all_packets.insert(p, root1)
                break
        root2 = process_line(lines[r+1])
        for p in range(len(all_packets)):
            if(is_right_order(root2, all_packets[p]) == threestate.RIGHT):
                print(f"inserting {root2} to {p}")
                all_packets.insert(p, root2)
                break

        right_order = is_right_order(root1, root2)
        print(f"packets in right order: {right_order}")
        right_ones += (r//3+1) if right_order == threestate.RIGHT else 0
    
    print(right_ones)
    for p in all_packets:
        print(p)
    first = all_packets.index(first_packet) + 1
    second = all_packets.index(second_packet) + 1
    print(f"{first}*{second}={first*second}")
