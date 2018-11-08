package org.gobiiproject.gobiiprocess.digester.validation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GermplasmValidationTest.class,
        GermplasmPropValidationTest.class,
        DnarunPropValidationTest.class,
        MarkerPropValidationTest.class,
        LinkageGroupValidationTest.class})

public class ValidationTestSuite {
}
