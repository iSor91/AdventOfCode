# monkeys shouting
# either a number or a result of a math operation
# numbers are provided from the beginning
# math operations need to wait for their actors.

# what does the root monkey yell? as its operation result

from calculation_monkey import calculation_monkey
from number_monkey import number_monkey


with open('day21') as input:

    dependencies = {}
    monkeys = {}

    for l in input:
        name, definition = l.strip().split(':')
        definition_parts = definition.strip().split()
        if(len(definition_parts) > 1):
            monkey = calculation_monkey(name, definition_parts[1])
            dependencies[name] = [definition_parts[0], definition_parts[2]]
        else:
            monkey = number_monkey(name, int(definition_parts[0]))
        monkeys[name] = monkey
        
    for monkey_to_fill in dependencies:
        curr_monkey = monkeys[monkey_to_fill]
        for dependency in dependencies[monkey_to_fill]:
            curr_monkey.add_dependency(monkeys[dependency])
    
    print(monkeys['root'].get_value())
    

    i = 3560324848000 #got to this with tries
    while(True):
        for m in monkeys:
            monkeys[m].reset()
        monkeys['humn'].value=i
        root_first = monkeys['root'].dependencies[0].get_value() 
        root_second = monkeys['root'].dependencies[1].get_value() 
        if(root_first == root_second):
            break
        # print(i, root_first, root_second)
        if(root_first < root_second):
            break
        i+=1
    
    print(i)
