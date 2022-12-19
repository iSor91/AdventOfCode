from contants import *


class task_state:
    def __init__(self, blueprint):
        self.blueprint = blueprint
        self.maxes = {}

        for robot_type in self.blueprint:
            for required_resource in self.blueprint[robot_type]:
                tmp_max = self.blueprint[robot_type][required_resource]
                if(required_resource not in self.maxes or tmp_max > self.maxes[required_resource]):
                    self.maxes[required_resource] = tmp_max


        self.treasury = {
            ORE: 0,
            CLAY: 0,
            OBSIDIAN: 0,
            GEODE: 0
        }

        self.robots = {
            ORE: 1,
            CLAY: 0,
            OBSIDIAN: 0,
            GEODE: 0
        }

    def copy(self):
        new_state = task_state(self.blueprint)
        new_state.maxes = self.maxes
        new_state.treasury = self.treasury.copy()
        new_state.robots = self.robots.copy()
        return new_state

    def reached_max(self, robot):
        return self.maxes[robot] <= self.robots[robot]

    def can_build_robots(self):
        can_build = []
        for robot_type in self.blueprint:
            if (all(self.treasury[required_res] >= self.blueprint[robot_type][required_res] for required_res in self.blueprint[robot_type])):
                can_build.append(robot_type)
        return can_build

    def build_robot(self, robot_to_build):
        self.robots[robot_to_build] += 1
        for required_resource in self.blueprint[robot_to_build]:
            self.treasury[required_resource] -= self.blueprint[robot_to_build][required_resource]
        # print(f"building {robot_to_build} - {self.treasury}")

    def update_treasury(self):
        # print(f"updating treasury {self.treasury}")
        for resource in self.robots:
            self.treasury[resource] += self.robots[resource]

    def __str__(self):
        return f"{self.robots} - {self.treasury} - {self.maxes}"
    
    __repr__ = __str__