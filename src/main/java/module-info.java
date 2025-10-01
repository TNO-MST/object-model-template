module nl.tno.omt {
	requires jakarta.xml.bind;

	// allow reflection access to jakarta.xml.bind
	opens nl.tno.omt to jakarta.xml.bind;

	exports nl.tno.omt;
	exports nl.tno.omt.helpers;
}
