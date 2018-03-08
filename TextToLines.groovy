import groovy.json.JsonBuilder

def String[] textToLines(String text)
{
	def lines     = text.split(/\n/)
	def trimmed   = lines.collect { it.trim() }
	def filtered  = trimmed.findAll { !(it.isEmpty() || it.take(1).equals('#')) }
	
	return filtered as String[]
}

def String[][] linesToColumns(String lines, Object... defaults)
{
	return linesToColumns(textToLines(lines), defaults)
}

def String[][] linesToColumns(String[] lines, Object... defaults)
{
	return lines.collect { line ->
		def columns = line.trim().split(/[ \t]*\|[ \t]*/)
		
		if (defaults != null)
		{
			def defaultsSize = defaults.size()
			def columnsSize  = columns .size()
			
			if (columnsSize < defaultsSize)
			{
				columns += defaults[columnsSize ..< defaultsSize]
			}
			
			def pairs = [columns, defaults].transpose()
			
			columns = pairs.collect { (it[0].isEmpty() || it[0].equals('=')) ? it[1] : it[0] }
		}
		
		return columns as String[]
	}
}


def raw = """
aaa|bbb
#qqq

iii|jjj|kkk
ppp||=|sss
|yyy
"""

def lines = textToLines(raw)

println (new JsonBuilder(lines))

def columnar = linesToColumns(lines, '111', '222', '333', '444', '555')

println "columnar.getClass()=${columnar.getClass()}"
println "${new JsonBuilder(columnar)}"

def column22 = linesToColumns(raw, '111', '222', '333', '444', '555')
println "${new JsonBuilder(column22)}"
