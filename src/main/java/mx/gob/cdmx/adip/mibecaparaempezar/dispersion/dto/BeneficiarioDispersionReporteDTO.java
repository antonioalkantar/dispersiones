package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

public class BeneficiarioDispersionReporteDTO {

	private Integer idNivelEducativo;
	private String curpTutor;
	private String curpBeneficiario;
	private String numeroCuenta;
	private Double monto;
	
	public Integer getIdNivelEducativo() {
		return idNivelEducativo;
	}

	public void setIdNivelEducativo(Integer idNivelEducativo) {
		this.idNivelEducativo = idNivelEducativo;
	}

	public String getCurpTutor() {
		return curpTutor;
	}
	
	public void setCurpTutor(String curpTutor) {
		this.curpTutor = curpTutor;
	}
	
	public String getCurpBeneficiario() {
		return curpBeneficiario;
	}
	
	public void setCurpBeneficiario(String curpBeneficiario) {
		this.curpBeneficiario = curpBeneficiario;
	}
	
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	public Double getMonto() {
		return monto;
	}
	
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
}
