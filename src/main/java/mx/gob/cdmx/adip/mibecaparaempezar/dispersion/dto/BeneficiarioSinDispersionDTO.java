package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.util.Date;

public class BeneficiarioSinDispersionDTO {

	private Long idBeneficiarioSinDispersion;
	private DispersionDTO dispersion;
	private String curpBeneficiario;
	private CatCicloEscolarDTO catCicloEscolar;
	private CatPeriodoEscolarDTO catPeriodoEscolar;
	private CatNivelEducativoDTO catNiveEducativo;
	private CatMotivoNoDispersionDTO catMotivoNoDispersion;
	private CatMontoApoyoDTO catMontoApoyo;
	private Date fechaCreacion;
	private Boolean esComplementaria;

	public BeneficiarioSinDispersionDTO() {
		catCicloEscolar = new CatCicloEscolarDTO();
		catPeriodoEscolar = new CatPeriodoEscolarDTO();
		catNiveEducativo = new CatNivelEducativoDTO();
		catMotivoNoDispersion = new CatMotivoNoDispersionDTO();
		catMontoApoyo = new CatMontoApoyoDTO();
	}

	public BeneficiarioSinDispersionDTO(Long idBeneficiarioSinDispersion, String curpBeneficiario, Long idDispersion, 
			Long idCicloEscolar, String descripcionCicloEscolar, Long idPeriodoEscolar, String descripcionPeriodo, 
			Integer idNivel, String descripcion, Integer idMontoApoyo, Long idMotivoNoDispersion, String descripcionMotivoNoDispersion, Date fechaCreacion, Boolean esComplementaria) {
		this.idBeneficiarioSinDispersion = idBeneficiarioSinDispersion;
		this.curpBeneficiario = curpBeneficiario;
		this.dispersion = new DispersionDTO(idDispersion);
		this.catPeriodoEscolar = new CatPeriodoEscolarDTO(idPeriodoEscolar, descripcionPeriodo);
		this.catCicloEscolar = new CatCicloEscolarDTO(idCicloEscolar, descripcionCicloEscolar);
		this.catNiveEducativo = new CatNivelEducativoDTO(idNivel, descripcion);
		this.catMotivoNoDispersion= new CatMotivoNoDispersionDTO(idMotivoNoDispersion, descripcionMotivoNoDispersion);
		this.catMontoApoyo = new CatMontoApoyoDTO(idMontoApoyo);
		this.fechaCreacion= fechaCreacion;
		this.esComplementaria = esComplementaria;
	}

	public Long getIdBeneficiarioSinDispersion() {
		return idBeneficiarioSinDispersion;
	}

	public void setIdBeneficiarioSinDispersion(Long idBeneficiarioSinDispersion) {
		this.idBeneficiarioSinDispersion = idBeneficiarioSinDispersion;
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

	public CatMotivoNoDispersionDTO getCatMotivoNoDispersion() {
		return catMotivoNoDispersion;
	}

	public void setCatMotivoNoDispersion(CatMotivoNoDispersionDTO catMotivoNoDispersion) {
		this.catMotivoNoDispersion = catMotivoNoDispersion;
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
	
}
