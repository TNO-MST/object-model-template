package nl.tno.omt.helpers;

/**
 * OMT 1516-2010 and OMT 1516-2025 constants.
 *
 * @author bergtwvd
 */
public class OmtMimConstants {

  // MIM transportation names
  public static final String HLABESTEFFORT = "HLAbestEffort";
  public static final String HLARELIABLE = "HLAreliable";

  // MIM root classes
  public static final String HLAOBJECTROOT = "HLAobjectRoot";
  public static final String HLAINTERACTIONROOT = "HLAinteractionRoot";

  // MIM encoding names
  public static final String HLAVARIABLEARRAY = "HLAvariableArray";
  public static final String HLAFIXEDARRAY = "HLAfixedArray";
  public static final String HLAFIXEDRECORD = "HLAfixedRecord";
  public static final String HLAVARIANTRECORD = "HLAvariantRecord";
  public static final String HLAEXTENDABLEVARIANTRECORD = "HLAextendableVariantRecord";

  // MIM cardinality names
  public static final String DYNAMIC = "Dynamic";

  // MIM representation names
  public static final String HLAOCTET = "HLAoctet";
  public static final String HLAOCTETPAIRBE = "HLAoctetPairBE";
  public static final String HLAINTEGER16BE = "HLAinteger16BE";
  public static final String HLAINTEGER32BE = "HLAinteger32BE";
  public static final String HLAINTEGER64BE = "HLAinteger64BE";
  public static final String HLAUNSIGNEDINTEGER16BE = "HLAunsignedInteger16BE";
  public static final String HLAUNSIGNEDINTEGER32BE = "HLAunsignedInteger32BE";
  public static final String HLAUNSIGNEDINTEGER64BE = "HLAunsignedInteger64BE";
  public static final String HLAFLOAT32BE = "HLAfloat32BE";
  public static final String HLAFLOAT64BE = "HLAfloat64BE";

  // MIM simple datatype names
  public static final String HLAASCIICHAR = "HLAASCIIchar";
  public static final String HLABYTE = "HLAbyte";
  public static final String HLAUNICODECHAR = "HLAunicodeChar";
  public static final String HLACOUNT = "HLAcount";
  public static final String HLASECONDS = "HLAseconds";
  public static final String HLAMSEC = "HLAmsec";
  public static final String HLANORMALIZEDFEDERATEHANDLE = "HLAnormalizedFederateHandle";
  public static final String HLAINDEX = "HLAindex";
  public static final String HLAINTEGER64TIME = "HLAinteger64Time";
  public static final String HLAFLOAT64TIME = "HLAfloat64Time";

  // MTM array datatype names
  public static final String HLAASCIISTRING = "HLAASCIIstring";
  public static final String HLAUNICODESTRING = "HLAunicodeString";
  public static final String HLAOPAQUEDATA = "HLAopaqueData";
  public static final String HLATOKEN = "HLAtoken";
  public static final String HLAHANDLE = "HLAhandle";
  public static final String HLATRANSPORTATIONNAME = "HLAtransportationName";
  public static final String HLAUPDATERATENAME = "HLAupdateRateName";
  public static final String HLALOGICALTIME = "HLAlogicalTime";
  public static final String HLATIMEINTERVAL = "HLAtimeInterval";
  public static final String HLAHANDLELIST = "HLAhandleList";
  public static final String HLAINTERACTIONSUBLIST = "HLAinteractionSubList";
  public static final String HLAARGUMENTLIST = "HLAargumentList";
  public static final String HLAOBJECTCLASSBASEDCOUNTS = "HLAobjectClassBasedCounts";
  public static final String HLAINTERACTIONCOUNTS = "HLAinteractionCounts";
  public static final String HLASYNCHPOINTLIST = "HLAsynchPointList";
  public static final String HLASYNCHPOINTFEDERATELIST = "HLAsynchPointFederateList";
  public static final String HLAMODULEDESIGNATORLIST = "HLAmoduleDesignatorList";

  // MIM enum datatype names
  public static final String HLABOOLEAN = "HLAboolean";
  public static final String HLAFEDERATESTATE = "HLAfederateState";
  public static final String HLATIMESTATE = "HLAtimeState";
  public static final String HLAOWNERSHIP = "HLAownership";
  public static final String HLARESIGNACTION = "HLAresignAction";
  public static final String HLAORDERTYPE = "HLAorderType";
  public static final String HLASWITCH = "HLAswitch";
  public static final String HLASYNCHPOINTSTATUS = "HLAsynchPointStatus";
  public static final String HLANORMALIZEDSERVICEGROUP = "HLAnormalizedServiceGroup";

  // MIM enum values
  public static final String HLATRUE = "HLAtrue";
  public static final String HLAFALSE = "HLAfalse";
}
