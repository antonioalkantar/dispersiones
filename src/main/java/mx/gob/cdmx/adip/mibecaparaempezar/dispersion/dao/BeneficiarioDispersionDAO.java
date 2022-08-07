package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.db.PostgresDatasource;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;

public class BeneficiarioDispersionDAO extends IBaseDAO<BeneficiarioDispersionDTO, Integer> {

	private static final Logger LOGGER = LogManager.getLogger(BeneficiarioDispersionDAO.class);

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

	public int[] guardarLista(Connection conn, List<BeneficiarioDispersionDTO> beneficiariosConDispersiones)
			throws SQLException {

		int[] resultado= null;
		PreparedStatement pstmtConDispersion = null;

		StringBuilder strInsert = new StringBuilder();
		strInsert.append("INSERT INTO mibecaparaempezar.beneficiario_dispersion");
		strInsert.append("(");
		strInsert.append("id_dispersion, ");
		strInsert.append("curp_beneficiario, ");
		strInsert.append("id_ciclo_escolar, ");
		strInsert.append("id_periodo_escolar, ");
		strInsert.append("id_nivel_educativo, ");
		strInsert.append("id_monto_apoyo, ");
		strInsert.append("fecha_creacion, ");
		strInsert.append("es_complementaria ");
//		strInsert.append("id_beneficiario_dispersion ");
		strInsert.append(") ");
		strInsert.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");

		pstmtConDispersion = conn.prepareStatement(strInsert.toString());

		for (int i = 0; i < beneficiariosConDispersiones.size(); i++) {
			// Dispersion
			pstmtConDispersion.setLong(1, beneficiariosConDispersiones.get(i).getDispersion().getIdDispersion());
			
			// CURP
			pstmtConDispersion.setString(2, beneficiariosConDispersiones.get(i).getCurpBeneficiario());
			
			// Ciclo Escolar
			pstmtConDispersion.setLong(3, beneficiariosConDispersiones.get(i).getCatCicloEscolar().getIdCicloEscolar());
			
			// Periodo Escolar
			pstmtConDispersion.setLong(4,
					beneficiariosConDispersiones.get(i).getCatPeriodoEscolar().getIdPeriodoEscolar());
			
			// Nivel Educativo
			pstmtConDispersion.setLong(5, beneficiariosConDispersiones.get(i).getCatNiveEducativo().getIdNivel());
			
			// Monto Apoyo
			pstmtConDispersion.setLong(6, beneficiariosConDispersiones.get(i).getCatMontoApoyo().getIdMontoApoyo());
			
			// Fecha Creacion
			pstmtConDispersion.setTimestamp(7, new Timestamp(beneficiariosConDispersiones.get(i).getFechaCreacion().getTime()));
			
			// Es Complementaria
			pstmtConDispersion.setBoolean(8, false);
			
			pstmtConDispersion.addBatch();
		}

		resultado = pstmtConDispersion.executeBatch();
		
		if(pstmtConDispersion != null){
    		try { pstmtConDispersion.close(); } catch (Exception e) { LOGGER.warn("No se pudo cerrar un statement"); } 
    	}
		
		return resultado;
	}

}
