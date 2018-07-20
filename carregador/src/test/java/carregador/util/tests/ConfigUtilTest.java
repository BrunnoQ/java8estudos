package carregador.util.tests;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import carregador.util.ConfigUtil;


/**
 * Testes unitários da Classe ConfigUtil
 * @author Brunno
 * @since 25/10/2017
 * @version 1.0
 */
public class ConfigUtilTest {
	@Test
	public void testarcarregarPropiedades(){
		ConfigUtil.carregarPropiedades();
		assertNotNull(ConfigUtil.propriedade);
	}
	
	@Test
	public void testarGetProperty(){
		assertNotNull(ConfigUtil.getProperty("oracle.username"));
	}
	
}
