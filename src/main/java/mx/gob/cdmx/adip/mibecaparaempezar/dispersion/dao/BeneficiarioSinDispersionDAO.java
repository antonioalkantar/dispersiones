package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;
import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioSinDispersionDTO;

public class BeneficiarioSinDispersionDAO extends IBaseDAO<BeneficiarioSinDispersionDTO, Integer>{

	private static final Logger LOGGER = LogManager.getLogger(BeneficiarioSinDispersionDAO.class);

	@Override
	public BeneficiarioSinDispersionDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeneficiarioSinDispersionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeneficiarioSinDispersionDTO> buscarPorCriterios(BeneficiarioSinDispersionDTO e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualizar(BeneficiarioSinDispersionDTO e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar(BeneficiarioSinDispersionDTO e) {
		// TODO Auto-generated method stub
		
	}
	
	public int[] guardarLista(Connection conn, List<BeneficiarioSinDispersionDTO> beneficiariosSinDispersiones)
			throws SQLException {
		int[] resultado = null;
		PreparedStatement pstmtSinDispersion = null;

		StringBuilder strInsert = new StringBuilder();
		strInsert.append("INSERT INTO mibecaparaempezar.beneficiario_sin_dispersion");
		strInsert.append("(");
		strInsert.append("id_dispersion, ");
		strInsert.append("curp_beneficiario, ");
		strInsert.append("id_ciclo_escolar, ");
		strInsert.append("id_periodo_escolar, ");
		strInsert.append("id_nivel_educativo, ");
		strInsert.append("id_motivo_no_dispersion, ");
		strInsert.append("id_monto_apoyo, ");
		strInsert.append("fecha_creacion ");
//		strInsert.append("id_beneficiario_dispersion ");
		strInsert.append(") ");
		strInsert.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");

		pstmtSinDispersion = conn.prepareStatement(strInsert.toString());

		for (int i = 0; i < beneficiariosSinDispersiones.size(); i++) {
			// Dispersion
			pstmtSinDispersion.setLong(1, beneficiariosSinDispersiones.get(i).getDispersion().getIdDispersion());
			
			// CURP
			pstmtSinDispersion.setString(2, beneficiariosSinDispersiones.get(i).getCurpBeneficiario());
			
			// Ciclo Escolar
			pstmtSinDispersion.setLong(3, beneficiariosSinDispersiones.get(i).getCatCicloEscolar().getIdCicloEscolar());
			
			// Periodo Escolar
			pstmtSinDispersion.setLong(4,
					beneficiariosSinDispersiones.get(i).getCatPeriodoEscolar().getIdPeriodoEscolar());
			
			// Nivel Educativo
			pstmtSinDispersion.setLong(5, beneficiariosSinDispersiones.get(i).getCatNiveEducativo().getIdNivel());
			
			// Monto No Dispersion
			pstmtSinDispersion.setLong(6, 1l);
			
			// Monto Apoyo
			pstmtSinDispersion.setDouble(7, beneficiariosSinDispersiones.get(i).getCatMontoApoyo().getIdMontoApoyo());
			
			// Fecha Creacion
			pstmtSinDispersion.setTimestamp(8, new Timestamp(beneficiariosSinDispersiones.get(i).getFechaCreacion().getTime()));
			
			pstmtSinDispersion.addBatch();
		}

		resultado = pstmtSinDispersion.executeBatch();
		
		if(pstmtSinDispersion != null){
    		try { pstmtSinDispersion.close(); } catch (Exception e) { LOGGER.warn("No se pudo cerrar un statement"); } 
    	}
		
		return resultado;
	}
	
}