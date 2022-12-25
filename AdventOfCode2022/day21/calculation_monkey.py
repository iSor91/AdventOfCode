from monkey import monkey


class calculation_monkey(monkey):

    def __init__(self, name, operation):
        super().__init__(name)
        self.operation = operation
        self.dependencies = []
    
    def calculate_value(self):
        first = self.dependencies[0].get_value()
        second = self.dependencies[1].get_value()
        # print(first, second)

        match(self.operation):
            case '+': return first + second
            case '-': return first - second
            case '*': return first * second
            case '/': return first // second

    def get_value(self):
        if(self.value == None):
            self.value = self.calculate_value()
            # print('calculated', self.value)
        return super().get_value()

    def add_dependency(self, dependency: 'monkey'):
        self.dependencies.append(dependency)
    
    def __str__(self):
        return f"{self.name} {self.operation} {self.dependencies}"

    __repr__ = __str__