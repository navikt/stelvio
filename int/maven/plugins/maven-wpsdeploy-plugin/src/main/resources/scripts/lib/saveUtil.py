import lib.logUtil as log
l = log.getLogger(__name__)

def save():
	l.info("Saving...")
	AdminConfig.save()
	l.info("Save done!")