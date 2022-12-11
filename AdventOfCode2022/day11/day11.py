import yaml
from yaml.loader import SafeLoader
import math

class monkey:

    def __init__(self, num, attributes):
        self.num = num
        self.items = list(map(lambda x: item(x), map(int, str(attributes['Starting items']).replace(',', '').split())))
        op = attributes['Operation'].split()
        self.change = int(op[4]) if op[4] != 'old' else -1
        self.operation = lambda x: (x + (self.change if self.change!=-1 else x)) if op[3] == '+' else (x * (self.change if self.change!=-1 else x))
        self.divider = int(attributes['Test'].split()[2])
        self.test = lambda x: x % self.divider == 0
        self.success = int(attributes['If true'].split()[3])
        self.unsuccess = int(attributes['If false'].split()[3])
        self.activity = 0
    
    def __str__(self):
        return f"Monkey {self.num}, items: {self.items}, activity: {self.activity}, change: {self.change}"
    
    __repr__ = __str__

    def init_congruents(self, congruents):
        for i in self.items:
            i.init_congruents(congruents)


class item:
    def __init__(self, initialworry):
        self.congruents = {}
        self.worry = initialworry
    
    def init_congruents (self, congruents):
        for c in congruents:
            print(f"{self.worry} % {c} = {self.worry % c}")
            self.congruents[c] = self.worry % c

    def __str__(self):
        return ','.join([str(self.congruents[c]) for c in self.congruents])
    
    __repr__ = __str__
    

m_d = {}

with open('day11') as input:
    monkeys = yaml.load(input, Loader=SafeLoader)
    congruents = []
    for m in monkeys:
        new_monkey = monkey(int(m.split()[1]), monkeys[m])
        m_d[new_monkey.num] = new_monkey
        congruents.append(new_monkey.divider)

    for m in m_d:
        m_d[m].init_congruents(congruents)
    print(congruents)
    print(m_d)
    
    for j in range(10000):
        for i in range(len(m_d)):
            cm = m_d[i]
            for it in cm.items:
                # new_val = cm.operation(it.worry)
                # new_val = new_val // 3
                # it.worry = new_val 
                # target = cm.success if cm.test(new_val) else cm.unsuccess

                for c in it.congruents:
                    it.congruents[c] = cm.operation(it.congruents[c]) % c
                target = cm.success if it.congruents[cm.divider] == 0 else cm.unsuccess

                m_d[target].items.append(it)
                # print(f"moving {it} to {target}")

                cm.activity += 1
            cm.items.clear()
            # print(cm)
    activities = sorted(list(map(lambda x: x.activity, m_d.values())))
    print(activities)
    result = math.prod(activities[-2:])
    print(result)


    