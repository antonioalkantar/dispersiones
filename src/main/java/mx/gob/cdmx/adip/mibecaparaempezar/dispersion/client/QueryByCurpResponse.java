
package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para queryByCurpResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="queryByCurpResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="response" type="{http://mci.ws.dds.aefcm.gob.mx/}MciResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryByCurpResponseOriginal", propOrder = {
    "response"
})
public class QueryByCurpResponse {

    protected MciResponse response;

    /**
     * Obtiene el valor de la propiedad response.
     * 
     * @return
     *     possible object is
     *     {@link MciResponse }
     *     
     */
    public MciResponse getResponse() {
        return response;
    }

    /**
     * Define el valor de la propiedad response.
     * 
     * @param value
     *     allowed object is
     *     {@link MciResponse }
     *     
     */
    public void setResponse(MciResponse value) {
        this.response = value;
    }

}
