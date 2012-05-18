# ibmfixes.py by Dave Brand, http://dbrand666.wordpress.com/
# version 0.7
#
# This module is a collection of fixes for Jython 2.1 especially as
# used by IBM's WebSphere wsadmin utility.  Import it early and often.
#
# Apologies for most of the constructs I've used in this file.  Most
# of what we're trying to accomplish here leans on dark details of the
# current wsadmin and Jython implementation.  I've stuck catch-all's
# around anything that I fear might change in future implementations.
# The theory is that future implementations won't need the fixes and
# they should fail silently.
#
# Feel free to use it without understanding it.  Read it and learn if
# you're so inclined but don't learn programmming style from it.
#
# The latest version can always be found at:
#	 http://dbrand666.wordpress.com/ibmfixes-py/

import __builtin__
import os, sys

# There isn't much here that's useful for Python, but let it pass anyway
try:
	import java
except:
	pass

# We're going to be referencing the top-level frame in many of the fixes.
# Find it once.
topframe = sys._getframe()
up1frame = topframe.f_back
while topframe.f_back:
	topframe = topframe.f_back

# Fix #1 (http://dbrand666.wordpress.com/fix1/)
#
# Fix for os.environ on Windows Servers post 2001.
#
# As of WebSphere 7.0, IBM is still shipping with Jython 2.1.  That
# means it doesn't know about any os.name's which might have been
# added since 2001, which would include versions of Windows starting
# with Windows 2003.  Without this, you'd see an error about /bin/sh
# when using os.environ and related functionality on Windows 2003 and
# later servers.
try:
	import javaos
	if javaos._osType == 'posix' and \
			java.lang.System.getProperty('os.name').startswith('Windows'):
		sys.registry.setProperty('python.os', 'nt')
		reload(javaos)
except:
	pass
# End fix #1

# Fix #2 (http://dbrand666.wordpress.com/fix2/)
#
# Allow the Admin* "modules" to be import'ed.
#
# The Admin* modules aren't actually available as modules.  You can
# use them in the script invoked directly by wsadmin, but various
# workarounds are required if you want to be able to use them in
# imported modules.  All of the fixes I've seen published depend on
# inserting a block of code into the top level script.  I wanted
# something less invasive so I came up with this.
try:
	for module in 'AdminApp', 'AdminConfig', 'AdminControl', 'AdminTask', 'Help':
		if topframe.f_globals.has_key(module):
			sys.modules[module] = topframe.f_globals[module]
except:
	pass
# End fix #2

# Fix #3 (http://dbrand666.wordpress.com/fix3/)
#
# True and False
#
# This one's trivial so why not do it?  Define a bool type and True
# and False (but only if they're not already defined).
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

# Fix #4 (http://dbrand666.wordpress.com/fix4/)
#
# __name__ of top level script should be __main__
#
# When running a script in wsadmin, the top-level __name__ is "main"
# instead of "__main__".  This will fix that.
#
# Since this could potentially break an existing main and since it's
# really only of interest to main, we only do this only if we're
# imported directly the top frame.
try:
	if up1frame == topframe and topframe.f_locals['__name__'] == 'main':
		topframe.f_locals['__name__'] = '__main__'
except:
	pass
# End fix #4

# Fix #5 (http://dbrand666.wordpress.com/fix5/)
# @Karl Gustav: Removed because it is fixed in Executor.py
# End fix #5

# Fix #6 (http://dbrand666.wordpress.com/fix6/)
# @Karl Gustav: Removed because it is fixed in Executor.py
# End fix #6

# Fix #7
#
# sorted()
#
# This isn't really a fix, but I like to be able to sort a list
# without modifying it.  This just implements the sorted function as
# introduced in Python 2.4.
# @Karl Gustav: We have made our own sorted function adhering more to the python implementation

try:
	sorted
except NameError:
	def sorted(list, key=None, reverse=False):
		if key:
			if not list: 
				return []
			else:
				pivot = list[0]
				pivotKey = key(pivot)
				lesser = sorted([x for x in list[1:] if key(x) < pivotKey], key)
				greater = sorted([x for x in list[1:] if key(x) >= pivotKey], key)
				list = lesser + [pivot] + greater
		else:
			list = list[:]
			list.sort()
			
		if reverse:
			list.reverse()
		
		return list
	__builtin__.sorted = sorted
# End fix #7

# Fix #8
# @Karl Gustav: dict is not implemented in Jython 2.1
try:
	dict
except NameError:
	def dict(sequence):
		returnDict = {}
		for key, value in sequence:
			returnDict[key] = value
		return returnDict
	__builtin__.dict = dict