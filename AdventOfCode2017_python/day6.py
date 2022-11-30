with open('day6.txt') as input:
    banks = [[int(i) for i in line.split()] for line in input]
    print(banks)
    
    for b in banks:
        had = []
        cycles = 0
        while(True):
            top_bank = max(b)
            index = b.index(top_bank)
            b[index] = 0
            index += 1
            while(top_bank > 0):
                i = index%len(b)
                b[i] += 1
                top_bank -= 1
                index+=1
            # print(b)
            s = ' '.join(map(str,b))
            if (s in had):
                print(cycles +1) #first task
                print(cycles - had.index(s)) #second task
                break
            had.append(s)
            cycles+=1
            # print(had)
