from __future__ import nested_scopes

class Table:
	def __init__(self, headlineList):
		self.sizesOfLongestCells = {}
		self.rows = []
		self.addRow(headlineList)
	
	def addRows(self, rowsList):
		for rowList in rowsList:
			self.addRow(rowList)
		
	def addRow(self, rowList):
		for i in range(len(rowList)):
			maxSize = self.sizesOfLongestCells.setdefault(i,0)
			cellSize = len(rowList[i])
			if cellSize > maxSize:
				self.sizesOfLongestCells[i] = cellSize
		self.rows.append(rowList)
		
	def sort(self, columb=0, reverse=False):
		if len(self.rows) > 1:
			headline = self.rows[0]
			
			def key(l): return l[columb]
			sortedList = sorted(self.rows[1:], key=key)
			if reverse:
				sortedList.reverse()
			self.rows = [headline] + sortedList
		return self
			
	def __str__(self):
		lineSeparator = ''
		cellNumbers = self.sizesOfLongestCells.keys()
		for i in sorted(cellNumbers):
			size = self.sizesOfLongestCells[i]
			lineSeparator += '+'
			for i in range(size+2):
				lineSeparator += '-'
		lineSeparator += '+\n'
		
		out = lineSeparator
		
		for row in self.rows:
			for i in range(len(row)):
				cell = row[i]
				size = self.sizesOfLongestCells[i]
				out += '|' + cell.center(size+2)
			out += '|\n'
			out += lineSeparator
				
		return out