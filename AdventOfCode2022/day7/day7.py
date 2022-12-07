class resource:

    children = None
    isdir = None

    def __init__(self, name, isdir = False):
        self.children = {}
        self.name = name
        self.isdir = isdir
        self.size = 0
    
    def __str__(self):
        return f"{self.name} {self.size}"
    
    __repr__ = __str__

def print_resource(res, indent):
    print(f"{''.join([' ' for i in range(indent)])}{res}")
    for c in res.children:
        if(c == '..'):
            continue
        else:
            print_resource(res.children[c], indent+2)

def calculate_size(res):
    if(not res.isdir):
        return res.size
    size = 0
    for c in res.children:
        if(c == '..'):
            continue
        size += calculate_size(res.children[c])
    res.size = size
    return size

dirs = []
with open('day7') as console_output:
    lines = [l.strip() for l in console_output]
    # print(lines)
    root = None
    current_resource = None
    for l in lines:
        parts = l.split()
        a = None
        if(len(parts) == 3):
            a, resource_type,resource_name = parts
        elif(len(parts) == 2 and parts[0] != '$'):
            resource_type, resource_name = parts
        else:
            continue
        
        if(a):
            # print(f"command {l}")
            if(current_resource == None):
                current_resource = resource(resource_name, True)
                root = current_resource
                dirs.append(current_resource)
            else:
                current_resource = current_resource.children[resource_name]
        elif(resource_type == 'dir'):
            # print(f"dir {l}")
            new_dir = resource(resource_name, True)
            dirs.append(new_dir)
            new_dir.children['..'] = current_resource
            current_resource.children[resource_name] = new_dir
        else:
            # print(f"file {l}")
            new_file = resource(resource_name)
            new_file.size = int(resource_type)
            current_resource.children[resource_name] = new_file

    calculate_size(root)
    print_resource(root, 0)
    sum_of_first = sum(list(map(lambda d: d.size, filter(lambda d: d.size < 100000, dirs))))
    print(f"first solution: {sum_of_first}")

    allocated_space = root.size
    all_space = 70000000
    required_space = 30000000
    free_space = all_space - allocated_space
    print(f"free space {free_space}")

    need_to_get = required_space - free_space

    print(f"need_to_get {need_to_get}")

    directories = list(map(lambda d: d.size, filter(lambda d: d.size > need_to_get, dirs)))
    directories.sort()
    print(f"second solution: {directories[0]}")

    


        
