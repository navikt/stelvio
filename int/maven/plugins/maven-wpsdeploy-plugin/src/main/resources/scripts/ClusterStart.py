import lib.logUtil
from lib.clusterUtil import getCell, getClusterRefs, removeParentheses, startCluster

log = lib.logUtil.getLogger(__name__)

def main():
	cellName, cellRef = getCell()
	clusterRefs = getClusterRefs()
	clusterRefs.reverse()  # Needs to be started in reverse order because apptarget is dependant upon support and messaging
	for clusterRef in clusterRefs:
		clusterName = removeParentheses(clusterRef)
		log.info("Starting cluster:", clusterName)
		startCluster(cellName, clusterRef)

if __name__ == '__main__': main()
