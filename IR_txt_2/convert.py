from os import listdir
from os.path import isfile, join
from datetime import date

import logging
import json

logging.basicConfig(level=logging.INFO)

def isInt(value):
	try:
		int(value)
		return True
	except:
		return False
	
def isFloat(value):
	try:
		float(value)
		return True
	except:
		return False

def onedIrIdConst(ccy, index):
	metaDic = {}
	
	metaDic['type'] = "YIELD_CURVE"
	metaDic['parameters'] = [ccy,index]
	
	return metaDic
	
def oneIrPointConst(days, value):
	pointDic = {}
	pointDic['x'] = int(days)
	pointDic['y'] = None
	pointDic['z'] = None
	pointDic['values'] = [value]

	return pointDic
	
startDate = date(2009, 1, 26)
daysLimit = 950
	
mypath = './data'
myout = './out'
outCcy = 'JPY'

onlyFilesNames = [f for f in listdir(mypath) if isfile(join(mypath, f))]

marketDataList = []

for oneFileName in onlyFilesNames:
	oneFilePath = join(mypath,oneFileName)
	ontFileOutPath  = join(myout, oneFileName)
	
	if len(oneFileName) < 4:
		continue;
		
	ccy = oneFileName[0:3]
	index = oneFileName[3:]
	index = index[:len(index)-4]
	
	if ccy <> outCcy:
		continue;
	
	oneFileMeta = oneFileName.split('.')

	logging.info('oneFile: %s' % (oneFilePath) )
	logging.info('shortName: %s' % (oneFileMeta[0]))
	
	with open(oneFilePath) as oneFile:
		dataStarted = False
		lines = oneFile.readlines()
		
		lines = [x.strip() for x in lines] 
		marketDataDic = {}
		pointsList = []
		
		for line in lines:
			tokens = []
			
			if dataStarted:
				tokens = line.split()
		
			if line.startswith('DATE'):
				dataStarted = True
			
			if len(tokens) < 2:
				continue
				
			#logging.info('lenTokens: %s, isFLoat: %s' % (len(tokens), isFloat(tokens[1])) )
			#logging.info('first: %s, second: %s' % (tokens[0], tokens[1]) )
			
			if len(tokens) < 2 or not isFloat(tokens[1]):
				continue
				
			dateToks = tokens[0].split('-')
			#logging.info('lenDates: %s, value: %s' % (len(dateToks), tokens[0]) )
			#logging.info('isInt1: %s, isInt2: %s, isInt3: %s' % ( isInt(dateToks[0]), isInt(dateToks[1]), isInt(dateToks[2]) ) )
			#logging.info('first: %s, second: %s, third: %s' % (isInt(dateToks[0]), isInt(dateToks[1]), isInt(dateToks[2]) ) )
			
			if len(dateToks) < 3 or not isInt(dateToks[0]) or not isInt(dateToks[1]) or not isInt(dateToks[2]):
				continue
			
			
			currentDate = date(int(dateToks[0]), int(dateToks[1]), int(dateToks[2]))
			dateDelta = currentDate - startDate
			#logging.info('delta: %s' % (dateDelta.days))
			
			if dateDelta.days > 0 and dateDelta.days < daysLimit:
				#logging.info('x: %s, value: %s' % (dateDelta.days, tokens[1]) )
				#logging.info('JSON.point: %s' % (json.dumps(oneIrPointConst(dateDelta.days, float(tokens[1])/100.), indent=2, sort_keys=False)) )
				pointsList.append(oneIrPointConst(dateDelta.days, float(tokens[1])/100.))

		marketDataDic['id'] = onedIrIdConst(ccy, index)
		marketDataDic['points'] = pointsList
		marketDataDic['metaData'] = {}
		
		#logging.info('JSON.marketData: %s' % (json.dumps(marketDataDic, indent=2, sort_keys=False)) )
		marketDataList.append(marketDataDic)

def outputOneFileAll(marketDataList):
	# first output, market data in one file		
	docDic = {}
	docDic['marketData'] = marketDataList
	docDic['historicalData'] = []
	docDic['configuration'] = {}

	outFile = open(join(myout,outCcy + '_allMarket.json'), 'w')
	outFile.write(json.dumps(docDic, indent=2, sort_keys=False))
	outFile.close
	
	return
	

def outputAllFiles(marketDataList):
	# second output, market data each in its own file
	for marketDataDic in marketDataList:
		docDic = {}
		docDic['marketData'] = [marketDataDic]
		docDic['historicalData'] = []
		docDic['configuration'] = {}
		
		fileName = join(myout, marketDataDic['id']['parameters'][0]+marketDataDic['id']['parameters'][1]+'.json')
		logging.info('fileName: %s' % (fileName) )
		outFile = open(fileName, 'w')
		outFile.write(json.dumps(docDic, indent=2, sort_keys=False))
		outFile.close
	return

def outputOneFileGroupScenario(marketDataList):
	# third output, market data in one file but scenarios groupped
	metaMarketDataDic = {}
	allPoints = {}
	pointsList = []

	# get a global list of points key is days
	for marketDataDic in marketDataList:
		for point in marketDataDic['points']:
			if point['x'] in allPoints:
				allPoints[point['x']]['values'].append(point['values'][0])
			if not point['x'] in allPoints:
				allPoints[point['x']] = oneIrPointConst(point['x'], point['values'][0])
	
	#logging.info('allPoints: %s' % (json.dumps(allPoints, indent=2, sort_keys=False)))
				
	# get the max scenario number
	maxLen = 0
	for key, point in allPoints.iteritems():
		if len(point['values']) > maxLen:
			maxLen = len(point['values'])
	#logging.info('maxLen: %s' % ( maxLen) )
	
	
	# construct a list of all values for each point that is present in all market datas
	for key, point in allPoints.iteritems():
		if len(point['values']) == maxLen:
			pointsList.append(point)
		
	metaMarketDataDic['id'] = onedIrIdConst(outCcy, 'parallel')
	metaMarketDataDic['points'] = pointsList
	metaMarketDataDic['metaData'] = {}
			
	docDic = {}
	docDic['marketData'] = [metaMarketDataDic]
	docDic['historicalData'] = []
	docDic['configuration'] = {}

	fileName = join(myout, outCcy + '_allMarketParallel.json')
	logging.info('fileName: %s' % (fileName) )
	outFile = open(fileName, 'w')
	outFile.write(json.dumps(docDic, indent=2, sort_keys=False))
	outFile.close
	
	return
	
outputOneFileAll(marketDataList)	
outputAllFiles(marketDataList)
outputOneFileGroupScenario(marketDataList)