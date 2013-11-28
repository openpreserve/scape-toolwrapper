//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.23 at 04:39:20 PM WEST 
//


package eu.scape_project.tool.data;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * A command line tool.
 * 
 * <p>Java class for Tool complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tool">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="installation" type="{http://scape-project.eu/tool}Installation" minOccurs="0"/>
 *         &lt;element name="operations" type="{http://scape-project.eu/tool}Operations"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://scape-project.eu/tool}nonEmptyString" />
 *       &lt;attribute name="version" use="required" type="{http://scape-project.eu/tool}nonEmptyString" />
 *       &lt;attribute name="homepage" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="schemaVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "tool")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tool", propOrder = {
    "installation",
    "operations"
})
public class Tool {

    protected Installation installation;
    @XmlElement(required = true)
    protected Operations operations;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String version;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String homepage;
    @XmlAttribute(required = true)
    protected BigDecimal schemaVersion;

    /**
     * Gets the value of the installation property.
     * 
     * @return
     *     possible object is
     *     {@link Installation }
     *     
     */
    public Installation getInstallation() {
        return installation;
    }

    /**
     * Sets the value of the installation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Installation }
     *     
     */
    public void setInstallation(Installation value) {
        this.installation = value;
    }

    /**
     * Gets the value of the operations property.
     * 
     * @return
     *     possible object is
     *     {@link Operations }
     *     
     */
    public Operations getOperations() {
        return operations;
    }

    /**
     * Sets the value of the operations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operations }
     *     
     */
    public void setOperations(Operations value) {
        this.operations = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the homepage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Sets the value of the homepage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomepage(String value) {
        this.homepage = value;
    }

    /**
     * Gets the value of the schemaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Sets the value of the schemaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSchemaVersion(BigDecimal value) {
        this.schemaVersion = value;
    }

}
