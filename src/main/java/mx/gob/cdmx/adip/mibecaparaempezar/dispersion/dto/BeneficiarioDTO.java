package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;
import java.util.Date;

public class BeneficiarioDTO implements Serializable {

	private static final long serialVersionUID = 4982319709001411365L;

	private Long idBeneficiario;
	private String curpBeneficiario;
	private String nombresBeneficiario;
	private String primerApellidoBeneficiario;
	private String segundoApellidoBeneficiario;
	private Date fechaNacimientoBeneficiario;
	private Boolean esTutor;
	private String nacionalidad;
	private int edad;
	private Boolean curpValidada;
	private Boolean esExtranjero;
	private Long idUsuarioLlaveCdmx;
	private Date fechaRegistro;

	public BeneficiarioDTO() {

	}

	public BeneficiarioDTO(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}
	
	public BeneficiarioDTO(Long idBeneficiario, String curp) {
		this.idBeneficiario = idBeneficiario;
		this.curpBeneficiario = curp;
	}

	public BeneficiarioDTO(Long idBeneficiario, String curpBeneficiario, String nombresBeneficiario,
			String primerApellidoBeneficiario, String segundoApellidoBeneficiario, Date fechaNacimientoBeneficiario,
			Boolean esTutor, String nacionalidad) {
		this.idBeneficiario = idBeneficiario;
		this.curpBeneficiario = curpBeneficiario;
		this.nombresBeneficiario = nombresBeneficiario;
		this.primerApellidoBeneficiario = primerApellidoBeneficiario;
		this.segundoApellidoBeneficiario = segundoApellidoBeneficiario;
		this.fechaNacimientoBeneficiario = fechaNacimientoBeneficiario;
		this.esTutor = esTutor;
		this.nacionalidad = nacionalidad;
	}
	public BeneficiarioDTO(Long idBeneficiario, String nombresBeneficiario,
			String primerApellidoBeneficiario, String segundoApellidoBeneficiario) {
		this.idBeneficiario = idBeneficiario;
		this.nombresBeneficiario = nombresBeneficiario;
		this.primerApellidoBeneficiario = primerApellidoBeneficiario;
		this.segundoApellidoBeneficiario = segundoApellidoBeneficiario;
	}
	
	public BeneficiarioDTO(Long idBeneficiario, String nombresBeneficiario,
			String primerApellidoBeneficiario, String segundoApellidoBeneficiario, String nacionalidad, String curpBeneficiario) {
		this.idBeneficiario = idBeneficiario;
		this.nombresBeneficiario = nombresBeneficiario;
		this.primerApellidoBeneficiario = primerApellidoBeneficiario;
		this.segundoApellidoBeneficiario = segundoApellidoBeneficiario;
		this.nacionalidad = nacionalidad;
		this.curpBeneficiario = curpBeneficiario;
	}

	public BeneficiarioDTO(Long idBeneficiario
			, String curpBeneficiario
			, String nombresBeneficiario
			, String primerApellidoBeneficiario
			, String segundoApellidoBeneficiario
			, String nacionalidad
			, Date fechaNacimientoBeneficiario
			, Boolean esTutor) {
		this.idBeneficiario = idBeneficiario;
		this.curpBeneficiario = curpBeneficiario;
		this.nombresBeneficiario = nombresBeneficiario;
		this.primerApellidoBeneficiario = primerApellidoBeneficiario;
		this.segundoApellidoBeneficiario = segundoApellidoBeneficiario;
		this.nacionalidad = nacionalidad;
		this.fechaNacimientoBeneficiario = fechaNacimientoBeneficiario;
		this.esTutor = esTutor;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getCurpBeneficiario() {
		return curpBeneficiario;
	}

	public void setCurpBeneficiario(String curpBeneficiario) {
		this.curpBeneficiario = curpBeneficiario;
	}

	public String getNombresBeneficiario() {
		return nombresBeneficiario;
	}

	public void setNombresBeneficiario(String nombresBeneficiario) {
		this.nombresBeneficiario = nombresBeneficiario;
	}

	public String getPrimerApellidoBeneficiario() {
		return primerApellidoBeneficiario;
	}

	public void setPrimerApellidoBeneficiario(String primerApellidoBeneficiario) {
		this.primerApellidoBeneficiario = primerApellidoBeneficiario;
	}

	public String getSegundoApellidoBeneficiario() {
		return segundoApellidoBeneficiario;
	}

	public void setSegundoApellidoBeneficiario(String segundoApellidoBeneficiario) {
		this.segundoApellidoBeneficiario = segundoApellidoBeneficiario;
	}

	public Date getFechaNacimientoBeneficiario() {
		return fechaNacimientoBeneficiario;
	}

	public void setFechaNacimientoBeneficiario(Date fechaNacimientoBeneficiario) {
		this.fechaNacimientoBeneficiario = fechaNacimientoBeneficiario;
	}

	public Boolean getEsTutor() {
		return esTutor;
	}

	public void setEsTutor(Boolean esTutor) {
		this.esTutor = esTutor;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public Boolean getCurpValidada() {
		return curpValidada;
	}

	public void setCurpValidada(Boolean curpValidada) {
		this.curpValidada = curpValidada;
	}

	public Boolean getEsExtranjero() {
		return esExtranjero;
	}

	public void setEsExtranjero(Boolean esExtranjero) {
		this.esExtranjero = esExtranjero;
	}

	public String getNombresCompletoBeneficiario() {		
		return nombresBeneficiario+" "+primerApellidoBeneficiario+" "+(segundoApellidoBeneficiario == null ? "" : segundoApellidoBeneficiario);		
	}

	public Long getIdUsuarioLlaveCdmx() {
		return idUsuarioLlaveCdmx;
	}

	public void setIdUsuarioLlaveCdmx(Long idUsuarioLlaveCdmx) {
		this.idUsuarioLlaveCdmx = idUsuarioLlaveCdmx;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
