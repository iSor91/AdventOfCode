class packet:

    def __init__(self, parent):
        self.content = []
        self.parent = parent
    
    def __str__(self):
        return f"{self.content}"

    __repr__ = __str__


def process_line(line):
    root = packet(None)
    current = root
    i = 1
    while (i in range(len(line))):
        c = line[i]
        # print(c)
        if(c == '['):
            subcontent = packet(current)
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
            num = int(line[i : i + numlen])
            current.content.append(num)
            i += numlen
    return root

def compare(a, b):
    al = len(a.content)
    bl = len(b.content)
    state = 0

    if(al != 0 and bl == 0):
        return 1
    
    for i in range(min(al,bl)):
        ac = a.content[i]
        bc = b.content[i]
        if(type(ac) == int and type(bc) == int):
            if(ac > bc):
                return 1
            if(ac < bc):
                return -1
        if(type(ac) == packet and type(bc) == packet):
            state = compare(ac, bc)
        if(type(ac) == int and type(bc) == packet):
            tmp = packet(None)
            tmp.content.append(ac)
            state = compare(tmp, bc)
        if(type(ac) == packet and type(bc) == int):
            tmp = packet(None)
            tmp.content.append(bc)
            state = compare(ac, tmp)

        if(state != 0):
            return state

    return 1 if al > bl else (-1 if al < bl else 0)


all_packets = []

def process_packet(l):
    packet = process_line(l)
    for p in range(len(all_packets)):
        if(compare(packet, all_packets[p]) == -1):
            all_packets.insert(p, packet)
            return packet 
    all_packets.append(packet)
    return packet

divider1 = process_packet('[[2]]')
divider2 = process_packet('[[6]]')

with open('day13') as input:
    lines = [l.strip() for l in input]
    right_ones = 0
    for r in range(0, len(lines), 3):
        p1 = process_packet(lines[r])
        p2 = process_packet(lines[r+1])
        right_ones += (r//3+1) if compare(p1, p2) == -1 else 0
    
    print(f"right order in {right_ones} cases")
    first = all_packets.index(divider1) + 1
    second = all_packets.index(divider2) + 1
    print(f"divider packets at {first}*{second}={first*second}")
