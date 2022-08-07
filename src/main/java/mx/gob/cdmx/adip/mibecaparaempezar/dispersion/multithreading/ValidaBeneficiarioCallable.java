package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.EntidadEducativaSoapClient;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciResponse;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.BeneficiarioSinDispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao.DispersionDAO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSinDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMotivoNoDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatNivelEducativoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.environment.Environment;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.CsvUtils;

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
	private DispersionDAO dispersionDAO;

	private int indexInicio;

	public ValidaBeneficiarioCallable(DispersionDTO dispersion, List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios,
			int indexInicio, List<CatMontoApoyoDTO> lstCatMontoApoyo,
			BeneficiarioDispersionDAO beneficiarioDispersionDAO,
			BeneficiarioSinDispersionDAO beneficiarioSinDispersionDAO, DispersionDAO dispersionDAO) {
		this.lstBeneficiarios = lstBeneficiarios;
		this.dispersion = dispersion;
		this.indexInicio = indexInicio;
		this.lstCatMontoApoyo = lstCatMontoApoyo;
		this.beneficiarioDispersionDAO = beneficiarioDispersionDAO;
		this.beneficiarioSinDispersionDAO = beneficiarioSinDispersionDAO;
		this.dispersionDAO = dispersionDAO;
	}

	@Override
	public ResultadoEjecucionDTO call() throws Exception {
		Connection conn = null;
		ResultadoEjecucionDTO resultadoEjecucionDTO = new ResultadoEjecucionDTO();

		List<BeneficiarioDispersionDTO> beneficiariosConDispersion = new ArrayList<>();
		List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion = new ArrayList<>();

		// Lista para reportes CSV con Dispersion
		Map<String, List<BeneficiarioDispersionDTO>> mapaReportesNivelAcademicoConDispersion = new HashMap<>();
		mapaReportesNivelAcademicoConDispersion.put("preescolar", new ArrayList<>());
		mapaReportesNivelAcademicoConDispersion.put("primaria", new ArrayList<>());
		mapaReportesNivelAcademicoConDispersion.put("secundaria", new ArrayList<>());
		mapaReportesNivelAcademicoConDispersion.put("laboral", new ArrayList<>());

		// Lista para reportes CSV sin Dispersion
		Map<String, List<BeneficiarioSinDispersionDTO>> mapaReportesNivelAcademicoSinDispersion = new HashMap<>();
		mapaReportesNivelAcademicoSinDispersion.put("preescolar", new ArrayList<>());
		mapaReportesNivelAcademicoSinDispersion.put("primaria", new ArrayList<>());
		mapaReportesNivelAcademicoSinDispersion.put("secundaria", new ArrayList<>());
		mapaReportesNivelAcademicoSinDispersion.put("laboral", new ArrayList<>());

		try {
			conn = PostgresDatasource.getInstance().getConnection();
			conn.setAutoCommit(false);
			validaEstatusConexion(conn);

			// Se insertan todos los preparedStatements
			int[] resultadosConDispersion = null;
			int[] resultadosSinDispersion = null;

			for (BeneficiarioSolicitudTutorDTO beneficiario : lstBeneficiarios) {

				// Ejecutar WS
				MciResponse respuesta = EntidadEducativaSoapClient.consultarCurp(beneficiario.getCurpBeneficiario());

				// Validar que el beneficiario está vigente.
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
							new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
					beneficiarioDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),
							dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
					beneficiarioDispersion.setFechaCreacion(new Date()); // Fecha Creacion
					beneficiarioDispersion.setEsComplementaria(false); // Es complementaria
					beneficiarioDispersion.setIdBeneficiarioSinDispersion(null); // Beneficiario Sin Dispersion
					beneficiarioDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

					beneficiariosConDispersion.add(beneficiarioDispersion);
					asignarListaReportesDispersion(beneficiarioDispersion, mapaReportesNivelAcademicoConDispersion);

				} else {
					// Si no esta vigente, se pasa a la lista de beneficiarios sin dispersion
					BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
					beneficiarioSinDispersion.setDispersion(dispersion); // Dispersion
					beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario()); // CURP Beneficiario
					beneficiarioSinDispersion.setCurpTutor(beneficiario.getCurpTutor()); // CURP Tutor
					beneficiarioSinDispersion.setCatCicloEscolar(dispersion.getCatCicloEscolar()); // Ciclo Escolar
					beneficiarioSinDispersion.setCatPeriodoEscolar(dispersion.getCatPeriodoEscolar()); // Periodo
																										// Escolar
					beneficiarioSinDispersion.setCatNiveEducativo(
							new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue())); // Nivel Educativo
					beneficiarioSinDispersion.setCatMotivoNoDispersion(new CatMotivoNoDispersionDTO(1l));// Motivo No
																											// Dispersion
					beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),
							dispersion.getCatCicloEscolar().getIdCicloEscolar())); // Monto Apoyo
					beneficiarioSinDispersion.setFechaCreacion(new Date()); // Fecha Creacion
					beneficiarioSinDispersion.setIdBeneficiarioDispersion(null); // Beneficiario Dispersion
					beneficiarioSinDispersion.setNumeroCuenta(beneficiario.getNumeroCuenta());

					beneficiariosSinDispersion.add(beneficiarioSinDispersion);
					asignarListaReportesSinDispersion(beneficiarioSinDispersion, mapaReportesNivelAcademicoSinDispersion);
				}
			}

			resultadosConDispersion = beneficiarioDispersionDAO.guardarLista(conn, beneficiariosConDispersion);
			resultadosSinDispersion = beneficiarioSinDispersionDAO.guardarLista(conn, beneficiariosSinDispersion);

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

			// Generamos los CSV
			crearReportesCSV(dispersion, mapaReportesNivelAcademicoConDispersion);
			
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

	private void asignarListaReportesDispersion(BeneficiarioDispersionDTO beneficiarioDispersion, Map<String, List<BeneficiarioDispersionDTO>> mapaReportesNivelAcademicoConDispersion) {
		switch (beneficiarioDispersion.getCatNiveEducativo().getIdNivel()) {
		case Constantes.ID_PREESCOLAR:
			mapaReportesNivelAcademicoConDispersion.get("preescolar").add(beneficiarioDispersion);
			break;
		case Constantes.ID_PRIMARIA:
			mapaReportesNivelAcademicoConDispersion.get("primaria").add(beneficiarioDispersion);
			break;
		case Constantes.ID_SECUNDARIA:
			mapaReportesNivelAcademicoConDispersion.get("secundaria").add(beneficiarioDispersion);
			break;
		case Constantes.ID_CAM_LABORAL:
			mapaReportesNivelAcademicoConDispersion.get("laboral").add(beneficiarioDispersion);
			break;
		default:
			break;
		}
	}
	
	private void asignarListaReportesSinDispersion(BeneficiarioSinDispersionDTO beneficiarioSinDispersion, Map<String, List<BeneficiarioSinDispersionDTO>> mapaReportesNivelAcademicoSinDispersion) {
		switch (beneficiarioSinDispersion.getCatNiveEducativo().getIdNivel()) {
		case Constantes.ID_PREESCOLAR:
			mapaReportesNivelAcademicoSinDispersion.get("preescolar").add(beneficiarioSinDispersion);
			break;
		case Constantes.ID_PRIMARIA:
			mapaReportesNivelAcademicoSinDispersion.get("primaria").add(beneficiarioSinDispersion);
			break;
		case Constantes.ID_SECUNDARIA:
			mapaReportesNivelAcademicoSinDispersion.get("secundaria").add(beneficiarioSinDispersion);
			break;
		case Constantes.ID_CAM_LABORAL:
			mapaReportesNivelAcademicoSinDispersion.get("laboral").add(beneficiarioSinDispersion);
			break;
		default:
			break;
		}
	}

	private void validaEstatusConexion(Connection conn) throws SQLException {
		if (conn.isClosed()) {
			// miDAO.closeResources();
			LOGGER.warn("Se dectectó conexión cerrada. Se renueva la conexión");
			conn = PostgresDatasource.getInstance().getConnection();
			// miDAO = new MiDAO(conn);
		}
	}

	private CatMontoApoyoDTO obtenerCatMontoApoyo(Long idNivelEducativo, Long idCicloEscolar) {
		return lstCatMontoApoyo.stream()
				.filter(cat -> cat.getCatNivelEducativoDTO().getIdNivel().intValue() == idNivelEducativo
						&& cat.getCatCicloEscolarDTO().getIdCicloEscolar() == idCicloEscolar)
				.findFirst().get();
	}
	
	private List<String[]> mapearDispersion(List<BeneficiarioDispersionDTO> lstBeneficiariosConDispersion) {
		List<String[]> datosReporte = new ArrayList<String[]>();
		for (BeneficiarioDispersionDTO sinDispersion : lstBeneficiariosConDispersion) {
			datosReporte.add(new String[] { sinDispersion.getCurpTutor(), sinDispersion.getNumeroCuenta(), sinDispersion.getCatMontoApoyo().getMonto().toString() });
		}
		return datosReporte;
	}
	
	private void crearReportesCSV(DispersionDTO dispersion, Map<String, List<BeneficiarioDispersionDTO>> mapaReportesNivelAcademicoConDispersion) {
		List<String[]> reporteDispersionPreescolar = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("preescolar"));
		List<String[]> reporteDispersionPrimaria = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("primaria"));
		List<String[]> reporteDispersionSecundaria = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("secundaria"));
		List<String[]> reporteDispersionLaboral = mapearDispersion(mapaReportesNivelAcademicoConDispersion.get("laboral"));
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + "reporte_preescolar.csv", reporteDispersionPreescolar);
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + "reporte_primaria.csv", reporteDispersionPrimaria);
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + "reporte_secundaria.csv", reporteDispersionSecundaria);
		CsvUtils.addDataToCSV(Environment.getPathFolderPadrones() + "reporte_laboral.csv", reporteDispersionLaboral);
		dispersionDAO.actualizarArchivos(dispersion, "reporte_preescolar.csv", "reporte_primaria.csv", "reporte_secundaria.csv", "reporte_laboral.csv");
	}

}
