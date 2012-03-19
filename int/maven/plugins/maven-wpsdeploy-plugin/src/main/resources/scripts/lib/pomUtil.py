import re, os

DEFAULT_MVN_REPOSITORY = 'http://maven.adeo.no/m2internal'
DEFAULT_MVN_SNAPSHOT_REPOSITORY = 'http://maven.adeo.no/nexus/content/repositories/m2snapshot'
DEFAULT_MVN_LOCAL_REPOSITORY = ''

class MavenRepo:
	def __init__(self, repository=None, snapshotRepository=None, localRepository=None):
		self.repository = repository or DEFAULT_MVN_REPOSITORY
		self.snapshotRepository = snapshotRepository or DEFAULT_MVN_SNAPSHOT_REPOSITORY
		self.localRepository = localRepository or DEFAULT_MVN_LOCAL_REPOSITORY

class PomModule:
	"""Denne modulen kan bli kalt med:
		- ikke navngitte arugmenter i denne rekkefolgen ('groupId', 'artifactId', 'type', 'version', 'repository')
		- navgitte argument i tilfeldig rekkefølge(groupId='', artifactId='', version='', type='', repository='')
	"""
	def __init__(self, *args, **kwargs):
		if kwargs:
			self.groupId = kwargs['groupId']
			self.artifactId = kwargs['artifactId']
			self.type = kwargs['type']
			self.version = kwargs['version']
			self.repository = kwargs['repository']
		else:
			self.groupId = args[0]
			self.artifactId = args[1]
			self.type = args[2]
			self.version = args[3]
			self.repository = args[4]

	def __hash__(self):
		return hash(self.artifactId + self.version)

	def __str__(self):
		return 'artifactId:\t%s\ngroupId:\t%s\nversion:\t%s\ntype:\t\t%s\n--' % (self.artifactId, self.groupId, self.version, self.type)

	def __eq__(self, other):
		if isinstance(other, self.__class__):
			return (self.artifactId == other.artifactId and
				self.groupId == other.groupId and
				self.type == other.type and
				self.version == other.version)
		else:
			return False
	
	def __resolveRepo(self):
		if self.version.endswith('-SNAPSHOT'):
			return self.repository.snapshotRepository
		else:
			return self.repository.repository

	def getUrl(self):
		groupUrl = self.groupId.replace('.','/')
		url = '/%s/%s/%s' % (groupUrl, self.artifactId, self.version)
		fileUrl = self.repository.localRepository + url
		httpUrl = self.__resolveRepo() + url
		if os.path.exists(fileUrl.replace('file://','')):
			return fileUrl
		else:
			return httpUrl

	def getWsdlifUrl(self):
		return '%s/%s-%s-wsdlif.zip' % (self.getUrl(), self.artifactId, self.version)