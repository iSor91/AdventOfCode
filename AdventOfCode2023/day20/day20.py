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

pulse_list = []

module_map={}

switch_map={}
conjunction_map={}

sent_pulse_map = {-1: 0, 1: 0}
state_map = {}
rx_pulses = []

def broadcast_pulse(caller, called, pulse):
    # print(f" {caller} {pulse} {called}")
    out_pulse = pulse
    for destination in module_map[called]['destination']:
        sent_pulse_map[out_pulse]+=1
        pulse_list.append((called, destination, pulse))

def switch_pulse(caller, called, pulse):
    # print(f" {caller} {pulse} {called}")
    if(pulse == -1):
        out_pulse = switch_map[called] * pulse
        switch_map[called] = out_pulse
        for destination in module_map[called]['destination']:
            sent_pulse_map[out_pulse]+=1
            pulse_list.append((called, destination, out_pulse))

def conjunction_pulse(caller, called, pulse):
    # print(f" {caller} {pulse} {called}")
    conjunction_map[called][caller] = pulse
    out_pulse = -1 if all(map(lambda x: x == 1, conjunction_map[called].values())) else 1
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

    for module in module_map.values():
        if (module['full_name'].startswith('&')):
            conjunction_map[module['name']]={}
    for module in module_map.values():
        for dest in module['destination']:
            if(dest in conjunction_map):
                conjunction_map[dest][module['name']]=-1

    # print(module_map)
    # print(conjunction_map)

state_id = f"{str(switch_map)}{str(conjunction_map)}"
cycle = 0
# while(state_id not in state_map and cycle < 1000):
while(len(rx_pulses) != 1):
    rx_pulses=[]
    pulse_list.append(('button', 'broadcaster', -1))
    sent_pulse_map[-1]+=1
    while(len(pulse_list) != 0):
        current_pulse = pulse_list[0]
        pulse_list=pulse_list[1:]
        if(current_pulse[1] in module_map):
            module_map[current_pulse[1]]['action'](*current_pulse)

    # state_map[state_id]=sent_pulse_map
    sent_pulse_map={-1: 0, 1: 0}
    # print(state_map)
    state_id = f"{str(switch_map)}{str(conjunction_map)}"
    cycle+=1
    print(cycle, rx_pulses)

full_cycles = int(1000/len(state_map))
remainder = 1000%len(state_map)
repeat_keys = [(i,x) for i,x in enumerate(state_map.keys()) if i < remainder]

print(full_cycles, remainder, repeat_keys)

sent_high_pulses_fc = [state_map[s][1] * full_cycles for s in state_map]
sent_low_pulses_fc = [state_map[s][-1] * full_cycles for s in state_map]

sent_high_pulses_rem = [state_map[s][1] for s in state_map if s in repeat_keys]
sent_low_pulses_rem = [state_map[s][-1] for s in state_map if s in repeat_keys]

print(sent_high_pulses_fc, sent_high_pulses_rem, sent_low_pulses_fc, sent_low_pulses_rem)

high=sum(sent_high_pulses_fc) + sum(sent_high_pulses_rem)
low=sum(sent_low_pulses_fc) + sum(sent_low_pulses_rem)

print(high, low, high*low)

print(cycle)
