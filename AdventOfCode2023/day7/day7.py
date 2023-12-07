
import collections


cards = ["A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J"]

#5ofakind 4ofakind fullhouse 3ofakind 2pairs 1pair highcardo
types=['5s', '4s', 'fh', '3s', '2p', '1p', 'hc']

def getType(hand):
    card_counts = collections.Counter(hand)
    jokers=0
    if('J' in card_counts):
        jokers=card_counts.pop('J')

    counts=list(filter(lambda x: x>1, card_counts.values()))

    if(len(counts) == 1 and counts[0] == 2):
        if(jokers == 3):
            return 0
        if(jokers == 2):
            return 1
        if(jokers == 1):
            return 3
        return 5
    if(len(counts) == 2 and counts[0] == 2 and counts[1] == 2):
        if(jokers == 1):
            return 2
        return 4
    if(len(counts) == 1 and counts[0]==3):
        if(jokers == 1):
            return 1
        if(jokers == 2):
            return 0
        return 3
    if(len(counts) == 1 and counts[0]==4):
        if(jokers==1):
            return 0
        return 1
    if(any(list(map(lambda x: x==3, counts))) and any(list(map(lambda x: x==2, counts)))):
        return 2
    if(len(counts) == 1 and counts[0]==5):
        return 0
    if(jokers == 5): return 0
    if(jokers == 4): return 0
    if(jokers == 3): return 1
    if(jokers == 2): return 3
    if(jokers == 1): return 5
    return 6


hands = []
with open('data') as input:
    for l in input.readlines():
        hand = l.strip().split(' ')[0]
        bid = int(l.strip().split(' ')[1])
        hands.append({'hand': hand, 'bid': bid, 't': getType(hand), 'cv': [cards.index(x) for x in hand]})

    sorted_hands=sorted(hands, key=lambda x: (x['t'], x['cv']), reverse=True)
    for s in list(filter(lambda x: 'J' in x['hand'], sorted_hands)):
        print(s)
    

    print(sum([x['bid'] * (i+1) for i,x in enumerate(sorted_hands)]))


