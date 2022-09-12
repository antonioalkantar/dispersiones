package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class PadronExternoDTO implements Serializable {
	
	private static final long serialVersionUID = -2069556410128317002L;
	
	private Long idBeneficiarioExt;
	private String curp;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String cct;
	private String nombreCct;
	private String calle;
	private String numeroExterior;
	private String colonia;
	private int idMunicipio;
	private String municipio;
	private String codigoPostal;
	private String turno;
	private String nivelEducativo;
	private String gradoEscolar;
	private String estatus;
	private String tipoEscuela;

	

	public PadronExternoDTO () {
		
	}
	
	public PadronExternoDTO(String curp, String nombres, String primerApellido,
			String segundoApellido, String cct, String nombreCct, String calle, String numeroExterior, String colonia,
			int idMunicipio, String municipio, String codigoPostal, String turno, String nivelEducativo,
			String gradoEscolar, String estatus, String tipoEscuela) {
		this.curp = curp;
		this.nombres = nombres;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.cct = cct;
		this.nombreCct = nombreCct;
		this.calle = calle;
		this.numeroExterior = numeroExterior;
		this.colonia = colonia;
		this.idMunicipio = idMunicipio;
		this.municipio = municipio;
		this.codigoPostal = codigoPostal;
		this.turno = turno;
		this.nivelEducativo = nivelEducativo;
		this.gradoEscolar = gradoEscolar;
		this.estatus = estatus;
		this.tipoEscuela = tipoEscuela;
	}

	public Long getIdBeneficiarioExt() {
		return idBeneficiarioExt;
	}

	public void setIdBeneficiarioExt(Long idBeneficiarioExt) {
		this.idBeneficiarioExt = idBeneficiarioExt;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getCct() {
		return cct;
	}

	public void setCct(String cct) {
		this.cct = cct;
	}

	public String getNombreCct() {
		return nombreCct;
	}

	public void setNombreCct(String nombreCct) {
		this.nombreCct = nombreCct;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumeroExterior() {
		return numeroExterior;
	}

	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public int getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getNivelEducativo() {
		return nivelEducativo;
	}

	public void setNivelEducativo(String nivelEducativo) {
		this.nivelEducativo = nivelEducativo;
	}

	public String getGradoEscolar() {
		return gradoEscolar;
	}

	public void setGradoEscolar(String gradoEscolar) {
		this.gradoEscolar = gradoEscolar;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getTipoEscuela() {
		return tipoEscuela;
	}

	public void setTipoEscuela(String tipoEscuela) {
		this.tipoEscuela = tipoEscuela;
	}
	
	

}
