package arquivosender.util.tests;

import static org.junit.Assert.fail;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import arquivosender.util.ConexaoBancoDados;

/**
 * @author Brunno
 * Testes unitários da Classe ConexaoBancoDados
 * @since 15/09/2017
 * @version 1.0
 */
public class ConexaoBancoDadosTest {
	
	@Test
	public void testarObterConexao(){
		
		ConexaoBancoDados conexaoBancoDados = new ConexaoBancoDados();
		
		try (Connection conexao = conexaoBancoDados.obterConexao()){
			assertNotNull(conexao);
		} catch (ClassNotFoundException | SQLException | CryptographyException e) {
			// TODO Auto-generated catch block
			fail(e.toString());
		}	
	}
}
