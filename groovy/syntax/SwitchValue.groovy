
def String switchback(Integer x)
{
	String r = { switch (x)
	{
		case 1: "one"; break
		case 2: "two"; break
		case 3: "tre"; break
		
		default: "WTF"; break;;;
	}}()
	
	r
}

def Integer backswitch(String s)
{
	Integer r = { switch (s)
	{
		case "one": 1; break
		case "two": 2; break
		case ~/(?i)TRe/: 3; break
		
		default: -1; break;
	}}()
	
	return r
}

for (i in 0..4) println("$i -> ${switchback(i)} -> ${backswitch(switchback(i))}")
