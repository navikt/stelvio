
def findCellName():
	cells = AdminConfig.list("Cell").split(java.lang.System.getProperty('line.separator'))

	if len(cells) > 1:
		print "[FATAL] More than one cells found. Bailing out..."

	cellName = AdminConfig.showAttribute(cells[0], "name")
	print "Using cell name: " + cellName
	return cellName
