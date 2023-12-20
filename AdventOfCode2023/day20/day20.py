#flip-flops (%) -> on/off, def off
#  - on high pulse nothing happens
#  - on low pulse switch state, send pulse -> on high, off low

#conjunction (&) -> stores the last input from all inputs, def low for every
#  - pulse stored
#  - if every pulse is high, send low
#  - send high otherwise

#broadcast (broadcaster) -> sends incoming pulse to each of its destination

#button -> when pushed, a single low pulse is sent to broadcaster
#  - after push, everything has to be done before second push

#let high pulse be 1 and low pulse be -1

from functools import reduce


pulse_list = []

module_map={}

switch_map={}
conjunction_map={}

sent_pulse_map = {-1: 0, 1: 0}
state_map = {}
rx_pulses = []
change_map = {}

cn_changes = {'sv':[], 'th':[], 'gh':[], 'ch':[]}

def broadcast_pulse(caller, called, pulse):
    # print(f" {caller} {pulse} {called}")
    out_pulse = pulse
    change_map[called].append({'cycle': cycle, 'pulse': out_pulse, 'step': step})
    for destination in module_map[called]['destination']:
        sent_pulse_map[out_pulse]+=1
        pulse_list.append((called, destination, pulse))

def switch_pulse(caller, called, pulse):
    # print(f" {caller} {pulse} {called}")
    if(pulse == -1):
        out_pulse = switch_map[called] * pulse
        change_map[called].append({'cycle': cycle, 'pulse': out_pulse, 'step': step})
        switch_map[called] = out_pulse
        for destination in module_map[called]['destination']:
            sent_pulse_map[out_pulse]+=1
            pulse_list.append((called, destination, out_pulse))

def conjunction_pulse(caller, called, pulse):
    # print(f" {caller} {pulse} {called}")
    prev_out = -1 if all(map(lambda x: x == 1, conjunction_map[called].values())) else 1
    conjunction_map[called][caller] = pulse
    out_pulse = -1 if all(map(lambda x: x == 1, conjunction_map[called].values())) else 1
    if(called =='cn' and pulse == 1):
        cn_changes[caller].append((cycle, step, caller, conjunction_map[called], out_pulse))
    change_map[called].append({'cycle': cycle, 'pulse': out_pulse, 'step': step})
    for destination in module_map[called]['destination']:
        sent_pulse_map[out_pulse]+=1
        pulse_list.append((called, destination, out_pulse))

def rx_pulse(caller, called, pulse):
    if(pulse == -1):
        rx_pulses.append(caller)

module_map['rx']={'full_name': 'rx', 'name': 'rx', 'destination': [], 'action': rx_pulse}

with open ('data') as asdf:

    lines = [l.strip() for l in asdf.readlines()]
    for l in lines:
        [full_name, destination] = l.split(" -> ")
        destination_modules=[d.strip() for d in destination.split(",")]
        action = None
        name=full_name
        if (full_name.startswith("%")):
            name=full_name[1:]
            switch_map[name] = -1
            action=switch_pulse
        elif (full_name.startswith("&")):
            action=conjunction_pulse
            name=full_name[1:]
        else:
            action= broadcast_pulse
        module = {
                'full_name': full_name,
                'name': name,
                'destination': destination_modules,
                'action': action
            }
        module_map[module['name']]=module
        change_map[module['name']]=[]

    for module in module_map.values():
        if (module['full_name'].startswith('&')):
            conjunction_map[module['name']]={}
    for module in module_map.values():
        for dest in module['destination']:
            if(dest in conjunction_map):
                conjunction_map[dest][module['name']]=-1

state_id = f"{str(switch_map)}{str(conjunction_map)}"
cycle = 1
step = 0
while (not all([len(x) > 0 for x in [cn_changes[y] for y in cn_changes]])):
    rx_pulses=[]
    pulse_list.append(('button', 'broadcaster', -1))
    sent_pulse_map[-1]+=1
    while(len(pulse_list) != 0):
        step+=1
        current_pulse = pulse_list[0]
        pulse_list=pulse_list[1:]
        if(current_pulse[1] in module_map):
            module_map[current_pulse[1]]['action'](*current_pulse)

    if(cycle == 1000):
        print(sent_pulse_map[-1], sent_pulse_map[1], sent_pulse_map[-1]*sent_pulse_map[1])

    state_map[state_id]=sent_pulse_map
    # print(state_map)
    state_id = f"{str(switch_map)}{str(conjunction_map)}"
    cycle+=1


for c in cn_changes:
    print(c, cn_changes[c])
print(reduce(lambda acc, a: acc * int(a), [cn_changes[c][0][0] for c in cn_changes], 1))
