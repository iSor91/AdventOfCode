class monkey:

    def __init__(self, name):
        self.name = name
        self.value = None

    def get_value(self):
        return self.value

    def __str__(self):
        return f"{self.name}, {self.value}"
    
    def reset(self):
        self.value = None
        
    __repr__ = __str__