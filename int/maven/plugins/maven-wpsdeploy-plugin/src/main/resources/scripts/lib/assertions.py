import sys, re
import types

'''Usage:
from lib.assertions import assertTrue, assertFalse, assertEqual, assertNotEqual, assertRegex, assertRaises
'''

False, True = 0,1 #Define False, True

def assertTrue(case, msg=None): assert case, msg
def assertFalse(case, msg=None): assert not case, msg
def assertEqual(case1, case2, msg=None): assert __equal(str(case1), str(case2)), msg
def assertNotEqual(case1, case2, msg=None): assert not __equal(str(case1), str(case2)), msg
def assertRegex(regex, text, msg=None): assert re.match(regex, text), msg
def assertRaises(exception, function, *args, **kwargs):
	try:
		function(*args, **kwargs)
		assert False, 'Function did not raise an exception of type "%s"!' % exception
	except exception:
		assert True
	except:
		assert False, 'Did not catch the expected exception of type "%s" but got an "%s" instead!' % (exception, sys.exc_info()[0])

def __equal(case1, case2):
	if isinstance(case1, types.StringType) or isinstance(case2, types.StringType):
		return str(case1) == str(case2)
	else:
		return case1 == case2