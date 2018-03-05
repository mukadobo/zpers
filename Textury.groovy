@interface NonCPS { }
import groovy.json.JsonBuilder
def trace(pfx, obj) { println "<TRACE> $pfx: ${new JsonBuilder(obj)}" }

// =============================================================================================================================
@NonCPS
def String[] textToLines(String raw)
{
	def lines     = raw.split(/\n/)
	def trimmed   = lines.collect   { line -> line.trim() }
	def filtered  = trimmed.findAll { line -> !(line.isEmpty() || line.take(1).equals('#')) }
	
	return filtered as String[]
}

@NonCPS
def linesToColumnarMaps(String multiLineText, headings)
{
	return linesToColumnarMaps(textToLines(multiLineText), headings)
}

@NonCPS
def Map<String,String>[] linesToColumnarMaps(String[] lines, LinkedHashMap<String,String> headingsMap)
{
	def width = headingsMap.size()
	def headingsMatrix = headingsMap.collect { hdr -> [hdr.key, hdr.value] }
	
	def columnarRaw = lines.collect { line -> 

		def columns = line.trim().split(/[ \t]*\|[ \t]*/)
		
		while (columns.size() < width)
		{
			columns += ''
		}
		
		return columns
	}
	
	def columnarHdr = columnarRaw.collect { line -> 
	
		[line, headingsMatrix].transpose() 
	}

	def lineMaps = columnarHdr.collect { line ->

		line.collectEntries { pair ->
			
			def col = pair[0]
			def hdr = pair[1][0]
			def dft = pair[1][1]
		
			def val = (col.isEmpty() || col.equals('=')) ? dft : col
			
			[(hdr),val]
		}
	}

	return lineMaps
}

// =============================================================================================================================


def echo(m) { println m }
def texturgy = this

// ---------------------

def s = """
aaa|bbb
#fff|ggg| #
    #|hhh

iii|jjj|kkk ||=
 | sss | = | uuu | vvv | www

"""
echo "s ~ ${s  .getClass()}"
echo "s = ${new JsonBuilder(s  )}"
echo ""

def   d = ['ONE':'111', 'TWO':'222', 'TRE':'333']
echo "d ~ ${d  .getClass()}"
echo "d = ${new JsonBuilder(d  )}"
echo ""

//def   h = d.collect { hdr -> [hdr.key, hdr.value] }
//echo "h ~ ${h  .getClass()}"
//echo "h = ${new JsonBuilder(h  )}"
//echo ""

def   t2l    = texturgy.textToLines(s)
echo "t2l(s) ~ ${t2l.getClass()}"
echo "t2l(s) = ${new JsonBuilder(t2l)}"
echo ""

def   l2c      = texturgy.linesToColumnarMaps(t2l, d)
echo "l2c(s,d) ~ ${l2c.getClass()}"
echo "l2c(s,d) = ${new JsonBuilder(l2c)}"
echo ""

def   l2cSM = texturgy.linesToColumnarMaps(s, ['UNO':'111', 'DOS':'222', 'TRE':'333', 'QTR':'444', 'CNQ':'555', 'SEX':'666'])
echo "l2c(s,[...]) ~ ${l2cSM.getClass()}"
echo "l2c(s,[...]) = ${new JsonBuilder(l2cSM)}"
echo ""

echo "s = ${new JsonBuilder(s  )}"
echo "d = ${new JsonBuilder(d  )}"
//echo "h = ${new JsonBuilder(h  )}"
echo "t2l(s) = ${new JsonBuilder(t2l)}"
echo "l2c(s,d) = ${new JsonBuilder(l2c)}"
echo "l2c(s,[...]) = ${new JsonBuilder(l2cSM)}"

// 
// 
// echo "l2c(s                ) = ${new JsonBuilder(texturgy.linesToColumnarMaps(s                ))}"
// echo "l2c(s, d             ) = ${new JsonBuilder(texturgy.linesToColumnarMaps(s, d             ))}"
// echo "l2c(textToLines(s), d) = ${new JsonBuilder(texturgy.linesToColumnarMaps(texturgy.textToLines(s), d))}"
// 
// echo "l2c(s, 'A','B','C'        ) = ${new JsonBuilder(texturgy.linesToColumnarMaps(s, 'A','B','C'        ))}"
// echo "l2c(s, 'A','B','C','='    ) = ${new JsonBuilder(texturgy.linesToColumnarMaps(s, 'A','B','C','='    ))}"
// echo "l2c(s, 'A','B','C','=','E') = ${new JsonBuilder(texturgy.linesToColumnarMaps(s, 'A','B','C','=','E'))}"

l2cSM.each {x -> echo x }

def giveback(a) { a }

if (! texturgy.giveback(null)) echo "Y" else echo "N"
if (! texturgy.giveback("q" )) echo "Y" else echo "N"
if (  texturgy.giveback(null)) echo "Y" else echo "N"
if (  texturgy.giveback("q" )) echo "Y" else echo "N"
