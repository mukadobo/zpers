def raw = "  \t #lalala".trim()

if (raw.take(1).equals('#')) println "COMMENT" else println raw
