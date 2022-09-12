package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class CatEstatusBeneficiarioDTO implements Serializable {
	
	private static final long serialVersionUID = 3623926218811116794L;
	private Integer idEstatusBeneficiario;
	private String 	descripcion;
	private Boolean estatus;
	
	public CatEstatusBeneficiarioDTO() {
		
	}
	
	/**
	 * @param idEstatusBeneficiario
	 * @param descripcion
	 * @param estatus
	 */
	public CatEstatusBeneficiarioDTO(Integer idEstatusBeneficiario) {
		this.idEstatusBeneficiario = idEstatusBeneficiario;
	}
	
	/**
	 * @param idEstatusBeneficiario
	 * @param descripcion
	 * @param estatus
	 */
	public CatEstatusBeneficiarioDTO(Integer idEstatusBeneficiario, String descripcion, Boolean estatus) {
		this.idEstatusBeneficiario = idEstatusBeneficiario;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}
	/**
	 * @param idEstatusBeneficiario
	 * @param descripcion
	 */
	public CatEstatusBeneficiarioDTO(Integer idEstatusBeneficiario, String descripcion) {
		this.idEstatusBeneficiario = idEstatusBeneficiario;
		this.descripcion = descripcion;
	}

	public Integer getIdEstatusBeneficiario() {
		return idEstatusBeneficiario;
	}

	public void setIdEstatusBeneficiario(Integer idEstatusBeneficiario) {
		this.idEstatusBeneficiario = idEstatusBeneficiario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
}