package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.client.MciResponse;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BitacoraDTO;

public class BitacoraDAO extends IBaseDAO<BeneficiarioDispersionDTO, Integer> {

	private static final Logger LOGGER = LogManager.getLogger(BitacoraDAO.class);

	@Override
	public BeneficiarioDispersionDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeneficiarioDispersionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(BeneficiarioDispersionDTO e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void guardar(BeneficiarioDispersionDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<BeneficiarioDispersionDTO> buscarPorCriterios(BeneficiarioDispersionDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] guardar(Connection conn, List<BitacoraDTO> lstBitacora) throws SQLException {

		int[] resultado = null;
		PreparedStatement pstmtBitacora = null;

		StringBuilder strInsert = new StringBuilder();
		strInsert.append("INSERT INTO mibecaparaempezar.bit_cambios_beneficiarios");
		strInsert.append("(");
		strInsert.append("id_beneficiario, ");
		strInsert.append("id_solicitud, ");
		strInsert.append("id_dispersion, ");
		strInsert.append("cct_anterior, ");
		strInsert.append("nombre_cct_anterior, ");
		strInsert.append("calle_cct_anterior, ");
		strInsert.append("colonia_cct_anterior, ");
		strInsert.append("codigo_postal_cct_anterior, ");
		strInsert.append("id_alcaldia_cct_anterior, ");
		strInsert.append("id_turno_anterior, ");
		strInsert.append("id_nivel_educativo_anterior, ");
		strInsert.append("grado_escolar_anterior, ");
		strInsert.append("estatus_beneficiario_anterior, ");
		strInsert.append("fecha_registro ");
		strInsert.append(") ");
		strInsert.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

		pstmtBitacora = conn.prepareStatement(strInsert.toString());

		for (int i = 0; i < lstBitacora.size(); i++) {
			// IdBeneficiario
			pstmtBitacora.setLong(1, lstBitacora.get(i).getIdBeneficiario());

			// IdSolicitud
			pstmtBitacora.setLong(2, lstBitacora.get(i).getIdSolicitud());

			// IdDispersion
			pstmtBitacora.setLong(3, lstBitacora.get(i).getIdDispersion());

			// CctAnterior
			pstmtBitacora.setString(4, lstBitacora.get(i).getActualizaCct().booleanValue() ? lstBitacora.get(i).getCctAnterior() : null);

			// NombreCctAnterior
			pstmtBitacora.setString(5, lstBitacora.get(i).getActualizaNombre().booleanValue() ? lstBitacora.get(i).getNombreCctAnterior() : null);

			// CalleCctAnterior
			pstmtBitacora.setString(6, lstBitacora.get(i).getActualizaCalle().booleanValue() ? lstBitacora.get(i).getCalleCctAnterior() : null);

			// ColoniaCctAnterior
			pstmtBitacora.setString(7, lstBitacora.get(i).getActualizaColonia().booleanValue() ? lstBitacora.get(i).getColoniaCctAnterior() : null);

			// CodigoPostalCctAnterior
			pstmtBitacora.setString(8, lstBitacora.get(i).getActualizaCodigoPostal().booleanValue() ? lstBitacora.get(i).getCodigoPostalCctAnterior() : null);

			// IdAlcaldiaCctAnterior
			if(lstBitacora.get(i).getActualizaAlcaldia().booleanValue()) {
				pstmtBitacora.setLong(9, lstBitacora.get(i).getIdAlcaldiaCctAnterior());
			} else {
				pstmtBitacora.setNull(9, Types.BIGINT);
			}

			// IdTurnoAnterior
			pstmtBitacora.setString(10, lstBitacora.get(i).getActualizaTurno().booleanValue() ? lstBitacora.get(i).getIdTurnoAnterior() : null);

			// IdNivelEducativoAnterior
			if(lstBitacora.get(i).getActualizaNivelEducativo().booleanValue()) {
				pstmtBitacora.setLong(11, lstBitacora.get(i).getIdNivelEducativoAnterior());
			} else {
				pstmtBitacora.setNull(11, Types.BIGINT);
			}

			// GradoEscolarAnterior
			pstmtBitacora.setString(12, lstBitacora.get(i).getActualizaGradoEscolar().booleanValue() ? lstBitacora.get(i).getGradoEscolarAnterior() : null);
			
			//EstatusBeneficiario
			pstmtBitacora.setString(13, lstBitacora.get(i).getActualizaEstatus().booleanValue() ? lstBitacora.get(i).getEstatusBeneficiarioAnterior() : null);

			// FechaRegistro
			pstmtBitacora.setTimestamp(14, new Timestamp(lstBitacora.get(i).getFechaRegistro().getTime()));

			pstmtBitacora.addBatch();
		}

		resultado = pstmtBitacora.executeBatch();
//		if(pstmtBitacora != null){
//    		try { pstmtBitacora.close(); } catch (Exception e) { LOGGER.warn("No se pudo cerrar un statement"); } 
//    	}
		return resultado;
	}

	public void actualizarDatosSolicitud(List<BitacoraDTO> lstBitacora) {

		for (BitacoraDTO bitacora : lstBitacora) {
			StringBuilder strQuery = new StringBuilder();
			String strQueryFinal = "";
			int contador = 0;
			
			strQuery.append("UPDATE mibecaparaempezar.solicitud ");
			strQuery.append("SET ");
			if (bitacora.getActualizaNivelEducativo().booleanValue()) {
				strQuery.append("id_nivel_educativo = ").append(bitacora.getIdNivelEducativoActualizado()).append(", ");
				++contador; // 1
			}
			if (bitacora.getActualizaCct().booleanValue()) {
				strQuery.append("cct = '").append(bitacora.getCctActualizado().replaceAll("'", "")).append("', ");
				++contador; // 2
			}
			if (bitacora.getActualizaTurno().booleanValue()) {
				strQuery.append("turno = '").append(bitacora.getIdTurnoActualizado().replaceAll("'", "")).append("', ");
				++contador; // 3
			}
			if (bitacora.getActualizaGradoEscolar().booleanValue()) {
				strQuery.append("grado_escolar = '").append(bitacora.getGradoEscolarActualizado().replaceAll("'", "")).append("', ");
				++contador; // 4
			}
			if (bitacora.getActualizaNombre().booleanValue()) {
				strQuery.append("nombre = '").append(bitacora.getNombreCctActualizado().replaceAll("'", "")).append("', ");
				++contador; // 5
			}
			if (bitacora.getActualizaCalle().booleanValue()) {
				strQuery.append("calle = '").append(bitacora.getCalleCctActualizado().replaceAll("'", "")).append("', ");
				++contador; // 6
			}
			if (bitacora.getActualizaColonia().booleanValue()) {
				strQuery.append("colonia = '").append(bitacora.getColoniaCctActualizado().replaceAll("'", "")).append("', ");
				++contador; // 7
			}
			if (bitacora.getActualizaAlcaldia().booleanValue()) {
				strQuery.append("id_alcaldia = ").append(bitacora.getIdAlcaldiaCctActualizado()).append(", ");
				++contador; // 8
			}
			if (bitacora.getActualizaCodigoPostal().booleanValue()) {
				strQuery.append("codigopostal = '").append(bitacora.getCodigoPostalCctActualizado().replaceAll("'", "")).append("', ");
				++contador; // 9
			}
			if (bitacora.getActualizaEstatus().booleanValue()) {
				strQuery.append("id_estatus_beneficiario = 1 ").append(", ");
				++contador; // 10
			}
			strQuery.append("WHERE ");
			strQuery.append(" id_solicitud = ").append(bitacora.getIdSolicitud());
			
			if(contador == 0) {
				continue;
			} else {
				strQueryFinal = strQuery.toString().replace(", WHERE"," WHERE");
			}

			Connection conn = null;
			Statement stm = null;
			try {
				conn = PostgresDatasource.getInstance().getConnection();
				stm = conn.createStatement();
				int registrosAfectados = stm.executeUpdate(strQueryFinal);
				if (registrosAfectados < 1) {
					throw new IllegalArgumentException("El idSolicitud " + bitacora.getIdSolicitud() + " no se actualizo correctamente [" + strQueryFinal + "]");
				}
			} catch (SQLException e1) {
				LOGGER.error(
						"Ocurrio un error al actualizar la solicitud con el DML [" + strQueryFinal + "]:",
						e1);
			} finally {
				PostgresDatasource.getInstance().close(null, stm, conn);
			}
		}
	}

}
