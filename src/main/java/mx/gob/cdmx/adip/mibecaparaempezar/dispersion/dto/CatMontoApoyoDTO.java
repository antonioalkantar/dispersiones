package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;
import java.util.Date;

public class CatMontoApoyoDTO implements Serializable {

	private static final long serialVersionUID = -8971224535899073308L;

	private Integer idMontoApoyo;
	private CatCicloEscolarDTO catCicloEscolarDTO;
	private CatNivelEducativoDTO catNivelEducativoDTO;
	private Double monto;
	private Boolean estatus;
	private Date fechaCreacion;

	public CatMontoApoyoDTO() {
	}

	public CatMontoApoyoDTO(Integer idMontoApoyo) {
		this.idMontoApoyo = idMontoApoyo;
	}

	/**
	 * @param idMontoApoyo
	 * @param catCicloEscolarDTO
	 * @param catNivelEducativoDTO
	 * @param monto
	 * @param estatus
	 * @param fechaCreacion
	 */
	public CatMontoApoyoDTO(Integer idMontoApoyo, Double monto, Integer idNivel, String descripcionNivel,
			Long idCicloEscolar, String cicloEscolar) {
		this.idMontoApoyo = idMontoApoyo;
		this.catCicloEscolarDTO = new CatCicloEscolarDTO(idCicloEscolar, cicloEscolar);
		this.catNivelEducativoDTO = new CatNivelEducativoDTO(idNivel, descripcionNivel);
		this.monto = monto;
	}

	public Integer getIdMontoApoyo() {
		return idMontoApoyo;
	}

	public void setIdMontoApoyo(Integer idMontoApoyo) {
		this.idMontoApoyo = idMontoApoyo;
	}

	public CatCicloEscolarDTO getCatCicloEscolarDTO() {
		return catCicloEscolarDTO;
	}

	public void setCatCicloEscolarDTO(CatCicloEscolarDTO catCicloEscolarDTO) {
		this.catCicloEscolarDTO = catCicloEscolarDTO;
	}

	public CatNivelEducativoDTO getCatNivelEducativoDTO() {
		return catNivelEducativoDTO;
	}

	public void setCatNivelEducativoDTO(CatNivelEducativoDTO catNivelEducativoDTO) {
		this.catNivelEducativoDTO = catNivelEducativoDTO;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

}
