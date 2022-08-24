package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

public class BeneficiarioSolicitudTutorDTO {

	private Long idUsuario;
	private String curpTutor;
	private Long idEstatusTutor;
	private String numeroCuenta;
	private Long idNivelEducativo;
	private Long idGradoEscolar;
	private Long idBeneficiario;
	private String curpBeneficiario;
	private Long idBeneficiarioSinDispersion;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCurpTutor() {
		return curpTutor;
	}

	public void setCurpTutor(String curpTutor) {
		this.curpTutor = curpTutor;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Long getIdNivelEducativo() {
		return idNivelEducativo;
	}

	public void setIdNivelEducativo(Long idNivelEducativo) {
		this.idNivelEducativo = idNivelEducativo;
	}

	public Long getIdGradoEscolar() {
		return idGradoEscolar;
	}

	public void setIdGradoEscolar(Long idGradoEscolar) {
		this.idGradoEscolar = idGradoEscolar;
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

	public Long getIdEstatusTutor() {
		return idEstatusTutor;
	}

	public void setIdEstatusTutor(Long idEstatusTutor) {
		this.idEstatusTutor = idEstatusTutor;
	}

	public Long getIdBeneficiarioSinDispersion() {
		return idBeneficiarioSinDispersion;
	}

	public void setIdBeneficiarioSinDispersion(Long idBeneficiarioSinDispersion) {
		this.idBeneficiarioSinDispersion = idBeneficiarioSinDispersion;
	}
	
}
