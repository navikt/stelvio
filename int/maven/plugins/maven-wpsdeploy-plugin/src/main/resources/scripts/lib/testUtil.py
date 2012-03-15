import sys, re
import lib.color as c

def assertTrue(case): assert case
def assertFalse(case): assert not case
def assertEqual(case1, case2): assert case1 == case2

def runTests(allTests):
	failedTests = 0
	executedTests = 0
	
	for testSetName in allTests.keys():
		testSet = allTests[testSetName]
		print "\nRunning tests in %s:" % testSetName
		for testName in testSet.keys():
			test = testSet[testName]
			print "\tRunning %s test:" % testName,
			try:
				test()
				print c.green("OK")
			except AssertionError, e:
				print c.red("Failed!")
				failedTests += 1
			executedTests += 1
	
	if failedTests:
		print "%s out of %s tests failed!" % (failedTests, executedTests)
		sys.exit(1)
	else:
		print "All %s tests asserted OK!" % executedTests