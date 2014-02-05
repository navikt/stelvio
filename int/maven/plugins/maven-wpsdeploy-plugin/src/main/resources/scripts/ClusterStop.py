import lib.logUtil
from lib.clusterUtil import getCell, getClusterRefs, getStuffBeforeParentheses, stopCluster

log = lib.logUtil.getLogger(__name__)

def main():
	cellName, cellRef = getCell()

	for clusterRef in getClusterRefs():
		clusterName = getStuffBeforeParentheses(clusterRef)
		log.info("Stopping cluster:", clusterName)
		stopCluster(cellName, clusterRef)

if __name__ == '__main__': main()
