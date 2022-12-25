from monkey import monkey

class number_monkey(monkey):

    def __init__(self, name, number):
        super().__init__(name)
        self.value = number

    def reset(self):
        self.value = self.value