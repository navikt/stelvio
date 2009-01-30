
def change_classloader(name):
    print "Modifying classloader settings...\n"
    deployment = AdminConfig.getid("/Deployment:" + name)
    if deployment == "":
        print name + " does not exist, unable to change classloader mode!"
        sys.exit(1)
    depObject = AdminConfig.showAttribute(deployment, "deployedObject")
    webMod = AdminConfig.showAttribute(depObject, "modules").replace("[","").replace("]","")
    classldr = AdminConfig.showAttribute(webMod, "classloader")
    AdminConfig.modify(classldr,[['mode', 'PARENT_LAST']])
    AdminConfig.save()
    print "Done!\n"
#enddef

#####################################
# MAIN
#####################################

if len(sys.argv) < 1:
	print "Invalid argument! Usage changeClassLoader appName"
	sys.exit(1)

appname = sys.argv[0]
change_classloader(appname)