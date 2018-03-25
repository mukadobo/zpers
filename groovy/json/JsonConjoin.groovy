package com.mukadobo.zpers.groovy.json

import org.codehaus.groovy.runtime.NullObject

import com.mukadobo.zpers.groovy.langx.ListX
import com.mukadobo.zpers.groovy.langx.MapX

class JsonConjoin
{
    private Object base

    JsonConjoin(Object base)
    {
        this.base = base
    }
    
    static Object of(Object base, Object under)
    {
        new JsonConjoin(base).of(under)
        null
    }
    
    Object of(Object under)
    {
        // when under is null, just return a deep copy of base

        if (under == null)
        {
            def dpcp = deepCopy(base)

            return dpcp
        }

        // when base is null,  return deep copy of under UNLESS under is a list of prototypes

        if (base  == null)
        {
            Boolean underIsProto = under in List
            Object  rval         = underIsProto ? null : deepCopy(under)

            return rval
        }

        if ((base in Number) && (under in Number)) return base

        if ((base in Map ) && (under in Map )) return ofMaps (base as Map , under as Map )
        if ((base in List) && (under in List)) return ofLists(base as List, under as List)

        if (base in under.getClass()) return deepCopy(base)

        throw new IllegalArgumentException("Can't conjoin types: base is ${base.getClass()}, under is ${under.getClass()}")
    }

    static private Map ofMaps(Map base, Map under)
    {    
        Map filteredDefaults    = MapX.collectEntries (under) { k, v -> base.containsKey(k) ? null : [(k): deepCopy(v) ] }
        Map recursiveBaseValues = MapX.collectEntries (base ) { k, v -> [(k) : of(base[k], under[k])] }

        return filteredDefaults << recursiveBaseValues
    }

    static private List ofLists(List base, List under)
    {
        if (under.isEmpty() ) return deepCopy(base)
        if (under.size() > 1) throw new IllegalArgumentException("Can't process under-side array item with length > 1")

        Object underItem = under[0]

        ListX.collect(base) { it -> [JsonConjoin.of(it, underItem)] }
    }

    static private Object deepCopy(Object item)
    {
        if (item == null) return null

        // NullObject class doesn't behave well in the switch, like the others...

        if (item.getClass() == NullObject.class) return item

        switch (item)
        {
            case Boolean:
            case Number:
            case String:
                
                return item

            case Date:

                return new Date(item.getTime())

            case Map:

                Map rval = MapX.collectEntries(item)  { k, v ->  [(k) : deepCopy(v)] }
                return rval

            case List:

                List rval = ListX.collect(item) { it -> [deepCopy(it)] }
                return rval

            default:

                throw new IllegalArgumentException("Don't know how to handle class: ${item.getClass()}")
            
        }
    }
}