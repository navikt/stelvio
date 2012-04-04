import __builtin__

# Define False, True for all scripts
try:
    True and False
except NameError:
    class bool(type(1)):
        def __init__(self, val = 0):
            if val:
                type(1).__init__(self, 1)
            else:
                type(1).__init__(self, 0)
        def __repr__(self):
            if self:
                return 'True'
            else:
                return 'False'

        __str__ = __repr__

    __builtin__.bool = bool
    __builtin__.False = bool(0)
    __builtin__.True = bool(1)
# End fix #3

from lib.testUtil import runTests
from tests import allTests
runTests(allTests)