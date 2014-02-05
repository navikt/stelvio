#!/bin/env python

import base64
import sys

if len(sys.argv) > 1:
	input = sys.argv[1] or sys.stdin.read()
else:
	input = sys.stdin.read()

print base64.b64encode(input.strip())
