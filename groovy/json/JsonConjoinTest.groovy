
Map mapEmpty   = [:]
List listEmpty = []

Map aaMaps = [
    aaNull      : [ aa : null ],
    aaString    : [ aa : "AA" ],
    aaBoolean   : [ aa : true ],
    aaInteger   : [ aa : 42   ],
    aaLong      : [ aa : 43L  ],
    aaFloat     : [ aa : 44.0 ],
    aaDate      : [ aa : new Date() ],
    aaMapE      : [ aa : [:] ],
    
    aaMapJJ     : [ aa : [jj:"J1"] ],
    aaMapKK     : [ aa : [kk:"K1"] ],
    aaMapJJLL   : [ aa : [jj:"J2",ll:"L1"] ],
    aaMapKKLL   : [ aa : [kk:"K2",ll:"L2"] ],
    
    jj          : [jj:"J1"],
    kk          : [kk:"K1"],
    jjkk        : [jj:"J2",ll:"L1"],
    kkll        : [kk:"K2",ll:"L2"],
]

aaMaps.each
{ nm, map ->

    println ("Test vs N.E.R.: $nm = $map")

    assert JsonConjoin.of(null, map ) == map
    assert JsonConjoin.of(map , null) == map
    assert JsonConjoin.of([:] , map ) == map
    assert JsonConjoin.of(map , [:] ) == map
    assert JsonConjoin.of(map , map ) == map
}
println()

lrc = [
    ['aaNull   ', 'aaString ', aaMaps.aaString ],
    ['aaString ', 'aaNull   ', aaMaps.aaString ],
    ['aaInteger', 'aaNull   ', aaMaps.aaInteger],
    ['aaInteger', 'aaFloat  ', aaMaps.aaFloat  ],
    ['aaInteger', 'aaString ', IllegalArgumentException.class],
    ['aaInteger', 'aaMapE   ', IllegalArgumentException.class],
    ['jj       ', 'kk       ', [jj:'J1',kk:'K1'] ],
    ['kk       ', 'jj       ', [jj:'J1',kk:'K1'] ],
    ['jjkk     ', 'kkll     ', [jj:'J2',kk:'K2',ll:'L2'] ],
    ['kkll     ', 'jjkk     ', [jj:'J2',kk:'K2',ll:'L1'] ],
]

lrc.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =aaMaps[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=aaMaps[nmRight] 
    Object expected  = spec[2]

    println ("Testing: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

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
