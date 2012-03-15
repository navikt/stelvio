import sys, re
import types
import lib.color as c

def assertTrue(case): assert case
def assertFalse(case): assert not case
def assertEqual(case1, case2): 
	if isinstance(case1, types.StringType) or isinstance(case2, types.StringType):
		assert str(case1) == str(case2)
	else:
		assert case1 == case2
def assertRegex(regex, text): assert re.match(regex, text)

def runTests(allTests):
	failedTests = 0
	executedTests = 0
	
	for testSetName in allTests.keys():
		print "\nRunning tests in %s:" % testSetName
		testSet = allTests[testSetName]
		testNames = testSet.keys()
		testNames.sort()
		for testName in testNames:
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
