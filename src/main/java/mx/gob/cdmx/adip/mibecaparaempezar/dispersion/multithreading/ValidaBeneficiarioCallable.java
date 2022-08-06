package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.multithreading;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSinDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSolicitudTutorDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMontoApoyoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatNivelEducativoDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.ResultadoEjecucionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.util.Constantes;

/**
 * @author raul
 */
public class ValidaBeneficiarioCallable implements Callable<ResultadoEjecucionDTO> {

	private static final Logger LOGGER = LogManager.getLogger(ValidaBeneficiarioCallable.class);

	private List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios;
	private DispersionDTO archivoPadronDTO;
	private List<CatMontoApoyoDTO> lstCatMontoApoyo;

	private int indexInicio;

	public ValidaBeneficiarioCallable(DispersionDTO archivoPadronDTO,
			List<BeneficiarioSolicitudTutorDTO> lstBeneficiarios, int indexInicio,
			List<CatMontoApoyoDTO> lstCatMontoApoyo) {
		this.lstBeneficiarios = lstBeneficiarios;
		this.archivoPadronDTO = archivoPadronDTO;
		this.indexInicio = indexInicio;
		this.lstCatMontoApoyo = lstCatMontoApoyo;
	}

	@Override
	public ResultadoEjecucionDTO call() throws Exception {
		ResultadoEjecucionDTO resultadoEjecucionDTO = new ResultadoEjecucionDTO();

		List<BeneficiarioDispersionDTO> beneficiariosConDispersion = new ArrayList<>();
		List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersion = new ArrayList<>();

		try (Connection conn = PostgresDatasource.getInstance().getConnection();) {
			conn.setAutoCommit(false);

			// Se insertan todos los preparedStatements
			int[] resultadosActualizacion = null;
			PreparedStatement pstmtConDispersion = null;
			PreparedStatement pstmtSinDispersion = null;

			for (BeneficiarioSolicitudTutorDTO beneficiario : lstBeneficiarios) {

				validaEstatusConexion(conn);

				// Ejecutar WS

				// Validar que el beneficiario está vigente.
				if (beneficiario.getIdEstatusTutor() == Constantes.ID_ESTATUS_APROBADA) {
					// Si el beneficiario esta vigente se pasa a la lista de beneficiarios con
					// dispersion

					BeneficiarioDispersionDTO beneficiarioDispersion = new BeneficiarioDispersionDTO();
					beneficiarioDispersion.setCatCicloEscolar(archivoPadronDTO.getCatCicloEscolar());
					beneficiarioDispersion.setCatPeriodoEscolar(archivoPadronDTO.getCatPeriodoEscolar());
					beneficiarioDispersion.setCatNiveEducativo(
							new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue()));
					beneficiarioDispersion.setDispersion(new DispersionDTO(archivoPadronDTO.getIdDispersion()));
					beneficiarioDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario());
					beneficiarioDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),
							archivoPadronDTO.getCatCicloEscolar().getIdCicloEscolar()));
					beneficiarioDispersion.setFechaCreacion(new Date());
					beneficiarioDispersion.setEsComplementaria(false);
					beneficiarioDispersion.setIdBeneficiarioSinDispersion(null);

					beneficiariosConDispersion.add(beneficiarioDispersion);

				} else {
					// Si no esta vigente, se pasa a la lista de beneficiarios sin dispersion
					BeneficiarioSinDispersionDTO beneficiarioSinDispersion = new BeneficiarioSinDispersionDTO();
					beneficiarioSinDispersion.setCatCicloEscolar(archivoPadronDTO.getCatCicloEscolar());
					beneficiarioSinDispersion.setCatPeriodoEscolar(archivoPadronDTO.getCatPeriodoEscolar());
					beneficiarioSinDispersion.setCatNiveEducativo(
							new CatNivelEducativoDTO(beneficiario.getIdNivelEducativo().intValue()));
					beneficiarioSinDispersion.setDispersion(new DispersionDTO(archivoPadronDTO.getIdDispersion()));
					beneficiarioSinDispersion.setCurpBeneficiario(beneficiario.getCurpBeneficiario());
					beneficiarioSinDispersion.setCatMontoApoyo(obtenerCatMontoApoyo(beneficiario.getIdNivelEducativo(),
							archivoPadronDTO.getCatCicloEscolar().getIdCicloEscolar()));
					beneficiarioSinDispersion.setFechaCreacion(new Date());
					beneficiarioSinDispersion.setIdBeneficiarioDispersion(null);

					beneficiariosSinDispersion.add(beneficiarioSinDispersion);
				}
			}

			prepararBatchConDispersiones(conn, pstmtConDispersion, beneficiariosConDispersion);

			prepararBatchSinDispersiones(conn, pstmtConDispersion, beneficiariosSinDispersion);
			
			

			conn.commit();
			conn.setAutoCommit(true);

			// Se suman los totales.
			resultadoEjecucionDTO.setTotalRegistros(lstBeneficiarios.size());
			resultadoEjecucionDTO.setTotalCorrectos(resultadosActualizacion[0]); // Número de updates que actualizaron
																					// por lo menos 1 registro en la BD
			resultadoEjecucionDTO.setTotalIncorrectos(resultadosActualizacion[1]); // Número de updates que NO
																					// actualizaron registros en la BD,
																					// es decir, que la CURP no se
																					// encontró activa en el Programa

			ContadorProgresoSynchronized.incrementarAvance(resultadosActualizacion[0] + resultadosActualizacion[1]);

			// LOGGER.info("El hilo "+Thread.currentThread().getName()+ " terminó la
			// ejecución de "+lstBeneficiarios.size()+" registros. Correctos:
			// "+totalCorrectos+", Incorrectos: "+totalIncorrectos);
		} catch (Exception e) {
			LOGGER.error("Ocurrió un error en la ejecución del Thread:", e);
		} finally {
			// miDAO.closeResources();
		}
		return resultadoEjecucionDTO;
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

	private void prepararBatchConDispersiones(Connection conn, PreparedStatement pstmtConDispersion,
			List<BeneficiarioDispersionDTO> beneficiariosConDispersiones) throws SQLException {

		StringBuilder strInsert = new StringBuilder();
		strInsert.append("INSERT INTO beneficiario_dispersion");
		strInsert.append("(");
		strInsert.append("id_dispersion, ");
		strInsert.append("curp_beneficiario, ");
		strInsert.append("id_ciclo_escolar, ");
		strInsert.append("id_periodo_escolar, ");
		strInsert.append("id_nivel_educativo, ");
		// strInsert.append("id_motivo_no_dispersion, ");
		strInsert.append("id_monto_apoyo, ");
		strInsert.append("fecha_creacion, ");
		strInsert.append("es_complementaria, ");
		strInsert.append("id_beneficiario_dispersion ");
		strInsert.append(") ");
		strInsert.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

		pstmtConDispersion = conn.prepareStatement(strInsert.toString());

		for (int i = 0; i < beneficiariosConDispersiones.size(); i++) {
			pstmtConDispersion.setLong(1, beneficiariosConDispersiones.get(i).getDispersion().getIdDispersion());
			pstmtConDispersion.setString(2, beneficiariosConDispersiones.get(i).getCurpBeneficiario());
			pstmtConDispersion.setLong(3, beneficiariosConDispersiones.get(i).getCatCicloEscolar().getIdCicloEscolar());
			pstmtConDispersion.setLong(4,
					beneficiariosConDispersiones.get(i).getCatPeriodoEscolar().getIdPeriodoEscolar());
			pstmtConDispersion.setLong(5, beneficiariosConDispersiones.get(i).getCatNiveEducativo().getIdNivel());
			pstmtConDispersion.setLong(6, beneficiariosConDispersiones.get(i).getCatMontoApoyo().getIdMontoApoyo());
			pstmtConDispersion.setDate(7,
					new java.sql.Date(beneficiariosConDispersiones.get(i).getFechaCreacion().getTime()));
			pstmtConDispersion.setBoolean(8, false);
			pstmtConDispersion.addBatch();
		}
	}

	private void prepararBatchSinDispersiones(Connection conn, PreparedStatement pstmtSinDispersion,
			List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersiones) throws SQLException {
		StringBuilder strInsert = new StringBuilder();
		strInsert.append("INSERT INTO beneficiario_sin_dispersion");
		strInsert.append("(");
		strInsert.append("id_dispersion, ");
		strInsert.append("curp_beneficiario, ");
		strInsert.append("id_ciclo_escolar, ");
		strInsert.append("id_periodo_escolar, ");
		strInsert.append("id_nivel_educativo, ");
		strInsert.append("id_motivo_no_dispersion, ");
		strInsert.append("id_monto_apoyo, ");
		strInsert.append("fecha_creacion, ");
		strInsert.append("id_beneficiario_dispersion ");
		strInsert.append(") ");
		strInsert.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

		pstmtSinDispersion = conn.prepareStatement(strInsert.toString());

		for (int i = 0; i < beneficiariosSinDispersiones.size(); i++) {
			pstmtSinDispersion.setLong(1, beneficiariosSinDispersiones.get(i).getDispersion().getIdDispersion());
			pstmtSinDispersion.setString(2, beneficiariosSinDispersiones.get(i).getCurpBeneficiario());
			pstmtSinDispersion.setLong(3, beneficiariosSinDispersiones.get(i).getCatCicloEscolar().getIdCicloEscolar());
			pstmtSinDispersion.setLong(4,
					beneficiariosSinDispersiones.get(i).getCatPeriodoEscolar().getIdPeriodoEscolar());
			pstmtSinDispersion.setLong(5, beneficiariosSinDispersiones.get(i).getCatNiveEducativo().getIdNivel());
			pstmtSinDispersion.setLong(6, 1l);
			pstmtSinDispersion.setDouble(7, beneficiariosSinDispersiones.get(i).getCatMontoApoyo().getIdMontoApoyo());
			pstmtSinDispersion.setDate(8,
					new java.sql.Date(beneficiariosSinDispersiones.get(i).getFechaCreacion().getTime()));
			pstmtSinDispersion.addBatch();
		}

	}

}
