from java.util import Calendar

import sys, re
import time
from lib.syncUtil import sync
from lib.saveUtil import save
from lib.businessProcesses import getUniqueBPList
from lib.Utils import readDistributionDirectory, parseApplicationNames, intervalToString, getApplicationName, readNumberOfFilesInDistributionDirectory

import lib.logUtil as log

l = log.getLogger(__name__)
DISTDIR				  = sys.argv[1]
distDir				  = DISTDIR

# file name without .ear extension from distDir
ears = readDistributionDirectory(distDir)
appNames = parseApplicationNames(ears)

def remove_v(name):
	return re.sub('_v\d+','',name)

def uninstallAll(appNames):
	start_u = time.clock()
	l.info("Reading distribution directory: ["+distDir+"].")

	l.info("Building appsToDeploy dict...")
	start = time.clock()
	appsToDeploy = {}
	procsToDeploy = {}
	nameVersionREGEX = re.compile('(.*)-((\d+).\d+.\d+(-alpha\d{3})?(-SNAPSHOT)?)')
	processREGEX = re.compile('-prosess-|-microflow-|-bproc-|-bsrv-frg-hentinstitusjonsoppholdliste')
	versionedREGEX = re.compile('-(tjeneste|produsent|konsument)-')
	for appName in appNames:
		name, version, majorVersion = nameVersionREGEX.match(appName).groups()[:3]
		if processREGEX.search(appName): procsToDeploy[name] = version
		else:
			if versionedREGEX.search(name): name = name +'_v'+ majorVersion
			appsToDeploy[name] = version
			
	stop = time.clock()
	l.info("Time elapsed: "+str(round((stop - start), 2))+" sec")

	l.info("Building appsDeployed dict...")
	start = time.clock()

	SCAModulesWithAppName = {}
	appsDeployed = {}


	for module in AdminTask.listSCAModules().split("\n"):
		tmp = module.split(":")
		SCAModulesWithAppName[tmp[0]] = tmp[1]
	#endFor

	pattern = re.compile(r'\s+')
	for SCAModule, appName in SCAModulesWithAppName.items():
		# all modules NOT process or microflow
		if (SCAModule.count('-prosess-') == 0 and SCAModule.count('-microflow-') == 0 and SCAModule.count('-bproc-') == 0):
			buildVersion = re.sub(pattern, '', AdminApp.view(appName, '-buildVersion').replace('Application Build ID:  ', ''))
			appsDeployed[SCAModule] = buildVersion
	#endFor

	stop = time.clock()
	l.info("Time elapsed: "+str(round((stop - start), 2))+" sec")
	bpDict = getUniqueBPList()

	l.info("Processes to deploy [" +str(len(procsToDeploy)) + "]:" + str(procsToDeploy))
	l.info("Processes deployed [" +str(len(bpDict)) + "]:" + str(bpDict))
	l.info("Applications to deploy [" +str(len(appsToDeploy)) + "]:" + str(appsToDeploy))
	l.info("Applications deployed [" +str(len(appsDeployed)) + "]:" + str(appsDeployed))


	# for status and time estimates
	totalNumberOfEars = len(appsToDeploy)
	totalNumberOfProcs = len(procsToDeploy)
	counter = 0
	for processIdToDeploy, versionToDeploy in procsToDeploy.items():
		idToDeploy = processIdToDeploy
		if (processIdToDeploy.count('-bproc-') > 0 or processIdToDeploy.count('-prosess-') > 0):
			idToDeploy = processIdToDeploy+'_v'+versionToDeploy.replace('.','_')
		for processIdDeployed, versionDeployed in bpDict.items():
			if (idToDeploy == processIdDeployed):
				print "\n\n########################################################################################################################"
				print "#"+str("     Processing process module ["+processIdToDeploy+"], application ["+str(counter+1)+"/"+str(totalNumberOfProcs)+"].").ljust(118)+"#"
				print "#"+str("     Old version: "+versionDeployed).ljust(118)+"#"
				print "#"+str("     New version: "+versionToDeploy).ljust(118)+"#"
				print "########################################################################################################################\n"

				bps = bpDict.keys()
				bpFound = 0
				for bp in bps:
					if bp.find(processIdToDeploy) != -1:
						check = versionHandler(processIdToDeploy, versionDeployed, versionToDeploy, 1)
						bpFound = 1
						if (check == 0 or check == 1):
							l.info("versionHandler returned: ["+str(check)+"]")
						else:
							l.error("Unable to find correct action for application " + processIdToDeploy + " with version " + versionToDeploy + " when the version deployed was " + versionDeployed + ".")
						break
				if bpFound:
					break
		counter = counter + 1
	#endFor

	save()

	total = 0
	time_per_app = 0
	time_left = 0
	counter = 0
	save_modulus = 25
	save_counter = 0
	
	for applicationIdToDeploy, versionToDeploy in appsToDeploy.items():
		start = time.clock()
		for applicationIdDeployed, versionDeployed in appsDeployed.items():				
			if (applicationIdToDeploy == applicationIdDeployed):
				print "\n\n########################################################################################################################"
				print "#"+str("         Processing ["+applicationIdToDeploy+"], application ["+str(counter+1)+"/"+str(totalNumberOfEars)+"].").ljust(118)+"#"
				print "#"+str("        Old version: "+versionDeployed).ljust(118)+"#"
				print "#"+str("        New version: "+versionToDeploy).ljust(118)+"#"
				print "#"+str("        Time left: "+intervalToString(round(time_left,0))).ljust(118)+"#"
				print "#"+str("        Time elapsed: "+intervalToString(round(total,0))).ljust(118)+"#"
				print "########################################################################################################################\n"

				check = versionHandler(remove_v(applicationIdToDeploy), versionDeployed, versionToDeploy, 0)

				if (check == 0 or check == 1):
					counter = counter + 1
					l.info("versionHandler returned: ["+str(check)+"]")
					if (check == 1):
						save_counter = save_counter + 1
				else:
					l.error("Unable to find correct action for application " + applicationIdDeployed + " with version " + versionToDeploy + " when the version deployed was " + versionDeployed + ".")

				if (save_counter != 0 and save_counter % save_modulus == 0):
						   save()

		# for status and time estimates
		stop = time.clock()
		total = total + (stop - start)
		if (counter > 0):
			time_per_app = total/counter
		else:
			time_per_app = total
		time_left = (totalNumberOfEars-counter)*time_per_app
	#endFor

	stop_u = time.clock()
	sync()
	l.info("Time elapsed: "+str(round((stop_u - start_u), 2))+" sec")
#endDef

def versionHandler(applicationId, deployedVersion, versionToDeploy, bproc):
	snapshot = versionToDeploy.find('SNAPSHOT')
	majorDeployed = deployedVersion.split('.')[0]
	majorToDeploy = versionToDeploy.split('.')[0]

	if deployedVersion == 'Unknown':
		if bproc:
			l.warning("Found unknown version of BPROC. Remove this manually when server is running.")
			return 0
		else:
			l.info("Found unknown version. Uninstalling.")
			deployed_app = getApplicationName(applicationId, majorToDeploy)
			AdminApp.uninstall(deployed_app)
			return 1

	elif bproc:
		if (versionToDeploy == deployedVersion):
			l.info("BPROC already installed.Deleting %s from list." % (applicationId+"-"+versionToDeploy))
			appNames.remove(applicationId+"-"+versionToDeploy)
		else:
			l.info("BPROC detected. Not uninstalling.")

		return 0

	elif snapshot >= 0:
		l.info("SNAPSHOT detected. Uninstalling.")
		deployed_app = getApplicationName(applicationId, majorToDeploy)
		AdminApp.uninstall(deployed_app)
		return 1

	elif (versionToDeploy == deployedVersion):
		l.info("Version already installed. Deleting %s from list." % (applicationId+"-"+versionToDeploy))
		appNames.remove(applicationId+"-"+versionToDeploy)
		return 0

	elif (majorDeployed != majorToDeploy):
		l.info("Different major version. Not uninstalling.")
		return 0

	else:
		l.info("Same major version. Uninstalling.")
		deployed_app = getApplicationName(applicationId, majorToDeploy)
		AdminApp.uninstall(deployed_app)
		return 1

	return -1
#endDef

##########################################################################
# FUNCTION:
#	installEAR: Installs a J2EE application
# SYNTAX:
#	installEAR appName
# PARAMETERS:
#	appName	 -	Application name
# USAGE NOTES:
#	Installs EAR file onto target as specified.
##########################################################################
def installEAR ( appName, cluster ):
	clusterName = cluster
	installed = ""

	appPath = DISTDIR +"/"+appName+".ear"

	if (clusterName == ""):
		l.error("No serverName/nodeName nor clusterName specified")
		
	options = "[ -verbose -cluster "+clusterName+" -distributeApp ]"
	l.info("Installing ["+appName+"] with options ["+options+"] on "+clusterName+".")

	try:
		installed = AdminApp.install(appPath, options)
	except:
		l.exception("Exception installing "+appName+" to "+clusterName)

	l.info(installed)



def installAll(appNames):
	totalNumberOfEarFiles = readNumberOfFilesInDistributionDirectory(distDir)
	counter = 0

	clusters = AdminConfig.list('ServerCluster', AdminConfig.list('Cell')).splitlines()
	clusterName = ''
	for cluster in clusters:
		if (cluster.find('AppTarget') > 0):
			clusterName = AdminConfig.showAttribute(cluster, 'name')

	if (len(clusterName) == 0):
		l.error("No cluster identified!")

	total = 0
	time_per_app = 0
	time_left = 0
	start = 0
	save_counter = 0
	save_modulus = 25

	for appName in appNames:
		start = time.clock()
		counter = counter + 1
		print "\n\n########################################################################################################################"
		print "#"+str("      Installing ["+appName+"], application ["+str(counter)+"/"+str(totalNumberOfEarFiles)+"].").ljust(118)+"#"
		print "#"+str("      Time left: "+intervalToString(round(time_left,0))).ljust(118)+"#"
		print "#"+str("      Time elapsed: "+intervalToString(round(total,0))).ljust(118)+"#"
		print "########################################################################################################################\n"
		installEAR(appName, clusterName)

		if (save_counter != 0 and save_counter % save_modulus == 0):
			save()

		save_counter = save_counter+1
		stop = time.clock()
	
		total = total + round(stop - start, 2)
		if (counter != 0):
			time_per_app = total/counter
		else:
			time_per_app = total
		time_left = round((totalNumberOfEarFiles-counter)*time_per_app,2)

	#endfor
	sync()
#endef



uninstallAll(appNames)
installAll(appNames)
