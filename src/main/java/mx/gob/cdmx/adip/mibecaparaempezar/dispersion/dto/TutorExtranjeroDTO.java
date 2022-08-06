package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto;

import java.io.Serializable;

public class TutorExtranjeroDTO implements Serializable {
	private static final long serialVersionUID = -1500328563389927086L;

	private Long idTutor;
	private String curp;
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String parentesco;
	private String calle;
	private String numInt;
	private String numExt;
	private String cp;
	private String correo;
	private String telefono;

	public TutorExtranjeroDTO() {
	}

	public TutorExtranjeroDTO(Long idTutor, String curp, String nombre, String primerApellido, String segundoApellido,
			String parentesco, String calle, String numInt, String numExt, String cp, String correo, String telefono) {
		this.idTutor = idTutor;
		this.curp = curp;
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.parentesco = parentesco;
		this.calle = calle;
		this.numInt = numInt;
		this.numExt = numExt;
		this.cp = cp;
		this.correo = correo;
		this.telefono = telefono;
	}

	public Long getIdTutor() {
		return idTutor;
	}

	public void setIdTutor(Long idTutor) {
		this.idTutor = idTutor;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumInt() {
		return numInt;
	}

	public void setNumInt(String numInt) {
		this.numInt = numInt;
	}

	public String getNumExt() {
		return numExt;
	}

	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}
