import com.mukadobo.zpers.groovy.json.JsonConjoin

Map  mapE  = [:]
List listE = []

Map aaMaps = 
[
    aaNull      : [ aa : null ],
    aaString    : [ aa : "AA" ],
    aaBoolean   : [ aa : true ],
    aaInteger   : [ aa : 42   ],
    aaLong      : [ aa : 43L  ],
    aaFloat     : [ aa : 44.0 ],
    aaDate      : [ aa : new Date() ],
    aaMapE      : [ aa : mapE ],
    
    asMapJ1     : [ aa : [jj:"J1"] ],
    asMapK1     : [ aa : [kk:"K1"] ],
    asMapJ2L1   : [ aa : [jj:"J2", ll:"L1"] ],
    asMapK2L2   : [ aa : [kk:"K2", ll:"L2"] ],
    
    asJ1        : [jj:'J1'],
    asJ2        : [jj:'J2'],
    asK1        : [kk:'K1'],
    asK2        : [kk:'K2'],
    asL1        : [ll:'L1'],
    asL2        : [ll:'L2'],

    asJ2L1      : [jj:"J2", ll:"L1"],
    asK2L2      : [kk:"K2", ll:"L2"],
]

aaMaps.each
{ nm, map ->

    println ("aaMaps vs N.E.R.: $nm = $map")

    assert JsonConjoin.of(null, map ) == map
    assert JsonConjoin.of(map , null) == map

    assert JsonConjoin.of(mapE, map ) == map
    assert JsonConjoin.of(map , mapE) == map

    assert JsonConjoin.of(map , map ) == map
}
println()

lrcMaps = 
[
    ['aaNull   ', 'aaString ', aaMaps.aaString ],
    ['aaString ', 'aaNull   ', aaMaps.aaString ],
    ['aaInteger', 'aaNull   ', aaMaps.aaInteger],
    ['aaInteger', 'aaFloat  ', aaMaps.aaInteger],
    ['aaInteger', 'aaString ', IllegalArgumentException.class],
    ['aaInteger', 'aaMapE   ', IllegalArgumentException.class],
    ['asJ1     ', 'asK1     ', [jj:'J1', kk:'K1'] ],
    ['asK1     ', 'asJ1     ', [jj:'J1', kk:'K1'] ],
    ['asJ2L1   ', 'asK2L2   ', [jj:'J2', kk:'K2', ll:'L1'] ],
    ['asK2L2   ', 'asJ2L1   ', [jj:'J2', kk:'K2', ll:'L2'] ],
]

lrcMaps.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =aaMaps[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=aaMaps[nmRight] 
    Object expected  = spec[2]

    println ("lrcMaps: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    Object    result = null
    Exception caught = null
    try
    {
        result = JsonConjoin.of(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        assert !caught
        assert result == expected
    }
}
println()

Map aaLists = 
[
    aaNull      : [ aaMaps['aaNull   '.trim()] ],
    aaString    : [ aaMaps['aaString '.trim()] ],
    aaBoolean   : [ aaMaps['aaBoolean'.trim()] ],
    aaInteger   : [ aaMaps['aaInteger'.trim()] ],
    aaLong      : [ aaMaps['aaLong   '.trim()] ],
    aaFloat     : [ aaMaps['aaFloat  '.trim()] ],
    aaDate      : [ aaMaps['aaDate   '.trim()] ],
    aaMapE      : [ aaMaps['aaMapE   '.trim()] ],
    
    asMapJ1     : [ aaMaps['asMapJ1  '.trim()] ],
    asMapK1     : [ aaMaps['asMapK1  '.trim()] ],
    asMapJ2L1   : [ aaMaps['asMapJ2L1'.trim()] ],
    asMapK2L2   : [ aaMaps['asMapK2L2'.trim()] ],

    asJ1        : [ aaMaps['asJ1       '.trim()] ],
    asK1        : [ aaMaps['asK1       '.trim()] ],
    asJ2L1      : [ aaMaps['asJ2L1     '.trim()] ],
    asK2L2      : [ aaMaps['asK2L2     '.trim()] ],

    asJ1x2      : [ aaMaps['asJ1       '.trim()], aaMaps['asJ1       '.trim()] ],
    asK1x2      : [ aaMaps['asK1       '.trim()], aaMaps['asK1       '.trim()] ],
    asJ2L1x2    : [ aaMaps['asJ2L1     '.trim()], aaMaps['asJ2L1     '.trim()] ],
    asJ2L1x2    : [ aaMaps['asK2L2     '.trim()], aaMaps['asK2L2     '.trim()] ],
]

aaLists.each
{ nm, list ->

    println ("aaLists vs N.E.R.: $nm = $list")

    Boolean isPlural = list?.size() > 1

    assert JsonConjoin.of(null , isPlural ? list.take(1) : list ) == null
    assert JsonConjoin.of(list , null ) == list

    assert JsonConjoin.of(listE, isPlural ? list.take(1) : list ) == listE
    assert JsonConjoin.of(list , listE) == list

   assert JsonConjoin.of(list  , isPlural ? list.take(1) : list ) == list
}
println()

List lrcAALists = 
[
    ['aaNull   ', 'aaString ', aaLists.aaString ],
    ['aaString ', 'aaNull   ', aaLists.aaString ],
    ['aaInteger', 'aaNull   ', aaLists.aaInteger],
    ['aaInteger', 'aaFloat  ', aaLists.aaInteger],
    ['aaInteger', 'aaString ', IllegalArgumentException.class],
    ['aaInteger', 'aaMapE   ', IllegalArgumentException.class],
    ['asJ1     ', 'asK1     ', [ [jj:'J1', kk:'K1']          ] ],
    ['asK1     ', 'asJ1     ', [ [jj:'J1', kk:'K1']          ] ],
    ['asJ2L1   ', 'asK2L2   ', [ [jj:'J2', kk:'K2', ll:'L1'] ] ],
    ['asK2L2   ', 'asJ2L1   ', [ [jj:'J2', kk:'K2', ll:'L2'] ] ],
    ['asJ1x2   ', 'asK1     ', [ [jj:'J1', kk:'K1',], [jj:'J1', kk:'K1',] ] ],
]

lrcAALists.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =aaLists[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=aaLists[nmRight] 
    Object expected  = spec[2]

    println ("lrcAALists: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    Object    result = null
    Exception caught = null
    try
    {
        result = JsonConjoin.of(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        assert !caught
        assert result == expected
    }
}
println()

Map bbLists = 
[
    asJ1        : [ aaMaps['asJ1       '.trim()] ],
    asK1        : [ aaMaps['asK1       '.trim()] ],
    asJ2L1      : [ aaMaps['asJ2L1     '.trim()] ],
    asK2L2      : [ aaMaps['asK2L2     '.trim()] ],

    asJ1cL1     : [ aaMaps['asJ1       '.trim()], aaMaps['asL1       '.trim()] ],
    asJ2cL2     : [ aaMaps['asJ2       '.trim()], aaMaps['asL2       '.trim()] ],
]

List lrcBBLists = 
[
    ['asJ1cL1   ', 'asK1     ', [ [jj:'J1', kk:'K1'         ], [ll:'L1', kk:'K1'] ] ],
    ['asJ1cL1   ', 'asK2L2   ', [ [jj:'J1', kk:'K2', ll:'L2'], [ll:'L1', kk:'K2'] ] ],
]

lrcBBLists.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =bbLists[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=bbLists[nmRight] 
    Object expected  = spec[2]

    println ("lrcBBLists: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    Object    result = null
    Exception caught = null
    try
    {
        result = JsonConjoin.of(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        assert !caught
        assert result == expected
    }
}
println()


/**/