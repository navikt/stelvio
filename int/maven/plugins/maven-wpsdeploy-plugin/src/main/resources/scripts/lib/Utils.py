import java.io as io
from java.util import *
from java.io import *
import time

import lib.logUtil as log
l = log.getLogger(__name__)

#--------------------------------------------------------------------
# Procedure:	doesAppExist
# Description:  Determine whether an appication exists
#
# Parameters:	applicationId (w/o version)
#
# Returns:	appName (exists)
#		0 (does not exist)
#--------------------------------------------------------------------
def doesAppExist(applicationId):
	applications = AdminApp.list().splitlines()
	
	for app in applications:
		if(app.find(applicationId + "App") >= 0):
			l.info("Application ["+app+"] was found.")
			return app
	return 0
#endDef 

def getApplicationName(applicationId, majorVersion):
	
	applications = AdminApp.list().splitlines()
	
	for app in applications: 
		if (app.startswith(applicationId + "_v" + majorVersion)):
			l.info("Application name is " + app)
			return app
		elif (app.startswith(applicationId + "App")):
			l.info("Application name is " + app)
			return app

def parseApplicationNames ( ears ):
	l.info("Executing parseApplicationNames...")
	start = time.clock()  
	appNames = []
	for ear in ears:
		app = ear
		dot = app.find(".ear")
		if (dot > 0):
			app = app[0:dot]
		#endIf
		sl = app.rfind("/")
		bs = app.rfind("\\")
		if (bs > sl):
			sl = bs
		#endIf
		if (sl > 0):
			app = app[(sl+1):]
		#endIf
						  
		appNames.append(app)
	#endFor
	stop = time.clock()
	l.info("Time elapsed: "+str(round((stop - start), 2))+" sec")
		
	return appNames 
#endDef

def readDistributionDirectory(distDir):
	l.info("Executing readDistributionDirectory...")
	start = time.clock()	 
	robFile = io.File(distDir);
	listDirs = None
	ears = []
	
	if ( not robFile.exists()):
		l.error("Distribution directory ["+distDir+"] does not exist.")
	if(robFile.canRead() and robFile.isDirectory()):
		listDirs = robFile.list();
		if (len(listDirs) == 0):
			l.error("No files found in distribution directory ["+distDir+"].")

		for name in listDirs:
			dot = name.rfind(".")
			if (dot > 1):
				ext = name[dot:]
				ext = ext.lower()
			else:
				ext = ""
			#endElse
			if (ext == ".ear"):
				ears.append(name)
			#endIf
		#endFor
	   
	else:	
		l.error("Unable to read directory ["+distDir+"].")
	
	stop = time.clock()
	l.info("Time elapsed: "+str(round((stop - start), 2))+" sec")
	
	return ears
#endDef

	
def readNumberOfFilesInDistributionDirectory(distDir):	 
	robFile = io.File(distDir);
	listDirs = None
	ears = []
	
	if ( not robFile.exists()):
		l.error("Distribution directory ["+distDir+"] does not exist.")
	if(robFile.canRead() and robFile.isDirectory()):
		listDirs = robFile.list();
		return len(listDirs)
	
	return 0
#endDef

def intervalToString(intervalSec):
	hour = 0
	minute = 0
	while intervalSec > 3600:
		hour = hour + 1
		intervalSec = intervalSec - 3600
	#endWhile
	while intervalSec > 60:
		minute = minute + 1
		intervalSec = intervalSec - 60
	if hour > 0:
		return "%d Hour(s) %d Minute(s) %d Seconds" %(hour, minute, intervalSec)
	if minute > 0:
		return "%d Minute(s) %d Second(s)" %(minute, intervalSec)
	return "%d Second(s)" %(intervalSec)

#******************************************************************************
# File Name:	   getTimeInterval
# Description:	returns (minutes, seconds) interval of two datetimes
#****************************************************************************** 
def getTimeInterval(start, end):
	seconds = int((end.getTimeInMillis() - start.getTimeInMillis()) / 1000)
	return seconds
#endef

#******************************************************************************
# File Name:	   getTimeIntervalString
# Description:	returns a pretty print string of a TTG based on total minutes
#		and total seconds
#
#  
#****************************************************************************** 
def calcTTGString(totSec, divider, remaining):   
	avgSec = float(totSec) / float(divider)
	remSec = avgSec * remaining
	remHour = 0
	remMin = 0
	while remSec > 3600:
		remHour = remHour + 1
		remSec = remSec - 3600
	while remSec > 60:
		remMin = remMin + 1
		remSec = remSec - 60
	if remHour > 0:
		return "%d Hour(s) %d Minute(s) %d Seconds" %(remHour, remMin, remSec)
	if remMin > 0:
		return "%d Minute(s) %d Second(s)" %(remMin, remSec)
	return "%d Second(s)" %(remSec)
#endef

def save():
	l.info("Saving...")
	AdminConfig.save()
	l.info("Save is done.")
#endDef

def deleteEarFile(appPath): 
	rval = File(str(appPath)).delete()
	l.info("Delete "+appPath+" was executed with return value["+str(rval)+"].")
	return rval

def wsadminToDict( text ) :
	cmdName = 'wsadminToDict'
	result = {}
	if ( text.count( '[' ) == text.count( ']' ) ) and ( text[ 0 ] == '[' ) and ( text[ -1 ] == ']' ) :
		innerText = text[ 1:-1 ].strip()
		delimiters = '.|!@#'		  # Possible delimiter values
		for delim in delimiters :
			if innerText.count( delim ) == 0 :
				for pair in innerText.replace( '] [', ']%s[' % delim ).split( delim ) :
					if ( pair.count('[') == pair.count(']') ) and ( pair[0] == '[' ) and ( pair[-1] == ']' ) :
						contents = pair[ 1:-1 ].strip()
						try :
							( name, value ) = contents.split( ' ', 1 )
						except :
							( name, value ) = ( contents, '' )
						result[ name ] = value
					else :
						l.warning("Unexpected text: ["+pair+"] (ignored).")
				break
		if (len(result) == 0):
			l.error("Unable to process ["+text+"]. No suitable delimiter was found in ["+delimiters+"]")

	else :
		l.error("Unexpected data format: ["+text+"], empty dictionary returned.")

	return result
#endDef