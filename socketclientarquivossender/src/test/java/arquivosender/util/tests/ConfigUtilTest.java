package arquivosender.util.tests;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import arquivosender.util.ConfigUtil;

/**
 * Testes unitários da Classe ConfigUtil
 * @author Brunno
 * @since 15/09/2017
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
