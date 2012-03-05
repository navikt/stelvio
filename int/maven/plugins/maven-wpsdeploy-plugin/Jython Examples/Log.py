#****************************************************************************
#
# File name: Log.py
#
# Description: Log method for wsadmin scripts
#              
#
# Author: test@example.com
#
#****************************************************************************

import os.environ
from time import strftime
import re

cmdLine = os.environ.get("IBM_JAVA_COMMAND_LINE")
scriptName = re.sub(".*-f ([^ ]*).*", r"\1", cmdLine).strip().split("/")

def log(loglevel, msg):
        print "[" + strftime("%Y-%m-%d %H:%M:%S") + ", " + loglevel + ", " + scriptName[len(scriptName)-1] + "] " + msg
# end def log
