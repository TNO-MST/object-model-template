package nl.tno.omt.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nl.tno.omt.ArrayDataTypesType;
import nl.tno.omt.EnumeratedDataTypesType;
import nl.tno.omt.FixedRecordDataTypesType;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.SimpleDataTypesType;
import nl.tno.omt.VariantRecordDataTypesType;

/**
 * Class with static helper methods to support the mapping of OMT names to and from Java names.
 *
 * @author bergtwvd
 */
public class OmtJavaMapping {

  // map of OMT presentation name to Java type
  private static final Map<String, JavaPrimitiveType> representationMap = new HashMap<>();

  // specific OMT datatype mappings to Java types
  private static final Map<String, JavaDataType> datatypeMap = new HashMap<>();

  // enumeration of Java primitive types
  public enum JavaPrimitiveType {
    BYTE("byte", "Byte"),
    SHORT("short", "Short"),
    INTEGER("int", "Integer"),
    LONG("long", "Long"),
    FLOAT("float", "Float"),
    DOUBLE("double", "Double"),
    CHARACTER("char", "Character");

    private final String unboxedType;
    private final String boxedType;

    JavaPrimitiveType(String unboxedType, String boxedType) {
      this.unboxedType = unboxedType;
      this.boxedType = boxedType;
    }

    public String getUnboxedType() {
      return this.unboxedType;
    }

    public String getBoxdedType() {
      return this.boxedType;
    }
  }

  // definition of a Java type name and necessary import statements
  public static class JavaDataType {

    // Java type name
    String datatypeName;

    // Java imports needed for this type
    Set<String> imports;

    public JavaDataType(String datatypeName, Set<String> imports) {
      this.datatypeName = datatypeName;
      this.imports = imports;
    }

    public String getDatatypeName() {
      return datatypeName;
    }

    public Set<String> getImports() {
      return imports;
    }
  }

  static {
    // add the standard OMT representations to the map
    representationMap.put(OmtMimConstants.HLAOCTET, JavaPrimitiveType.BYTE);
    representationMap.put(OmtMimConstants.HLAINTEGER16BE, JavaPrimitiveType.SHORT);
    representationMap.put(OmtMimConstants.HLAINTEGER32BE, JavaPrimitiveType.INTEGER);
    representationMap.put(OmtMimConstants.HLAINTEGER64BE, JavaPrimitiveType.LONG);
    representationMap.put(OmtMimConstants.HLAUNSIGNEDINTEGER16BE, JavaPrimitiveType.SHORT);
    representationMap.put(OmtMimConstants.HLAUNSIGNEDINTEGER32BE, JavaPrimitiveType.INTEGER);
    representationMap.put(OmtMimConstants.HLAUNSIGNEDINTEGER64BE, JavaPrimitiveType.LONG);
    representationMap.put(OmtMimConstants.HLAFLOAT32BE, JavaPrimitiveType.FLOAT);
    representationMap.put(OmtMimConstants.HLAFLOAT64BE, JavaPrimitiveType.DOUBLE);
    representationMap.put(OmtMimConstants.HLAOCTETPAIRBE, JavaPrimitiveType.CHARACTER);
  }

  /**
   * Add additional representations to map via this method
   *
   * @param representations Map with representations
   */
  public static void addRepresentations(Map<String, JavaPrimitiveType> representations) {
    representationMap.putAll(representations);
  }

  /**
   * Add additional OMT data types to map via this method
   *
   * @param omtDatatypeName OMT datatype name
   * @param javaTypeName Java type name
   * @param imports required Java import statements for datatype
   */
  public static void addDatatype(String omtDatatypeName, String javaTypeName, Set<String> imports) {
    datatypeMap.put(omtDatatypeName, new JavaDataType(javaTypeName, imports));
  }

  /**
   * Returns Java type name for the provided OMT datatype name or null if none was mapped.
   *
   * @param omtDatatypeName OMT datatype name
   * @return Java type name or null
   */
  public static JavaDataType getJavaDatatypeName(String omtDatatypeName) {
    return datatypeMap.get(omtDatatypeName);
  }

  /**
   * Converts an OMT name to a Java name.
   *
   * <p>Escape "-" in the OMT name as follows: convert n x "_" to 2n x "_" and convert n x "-" to
   * 2n+1 x "_".
   *
   * @param omtName OMT name
   * @return escaped Java name
   */
  public static String toJavaName(String omtName) {
    if (omtName.equals("Class")) return "Class_";

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < omtName.length(); ) {
      switch (omtName.charAt(i)) {
        case '-' -> {
          sb.append("_");
          for (; i < omtName.length(); i++) {
            if (omtName.charAt(i) == '-') {
              sb.append("__");
            } else break;
          }
        }
        case '_' -> {
          for (; i < omtName.length(); i++) {
            if (omtName.charAt(i) == '_') {
              sb.append("__");
            } else break;
          }
        }
        default -> sb.append(omtName.charAt(i++));
      }
    }
    return sb.toString();
  }

  /**
   * Converts a Java name to an OMT name.
   *
   * <p>Unescape "_" in the Java name as follows: convert 2n x "_" to n x "_" and convert 2n+1 x "_"
   * to n x "-"
   *
   * @param javaName Java name
   * @return unescaped OMT name
   */
  public static String toOmtName(String javaName) {
    if (javaName.equals("Class_")) return "Class";

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < javaName.length(); ) {
      switch (javaName.charAt(i)) {
        case '_' -> {
          int count = 0;
          for (; i < javaName.length(); i++) {
            if (javaName.charAt(i) == '_') {
              count++;
            } else break;
          }

          char c = (count % 2 == 0) ? '_' : '-';
          for (int j = 0; j < count / 2; j++) {
            sb.append(c);
          }
        }
        default -> sb.append(javaName.charAt(i++));
      }
    }
    return sb.toString();
  }

  /**
   * Returns the getMethod name for a given class field name.
   *
   * @param javaFieldName
   * @return name of get method
   */
  public static String toJavaGetterName(String javaFieldName) {
    return "get" + javaFieldName.substring(0, 1).toUpperCase() + javaFieldName.substring(1);
  }

  /**
   * Returns the setMethod name for a given class field name.
   *
   * @param javaFieldName
   * @return name of set method
   */
  public static String toJavaSetterName(String javaFieldName) {
    return "set" + javaFieldName.substring(0, 1).toUpperCase() + javaFieldName.substring(1);
  }

  /**
   * Determine Java type name for the provided OMT datatype name.
   *
   * @param modules The FOM modules to search in.
   * @param omtDatatypeName The OMT datatype name.
   * @param dimension OMT Array dimension (must be >= 0)
   * @param useList Use Java List datatype for array (i.e. dimension > 0)
   * @param useBoxed Return boxed or unboxed name
   * @return Java type name
   * @throws java.lang.Exception when no mapping can be provided
   */
  public static String getJavaDatatypeName(
      ObjectModelType[] modules,
      String omtDatatypeName,
      int dimension,
      boolean useList,
      boolean useBoxed)
      throws Exception {

    if (dimension == 0) {
      // the datatype does not concern an array; return a boxed type if specified
      return getJavaDatatypeName(modules, omtDatatypeName, useBoxed);
    }

    // the datatype concerns an array
    if (useList) {
      // for List we must use a Boxed type for the array component
      String javaDatatypeName = "";
      for (int i = 0; i < dimension; i++) {
        javaDatatypeName += "List<";
      }
      javaDatatypeName += getJavaDatatypeName(modules, omtDatatypeName, true);
      for (int i = 0; i < dimension; i++) {
        javaDatatypeName += ">";
      }
      return javaDatatypeName;
    } else {
      // for [] we use a primitive type for the base array component
      String javaDatatypeName = getJavaDatatypeName(modules, omtDatatypeName, false);
      for (int i = 0; i < dimension; i++) {
        javaDatatypeName += "[]";
      }
      return javaDatatypeName;
    }
  }

  /**
   * Returns enum type name for provided OMT data type, or null if not possible.
   *
   * @param modules FOM modules
   * @param omtDatatypeName OMT datatype name
   * @param useBoxed Return boxed or unboxed Java type name
   * @return Java type name
   * @throws java.lang.Exception when there is no mapping
   */
  public static String getJavaDatatypeNameForEnumerationType(
      ObjectModelType[] modules, String omtDatatypeName, boolean useBoxed) throws Exception {

    EnumeratedDataTypesType.EnumeratedData enumeratedData =
        OmtFunctions.getEnumeratedDataByName(modules, omtDatatypeName);
    if (enumeratedData != null) {
      // OMT HLABOOLEAN is mapped to Java boolean
      if (omtDatatypeName.equals(OmtMimConstants.HLABOOLEAN)) {
        return (useBoxed) ? "Boolean" : "boolean";
      } else {
        return omtDatatypeName;
      }
    }
    throw new Exception("Unknown enumeration type " + omtDatatypeName);
  }

  /**
   * Returns the Java type name for the provided OMT datatype name.
   *
   * @param modules FOM modules
   * @param omtDatatypeName OMT datatype name
   * @param useBoxed Return boxed or unboxed Java type name
   * @return Java type name
   * @throws java.lang.Exception when there is no mapping
   */
  public static String getJavaDatatypeName(
      ObjectModelType[] modules, String omtDatatypeName, boolean useBoxed) throws Exception {

    // check for specific mapping first
    JavaDataType javaDatatype = datatypeMap.get(omtDatatypeName);
    if (javaDatatype != null) {
      return javaDatatype.getDatatypeName();
    }

    SimpleDataTypesType.SimpleData simpleData =
        OmtFunctions.getSimpleDataByName(modules, omtDatatypeName);
    if (simpleData != null) {
      return getJavaDatatypeNameForRepresentation(
          simpleData.getRepresentation().getValue(), useBoxed);
    }

    FixedRecordDataTypesType.FixedRecordData fixedRecordData =
        OmtFunctions.getFixedRecordDataByName(modules, omtDatatypeName);
    if (fixedRecordData != null) {
      return omtDatatypeName;
    }

    VariantRecordDataTypesType.VariantRecordData variantRecordData =
        OmtFunctions.getVariantRecordDataByName(modules, omtDatatypeName);
    if (variantRecordData != null) {
      return omtDatatypeName;
    }

    EnumeratedDataTypesType.EnumeratedData enumeratedData =
        OmtFunctions.getEnumeratedDataByName(modules, omtDatatypeName);
    if (enumeratedData != null) {
      // OMT HLABOOLEAN is mapped to Java boolean
      if (omtDatatypeName.equals(OmtMimConstants.HLABOOLEAN)) {
        return (useBoxed) ? "Boolean" : "boolean";
      } else {
        return omtDatatypeName;
      }
    }

    ArrayDataTypesType.ArrayData arrayData =
        OmtFunctions.getArrayDataByName(modules, omtDatatypeName);
    if (arrayData != null) {
      // can only be char type; an array of char - regardless of the cardinality - is mapped to Java
      // String
      if (arrayData.getDataType().getValue().equals(OmtMimConstants.HLAUNICODECHAR)
          || arrayData.getDataType().getValue().equals(OmtMimConstants.HLAASCIICHAR)) {
        return "String";
      } else throw new Exception("Expected character type, but got " + omtDatatypeName);
    }

    throw new Exception("Unknown datatype " + omtDatatypeName);
  }

  /**
   * Returns Java type name for the provided OMT representation name.
   *
   * @param omtRepresentatioName The OMT representation name.
   * @param useBoxed Return boxed or unboxed Java type name
   * @return Java type name
   * @throws java.lang.Exception when there is no mapping
   */
  public static String getJavaDatatypeNameForRepresentation(
      String omtRepresentatioName, boolean useBoxed) throws Exception {
    JavaPrimitiveType javaType = OmtJavaMapping.representationMap.get(omtRepresentatioName);
    if (javaType != null) {
      return useBoxed ? javaType.getBoxdedType() : javaType.getUnboxedType();
    }

    throw new Exception("Unknown representation " + omtRepresentatioName);
  }
}
