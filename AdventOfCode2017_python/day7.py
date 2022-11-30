from collections import Counter

class disc:

    subnames=None
    subs=None
    parent=None
    full_weight=0

    def sub_weight(self):
        if(self.full_weight != 0):
            return self.full_weight
        sub_weight_list = map(lambda sub: sub.sub_weight(), self.subs)
        sub_weight_sum = sum(sub_weight_list) 
        weight_sum = sub_weight_sum + self.weight
        return weight_sum

    def __init__(self, name, weight):
        self.subs=[]
        self.subnames=[]
        self.name = name
        self.weight = weight
        self.full_weight = 0

    def __str__(self):
        return f"{self.name} - {self.weight}, parent: {self.parent.name if self.parent is not None else None}"

    __repr__ = __str__

def find_wrong_breach(disc):
    weight_frequency = Counter(d.sub_weight() for d in disc.subs)
    branch_weights = list(filter(lambda f: weight_frequency[f] == 1, weight_frequency))
    if(len(branch_weights) == 0):
        return None
    wrong_branch = list(filter(lambda d: d.sub_weight() == branch_weights[0], disc.subs))[0]
    wrong_sub_branch = find_wrong_breach(wrong_branch)
    return wrong_branch if wrong_sub_branch is None else wrong_sub_branch


discs = []
discdict = {}
with open('day7.txt') as input:
    for i in input:
        props = i.split()
        newdisc = disc(props[0],int(props[1].replace("(","").replace(")","")))
        discs.append(newdisc)
        discdict[props[0]] = newdisc
        for sub in props[3:]:
            newdisc.subnames.append(sub.replace(",",""))

for d in discs:
    for sub in d.subnames:
        sub_disc = discdict[sub]
        d.subs.append(sub_disc)
        sub_disc.parent = d

root = list(filter(lambda d: d.parent is None, discs))[0]
print(f"root: {root}")

wrong_branch = find_wrong_breach(root)
print(f"wrong_branch: {wrong_branch}")


wrong_branch_should_be_weight = list(filter(lambda b: b != wrong_branch, wrong_branch.parent.subs))[0].sub_weight()

print(f"wrong disc weight should be: {wrong_branch_should_be_weight - wrong_branch.sub_weight() + wrong_branch.weight}")
