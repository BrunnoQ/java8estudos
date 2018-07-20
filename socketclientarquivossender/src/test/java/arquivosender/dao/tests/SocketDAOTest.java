package arquivosender.dao.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import arquivosender.enuns.TipoProcessoEnum;
import arquivosender.sinais.dao.SocketDAO;
import arquivosender.util.ConexaoBancoDados;

/**
 * Testes unitários da Classe SocketDAO
 * @author Brunno
 * @since 15/09/2017
 * @version 1.0
 */
public class SocketDAOTest {

	@Test
	public void testarObterConfiguracoesSocket() {

		try (Connection conexao = new ConexaoBancoDados().obterConexao()) {

			SocketDAO sockeDAO = new SocketDAO(conexao);
			
			assertNotNull(sockeDAO.obterConfiguracoesSocket("IP_AQUI", "socketr",
					TipoProcessoEnum.SERVER.getTipoProcesso()));

		} catch (ClassNotFoundException | SQLException | CryptographyException | DaoException e) {
			// TODO Auto-generated catch block
			fail(e.toString());
		}
	}
}
