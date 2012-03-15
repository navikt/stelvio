'''
	code for making "from lib import *" posible
'''
import re, os
import types

False,True=0,1
testFunctionREGEX = re.compile('.*Test$')
allTests = {}
def main():
	for file in os.listdir(__name__):
		if file.endswith('.py') and not file.startswith('__'):
			impName = file.replace('.py','')
			impPath = __name__+'.'+impName
			tests = {}
			exec('import %s as imp'%impPath)
			for funcName in dir(imp):
				func = getattr(imp, funcName)
				if isTestFunction(func):
					tests[funcName] = func
			allTests[impName] = tests
		
def isTestFunction(func):
	if isinstance(func, types.FunctionType):
		if testFunctionREGEX.match(func.__name__):
			return True
	return False

main()
