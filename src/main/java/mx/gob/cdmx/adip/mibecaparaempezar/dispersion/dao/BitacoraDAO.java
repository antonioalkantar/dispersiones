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
		strInsert.append("fecha_registro ");
		strInsert.append(") ");
		strInsert.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

		pstmtBitacora = conn.prepareStatement(strInsert.toString());

		for (int i = 0; i < lstBitacora.size(); i++) {
			// IdBeneficiario
			pstmtBitacora.setLong(1, lstBitacora.get(i).getIdBeneficiario());

			// IdSolicitud
			pstmtBitacora.setLong(2, lstBitacora.get(i).getIdSolicitud());

			// IdDispersion
			pstmtBitacora.setLong(3, lstBitacora.get(i).getIdDispersion());

			// CctAnterior
			pstmtBitacora.setString(4, lstBitacora.get(i).getActualizaCct() ? lstBitacora.get(i).getCctAnterior() : null);

			// NombreCctAnterior
			pstmtBitacora.setString(5, lstBitacora.get(i).getActualizaNombre() ? lstBitacora.get(i).getNombreCctAnterior() : null);

			// CalleCctAnterior
			pstmtBitacora.setString(6, lstBitacora.get(i).getActualizaCalle() ? lstBitacora.get(i).getCalleCctAnterior() : null);

			// ColoniaCctAnterior
			pstmtBitacora.setString(7, lstBitacora.get(i).getActualizaColonia() ? lstBitacora.get(i).getColoniaCctAnterior() : null);

			// CodigoPostalCctAnterior
			pstmtBitacora.setString(8, lstBitacora.get(i).getActualizaCodigoPostal() ? lstBitacora.get(i).getCodigoPostalCctAnterior() : null);

			// IdAlcaldiaCctAnterior
			if(lstBitacora.get(i).getActualizaAlcaldia()) {
				pstmtBitacora.setLong(9, lstBitacora.get(i).getIdAlcaldiaCctAnterior());
			} else {
				pstmtBitacora.setNull(9, Types.BIGINT);
			}

			// IdTurnoAnterior
			pstmtBitacora.setString(10, lstBitacora.get(i).getActualizaTurno() ? lstBitacora.get(i).getIdTurnoAnterior() : null);

			// IdNivelEducativoAnterior
			if(lstBitacora.get(i).getActualizaNivelEducativo()) {
				pstmtBitacora.setLong(11, lstBitacora.get(i).getIdNivelEducativoAnterior());
			} else {
				pstmtBitacora.setNull(11, Types.BIGINT);
			}

			// GradoEscolarAnterior
			pstmtBitacora.setString(12, lstBitacora.get(i).getActualizaGradoEscolar() ? lstBitacora.get(i).getGradoEscolarAnterior() : null);

			// FechaRegistro
			pstmtBitacora.setTimestamp(13, new Timestamp(lstBitacora.get(i).getFechaRegistro().getTime()));

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
			if (bitacora.getActualizaNivelEducativo()) {
				strQuery.append("id_nivel_educativo = ").append(bitacora.getIdNivelEducativoAnterior()).append(", ");
				++contador; // 1
			}
			if (bitacora.getActualizaCct()) {
				strQuery.append("cct = '").append(bitacora.getCctAnterior()).append("', ");
				++contador; // 2
			}
			if (bitacora.getActualizaTurno()) {
				strQuery.append("turno = '").append(bitacora.getIdTurnoAnterior()).append("', ");
				++contador; // 3
			}
			if (bitacora.getActualizaGradoEscolar()) {
				strQuery.append("grado_escolar = '").append(bitacora.getGradoEscolarAnterior()).append("', ");
				++contador; // 4
			}
			if (bitacora.getActualizaNombre()) {
				strQuery.append("nombre = '").append(bitacora.getNombreCctAnterior()).append("', ");
				++contador; // 5
			}
			if (bitacora.getActualizaCalle()) {
				strQuery.append("calle = '").append(bitacora.getCalleCctAnterior()).append("', ");
				++contador; // 6
			}
			if (bitacora.getActualizaColonia()) {
				strQuery.append("colonia = '").append(bitacora.getColoniaCctAnterior()).append("', ");
				++contador; // 7
			}
			if (bitacora.getActualizaAlcaldia()) {
				strQuery.append("id_alcaldia = ").append(bitacora.getIdAlcaldiaCctAnterior()).append(", ");
				++contador; // 8
			}
			if (bitacora.getActualizaCodigoPostal()) {
				strQuery.append("codigopostal = '").append(bitacora.getCodigoPostalCctAnterior()).append("', ");
				++contador; // 9
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
					throw new IllegalArgumentException("El idSolicitud " + bitacora.getIdSolicitud() + " no se actualizó correctamente [" + strQueryFinal + "]");
				}
			} catch (SQLException e1) {
				LOGGER.error(
						"Ocurrió un error al actualizar un archivo de padrón con el DML [" + strQueryFinal + "]:",
						e1);
			} finally {
				PostgresDatasource.getInstance().close(null, stm, conn);
			}
		}
	}

}