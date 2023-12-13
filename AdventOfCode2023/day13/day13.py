def identify_horizontal_mirror(current, possible_h_mirrors):
    print(current, possible_h_mirrors)
    for possible_mirror in possible_h_mirrors:

        rows_to_check = min(len(current)-possible_mirror, possible_mirror)
        print(rows_to_check)
        # is_mirror = all([current[possible_mirror-i-1] == current[possible_mirror+i] for i in range(rows_to_check)])
        # if(is_mirror):
        #     return possible_mirror
        hamming_dist_list=[calculate_hamming_dist(current[possible_mirror-i-1], current[possible_mirror+i]) for i in range(rows_to_check)]
        print("dists", hamming_dist_list, sum(hamming_dist_list))
        if(sum(hamming_dist_list) == 1):
            return possible_mirror
    return 0

def calculate_hamming_dist(a,b):
    hamming_dist_list = [a[j]==b[j] for j in range(len(a))]
    return len(list(filter(lambda x: not x, hamming_dist_list)))

def find_mirrors(current):
    for l in current:
        print(l)
    possible_h_mirrors=[]
    for i in range(1,len(current)):
        # if(current[i] == current[i-1]):
        #     possible_h_mirrors.append(i)
        if(calculate_hamming_dist(current[i], current[i-1])<=1):
            possible_h_mirrors.append(i)
    h=identify_horizontal_mirror(current, possible_h_mirrors)
    return h

sum1=0

with open('data') as input:
    current = []
    start=0
    possible_h_mirrors=[]
    for line in [l.strip() for l in input.readlines()]:
        if(line==''):
            horizontal=find_mirrors(current)
            transposed = (["".join([current[i][j] for i in range(len(current))]) for j in range(len(current[0])) ])
            vertical=find_mirrors(transposed)
            res=horizontal*100 + vertical
            print(horizontal, vertical, res)
            sum1+=res
            current = []
            possible_h_mirrors=[]
            continue
        else:
            current.append(line)

print(sum1)
