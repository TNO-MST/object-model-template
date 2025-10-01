package nl.tno.omt.helpers.test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import nl.tno.omt.EnumeratedDataTypesType.EnumeratedData;
import nl.tno.omt.FixedRecordDataTypesType.FixedRecordData;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.VariantRecordDataTypesType.VariantRecordData;
import nl.tno.omt.helpers.OmtFunctions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author bergtwvd
 */
public class TestOmt {

	static ObjectModelType rpr;
	static ObjectModelType mim;

	@BeforeAll
	public static void setUpClass() throws IOException {
		rpr = OmtFunctions.readOmt(TestOmt.class.getResource("/foms/RPR_FOM_v2.0_1516-2010.xml"));
		mim = OmtFunctions.readOmt(TestOmt.class.getResource("/foms/HLAstandardMIM.xml"));
	}

	@AfterAll
	public static void tearDownClass() {
	}

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	public void test() throws IOException {
		EnumeratedData data1 = OmtFunctions.getEnumeratedDataByName(new ObjectModelType[]{mim, rpr}, "HLAboolean");
		Assertions.assertNotNull(data1);
		Assertions.assertEquals("HLAboolean", data1.getName().getValue());

		EnumeratedData data2 = OmtFunctions.getEnumeratedDataByName(new ObjectModelType[]{mim, rpr}, "RPRboolean");
		Assertions.assertNotNull(data2);
		Assertions.assertEquals("RPRboolean", data2.getName().getValue());

		FixedRecordData data3 = OmtFunctions.getFixedRecordDataByName(new ObjectModelType[]{mim, rpr}, "SpatialStaticStruct");
		Assertions.assertNotNull(data3);
		Assertions.assertEquals("SpatialStaticStruct", data3.getName().getValue());

		VariantRecordData data4 = OmtFunctions.getVariantRecordDataByName(new ObjectModelType[]{mim, rpr}, "SpatialVariantStruct");
		Assertions.assertNotNull(data4);
		Assertions.assertEquals("SpatialVariantStruct", data4.getName().getValue());
		
		// test if we can write an OMT and read it back in
		Writer sw = OmtFunctions.writeOmt(rpr, new StringWriter());
		ObjectModelType rpr2 = OmtFunctions.readOmt(new StringReader(sw.toString()));
		VariantRecordData data5 = OmtFunctions.getVariantRecordDataByName(rpr2, "SpatialVariantStruct");
		Assertions.assertNotNull(data5);
		Assertions.assertEquals("SpatialVariantStruct", data5.getName().getValue());
	}
}
