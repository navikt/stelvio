###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:	monitor.py
# Description:	This file contains the following server monitor procedures:
#		
#			togglePerfMonitor
#
#
#******************************************************************************
#******************************************************************************
# Procedure:   	togglePerfMonitor
# Description:	Enable/Disable the Performance Monitoring Service for a server
#****************************************************************************** 
def togglePerfMonitor ( nodeName, serverName, flag ):

	#---------------------------------------------------------------------------------
	# Enable/Disable the Performance Monitoring Service
	#---------------------------------------------------------------------------------
	print "\n ====== Toggle Perf Monitoring Service for "+serverName+" on "+nodeName+" to "+flag+" ======"

	service = toggleService("PMIService", serverName, nodeName, flag)
	if (service == 0):
		print "PMI Service set to "+flag+" successfully"
	else:
		print "PMI Service toggling failed."
	#endIf

#endDef

