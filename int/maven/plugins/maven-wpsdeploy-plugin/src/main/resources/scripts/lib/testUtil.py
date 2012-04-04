import sys, re
import types
import lib.color as c

def runTests(allTests):
	failedTests = 0
	executedTests = 0
	
	for testSetName in allTests.keys():
		print '\nRunning tests in %s:' % testSetName
		testSet = allTests[testSetName]
		testNames = testSet.keys()
		testNames.sort()
		for testName in testNames:
			test = testSet[testName]
			print '\t%s:' % testName,
			try:
				test()
				print c.green('OK')
			except AssertionError, e:
				if str(e):
					print c.red('Failed:', e)
				else:
					print c.red('Failed!')
				failedTests += 1
			executedTests += 1
	
	if failedTests:
		print c.red('%s out of %s tests failed!' % (failedTests, executedTests))
		sys.exit(1)
	else:
		print c.green('All %s tests asserted OK!' % executedTests)