#!/bin/env python

import base64
import sys

if len(sys.argv) > 1:
	input = sys.argv[1] 
else:
	if sys.stdin.isatty():
		print "Write the string you want to encode and hit enter:"
	input = sys.stdin.readline()

print base64.b64encode(input.strip())
