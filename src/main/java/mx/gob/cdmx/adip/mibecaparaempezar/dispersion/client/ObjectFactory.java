
package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mx.gob.cdmx.adip.mibecaparaempezar.soap.client.aefcm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryByCurp_QNAME = new QName("http://mci.ws.dds.aefcm.gob.mx/", "queryByCurp");
    private final static QName _QueryByCurpResponse_QNAME = new QName("http://mci.ws.dds.aefcm.gob.mx/", "queryByCurpResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mx.gob.cdmx.adip.mibecaparaempezar.soap.client.aefcm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryByCurpResponse }
     * 
     */
    public QueryByCurpResponse createQueryByCurpResponse() {
        return new QueryByCurpResponse();
    }

    /**
     * Create an instance of {@link QueryByCurp }
     * 
     */
    public QueryByCurp createQueryByCurp() {
        return new QueryByCurp();
    }

    /**
     * Create an instance of {@link MciResponse }
     * 
     */
    public MciResponse createMciResponse() {
        return new MciResponse();
    }

    /**
     * Create an instance of {@link MciRequest }
     * 
     */
    public MciRequest createMciRequest() {
        return new MciRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryByCurp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mci.ws.dds.aefcm.gob.mx/", name = "queryByCurp")
    public JAXBElement<QueryByCurp> createQueryByCurp(QueryByCurp value) {
        return new JAXBElement<QueryByCurp>(_QueryByCurp_QNAME, QueryByCurp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryByCurpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mci.ws.dds.aefcm.gob.mx/", name = "queryByCurpResponse")
    public JAXBElement<QueryByCurpResponse> createQueryByCurpResponse(QueryByCurpResponse value) {
        return new JAXBElement<QueryByCurpResponse>(_QueryByCurpResponse_QNAME, QueryByCurpResponse.class, null, value);
    }

}
