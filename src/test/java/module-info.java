module nl.tno.omt.test {
	requires nl.tno.omt;

	// requires for junit tests
	requires org.junit.jupiter.api;
	
	// provide JUnit access to annotations or alternatively declare this module as open
	opens nl.tno.omt.helpers.test to org.junit.platform.commons;
}
