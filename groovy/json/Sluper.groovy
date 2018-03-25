import groovy.json.JsonSlurper

	
serviceText = '{"status":"HELLO&app=WIRLD","application":{"status":"UP"}}'
health      = new JsonSlurper().parseText(serviceText)
	
println("health.status            =${health.status            }")
println("health.application       =${health.application       }")
println("health.application.status=${health.application.status}")
	
	
	
serviceText = '{"status":"UP","application":{"status":"DOWN"}}'
health      = new JsonSlurper().parseText(serviceText)
	
println("health.status            =${health.status            }")
println("health.application       =${health.application       }")
println("health.application.status=${health.application.status}")


	
serviceText = '{"status":"UP"}'
health      = new JsonSlurper().parseText(serviceText)
	
println("health.status.toLowerCase()             =${health.status.toLowerCase()             }")
println("health.application                      =${health.application                      }")
println("health.application?.status?.toLowerCase()=${health.application?.status?.toLowerCase()}")

println health.status.toLowerCase() != 'up'
println health.application?.status?.toLowerCase() != 'up'

println 'abcdefh'.take(5)
println 'abc'.take(5)
