# i have one ore-collecting robot -> need ore for any robot
# need clay collecting robot
# need obsidian collecting robot -> waterproof with clay
# need geode-cracking robot from obsidian

# every robot collects its resource type 1/min
# robot factory produces robots 1/min
#   consumes the requested resources

# i have blueprints, need to figure out which is the best
# what is the max output of opened geodes after 24 minutes?

# calculate the quality level of each blueprint : blueprint id * max opened geodes
# sum up all the blueprint quality levels

from task_state import task_state
from contants import *


with open('day19_test') as input:
    bp = [[l for l in line.strip().split(':')]for line in input]

blueprints = {}

for b in bp:
    blueprint_id = int(b[0].split()[1])
    blueprint_details = {}
    robot_line = list(filter(lambda l: l != '', b[1].strip().split('.')))
    for robot in robot_line:
        rp = robot.split()
        blueprint_details[rp[1]] = {rp[i]: int(rp[i-1])
                                    for i in range(5, len(rp), 3)}
    blueprints[blueprint_id] = blueprint_details

def calculate_geodes(base_time, i) -> task_state:
    current_blueprint = blueprints[i]
    starting_state = task_state(current_blueprint)
    states = [starting_state]

    max_geode = 0
    prev_max_geode = 0
    for t in range(base_time):
        print(t, end=' - ')
        
        new_states = []
        for current_state in states:
            build_available = current_state.can_build_robots()
            current_state.update_treasury()
            for robot_to_build in priority:
                if( robot_to_build in build_available
                    and (robot_to_build == GEODE 
                        or not current_state.reached_max(robot_to_build))):
                    build_robot_state = current_state.copy()
                    build_robot_state.build_robot(robot_to_build)
                    new_states.append(build_robot_state)

                    if(robot_to_build == GEODE):
                        curr_geode = build_robot_state.robots[GEODE]
                        if(curr_geode > max_geode):
                            max_geode = curr_geode 
                        break
        
        for new_state in new_states:
            states.append(new_state)
        if(max_geode != prev_max_geode):
            states = list(filter(lambda state: state.robots[GEODE] >= max_geode, states))
            prev_max_geode = max_geode
        print(f"states count: {len(states)}")
    states.sort(key=lambda state: state.treasury[GEODE])
    print(f"states count: {len(states)}, max geode = {states[-1]}")
    return states[-1]

    # print(states)


quality_levels = []
for bp_id in blueprints:
    max_state = calculate_geodes(24, bp_id)
    quality_levels.append(max_state.treasury[GEODE] * bp_id)

print(quality_levels, sum(quality_levels))
