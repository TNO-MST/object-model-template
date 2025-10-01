package nl.tno.omt.helpers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tno.omt.ArrayDataTypesType.ArrayData;
import nl.tno.omt.Attribute;
import nl.tno.omt.BasicDataRepresentationsType;
import nl.tno.omt.BasicDataRepresentationsType.BasicData;
import nl.tno.omt.DataTypesType.ArrayDataTypes;
import nl.tno.omt.EnumeratedDataTypesType;
import nl.tno.omt.EnumeratedDataTypesType.EnumeratedData;
import nl.tno.omt.FixedRecordDataTypesType;
import nl.tno.omt.FixedRecordDataTypesType.FixedRecordData;
import nl.tno.omt.InteractionClass;
import nl.tno.omt.ObjectClass;
import nl.tno.omt.ObjectFactory;
import nl.tno.omt.ObjectModelType;
import nl.tno.omt.Parameter;
import nl.tno.omt.SimpleDataTypesType;
import nl.tno.omt.SimpleDataTypesType.SimpleData;
import nl.tno.omt.TransportationsType.Transportation;
import nl.tno.omt.VariantRecordDataTypesType;
import nl.tno.omt.VariantRecordDataTypesType.VariantRecordData;

/**
 *
 * @author bergtwvd
 */
public class OmtFunctions {

	// static context for creating JAXB ObjectModelType marshallers
	private static JAXBContext jaxbContext = null;

	// static factory for creating objects
	private static final ObjectFactory objectFactory = new ObjectFactory();

	private static synchronized JAXBContext getContext() throws IOException {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(ObjectModelType.class);
			} catch (JAXBException ex) {
				throw new IOException(ex);
			}
		}

		return jaxbContext;
	}

	private static Unmarshaller createUnmarshaller() throws JAXBException, IOException {
		return getContext().createUnmarshaller();
	}

	private static Marshaller createMarshaller(boolean formatted) throws JAXBException, IOException {
		Marshaller jaxbMarshaller = getContext().createMarshaller();
		if (formatted) {
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		}
		return jaxbMarshaller;
	}

	private static ObjectModelType castObject(Object object) {
		@SuppressWarnings("unchecked")
		JAXBElement<ObjectModelType> element = (JAXBElement<ObjectModelType>) object;
		return element.getValue();
	}

	public static ObjectModelType readOmt(URL module) throws IOException {
		return readOmt(module.openStream());
	}

	public static ObjectModelType readOmt(InputStream input) throws IOException {
		try (input) {
			return castObject(createUnmarshaller().unmarshal(input));
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}
	}

	public static ObjectModelType readOmt(Reader input) throws IOException {
		try (input) {
			return castObject(createUnmarshaller().unmarshal(input));
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}
	}

	public static Writer writeOmt(ObjectModelType omt, Writer output, boolean formatted) throws IOException {
		try {
			createMarshaller(formatted).marshal(objectFactory.createObjectModel(omt), output);
			return output;
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}
	}

	public static Writer writeOmt(ObjectModelType omt, Writer output) throws IOException {
		return writeOmt(omt, output, false);
	}

	public static File writeOmt(ObjectModelType omt, File output, boolean formatted) throws IOException {
		try {
			createMarshaller(formatted).marshal(objectFactory.createObjectModel(omt), output);
			return output;
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}
	}

	public static File writeOmt(ObjectModelType omt, File output) throws IOException {
		return writeOmt(omt, output, false);
	}

	public static OutputStream writeOmt(ObjectModelType omt, OutputStream output, boolean formatted) throws IOException {
		try {
			createMarshaller(formatted).marshal(objectFactory.createObjectModel(omt), output);
			return output;
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}
	}

	public static OutputStream writeOmt(ObjectModelType omt, OutputStream output) throws IOException {
		return writeOmt(omt, output, false);
	}

	public static boolean isScaffoldingClass(ObjectClass oc) {
		return ((oc.getAttribute() == null || oc.getAttribute().isEmpty()) && oc.getSemantics() == null && oc.getSharing() == null);
	}

	public static boolean isScaffoldingClass(InteractionClass ic) {
		return ((ic.getParameter() == null || ic.getParameter().isEmpty()) && ic.getSemantics() == null && ic.getSharing() == null);
	}

	public static ObjectModelType getObjectClassModule(ObjectModelType[] modules, String fqName) {
		return getObjectClassModule(modules, fqName, true);
	}

	public static ObjectModelType getObjectClassModule(ObjectModelType[] modules, String fqName, boolean nullOnScaffolding) {
		for (ObjectModelType module : modules) {
			if (getObjectClass(module, fqName, nullOnScaffolding) != null) {
				return module;
			}
		}

		return null;
	}

	public static ObjectClass getObjectClass(ObjectModelType[] modules, String fqName) {
		return getObjectClass(modules, fqName, true);
	}

	public static ObjectClass getObjectClass(ObjectModelType[] modules, String fqName, boolean nullOnScaffolding) {
		for (ObjectModelType module : modules) {
			ObjectClass oc = getObjectClass(module, fqName, nullOnScaffolding);
			if (oc != null) {
				return oc;
			}
		}

		return null;
	}

	private static ObjectClass selectObjectClass(List<ObjectClass> list, String name) {
		for (ObjectClass oc : list) {
			if (oc.getName().getValue().equals(name)) {
				return oc;
			}
		}

		return null;
	}

	public static ObjectClass getObjectClass(ObjectModelType module, String fqName) {
		return getObjectClass(module, fqName, true);
	}

	/**
	 * Looks up the ObjectClass for the given FQ OMT name. The returned value is
	 * an ObjectClass or null if none found. The returned ObjectClass may be a
	 * non-null scaffolding class, depending in the setting of
	 * nullOnScaffolding. If nullOnScaffolding is true then a null value is
	 * returned if the found ObjectClass is a scaffolding class, otherwise the
	 * found ObjectClass is returned.
	 *
	 * @param module: module to search
	 * @param fqName: FQ OMT name
	 * @param nullOnScaffolding: setting for scaffolding classes
	 * @return the ObjectClass or null
	 */
	public static ObjectClass getObjectClass(ObjectModelType module, String fqName, boolean nullOnScaffolding) {
		if (module.getObjects() == null) {
			return null;
		}

		String[] v = fqName.split("\\.");
		if (v.length == 0) {
			return null;
		}

		ObjectClass oc = module.getObjects().getObjectClass();
		if (!oc.getName().getValue().equals(v[0])) {
			return null;
		}

		for (int i = 1; i < v.length; i++) {
			oc = selectObjectClass(oc.getObjectClass(), v[i]);
			if (oc == null) {
				return null;
			}
		}

		return nullOnScaffolding ? (isScaffoldingClass(oc) ? null : oc) : oc;
	}

	public static Set<Attribute> getObjectClassAttributes(ObjectModelType[] modules, String fqName) {
		Set<Attribute> attributes = new HashSet<>();
		String name = null;

		for (String part : fqName.split("\\.")) {
			name = (name == null) ? part : name + "." + part;

			ObjectClass oc = OmtFunctions.getObjectClass(modules, name);
			if (oc != null) {
				attributes.addAll(oc.getAttribute());
			} else {
				return null;
			}
		}

		return attributes;
	}

	public static ObjectModelType getInteractionClassModule(ObjectModelType[] modules, String fqName) {
		return getInteractionClassModule(modules, fqName, true);
	}

	public static ObjectModelType getInteractionClassModule(ObjectModelType[] modules, String fqName, boolean nullOnScaffolding) {
		for (ObjectModelType module : modules) {
			if (getInteractionClass(module, fqName, nullOnScaffolding) != null) {
				return module;
			}
		}

		return null;
	}

	public static InteractionClass getInteractionClass(ObjectModelType[] modules, String fqName) {
		return getInteractionClass(modules, fqName, true);
	}

	public static InteractionClass getInteractionClass(ObjectModelType[] modules, String fqName, boolean nullOnScaffolding) {
		for (ObjectModelType module : modules) {
			InteractionClass ic = getInteractionClass(module, fqName, nullOnScaffolding);
			if (ic != null) {
				return ic;
			}
		}

		return null;
	}

	private static InteractionClass selectInteractionClass(List<InteractionClass> list, String name) {
		for (InteractionClass ic : list) {
			if (ic.getName().getValue().equals(name)) {
				return ic;
			}
		}

		return null;
	}

	public static InteractionClass getInteractionClass(ObjectModelType module, String fqName) {
		return getInteractionClass(module, fqName, true);
	}

	/**
	 * Looks up the InteractionClass for the given FQ OMT name. The returned
	 * value is an InteractionClass or null if none found. The returned
	 * InteractionClass may be a non-null scaffolding class, depending in the
	 * setting of nullOnScaffolding. If nullOnScaffolding is true then a null
	 * value is returned if the found InteractionClass is a scaffolding class,
	 * otherwise the found InteractionClass is returned.
	 *
	 * @param module: module to search
	 * @param fqName: FQ OMT name
	 * @param nullOnScaffolding: setting for scaffolding classes
	 * @return the ObjectClass or null
	 */
	public static InteractionClass getInteractionClass(ObjectModelType module, String fqName, boolean nullOnScaffolding) {
		if (module.getInteractions() == null) {
			return null;
		}

		String[] v = fqName.split("\\.");
		if (v.length == 0) {
			return null;
		}

		InteractionClass ic = module.getInteractions().getInteractionClass();
		if (!ic.getName().getValue().equals(v[0])) {
			return null;
		}

		for (int i = 1; i < v.length; i++) {
			ic = selectInteractionClass(ic.getInteractionClass(), v[i]);
			if (ic == null) {
				return null;
			}
		}

		return nullOnScaffolding ? (isScaffoldingClass(ic) ? null : ic) : ic;
	}

	public static Set<Parameter> getInteractionClassParameters(ObjectModelType[] modules, String fqName) {
		Set<Parameter> parameters = new HashSet<>();
		String name = null;

		for (String v : fqName.split("\\.")) {
			name = (name == null) ? v : name + "." + v;

			InteractionClass ic = OmtFunctions.getInteractionClass(modules, name);
			if (ic != null) {
				parameters.addAll(ic.getParameter());
			} else {
				return null;
			}
		}

		return parameters;
	}

	public static BasicData getBasicDataByName(ObjectModelType[] modules, String representationName) {
		for (ObjectModelType module : modules) {
			BasicData basicData = getBasicDataByName(module, representationName);
			if (basicData != null) {
				return basicData;
			}
		}

		return null;
	}

	public static BasicData getBasicDataByName(ObjectModelType module, String representationName) {
		if (module.getDataTypes() != null) {
			BasicDataRepresentationsType dt = module.getDataTypes().getBasicDataRepresentations();
			if (dt != null) {
				List<BasicData> list = dt.getBasicData();
				for (BasicData e : list) {
					if (e.getName().getValue().equals(representationName)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public static ObjectModelType getSimpleDataModule(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			if (getSimpleDataByName(module, datatypeName) != null) {
				return module;
			}
		}

		return null;
	}

	public static SimpleData getSimpleDataByName(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			SimpleData simpleData = getSimpleDataByName(module, datatypeName);
			if (simpleData != null) {
				return simpleData;
			}
		}

		return null;
	}

	public static SimpleData getSimpleDataByName(ObjectModelType module, String datatypeName) {
		if (module.getDataTypes() != null) {
			SimpleDataTypesType dt = module.getDataTypes().getSimpleDataTypes();
			if (dt != null) {
				List<SimpleData> list = dt.getSimpleData();
				for (SimpleData e : list) {
					if (e.getName().getValue().equals(datatypeName)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public static ObjectModelType getFixedRecordDataModule(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			if (getFixedRecordDataByName(module, datatypeName) != null) {
				return module;
			}
		}

		return null;
	}

	public static FixedRecordData getFixedRecordDataByName(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			FixedRecordData fixedRecordData = getFixedRecordDataByName(module, datatypeName);
			if (fixedRecordData != null) {
				return fixedRecordData;
			}
		}

		return null;
	}

	public static FixedRecordData getFixedRecordDataByName(ObjectModelType module, String datatypeName) {
		if (module.getDataTypes() != null) {
			FixedRecordDataTypesType dt = module.getDataTypes().getFixedRecordDataTypes();
			if (dt != null) {
				List<FixedRecordDataTypesType.FixedRecordData> list = dt.getFixedRecordData();
				for (FixedRecordDataTypesType.FixedRecordData e : list) {
					if (e.getName().getValue().equals(datatypeName)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public static ObjectModelType getVariantRecordDataModule(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			if (getVariantRecordDataByName(module, datatypeName) != null) {
				return module;
			}
		}

		return null;
	}

	public static VariantRecordData getVariantRecordDataByName(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			VariantRecordData variantRecordData = getVariantRecordDataByName(module, datatypeName);
			if (variantRecordData != null) {
				return variantRecordData;
			}
		}

		return null;
	}

	public static VariantRecordData getVariantRecordDataByName(ObjectModelType module, String datatypeName) {
		if (module.getDataTypes() != null) {
			VariantRecordDataTypesType dt = module.getDataTypes().getVariantRecordDataTypes();
			if (dt != null) {
				List<VariantRecordDataTypesType.VariantRecordData> list = dt.getVariantRecordData();
				for (VariantRecordDataTypesType.VariantRecordData e : list) {
					if (e.getName().getValue().equals(datatypeName)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public static ObjectModelType getEnumeratedDataModule(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			if (getEnumeratedDataByName(module, datatypeName) != null) {
				return module;
			}
		}

		return null;
	}

	public static EnumeratedData getEnumeratedDataByName(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			EnumeratedData enumeratedData = getEnumeratedDataByName(module, datatypeName);
			if (enumeratedData != null) {
				return enumeratedData;
			}
		}

		return null;
	}

	public static EnumeratedData getEnumeratedDataByName(ObjectModelType module, String datatypeName) {
		if (module.getDataTypes() != null) {
			EnumeratedDataTypesType dt = module.getDataTypes().getEnumeratedDataTypes();
			if (dt != null) {
				List<EnumeratedData> list = dt.getEnumeratedData();
				for (EnumeratedData e : list) {
					if (e.getName().getValue().equals(datatypeName)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public static ObjectModelType getArrayDataModule(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			if (getArrayDataByName(module, datatypeName) != null) {
				return module;
			}
		}

		return null;
	}

	public static ArrayData getArrayDataByName(ObjectModelType[] modules, String datatypeName) {
		for (ObjectModelType module : modules) {
			ArrayData arrayData = getArrayDataByName(module, datatypeName);
			if (arrayData != null) {
				return arrayData;
			}
		}

		return null;
	}

	public static ArrayData getArrayDataByName(ObjectModelType module, String datatypeName) {
		if (module.getDataTypes() != null) {
			ArrayDataTypes dt = module.getDataTypes().getArrayDataTypes();
			if (dt != null) {
				List<ArrayData> list = dt.getArrayData();
				for (ArrayData e : list) {
					if (e.getName().getValue().equals(datatypeName)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public static Transportation getTransportationByName(ObjectModelType module, String value) {
		if (module.getTransportations() != null) {
			List<Transportation> list = module.getTransportations().getTransportation();
			if (list != null) {
				for (Transportation e : list) {
					if (e.getName().getValue().equals(value)) {
						return e;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Parse the cardinality of an array type.
	 *
	 * Syntax is: '[' <min> '..' <max> ']' | <value> | 'Dynamic'
	 *
	 * @param dt Array datatype
	 * @return cardinality (>= 0) or null on error
	 */
	public static int[] geCardinality(ArrayData dt) {
		try {
			String cardinality = dt.getCardinality().getValue();

			if (cardinality.equals(OmtMimConstants.DYNAMIC)) {
				return new int[]{0, Integer.MAX_VALUE};
			}

			int first = cardinality.indexOf('[');
			int last = cardinality.lastIndexOf(']');

			if (first == -1 && last == -1) {
				// must be a single value
				int v = Integer.parseInt(cardinality);
				return (v < 0) ? null : new int[]{v, v};
			}

			if (first == -1 || last == -1) {
				return null;
			}

			// must be a min, max
			String part[] = cardinality.substring(first + 1, last).split("\\.\\.");
			if (part == null || part.length != 2 || part[0] == null || part[1] == null) {
				return null;
			}

			return new int[]{Integer.parseInt(part[0]), Integer.parseInt(part[1])};
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	/**
	 * Gets all array datatype with a given encoding
	 *
	 * @param modules: modules to be searched
	 * @param encodings: set of encodings to look for
	 * @return
	 */
	public static Set<String> getArrayDataTypesWithEncoding(ObjectModelType[] modules, Set<String> encodings) {
		Set<String> dataTypes = new HashSet<>();

		for (ObjectModelType module : modules) {
			if (module.getDataTypes() == null || module.getDataTypes().getArrayDataTypes() == null || module.getDataTypes().getArrayDataTypes().getArrayData() == null) {
				continue;
			}

			for (ArrayData arrayData : module.getDataTypes().getArrayDataTypes().getArrayData()) {
				if (encodings.contains(arrayData.getEncoding().getValue())) {
					dataTypes.add(arrayData.getName().getValue());
				}
			}
		}

		return dataTypes;
	}

}
