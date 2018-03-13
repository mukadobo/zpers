import groovy.json.JsonSlurper

class Thingus
{

Integer x = 0

def Object selectorWaitUntilExtant(Map args = [:], selector)
{
	def MSG_PFX  = 'selectorWaitUntilExtant: '
	
	Boolean firstOnly  = args.firstOnly  ?: false
	
	def obj = new JsonSlurper().parseText(selector)
	
	for(y in 0.. this.x) obj += ['++']
	
	++x
	
	return firstOnly ? obj[0] : obj
}

def SAY(msg)
{
	println(msg)
}

def ERROR(msg)
{
	println(msg)
	throw new Exception(msg)
}

def timeout(Map args = [:], Closure klozure)
{
	Integer time     = args.time     ?: 30
	String  unit     = args.unit     ?: 'SECONDS'
	
	println "timeout: time=$time, unit=$unit"
	
	klozure()
}


def Object selectorObjectsIf(Map args = [:], selector, Closure predicate)
{
	def MSG_PFX  = 'selectorObjectsIf: '
	
	Boolean verbose   = args.verbose  ?: false
	Integer time      = args.time     ?: 30
	String  unit      = args.unit     ?: 'SECONDS'
	Boolean inverted  = args.inverted ?: false
	Boolean firstOnly = args.firstOnly ?: false
	
	def objects = timeout(time:time, unit:unit)
	{ ->
		while (true)
		{
			def objects = selectorWaitUntilExtant(selector, firstOnly:firstOnly)
			
			if (inverted ^ predicate(objects)) return objects
		}
	}
	SAY ("${MSG_PFX}post-timeout: objects=$objects")
	
	objects ? objects :  ERROR ("${MSG_PFX}Resource object(s) do not satisfy predicate within timeout($time, $unit)")
}

}

def thingus = new Thingus()

thingus.selectorObjectsIf('[{"aa":"AA","bb":"BB"},{"ii":"II","jj":"JJ"}]', inverted:false, firstOnly:true)
{ objList ->
	def rval = objList.aa == 'AA'
	println "predicate: rval=$rval; objList.class=${objList.getClass()}; objList=${objList}"
	
	rval
}
