import groovy.json.JsonBuilder

def echoError(msg) { println msg; throw new Exception(msg) }

enum Directive {
	CREATE_ONLY        (false , true ),
	DELETE_ONLY        (true  , false),
	DELETE_THEN_CREATE (true  , true ),
	;
	
	final Boolean delete;
	final Boolean create;
	
	Directive(Boolean delete, Boolean create)
	{
		this.delete = delete;
		this.create = create;
	}
	
	/**
	 * Can't use `... as Mode` - it causes pipeline sandbox error:
	 *   org.jenkinsci.plugins.scriptsecurity.sandbox.RejectedAccessException: unclassified staticMethod Mode valueOf java.lang.Class java.lang.String
	 */
	static Directive fromText(text)
	{
		text = text.trim().replaceAll(/ *#.*/, '').toUpperCase().replaceAll(/[- ]+/, '_')
		
		def values = Directive.values() as Directive[]
		
		for (int i = 0; i < values.size(); ++i)
		{
			if (values[i].toString().equals(text)) return values[i];
		}
		
		echoError "No such Directive: '${text}'"
	}
}


['Delete only', 'create ONLY ', '   DELETE_THEN_CREATE  # Do Both', 'foo Bar'].each { text ->
	def m = Directive.fromText text
	println "'$text' => m=${new JsonBuilder(m)}: $m.delete; $m.delete"
}
println ''
