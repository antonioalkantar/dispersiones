package mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.cdmx.adip.mibecaparaempezar.dispersion.dto.BeneficiarioDispersionDTO;

public class BeneficiarioDispersionDAO extends IBaseDAO<BeneficiarioDispersionDTO, Integer>{

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
}
