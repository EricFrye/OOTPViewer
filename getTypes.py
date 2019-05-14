import sys,re,os

def handle_file (file):
	for _ in range(0,7):
		file.readline()

	data_line = file.readline()		
	
	ret = []
	
	mo = re.search(pattern, data_line)
	while mo:		
		if 'INT' in mo.group(2):
			ret.append('I')
		elif 'VARCHAR' in mo.group(2):
			ret.append('S')
		elif 'DOUBLE' in mo.group(2):
			ret.append('D')
		else:
			ret.append('S')
		
		data_line = data_line[mo.end(2):]
		mo = re.search(pattern, data_line)
	
	return ','.join(ret) 
		
path = 'settings/mysql'

pattern = '`(\w+)` (\w+)'

for root, dirs, files in os.walk(path):
	
	out = ''
	
	for file in files:	
		cur_file = open(path + "/" + file, 'r')
		
		file_pattern = '(\w+.)'
		fom = re.search(file_pattern, file)
		
		out += '{}:{}'.format(file[:fom.end(1)] + 'csv',handle_file(cur_file)) + '\n'
		
output = open(sys.argv[1], 'w')
output.write(out)
