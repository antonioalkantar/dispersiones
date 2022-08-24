package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatCicloEscolarDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatEstatusDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatPeriodoEscolarDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatTipoDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.DispersionDTO;

public class DispersionDAO extends IBaseDAO<DispersionDTO, Integer> {

	private static final Logger LOGGER = LogManager.getLogger(DispersionDAO.class);

	@Override
	public DispersionDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DispersionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DispersionDTO> buscarPorCriterios(DispersionDTO e) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("SELECT ");
		strQuery.append("  d.id_dispersion as idDispersion,");
		strQuery.append("  d.id_ciclo_escolar as idCicloEscolar, ");
		strQuery.append("  d.id_periodo_escolar as idPeriodoEscolar, ");
		strQuery.append("  d.id_tipo_dispersion as idTipoDispersion, ");
		strQuery.append("  d.num_beneficiarios as numBeneficiarios,");
		strQuery.append("  d.fecha_ejecucion as fechaEjecucion, ");
		strQuery.append("  d.id_usuario_ejecucion as idUsuarioEjecucion, ");
		strQuery.append("  d.fecha_conclusion as fechaConclusion, ");
		strQuery.append("  d.id_estatus_dispersion as idEstatusDispersion, ");
		strQuery.append("  d.aplica_dispersion_porcentaje as aplicaDispersionPorcentaje,");
		strQuery.append("  d.aplica_dispersion_numero as aplicaDispersionNumero, ");
		strQuery.append("  d.no_aplica_dispersion_porcentaje as noAplicaDispersionPorcentaje, ");
		strQuery.append("  d.no_aplica_dispersion_numero as noAplicaDispersionNumero, ");
		strQuery.append("  d.fecha_descarga as fechaDescarga, ");
		strQuery.append("  d.permite_ejecucion as permiteEjecucion, ");
		strQuery.append("  d.ruta_archivo_preescolar as rutaArchivoPreescolar, ");
		strQuery.append("  d.ruta_archivo_primaria as rutaArchivoPrimaria, ");
		strQuery.append("  d.ruta_archivo_secundaria as rutaArchivoSecundaria, ");
		strQuery.append("  d.ruta_archivo_laboral as rutaArchivoLaboral ");
		strQuery.append("FROM mibecaparaempezar.dispersion d ");
		strQuery.append("WHERE ");
		strQuery.append("  id_estatus_dispersion = 1 ");

		// El orden es importante, ya que por ejemplo, se pudo pedir en un primer
		// archivo que se cargara la CURP X y en un segundo archivo pedir que se dé de
		// baja, si se procesan en desorden no va a dar el resultado esperado
		strQuery.append("ORDER BY d.fecha_ejecucion asc; ");

		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;

		List<DispersionDTO> lstDispersionesDTO = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
			while (rs.next()) {
				lstDispersionesDTO.add(mapearDispersionDTO(rs));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		return lstDispersionesDTO;
	}

	@Override
	public void actualizar(DispersionDTO e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void guardar(DispersionDTO e) {
		// TODO Auto-generated method stub
	}

	public DispersionDTO obtenerUltimaDispersionPorFechaConclusion(DispersionDTO dispersion) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("SELECT ");
		strQuery.append("  d.id_dispersion as idDispersion,");
		strQuery.append("  d.id_ciclo_escolar as idCicloEscolar, ");
		strQuery.append("  d.id_periodo_escolar as idPeriodoEscolar, ");
		strQuery.append("  d.id_tipo_dispersion as idTipoDispersion, ");
		strQuery.append("  d.num_beneficiarios as numBeneficiarios,");
		strQuery.append("  d.fecha_ejecucion as fechaEjecucion, ");
		strQuery.append("  d.id_usuario_ejecucion as idUsuarioEjecucion, ");
		strQuery.append("  d.fecha_conclusion as fechaConclusion, ");
		strQuery.append("  d.id_estatus_dispersion as idEstatusDispersion, ");
		strQuery.append("  d.aplica_dispersion_porcentaje as aplicaDispersionPorcentaje,");
		strQuery.append("  d.aplica_dispersion_numero as aplicaDispersionNumero, ");
		strQuery.append("  d.no_aplica_dispersion_porcentaje as noAplicaDispersionPorcentaje, ");
		strQuery.append("  d.no_aplica_dispersion_numero as noAplicaDispersionNumero, ");
		strQuery.append("  d.fecha_descarga as fechaDescarga, ");
		strQuery.append("  d.permite_ejecucion as permiteEjecucion, ");
		strQuery.append("  d.ruta_archivo_preescolar as rutaArchivoPreescolar, ");
		strQuery.append("  d.ruta_archivo_primaria as rutaArchivoPrimaria, ");
		strQuery.append("  d.ruta_archivo_secundaria as rutaArchivoSecundaria, ");
		strQuery.append("  d.ruta_archivo_laboral as rutaArchivoLaboral ");
		strQuery.append("FROM mibecaparaempezar.dispersion d ");
		strQuery.append(" WHERE id_ciclo_escolar = ").append(dispersion.getCatCicloEscolar().getIdCicloEscolar());
//		strQuery.append(" AND id_periodo_escolar = ").append(dispersion.getCatPeriodoEscolar().getIdPeriodoEscolar());
		strQuery.append(" AND fecha_conclusion is not null ");
		strQuery.append(" order by fecha_conclusion desc limit 1; ");

		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;

		List<DispersionDTO> lstDispersionesDTO = new ArrayList<>();
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(strQuery.toString());
			while (rs.next()) {
				lstDispersionesDTO.add(mapearDispersionDTO(rs));
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al consultar dispersiones con el query [" + strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(rs, stm, conn);
		}
		if(lstDispersionesDTO.size() > 0) {
			return lstDispersionesDTO.get(0);
		} else {
			return null;
		}
	}

	public void actualizarEstatus(long idDispersion, long idEstatusDispersion) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("UPDATE mibecaparaempezar.dispersion ");
		strQuery.append("SET ");
		strQuery.append("  id_estatus_dispersion = ").append(idEstatusDispersion);
		strQuery.append(" WHERE ");
		strQuery.append("  id_dispersion = ").append(idDispersion);

		Connection conn = null;
		Statement stm = null;
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			int registrosAfectados = stm.executeUpdate(strQuery.toString());
			if (registrosAfectados < 1) {
				throw new IllegalArgumentException("El idDispersion " + idDispersion + " no se actualizó su estatus ["
						+ strQuery.toString() + "]");
			}
		} catch (SQLException e1) {
			LOGGER.error(
					"Ocurrió un error al actualizar un archivo de padrón con el DML [" + strQuery.toString() + "]:",
					e1);
		} finally {
			PostgresDatasource.getInstance().close(null, stm, conn);
		}
	}

	public void actualizarPermiteEjecucion(DispersionDTO dispersion) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("UPDATE mibecaparaempezar.dispersion ");
		strQuery.append("SET ");
		strQuery.append("  permite_ejecucion = false ");
		strQuery.append(" WHERE ");
		strQuery.append("  id_dispersion in (");
		strQuery.append("  SELECT id_dispersion FROM mibecaparaempezar.dispersion d ");
		strQuery.append("	WHERE ");
		strQuery.append("  	id_ciclo_escolar = ? ");
		strQuery.append("	AND ");
		strQuery.append("	id_periodo_escolar = ? ");
		strQuery.append("	AND ");
		strQuery.append("	fecha_conclusion IS NOT NULL);");

		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			pstm = conn.prepareStatement(strQuery.toString());
			pstm.setLong(1, dispersion.getCatCicloEscolar().getIdCicloEscolar());
			pstm.setLong(2, dispersion.getCatPeriodoEscolar().getIdPeriodoEscolar());
			int registrosAfectados = pstm.executeUpdate();
			if (registrosAfectados < 1) {
				throw new IllegalArgumentException("El idDispersion " + dispersion.getIdDispersion()
						+ " no actualizó la bandera permiteEjecucion [" + strQuery.toString() + "]");
			}
		} catch (SQLException e1) {
			LOGGER.error(
					"Ocurrió un error al actualizar un archivo de padrón con el DML [" + strQuery.toString() + "]:",
					e1);
		} finally {
			PostgresDatasource.getInstance().close(null, pstm, conn);
		}
	}

	public void actualizarFechaConcluido(long idDispersion, Date fecha) {
		StringBuilder strQuery = new StringBuilder();

		strQuery.append("UPDATE mibecaparaempezar.dispersion ");
		strQuery.append("SET ");
		strQuery.append("  fecha_conclusion = ? ");
		strQuery.append(" WHERE ");
		strQuery.append("  id_dispersion = ?");

		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			pstm = conn.prepareStatement(strQuery.toString());
			pstm.setTimestamp(1, new Timestamp(fecha.getTime()));
			pstm.setLong(2, idDispersion);
			int registrosAfectados = pstm.executeUpdate();
			if (registrosAfectados < 1) {
				throw new IllegalArgumentException("El idDispersion " + idDispersion + " no se actualizó su estatus ["
						+ strQuery.toString() + "]");
			}
		} catch (SQLException e1) {
			LOGGER.error(
					"Ocurrió un error al actualizar un archivo de padrón con el DML [" + strQuery.toString() + "]:",
					e1);
		} finally {
			PostgresDatasource.getInstance().close(null, pstm, conn);
		}
	}

	public void actualizarContadores(DispersionDTO dispersionDTO, double porcentajeDispersados, int dispersados,
			double porcentajeNoDispersados, int noDispersados) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE mibecaparaempezar.dispersion ");
		strQuery.append("SET ");
		strQuery.append("  aplica_dispersion_porcentaje = ").append(porcentajeDispersados).append(",");
		strQuery.append("  aplica_dispersion_numero = ").append(dispersados).append(",");
		strQuery.append("  no_aplica_dispersion_porcentaje = ").append(porcentajeNoDispersados).append(",");
		strQuery.append("  no_aplica_dispersion_numero = ").append(noDispersados);
		strQuery.append(" WHERE ");
		strQuery.append("  id_dispersion = ").append(dispersionDTO.getIdDispersion());
		Connection conn = null;
		Statement stm = null;
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			stm = conn.createStatement();
			int registrosAfectados = stm.executeUpdate(strQuery.toString());
			if (registrosAfectados < 1) {
				throw new IllegalArgumentException("Para el idDispersion" + dispersionDTO.getIdDispersion()
						+ " no se actualizaron sus contadores [" + strQuery.toString() + "]");
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al actualizar los contadores de una dispersión con el DML ["
					+ strQuery.toString() + "]:", e1);
		} finally {
			PostgresDatasource.getInstance().close(null, stm, conn);
		}
	}

	public void actualizarArchivos(DispersionDTO dispersionDTO, String reportePreescolar, String reportePrimaria,
			String reporteSecundaria, String reporteLaboral) {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE mibecaparaempezar.dispersion ");
		strQuery.append("SET ");
		strQuery.append("  ruta_archivo_preescolar = ?,");
		strQuery.append("  ruta_archivo_primaria = ?,");
		strQuery.append("  ruta_archivo_secundaria = ?,");
		strQuery.append("  ruta_archivo_laboral = ? ");
		strQuery.append(" WHERE ");
		strQuery.append("  id_dispersion = ? ");
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = PostgresDatasource.getInstance().getConnection();
			pstm = conn.prepareStatement(strQuery.toString());
			pstm.setString(1, reportePreescolar);
			pstm.setString(2, reportePrimaria);
			pstm.setString(3, reporteSecundaria);
			pstm.setString(4, reporteLaboral);
			pstm.setLong(5, dispersionDTO.getIdDispersion());
			int registrosAfectados = pstm.executeUpdate();
			if (registrosAfectados < 1) {
				throw new IllegalArgumentException("Para el idDispersion" + dispersionDTO.getIdDispersion()
						+ " no se actualizaron nombres de los archivos correctamente.");
			}
		} catch (SQLException e1) {
			LOGGER.error("Ocurrió un error al actualizar los nombres de los archivo de la dispersión: "
					+ dispersionDTO.getIdDispersion().toString(), e1);
		} finally {
			PostgresDatasource.getInstance().close(null, pstm, conn);
		}
	}

	private DispersionDTO mapearDispersionDTO(ResultSet rs) throws SQLException {
		DispersionDTO dispersion = new DispersionDTO();
		dispersion.setIdDispersion(rs.getLong("idDispersion"));
		dispersion.setCatCicloEscolar(new CatCicloEscolarDTO(rs.getLong("idCicloEscolar")));
		dispersion.setCatPeriodoEscolar(new CatPeriodoEscolarDTO(rs.getLong("idPeriodoEscolar")));
		dispersion.setCatTipoDispersion(new CatTipoDispersionDTO(rs.getLong("idTipoDispersion")));
		dispersion.setNumBeneficiarios(rs.getLong("numBeneficiarios"));
		dispersion.setFechaEjecucion(rs.getDate("fechaEjecucion"));
		dispersion.setIdUsuarioEjecucion(rs.getLong("idUsuarioEjecucion"));
		dispersion.setFechaConclusion(rs.getDate("fechaEjecucion"));
		dispersion.setCatEstatusDispersion(new CatEstatusDispersionDTO(rs.getLong("idEstatusDispersion")));
		dispersion.setAplicaDispersionPorcentaje(rs.getDouble("aplicaDispersionPorcentaje"));
		dispersion.setAplicaDispersionNumero(rs.getLong("aplicaDispersionNumero"));
		dispersion.setNoAplicaDispersionPorcentaje(rs.getDouble("noAplicaDispersionPorcentaje"));
		dispersion.setNoAplicaDispersionNumero(rs.getLong("noAplicaDispersionNumero"));
		dispersion.setFechaDescarga(rs.getDate("fechaDescarga"));
		dispersion.setPermiteEjecucion(rs.getBoolean("permiteEjecucion"));
		dispersion.setRutaArchivoPreescolar(rs.getString("rutaArchivoPreescolar"));
		dispersion.setRutaArchivoPrimaria(rs.getString("rutaArchivoPrimaria"));
		dispersion.setRutaArchivoSecundaria(rs.getString("rutaArchivoSecundaria"));
		dispersion.setRutaArchivoSecundaria(rs.getString("rutaArchivoLaboral"));
		return dispersion;
	}

}
