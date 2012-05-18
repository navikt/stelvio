class FrozenClass:
    __isfrozen = False
    def __setattr__(self, key, value):
        if self.__isfrozen and not hasattr(self, key):
            raise TypeError( "%r is a frozen class" % self )
        self.__dict__[key] = value
		
    def freeze(self): 
        self.__isfrozen = True
		
		
'''
class Example(FrozenClass):
	def __init__(self):
		self.x = 42
		self.y = 2**3
		
		self.freeze() # no new attributes after this point. 
 
a = Example()
a.x = 10 
a.z = 10 # fails
'''
