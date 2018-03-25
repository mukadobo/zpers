package com.mukadobo.zpers.groovy.langx

class MapX
{
    static Object getOrDefault(java.util.Map map, Object key, Object value)
    {
        map.getOrDefault(key, value)
    }

    static Object putIfAbsent(java.util.Map map, Object key, Object value)
    {
        map.putIfAbsent(key, value)
    }

    static Map collectEntries(Map map, Closure closure)
    {
        // GRRR: Jenkins CPS is broken ~ Map#collectEntries // https://issues.jenkins-ci.org/browse/JENKINS-28276

        if (!map || !closure) return [:]

        Map rval = [:]
        for(e in map)
        {
            Map transformedEntry = closure(e.key, e.value)
            if (transformedEntry)
            {
                rval << transformedEntry
            }
        }

        return rval
    }

   static Map overlay(java.util.Map... maps)
    {
        Map rval = [:]

        for (map in maps) 
            if (map) 
                rval << map

        rval
    }
}