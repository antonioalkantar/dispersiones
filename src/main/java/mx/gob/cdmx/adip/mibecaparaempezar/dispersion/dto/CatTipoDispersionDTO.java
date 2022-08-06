package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class CatTipoDispersionDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3827780635510968367L;
	private Long idTipoDispersion;
	private String descripcion;
	private Boolean estatus;

	public CatTipoDispersionDTO() {
	}
	
	public CatTipoDispersionDTO(Long idTipoDispersion) {
		this.idTipoDispersion = idTipoDispersion;
	}
	
	public CatTipoDispersionDTO(Long idTipoDispersion, String descripcion) {
		this.idTipoDispersion = idTipoDispersion;
		this.descripcion = descripcion;
	}

	public CatTipoDispersionDTO(Long idTipoDispersion, String descripcion, Boolean estatus) {
		this.idTipoDispersion = idTipoDispersion;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Long getIdTipoDispersion() {
		return idTipoDispersion;
	}

	public void setIdTipoDispersion(Long idTipoDispersion) {
		this.idTipoDispersion = idTipoDispersion;
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
