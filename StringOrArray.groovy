
def s = "sss";
def a = ["aaa", "bbb"]

println(s)
println(a)

println (s instanceof String)
println (a.getClass().isArray())
println (a instanceof List)
println ("-----")

def bases = ["foo", ["bar", "barty"], ["tiz", "tuxedo"], "nut"]

for (b in bases) {
	if (b instanceof String)
		println (b)
	else
		println ("${b[0]} ~ ${b[1]}")
}