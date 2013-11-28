//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.23 at 04:39:20 PM WEST 
//


package eu.scape_project.tool.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperatingSystemName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OperatingSystemName">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Debian"/>
 *     &lt;enumeration value="Ubuntu"/>
 *     &lt;enumeration value="Redhat"/>
 *     &lt;enumeration value="CentOS"/>
 *     &lt;enumeration value="Windows"/>
 *     &lt;enumeration value="MacOS"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OperatingSystemName")
@XmlEnum
public enum OperatingSystemName {

    @XmlEnumValue("Debian")
    DEBIAN("Debian"),
    @XmlEnumValue("Ubuntu")
    UBUNTU("Ubuntu"),
    @XmlEnumValue("Redhat")
    REDHAT("Redhat"),
    @XmlEnumValue("CentOS")
    CENT_OS("CentOS"),
    @XmlEnumValue("Windows")
    WINDOWS("Windows"),
    @XmlEnumValue("MacOS")
    MAC_OS("MacOS"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    OperatingSystemName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperatingSystemName fromValue(String v) {
        for (OperatingSystemName c: OperatingSystemName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
