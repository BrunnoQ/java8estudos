package carregador.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia as conexoes com o banco de dados.
 * 
 * @since 11/10/2017
 * @author Brunno Silva
 * @version 1.0
 */
public class ConexaoBancoDados {

	private static Connection CONEXAO;
	private static ConexaoBancoDados INSTANCE;
	private String driver;
	private String connectionString;
	private String user;
	private String pass;
	private static final LogManager LOGGER = LogManager.getLog(ConexaoBancoDados.class);

	private void configurar() throws CryptographyException {
		connectionString = ConfigUtil.getProperty("oracle.url");
		user = ConfigUtil.getProperty("oracle.username");
		pass = Cryptography.decrypt(ConfigUtil.getProperty("oracle.password"));
		driver = ConfigUtil.getProperty("oracle.driver");
	}
	
	/**
	 * Obtem a conexao com o banco de dados do.
	 * 
	 * @return Conexão com o banco de dados
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws CryptographyException 
	 */
	public Connection obterConexao() throws SQLException, ClassNotFoundException, CryptographyException {

		LOGGER.info("Obtendo conexao com o banco de dados");

		if (CONEXAO == null || CONEXAO.isClosed()) {
			configurar();
			Class.forName(driver);
			CONEXAO = DriverManager.getConnection(connectionString, user, pass);
			LOGGER.info("Abriu nova conexao!");
		}

		return CONEXAO;

	}

	/**
	 * 
	 * @return Retorna uma instância da Classe.
	 */
	public static ConexaoBancoDados getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConexaoBancoDados();
		}

		return INSTANCE;
	}

}
