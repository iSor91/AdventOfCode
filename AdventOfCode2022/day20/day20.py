debug = False
#NOT FAST ENOUGH!

class part :

    def __init__(self, value):
        self.value = value
    
    def __str__(self):
        return f"{self.value}"
    
    def init(self, prev, next):
        self.order_next = next
        self.next = next
        self.prev = prev

    __repr__ = __str__

def print_list(head, list_length):
    current = head
    for _ in range(list_length):
        print(current, end=' ')
        current = current.next
    print()

def get_nth(head, n, list_length):
    current = head
    for i in range(n%list_length):
        current = current.next
    return current

def move_one(head, current, list_length):
    if(debug):
        print_list(head, list_length)
    new_head = head
    previous = current.prev
    next = current.next

    # find the new place for the current
    new_place = current
    steps = current.value % (list_length-1)
    if(steps == 0):
        if(debug):
            print(f"doing nothing, because {current} shall be moved {current.value} which results {steps} steps")
        return current.order_next, new_head
    for _ in range(steps):
        new_place = new_place.next
    
    # pop out current from its place
    previous.next = next
    next.prev = previous

    # insert the current to its new place
    if(debug):
        print(f"moving {current} {steps} steps after {new_place}")
    new_next = new_place.next 
    current.next = new_next
    current.prev = new_place
    new_next.prev = current
    new_place.next = current
    # if(head == new_next):
    #     if(debug):
    #         print(f"set {current} as head")
    #     new_head = current

    if(head == current):
        if(debug):
            print("current was the head")
        new_head = next
    
    return current.order_next, new_head

def mix_list(head, order_start, list_length):
    current = order_start
    current, head = move_one(head, current, list_length)
    while current != order_start:
        current, head = move_one(head, current, list_length)
    if(debug):
        print()
    return head

with open('day20') as input:
    values = [int(l.strip()) * 811589153 for l in input]
    parts = {l: part(values[l]) for l in range(len(values))}

for i in range(len(parts)):
    parts[i].init(parts[(i-1)%len(parts)], parts[(i+1)%len(parts)])

head = parts[0]
order_start = parts[0]
zero_list = list(filter(lambda x: parts[x].value == 0, parts))[0]
print(parts[zero_list], zero_list)
zero = parts[zero_list]

for _ in range(10):
    head = mix_list(head, order_start, len(values))

grove = list(map(lambda x: x.value, [get_nth(zero, i, len(values)) for i in range(1000, 4000, 1000)]))

print(grove, sum(grove))