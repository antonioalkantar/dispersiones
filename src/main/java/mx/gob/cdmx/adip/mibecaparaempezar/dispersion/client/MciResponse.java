
package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para MciResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="MciResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cicloEscolarId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="cicloEscolar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="servicioEducativoId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="servicioEducativo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nivelEducativoId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="nivelEducativo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="turnoId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="turno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="gradoEscolar" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="tipoEscuela" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="estatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="curp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nombres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primerApellido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="segundoApellido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nombreCCT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="calle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroExterior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="colonia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="alcaldiaId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="alcaldia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codigoPostal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nivelEducativoFIBIEDCDMXId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="nivelEducativoFIBIEDCDMX" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MciResponse", propOrder = {
    "cicloEscolarId",
    "cicloEscolar",
    "servicioEducativoId",
    "servicioEducativo",
    "nivelEducativoId",
    "nivelEducativo",
    "cct",
    "turnoId",
    "turno",
    "gradoEscolar",
    "tipoEscuela",
    "estatus",
    "curp",
    "nombres",
    "primerApellido",
    "segundoApellido",
    "nombreCCT",
    "calle",
    "numeroExterior",
    "colonia",
    "alcaldiaId",
    "alcaldia",
    "codigoPostal",
    "nivelEducativoFIBIEDCDMXId",
    "nivelEducativoFIBIEDCDMX"
})
public class MciResponse {

    protected int cicloEscolarId;
    protected String cicloEscolar;
    protected int servicioEducativoId;
    protected String servicioEducativo;
    protected int nivelEducativoId;
    protected String nivelEducativo;
    protected String cct;
    protected int turnoId;
    protected String turno;
    protected int gradoEscolar;
    protected String tipoEscuela;
    protected String estatus;
    protected String curp;
    protected String nombres;
    protected String primerApellido;
    protected String segundoApellido;
    protected String nombreCCT;
    protected String calle;
    protected String numeroExterior;
    protected String colonia;
    protected String alcaldiaId;
    protected String alcaldia;
    protected String codigoPostal;
    protected int nivelEducativoFIBIEDCDMXId;
    protected String nivelEducativoFIBIEDCDMX;

    /**
     * Obtiene el valor de la propiedad cicloEscolarId.
     * 
     */
    public int getCicloEscolarId() {
        return cicloEscolarId;
    }

    /**
     * Define el valor de la propiedad cicloEscolarId.
     * 
     */
    public void setCicloEscolarId(int value) {
        this.cicloEscolarId = value;
    }

    /**
     * Obtiene el valor de la propiedad cicloEscolar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCicloEscolar() {
        return cicloEscolar;
    }

    /**
     * Define el valor de la propiedad cicloEscolar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCicloEscolar(String value) {
        this.cicloEscolar = value;
    }

    /**
     * Obtiene el valor de la propiedad servicioEducativoId.
     * 
     */
    public int getServicioEducativoId() {
        return servicioEducativoId;
    }

    /**
     * Define el valor de la propiedad servicioEducativoId.
     * 
     */
    public void setServicioEducativoId(int value) {
        this.servicioEducativoId = value;
    }

    /**
     * Obtiene el valor de la propiedad servicioEducativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicioEducativo() {
        return servicioEducativo;
    }

    /**
     * Define el valor de la propiedad servicioEducativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicioEducativo(String value) {
        this.servicioEducativo = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelEducativoId.
     * 
     */
    public int getNivelEducativoId() {
        return nivelEducativoId;
    }

    /**
     * Define el valor de la propiedad nivelEducativoId.
     * 
     */
    public void setNivelEducativoId(int value) {
        this.nivelEducativoId = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelEducativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivelEducativo() {
        return nivelEducativo;
    }

    /**
     * Define el valor de la propiedad nivelEducativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivelEducativo(String value) {
        this.nivelEducativo = value;
    }

    /**
     * Obtiene el valor de la propiedad cct.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCct() {
        return cct;
    }

    /**
     * Define el valor de la propiedad cct.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCct(String value) {
        this.cct = value;
    }

    /**
     * Obtiene el valor de la propiedad turnoId.
     * 
     */
    public int getTurnoId() {
        return turnoId;
    }

    /**
     * Define el valor de la propiedad turnoId.
     * 
     */
    public void setTurnoId(int value) {
        this.turnoId = value;
    }

    /**
     * Obtiene el valor de la propiedad turno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTurno() {
        return turno;
    }

    /**
     * Define el valor de la propiedad turno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTurno(String value) {
        this.turno = value;
    }

    /**
     * Obtiene el valor de la propiedad gradoEscolar.
     * 
     */
    public int getGradoEscolar() {
        return gradoEscolar;
    }

    /**
     * Define el valor de la propiedad gradoEscolar.
     * 
     */
    public void setGradoEscolar(int value) {
        this.gradoEscolar = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEscuela.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEscuela() {
        return tipoEscuela;
    }

    /**
     * Define el valor de la propiedad tipoEscuela.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEscuela(String value) {
        this.tipoEscuela = value;
    }

    /**
     * Obtiene el valor de la propiedad estatus.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * Define el valor de la propiedad estatus.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstatus(String value) {
        this.estatus = value;
    }

    /**
     * Obtiene el valor de la propiedad curp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurp() {
        return curp;
    }

    /**
     * Define el valor de la propiedad curp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurp(String value) {
        this.curp = value;
    }

    /**
     * Obtiene el valor de la propiedad nombres.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Define el valor de la propiedad nombres.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombres(String value) {
        this.nombres = value;
    }

    /**
     * Obtiene el valor de la propiedad primerApellido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimerApellido() {
        return primerApellido;
    }

    /**
     * Define el valor de la propiedad primerApellido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimerApellido(String value) {
        this.primerApellido = value;
    }

    /**
     * Obtiene el valor de la propiedad segundoApellido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegundoApellido() {
        return segundoApellido;
    }

    /**
     * Define el valor de la propiedad segundoApellido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegundoApellido(String value) {
        this.segundoApellido = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreCCT.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCCT() {
        return nombreCCT;
    }

    /**
     * Define el valor de la propiedad nombreCCT.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCCT(String value) {
        this.nombreCCT = value;
    }

    /**
     * Obtiene el valor de la propiedad calle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Define el valor de la propiedad calle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalle(String value) {
        this.calle = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroExterior.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroExterior() {
        return numeroExterior;
    }

    /**
     * Define el valor de la propiedad numeroExterior.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroExterior(String value) {
        this.numeroExterior = value;
    }

    /**
     * Obtiene el valor de la propiedad colonia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Define el valor de la propiedad colonia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColonia(String value) {
        this.colonia = value;
    }

    /**
     * Obtiene el valor de la propiedad alcaldiaId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlcaldiaId() {
        return alcaldiaId;
    }

    /**
     * Define el valor de la propiedad alcaldiaId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlcaldiaId(String value) {
        this.alcaldiaId = value;
    }

    /**
     * Obtiene el valor de la propiedad alcaldia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlcaldia() {
        return alcaldia;
    }

    /**
     * Define el valor de la propiedad alcaldia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlcaldia(String value) {
        this.alcaldia = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Define el valor de la propiedad codigoPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPostal(String value) {
        this.codigoPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelEducativoFIBIEDCDMXId.
     * 
     */
    public int getNivelEducativoFIBIEDCDMXId() {
        return nivelEducativoFIBIEDCDMXId;
    }

    /**
     * Define el valor de la propiedad nivelEducativoFIBIEDCDMXId.
     * 
     */
    public void setNivelEducativoFIBIEDCDMXId(int value) {
        this.nivelEducativoFIBIEDCDMXId = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelEducativoFIBIEDCDMX.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivelEducativoFIBIEDCDMX() {
        return nivelEducativoFIBIEDCDMX;
    }

    /**
     * Define el valor de la propiedad nivelEducativoFIBIEDCDMX.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivelEducativoFIBIEDCDMX(String value) {
        this.nivelEducativoFIBIEDCDMX = value;
    }

}
