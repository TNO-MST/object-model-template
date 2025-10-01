# OMT - ObjectModelType
This Object Model Type (OMT) project provides basic functionality to support other projects to work with HLA (IEEE 1516) OMT data.
- HLA OMT constant definitions.
- functionality to read/write an OMT file and to query OMT data
- functionality to, amongst others, map between Java type names and HLA OMT names.

The present version of the project supports IEEE 1516-2010 format files.

## Building the OMT library

To build the OMT library:

````
mvn clean install
```` 

The HLA OMT schema used in this project is located under the resources directory and is expanded to Java code using JAXB. The generated Java source code is located and compiled under Java package `nl.tno.omt`.

## HLA 1516-2010 OMT schema
The original HLA OMT schema can be found on the SISO site at: https://www.sisostandards.org/page/DataFiles.

Two adaptations have been done to the HLA OMT schema provided from the SISO site in order to successfully generate Java code from the HLA OMT schema:

- Changed `notes` to `notes2` to work around a double naming conflict:

````
<xs:element name="notes2" type="notesType" minOccurs="0">
	<xs:annotation>
		<xs:documentation>specifies all referenced notes</xs:documentation>
	</xs:annotation>
</xs:element>
````

- Set the xml namespace to `"http://standards.ieee.org/IEEE1516-2010"`, because most (if not all) FOM XML files use this namespace.

````
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns="http://standards.ieee.org/IEEE1516-2010"
	xmlns:ns="http://standards.ieee.org/IEEE1516-2010"
	targetNamespace="http://standards.ieee.org/IEEE1516-2010"
	elementFormDefault="qualified" attributeFormDefault="unqualified" version="2010">
````
