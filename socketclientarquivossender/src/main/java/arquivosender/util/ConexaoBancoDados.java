package arquivosender.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerencia as conexoes com o banco de dados.
 * 
 * @since 04/09/2017
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

	/**
	 * Fecha a conexao com o banco de dados do 
	 * 
	 * @param statement
	 *            Comando SQL.
	 * @param resultSet
	 *            Resultado do comando SQL.
	 * @param preparedStatment
	 *            Comando SQL pre-compilado (Parseado, Compilado, Caminho de
	 *            busca otimizado).
	 */
	public synchronized void fecharConexao(Statement statement, ResultSet resultSet,
			PreparedStatement preparedStatment) {

		try {

			if (statement != null) {
				statement.close();
				LOGGER.info("Fechou a conexão com o banco de dados(statement).");
			}
			if (resultSet != null) {
				resultSet.close();
				LOGGER.info("Fechou a conexão com o banco de dados(resultSet).");
			}
			if (preparedStatment != null) {
				preparedStatment.close();
				LOGGER.info("Fechou a conexão com o banco de dados(preparedStatment).");
			}

		} catch (SQLException e) {
			LOGGER.error("Erro ao fechar conexão com o banco de dados!.", e);
		}

		LOGGER.info("Fechou a conexão com o banco de dados.");

	}

}
