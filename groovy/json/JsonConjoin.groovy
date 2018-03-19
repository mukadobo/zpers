import org.codehaus.groovy.runtime.NullObject

class JsonConjoin
{
    static Object of(Object left, Object right)
    {
        // when right is null, just return a deep copy of left

        if (right == null) return deepCopy(left )

        // when left if null,  return deep copy of right UNLESS right is a list of prototypes

        if (left  == null)
        {
            Boolean rightIsProto = right in List

            return rightIsProto ? null : deepCopy(right)
        }

        if ((left in Number) && (right in Number)) return left

        if ((left in Map ) && (right in Map )) return ofMaps (left as Map , right as Map )
        if ((left in List) && (right in List)) return ofLists(left as List, right as List)

        if (left in right.getClass()) return deepCopy(left)

        throw new IllegalArgumentException("Can't conjoin types: left is ${left.getClass()}, right is ${right.getClass()}")
    }

    static private Map ofMaps(Map left, Map right)
    {    
        Map rval = [:] << right << left.collectEntries { k, v -> [k, of(left[k], right[k])] }
    }

    static private List ofLists(List left, List right)
    {
        if (right.isEmpty() ) return deepCopy(left)
        if (right.size() > 1) throw new IllegalArgumentException("Can't process right-side array item with length > 1")

        Object rightItem = right[0]

        left.collect { it -> JsonConjoin.of(it, rightItem) }
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

                return item.collectEntries { key, value -> [key, deepCopy(value)] }

            case List:

                return item.collect { it -> deepCopy(it) }

            default:

                throw new IllegalArgumentException("Don't know how to handle class: ${item.getClass()}")
            
        }
    }
}