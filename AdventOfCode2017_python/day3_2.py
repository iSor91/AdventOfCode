c2n={(0,0): 1}

def print_circle(size, startnum):
    i=size
    n=startnum

    x=int(size/2)
    y=int(size/2)

    for r in range(i):
        y-=1
        n=calculate_n(x,y,n)
        c2n[(x,y)]=n
    for u in range(i):
        x-=1
        n=calculate_n(x,y,n)
        c2n[(x,y)]=n
    for l in range(i):
        y+=1
        n=calculate_n(x,y,n)
        c2n[(x,y)]=n
    for d in range(i):
        x+=1
        n=calculate_n(x,y,n)
        c2n[(x,y)]=n
    return n

def calculate_n(x,y,n):

    # new_n = n+1
    new_n=calculate_from_map(x,y)
    if(new_n >= input):
        raise Exception(x, y, new_n)

    return new_n
        
def calculate_from_map(x,y):
    n = 0
    n+=c2n.get((x,y+1),0)
    n+=c2n.get((x,y-1),0)
    n+=c2n.get((x+1,y),0)
    n+=c2n.get((x-1,y),0)
    n+=c2n.get((x+1,y+1),0)
    n+=c2n.get((x-1,y+1),0)
    n+=c2n.get((x+1,y-1),0)
    n+=c2n.get((x-1,y-1),0)
    print(n)
    return n

input=325489
startnum = 1
level=0
while(startnum < input):
    try:
        startnum = print_circle(level,startnum)
        level+=2
    except Exception as e:
        print(e)
        print(c2n)
        break
