import math
with open('data') as input:

    cards = {}
    won_cards={}
    sum = 0
    for l in input.readlines():
        print(l)
        parts=[item for row in [s.split('|') for s in l.split(':')] for item in row]
        parts2=list(map(lambda x: x.strip(), parts))

        number=int(parts2[0].split()[1])
        winning=parts2[1].split()
        having=parts2[2].split()


        print(winning, having)

        matches=list(filter(lambda x: x in winning, having))

        cards[number] = {'w':winning, 'h':having, 'm': matches}

        print(matches)

        sum = sum + int(math.pow(2, len(matches)-1))

    print(sum)
    print(cards)

    for c in cards:
        if(c not in won_cards):
            won_cards[c] = 1
        for l in range(1,len(cards[c]['m'])+1):
            for _ in range(won_cards[c]):
                if(c+l not in won_cards):
                    won_cards[c+l]=1
                won_cards[c+l] = won_cards[c+l]+1
    
    print(won_cards)
    sum2=0
    for c in won_cards:
        sum2+=won_cards[c]

    print(sum2)

