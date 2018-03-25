//import groovy.json.JsonBuilder

def echoError(desc) {
	println "Error: $desc"
}

def raw = """
# [P1/][=+]name1:tag1  | [P2/][=+]name2:tag2
aaaa | bbbb | cccc
aaaa || cccc
pppp/=:tttt

aaaa
@aaaa:iiii; pppp/=aaaa | @:uuuu
pppp/=aaaa:iiii |
pppp/=aaaa:iiii | @
pppp/@aaaa:iiii | qqqq/=

-/=aaaa
pppp/@iiii:tttt 
pppp/@iiii:tttt | qqqq/=jjjj:uuuu
iiii:tttt | =-:uuuu
iiii:tttt | qqqq/-
"""

def setIfPresent(map, key, String value) {
	if (value == null)
		return
		
	value = value.trim()
	
	if (value.isEmpty() || value.equals("-"))
		return

	map[key] = value
}

//@NonCPS
def configAbsorbSpec(config, spec) {
	def matchup = spec =~ /^\s*(([-\w]+)\s*\/\s*)?([=@])?(([-\w]+)\s*)?(:\s*([-.\w]+))?\s*$/
	if (matchup.size() == 0) {
		echoError "Can't parse specification: '$spec'"
		return [:]
	}
	def groupings=matchup[0]
	
	setIfPresent(config, 'project', groupings[2])
	setIfPresent(config, 'pfxMark', groupings[3])
	setIfPresent(config, 'name'   , groupings[5])
	setIfPresent(config, 'tag'    , groupings[7])
	
	return config
}

def specList = raw.split(/[;\n]/)
for (int i = 0; i < specList.size(); ++i) {
	spec = specList[i].trim()
	if (spec.isEmpty() || spec.take(1).equals("#")) continue
	println '================================================================================'

	def specPair = spec.split(/[ \t]*\|[ \t]*/)
	
	def srcSpec = null
	def tgtSpec = null
	switch(specPair.size())
	{
		case 1: srcSpec = specPair[0]; tgtSpec = '-'        ; break;
		case 2: srcSpec = specPair[0]; tgtSpec = specPair[1]; break;
		
		default: 	echoError "Bad specification: too many '|' separators: '${spec}'"
					continue
	}
	println "SpecPair: $srcSpec -> $tgtSpec"
	
	def srcConfig = [
		project : 'PPPP',
		pfxMark : '@',
		name    : null,
		tag     : 'TTTT',
	] 
	configAbsorbSpec(srcConfig, srcSpec)

	def tgtConfig = [
		project : 'QQQQ',
		pfxMark : srcConfig.pfxMark,
		name    : srcConfig.name,
		tag     : srcConfig.tag,
	] 
	configAbsorbSpec(tgtConfig, tgtSpec)

	println "srcConfig: ${new JsonBuilder(srcConfig)}"
	println "tgtConfig: ${new JsonBuilder(tgtConfig)}"
	
	if (srcConfig.name == null) {
		echoError "Bad specification: no source image base-name: '${spec}'"
		continue
	}
}
println '================================================================================'
