package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.EntidadEducativaSoapClient;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciResponse;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioSinDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BitacoraDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.DispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.PadronExternoDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionReporteDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSinDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BitacoraDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMotivoNoDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMunicipiosDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatNivelEducativoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.PadronExternoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;

/**
 * @author raul
 */
public class ValidaBeneficiarioCallable implements Callable<ResultadoEjecucionDTO> {

	private static final Logger LOGGER = LogManager.getLogger(ValidaBeneficiarioCallable.class);

	private List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios;
	private DispersionDTO dispersion;
	private List<CatMontoApoyoDTO> lstCatMontoApoyo;
	private BeneficiarioDispersionDAO beneficiarioDispersionDAO;
	private BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO;
	private BitacoraDAO bitacoraDAO;
	private DispersionDAO dispersionDAO;
	private PadronExternoDAO padronExternoDAO;

	private int indexInicio;

	public ValidaBeneficiarioCallable(DispersionDTO dispersion, List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios,
			int indexInicio, List<CatMontoApoyoDTO> lstCatMontoApoyo,
			BeneficiarioDispersionDAO beneficiarioDispersionDAO,
			BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO, DispersionDAO dispersionDAO,
			BitacoraDAO bitacoraDAO, PadronExternoDAO padronExternoDAO) {
		this.lstBeneficiarios = lstBeneficiarios;
		this.dispersion = dispersion;
		this.indexInicio = indexInicio;
		this.lstCatMontoApoyo = lstCatMontoApoyo;
		this.beneficiarioDispersionDAO = beneficiarioDispersionDAO;
		this.beneficiarioSinDispersionDAO = beneficiarioSinDispersionDAO;
		this.dispersionDAO = dispersionDAO;
		this.bitacoraDAO = bitacoraDAO;
		this.padronExternoDAO = padronExternoDAO;
	}

	@Override
	public ResultadoEjecucionDTO call() throws Exception {
		Connection conn = null;
		ResultadoEjecucionDTO resultadoEjecucionDTO = new ResultadoEjecucionDTO();

		List<BeneficiarioDispersionDTO> beneficiariosConDispersion = new ArrayList<>();
		List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion = new ArrayList<>();
		List<BitacoraDTO> bitacoraCambios = new ArrayList<>();

		try {
			conn = PostgresDatasource.getInstance().getConnection();
			conn.setAutoCommit(false);
			validaEstatusConexion(conn);

			// Se insertan todos los preparedStatements
			int[] resultadosConDispersion = null;
			int[] resultadosSinDispersion = null;
			int[] resgitrosBitacora = null;

			for (BeneficiarioSolicitudTutorDTO beneficiario : lstBeneficiarios) {

				// Ejecutar WS
				MciResponse respuesta = EntidadEducativaSoapClient.consultarCurp(beneficiario.getCurpBeneficiario());

				// Se valida si existen cambios para hacer la actualizacion y guardar la
				// bitacora de cambios
				obtenerDiferencias(bitacoraCambios, beneficiario, respuesta);

				// Validar que el beneficiario está vigente.
				if (!respuesta.getEstatus().equals(Constantes.BENEFICIARIO_LOCALIZADO)) {
					if (respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)
							&& beneficiario.getIdEstatusTutor() == Constantes.ID_ESTATUS_APROBADA) {
						// Si el beneficiario esta vigente se pasa a la lista de beneficiarios con
						// dispersion
						BeneficiarioDispersionDTO beneficiarioDispersion = new BeneficiarioDispersionDTO();
						beneficiarioDispersion.setDispersion(dispersion); // Dispersion
						beneficiarioDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
						beneficiarioDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
						beneficiarioDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
						beneficiarioDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
						beneficiarioDispersion.setCatNiveEducativo(
								new CatNivelEducativoDTO(respuesta.getNivelEducativoFIBIEDCDMXId() != 0 ? respuesta.getNivelEducativoFIBIEDCDMXId() : beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
						beneficiarioDispersion
								.setCatMontoApoyo(obtenerCatMontoApoyo(respuesta.getNivelEducativoFIBIEDCDMXId() != 0 ? (long) respuesta.getNivelEducativoFIBIEDCDMXId() : beneficiario.getIdNivelEducativo(),
										dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
						beneficiarioDispersion.setFechaCreacion(new Date()); // Fecha Creacion
						if (dispersion.getCatTipoDispersion()
								.getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
							beneficiarioDispersion.setEsComplementaria(true); // Es complementaria
						} else {
							beneficiarioDispersion.setEsComplementaria(false); // Es complementaria
						}
						beneficiarioDispersion.setIdBeneficiarioSinDispersion(null); // Beneficiario Sin Dispersion
						beneficiarioDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
						if (dispersion.getCatTipoDispersion()
								.getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
							beneficiarioDispersion
									.setIdBeneficiarioSinDispersion(beneficiario.getIdBeneficiarioSinDispersion());
						}

						beneficiariosConDispersion.add(beneficiarioDispersion);

					} else {
						// Si no esta vigente, se pasa a la lista de beneficiarios sin dispersion
						BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
						beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
						beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
						beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
						beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
						beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo Escolar
						beneficiarioSinDispersion.setCatNiveEducativo(
								new CatNivelEducativoDTO(respuesta.getNivelEducativoFIBIEDCDMXId() != 0 ? respuesta.getNivelEducativoFIBIEDCDMXId() : beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
						if (respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)
								&& beneficiario.getIdEstatusTutor() != Constantes.ID_ESTATUS_APROBADA) {
							beneficiarioSinDispersion.setCatMotivoNoDispersion(
									new CatMotivoNoDispersionDTO(Constantes.TUTOR_NO_APROBADO));// Motivo No Dispersion
						} else if (!respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)
								&& beneficiario.getIdEstatusTutor() != Constantes.ID_ESTATUS_APROBADA) {
							beneficiarioSinDispersion.setCatMotivoNoDispersion(
									new CatMotivoNoDispersionDTO(Constantes.BENEFICIARIO_NO_ACTIVO));// Motivo No Dispersion
						}
						beneficiarioSinDispersion
								.setCatMontoApoyo(obtenerCatMontoApoyo(respuesta.getNivelEducativoFIBIEDCDMXId() != 0 ? (long) respuesta.getNivelEducativoFIBIEDCDMXId() : beneficiario.getIdNivelEducativo(),
										dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
						beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
						beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
						beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

						beneficiariosSinDispersion.add(beneficiarioSinDispersion);
					}
				} else {
					// Revisamos en la bd el padron externo
					List<PadronExternoDTO> lstPadronExternoDTO = padronExternoDAO
							.buscarPorCurp(beneficiario.getCurpBeneficiario());
					if (lstPadronExternoDTO.size() > 0) {
						PadronExternoDTO padronExterno = lstPadronExternoDTO.get(0);
						BeneficiarioSolicitudTutorDTO beneficiarioSolicitudDTO = new BeneficiarioSolicitudTutorDTO();
						beneficiarioSolicitudDTO.setCurpBeneficiario(beneficiario.getCurpBeneficiario());
						beneficiarioSolicitudDTO.setCctSolicitud(padronExterno.getCct());
						beneficiarioSolicitudDTO.setNombreCctSolicitud(padronExterno.getNombreCct());
						beneficiarioSolicitudDTO.setCalleSolicitud(padronExterno.getCalle());
						beneficiarioSolicitudDTO.setColoniaSolicitud(padronExterno.getColonia());
						beneficiarioSolicitudDTO
								.setAlcaldiaSolicitud(padronExterno.getIdMunicipio() == Constantes.CODIGO_MUNICIPIO_ZERO
										? (long) Constantes.ID_MUNICIPIO_FORANEO
										: (long) padronExterno.getIdMunicipio());
						beneficiarioSolicitudDTO.setCodigoPostalSolicitud(padronExterno.getCodigoPostal());
						beneficiarioSolicitudDTO.setTurnoSolicitud(padronExterno.getTurno());
						switch (padronExterno.getNivelEducativo()) {
						case Constantes.DESC_PREESCOLAR:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_PREESCOLAR);
							break;
						case Constantes.DESC_PRIMARIA:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_PRIMARIA);
							break;
						case Constantes.DESC_SECUNDARIA:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_SECUNDARIA);
							break;
						case Constantes.DESC_PRIMARIA_ADULTOS_PAD_EXT:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_PRIMARIA_ADULTOS);
							break;
						case Constantes.DESC_SECUNDARIA_ADULTOS_PAD_EXT:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_SECUNDARIA_ADULTOS);
							break;
						case Constantes.DESC_CAM_PREESCOLAR_PAD_EXT:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_CAM_PREESCOLAR);
							break;
						case Constantes.DESC_CAM_PRIMARIA_PAD_EXT:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_CAM_PRIMARIA);
							break;
						case Constantes.DESC_CAM_SECUNDARIA_PAD_EXT:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_CAM_SECUNDARIA);
							break;
						case Constantes.DESC_CAM_LABORAL_PAD_EXT:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_CAM_LABORAL);
							break;
						default:
							beneficiarioSolicitudDTO.setIdNivelEducativo((long) Constantes.ID_OTRO);
							break;
						}
						beneficiarioSolicitudDTO.setGradoEscolarSolicitud(padronExterno.getGradoEscolar());
						if (padronExterno.getEstatus().equals("ACTIVO")) {
							beneficiarioSolicitudDTO.setIdEstatusTutor((long) Constantes.ESTATUS_BENEFICIARIO_ACTIVO);
							
							BeneficiarioDispersionDTO beneficiarioDispersion = new BeneficiarioDispersionDTO();
							beneficiarioDispersion.setDispersion(dispersion); // Dispersion
							beneficiarioDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP
																											// Beneficiario
							beneficiarioDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
							beneficiarioDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
							beneficiarioDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo
																											// Escolar
							beneficiarioDispersion.setCatNiveEducativo(
									new CatNivelEducativoDTO(beneficiarioSolicitudDTO.getIdNivelEducativo().intValue())); // Nivel Educativo
							beneficiarioDispersion
									.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiarioSolicitudDTO.getIdNivelEducativo(),
											dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
							beneficiarioDispersion.setFechaCreacion(new Date()); // Fecha Creacion
							if (dispersion.getCatTipoDispersion()
									.getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
								beneficiarioDispersion.setEsComplementaria(true); // Es complementaria
							} else {
								beneficiarioDispersion.setEsComplementaria(false); // Es complementaria
							}
							beneficiarioDispersion.setIdBeneficiarioSinDispersion(null); // Beneficiario Sin Dispersion
							beneficiarioDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());
							if (dispersion.getCatTipoDispersion()
									.getIdTipoDispersion() == Constantes.ID_TIPO_DISPERSION_COMPLEMENTARIA) {
								beneficiarioDispersion
										.setIdBeneficiarioSinDispersion(beneficiario.getIdBeneficiarioSinDispersion());
							}

							beneficiariosConDispersion.add(beneficiarioDispersion);
						} else {
							beneficiarioSolicitudDTO.setIdEstatusTutor((long) Constantes.ID_ESTATUS_BENEFICIARIO_OTRO);
							
							BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
							beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
							beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP
																												// Beneficiario
							beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
							beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
							beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo
																												// Escolar
							beneficiarioSinDispersion.setCatNiveEducativo(
									new CatNivelEducativoDTO(beneficiarioSolicitudDTO.getIdNivelEducativo().intValue())); // Nivel Educativo
							if (respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)
									&& beneficiario.getIdEstatusTutor() != Constantes.ID_ESTATUS_APROBADA) {
								beneficiarioSinDispersion.setCatMotivoNoDispersion(
										new CatMotivoNoDispersionDTO(Constantes.TUTOR_NO_APROBADO));// Motivo No Dispersion
							} else if (!respuesta.getEstatus().equals(Constantes.BENEFICIARIO_ACTIVO)
									&& beneficiario.getIdEstatusTutor() != Constantes.ID_ESTATUS_APROBADA) {
								beneficiarioSinDispersion.setCatMotivoNoDispersion(
										new CatMotivoNoDispersionDTO(Constantes.BENEFICIARIO_NO_ACTIVO));// Motivo No
																											// Dispersion
							}
							beneficiarioSinDispersion
									.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiarioSolicitudDTO.getIdNivelEducativo(),
											dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
							beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
							beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
							beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

							beneficiariosSinDispersion.add(beneficiarioSinDispersion);
						}
					}
				}
			}

			resultadosConDispersion = beneficiarioDispersionDAO.guardarLista(conn, beneficiariosConDispersion);
			resultadosSinDispersion = beneficiarioSinDispersionDAO.guardarLista(conn, beneficiariosSinDispersion);

			// Se actualizan los registros y se persisten los registros de bitacora
			bitacoraDAO.actualizarDatosSolicitud(bitacoraCambios);
			resgitrosBitacora = bitacoraDAO.guardar(conn, bitacoraCambios);
			LOGGER.info("Registros en bitáctora actualizados: " + resgitrosBitacora.length);

			conn.commit();
			conn.setAutoCommit(true);

			// Se suman los totales.
			resultadoEjecucionDTO.setTotalRegistros(lstBeneficiarios.size());
			resultadoEjecucionDTO.setTotalDispersados(resultadosConDispersion.length);
			resultadoEjecucionDTO.setTotalNoDispersados(resultadosSinDispersion.length);

			ContadorProgresoSynchronized
					.incrementarAvance(resultadosConDispersion.length + resultadosSinDispersion.length);

			LOGGER.info("El hilo " + Thread.currentThread().getName() + " terminó la ejecución de "
					+ lstBeneficiarios.size() + " registros Dispersados:" + resultadosConDispersion.length
					+ ", No Dispersados: " + resultadosSinDispersion.length);
		} catch (Exception e) {
			LOGGER.error("Ocurrió un error en la ejecución del Thread:", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					LOGGER.warn("No se pudo cerrar una conexión");
				}
			}
		}
		return resultadoEjecucionDTO;
	}

	private void obtenerDiferencias(List<BitacoraDTO> lstBitacoraCambios,
			BeneficiarioSolicitudTutorDTO beneficiarioSolicitud, MciResponse entidadEducativa) {
		BitacoraDTO bitacora = new BitacoraDTO();
		bitacora.setActualizaCct(false);
		bitacora.setActualizaNombre(false);
		bitacora.setActualizaCalle(false);
		bitacora.setActualizaColonia(false);
		bitacora.setActualizaTurno(false);
		bitacora.setActualizaAlcaldia(false);
		bitacora.setActualizaNivelEducativo(false);
		bitacora.setActualizaGradoEscolar(false);
		bitacora.setActualizaCodigoPostal(false);

		// Se comparan los objetos para obtener las diferencias del nivel educativo
		if (!beneficiarioSolicitud.getIdNivelEducativo().toString()
				.equals(String.valueOf(entidadEducativa.getNivelEducativoFIBIEDCDMXId()))) {
			bitacora.setActualizaNivelEducativo(true);
			bitacora.setIdNivelEducativoAnterior(beneficiarioSolicitud.getIdNivelEducativo());
		}

		// Se comparan los objetos para obtener las diferencias del cct
		if (!beneficiarioSolicitud.getCctSolicitud().equals(entidadEducativa.getCct())) {
			bitacora.setActualizaCct(true);
			bitacora.setCctAnterior(beneficiarioSolicitud.getCctSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias del turno
		if (!beneficiarioSolicitud.getTurnoSolicitud().equals(String.valueOf(entidadEducativa.getTurnoId()))) {
			bitacora.setActualizaTurno(true);
			bitacora.setIdTurnoAnterior(beneficiarioSolicitud.getTurnoSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias del grado escolar
		if (!beneficiarioSolicitud.getGradoEscolarSolicitud().toString()
				.equals(String.valueOf(entidadEducativa.getGradoEscolar()))) {
			bitacora.setActualizaGradoEscolar(true);
			bitacora.setGradoEscolarAnterior(beneficiarioSolicitud.getGradoEscolarSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias del nombre cct
		if (!beneficiarioSolicitud.getNombreCctSolicitud().equals(entidadEducativa.getNombreCCT())) {
			bitacora.setActualizaNombre(true);
			bitacora.setNombreCctAnterior(beneficiarioSolicitud.getNombreCctSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias de la calle
		if (!beneficiarioSolicitud.getCalleSolicitud().equals(entidadEducativa.getCalle())) {
			bitacora.setActualizaCalle(true);
			bitacora.setCalleCctAnterior(beneficiarioSolicitud.getCalleSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias de la colonia
		if (!beneficiarioSolicitud.getColoniaSolicitud().equals(entidadEducativa.getColonia())) {
			bitacora.setActualizaColonia(true);
			bitacora.setColoniaCctAnterior(beneficiarioSolicitud.getColoniaSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias de la alcaldia
		if (!beneficiarioSolicitud.getAlcaldiaSolicitud().toString().equals(entidadEducativa.getAlcaldiaId())) {
			bitacora.setActualizaAlcaldia(true);
			bitacora.setIdAlcaldiaCctAnterior(beneficiarioSolicitud.getAlcaldiaSolicitud());
		}

		// Se comparan los objetos para obtener las diferencias del codigoPostal
		if (!beneficiarioSolicitud.getCodigoPostalSolicitud().equals(entidadEducativa.getCodigoPostal())) {
			bitacora.setActualizaCodigoPostal(true);
			bitacora.setCodigoPostalCctAnterior(beneficiarioSolicitud.getCodigoPostalSolicitud());
		}

		bitacora.setIdDispersion(dispersion.getIdDispersion());
		bitacora.setIdSolicitud(beneficiarioSolicitud.getIdSolicitud());
		bitacora.setIdBeneficiario(beneficiarioSolicitud.getIdBeneficiario());
		bitacora.setFechaRegistro(new Date());

		lstBitacoraCambios.add(bitacora);
	}

	private void validaEstatusConexion(Connection conn) throws SQLException {
		if (conn.isClosed()) {
			LOGGER.warn("Se dectectó conexión cerrada. Se renueva la conexión");
			conn = PostgresDatasource.getInstance().getConnection();
		}
	}

	private CatMontoApoyoDTO obtenerCatMontoApoyo(Long idNivelEducativo, Long idCicloEscolar) {
		return lstCatMontoApoyo.stream()
				.filter(cat -> cat.getCatNivelEducativoDTO().getIdNivel().intValue() == idNivelEducativo
						&& cat.getCatCicloEscolarDTO().getIdCicloEscolar() == idCicloEscolar)
				.findFirst().get();
	}

}
