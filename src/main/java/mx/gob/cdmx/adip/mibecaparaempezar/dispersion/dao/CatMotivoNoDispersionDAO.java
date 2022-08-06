package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatMotivoNoDispersionDTO;

public class CatMotivoNoDispersionDAO extends IBaseDAO<CatMotivoNoDispersionDTO, Integer>{

	private static final Logger LOGGER = LogManager.getLogger(CatMotivoNoDispersionDAO.class);
	
	@Override
	public CatMotivoNoDispersionDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatMotivoNoDispersionDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void actualizar(CatMotivoNoDispersionDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void guardar(CatMotivoNoDispersionDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CatMotivoNoDispersionDTO> buscarPorCriterios(CatMotivoNoDispersionDTO e) {
		// TODO Auto-generated method stub
		return null;
	}
}
