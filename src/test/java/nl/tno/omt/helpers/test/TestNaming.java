package nl.tno.omt.helpers.test;

import nl.tno.omt.helpers.OmtJavaMapping;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author bergtwvd
 */
public class TestNaming {

  @BeforeAll
  public static void setUpClass() {}

  @AfterAll
  public static void tearDownClass() {}

  @BeforeEach
  public void setUp() {}

  @AfterEach
  public void tearDown() {}

  @Test
  public void test1() {
    String omtName = "aa-bb--c_d__e";
    String javaName = OmtJavaMapping.toJavaName(omtName);
    Assertions.assertEquals("aa___bb_____c__d____e", javaName);
  }

  @Test
  public void test2() {
    String javaName = "aa___bb_____c__d____e";
    String omtName = OmtJavaMapping.toOmtName(javaName);
    Assertions.assertEquals("aa-bb--c_d__e", omtName);
  }

  @Test
  public void test3() {
    String omtName = "Class";
    String javaName = OmtJavaMapping.toJavaName(omtName);
    Assertions.assertEquals("Class_", javaName);
  }

  @Test
  public void test4() {
    String javaName = "Class_";
    String omtName = OmtJavaMapping.toOmtName(javaName);
    Assertions.assertEquals("Class", omtName);
  }

  @Test
  public void test5() {
    String omtName = "Class_";
    String javaName = OmtJavaMapping.toJavaName(omtName);
    Assertions.assertEquals("Class__", javaName);
  }

  @Test
  public void test6() {
    String javaName = "Class__";
    String omtName = OmtJavaMapping.toOmtName(javaName);
    Assertions.assertEquals("Class_", omtName);
  }
}
