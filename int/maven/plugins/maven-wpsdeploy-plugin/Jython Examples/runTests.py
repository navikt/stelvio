from glob import glob
tests = glob('tests/*.py')
for f in tests:
	print "Running tests in %s:" % f
	execfile(f)