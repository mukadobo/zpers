@interface NonCPS { }
import groovy.json.JsonBuilder
def trace(pfx, obj) { println "<TRACE> $pfx: ${new JsonBuilder(obj)}" }

// =============================================================================================================================


// =============================================================================================================================

def d = [ aaa : 'aaa', bbb : 'bbb', ]
def m = [ aaa : 'AAA',              ccc : 'CCC' ]

println "d=${d}"
println "m=${m}"
println ''

def s = [:] << d << m
println "d=${d}"
println "m=${m}"
println "s=${s}"
println ''

def t = [:] << (d << m)
println "d=${d}"
println "m=${m}"
println "t=${t}"
println ''

def u = [:] << d <<
[
	bbb: '222', ddd: '444'
]
println "d=${d}"
println "m=${m}"
println "t=${t}"
println "u=${u}"
println ''

println "u.aaa=${u.aaa}"
println "u['aaa']=${u['aaa']}"
println "u['qqq']=${u['qqq']}"
println "u.qqq=${u.qqq}"
println ''

String vuaaa = u.qqq ?: null
println "vuaaa=${vuaaa}"
println ''

def echo (m) { println(m) }
def error(m) { }

def Object argGetOrError(String pfx, Map<String, Object> map, String key, defaultValue = null)
{
	Object value = map[key] ?: defaultValue
	
	if (! value)
	{
		String msg = "${pfx}No such argument key '${key}'"
		echo   msg
		error  msg
	}
	
	value
}

String argAaa = argGetOrError("PFX: ", u, 'aaa')
String argGgg = argGetOrError("PFX: ", u, 'ggg')
String argHhh = argGetOrError("PFX: ", u, 'hhh', 'HHH')

println "argAaa=${argAaa}"
println "argGgg=${argGgg}"
println "argHhh=${argHhh}"
println ''
