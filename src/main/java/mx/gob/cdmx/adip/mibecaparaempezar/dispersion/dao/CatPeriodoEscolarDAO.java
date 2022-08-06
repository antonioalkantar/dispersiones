package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.CatPeriodoEscolarDTO;

public class CatPeriodoEscolarDAO extends IBaseDAO<CatPeriodoEscolarDTO, Integer>{

	private static final Logger LOGGER = LogManager.getLogger(CatPeriodoEscolarDAO.class);
	
	@Override
	public CatPeriodoEscolarDTO buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CatPeriodoEscolarDTO> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void actualizar(CatPeriodoEscolarDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void guardar(CatPeriodoEscolarDTO e) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CatPeriodoEscolarDTO> buscarPorCriterios(CatPeriodoEscolarDTO e) {
		// TODO Auto-generated method stub
		return null;
	}
}
