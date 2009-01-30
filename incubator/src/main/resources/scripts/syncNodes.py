###############################################################################
# "This program may be used, executed, copied, modified and distributed without
# royalty for the purpose of developing, using, marketing, or distributing."
#
# Product 5630-A36 (C) COPYRIGHT International Business Machines Corp., 2006, 2007
# All Rights Reserved * Licensed Materials - Property of IBM
###############################################################################

#******************************************************************************
# File Name:   	syncNodes.py
# Description: 	Used to synchronize nodes.
#			
# History:     
#******************************************************************************
import sys
import java

execfile( WSADMIN_SCRIPTS_HOME+"/utils6.py" )

#===========================================================================================
#  Synchronize Nodes
#===========================================================================================
print "\n** Synching Nodes To Master **"
# USAGE:  syncNodesToMaster( nodeList )
syncNodesToMaster( WPSnodeName )

