package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;

public class BeneficiarioDispersionDTO {

	private Long idBeneficiarioDispersion;
	private DispersionDTO dispersion;
	@CsvBindByName(column = "curpBeneficiario")
	private String curpBeneficiario;
	private String curpTutor;
	private CatCicloEscolarDTO catCicloEscolar;
	private CatPeriodoEscolarDTO catPeriodoEscolar;
	private CatNivelEducativoDTO catNiveEducativo;
	@CsvBindByName(column = "montoApoyo")
	private CatMontoApoyoDTO catMontoApoyo;
	private Date fechaCreacion;
	private Boolean esComplementaria;
	private Long idBeneficiarioSinDispersion;
	@CsvBindByName(column = "numeroCuenta")
	private String numeroCuenta;
	private Double montoAcumulado;

	public BeneficiarioDispersionDTO() {
		catCicloEscolar = new CatCicloEscolarDTO();
		catPeriodoEscolar = new CatPeriodoEscolarDTO();
		catNiveEducativo = new CatNivelEducativoDTO();
		catMontoApoyo = new CatMontoApoyoDTO();
		montoAcumulado = 0.0;
	}

	public BeneficiarioDispersionDTO(Long idBeneficiarioDispersion, Long idDispersion, String curpBeneficiario, Long idCicloEscolar, 
			String descripcionCicloEscolar, Long idPeriodoEscolar, String descripcionPeriodo, Integer idNivel, String descripcion, 
			Integer idMontoApoyo, Date fechaCreacion, Boolean esComplementaria) {
		this.idBeneficiarioDispersion = idBeneficiarioDispersion;
		this.dispersion = new DispersionDTO(idDispersion);
		this.curpBeneficiario = curpBeneficiario;
		this.catPeriodoEscolar = new CatPeriodoEscolarDTO(idPeriodoEscolar, descripcionPeriodo);
		this.catCicloEscolar = new CatCicloEscolarDTO(idCicloEscolar, descripcionCicloEscolar);
		this.catNiveEducativo = new CatNivelEducativoDTO(idNivel, descripcion);
		this.catMontoApoyo = new CatMontoApoyoDTO();
		this.fechaCreacion= fechaCreacion;
		this.esComplementaria = esComplementaria;
	}

	public Long getIdBeneficiarioDispersion() {
		return idBeneficiarioDispersion;
	}

	public void setIdBeneficiarioDispersion(Long idBeneficiarioDispersion) {
		this.idBeneficiarioDispersion = idBeneficiarioDispersion;
	}
	
	public DispersionDTO getDispersion() {
		return dispersion;
	}

	public void setDispersion(DispersionDTO dispersion) {
		this.dispersion = dispersion;
	}

	public String getCurpBeneficiario() {
		return curpBeneficiario;
	}

	public void setCurpBeneficiario(String curpBeneficiario) {
		this.curpBeneficiario = curpBeneficiario;
	}

	public CatCicloEscolarDTO getCatCicloEscolar() {
		return catCicloEscolar;
	}

	public void setCatCicloEscolar(CatCicloEscolarDTO catCicloEscolar) {
		this.catCicloEscolar = catCicloEscolar;
	}

	public CatPeriodoEscolarDTO getCatPeriodoEscolar() {
		return catPeriodoEscolar;
	}

	public void setCatPeriodoEscolar(CatPeriodoEscolarDTO catPeriodoEscolar) {
		this.catPeriodoEscolar = catPeriodoEscolar;
	}

	public CatNivelEducativoDTO getCatNiveEducativo() {
		return catNiveEducativo;
	}

	public void setCatNiveEducativo(CatNivelEducativoDTO catNiveEducativo) {
		this.catNiveEducativo = catNiveEducativo;
	}

	public CatMontoApoyoDTO getCatMontoApoyo() {
		return catMontoApoyo;
	}

	public void setCatMontoApoyo(CatMontoApoyoDTO catMontoApoyo) {
		this.catMontoApoyo = catMontoApoyo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Boolean getEsComplementaria() {
		return esComplementaria;
	}

	public void setEsComplementaria(Boolean esComplementaria) {
		this.esComplementaria = esComplementaria;
	}

	public Long getIdBeneficiarioSinDispersion() {
		return idBeneficiarioSinDispersion;
	}

	public void setIdBeneficiarioSinDispersion(Long idBeneficiarioSinDispersion) {
		this.idBeneficiarioSinDispersion = idBeneficiarioSinDispersion;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getCurpTutor() {
		return curpTutor;
	}

	public void setCurpTutor(String curpTutor) {
		this.curpTutor = curpTutor;
	}

	public Double getMontoAcumulado() {
		return montoAcumulado;
	}

	public void setMontoAcumulado(Double montoAcumulado) {
		this.montoAcumulado = montoAcumulado;
	}
	
}
