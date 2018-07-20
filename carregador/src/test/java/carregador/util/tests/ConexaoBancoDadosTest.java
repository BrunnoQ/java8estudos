package carregador.util.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import carregador.util.ConexaoBancoDados;

/**
 * @author Brunno
 * Testes unitários da Classe ConexaoBancoDados
 * @since 25/19/2017
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
