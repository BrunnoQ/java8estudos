package arquivosender.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe responsável por carregar parâmetros de configuração da aplicação.
 * 
 * @author Brunno Silva
 * @since 04/09/2017
 * @version 1.0
 */
public class ConfigUtil {

	//Aponta a aplicação para o ambiente desejado (DESENV/TST/HML/PRD)
	public static final String CONFIG_PATH = "config.properties";
	public static Properties propriedade;
	public static final LogManager LOGGER = LogManager.getLog(ConfigUtil.class);

	/**
	 * Carrega o arquivo de propriedade no atributo estatico "propriedade".
	 * 
	 * @since 04/09/2017
	 * @author Brunno Silva
	 */
	public static void carregarPropiedades() {

		try (FileInputStream fs = new FileInputStream(CONFIG_PATH)) {

			propriedade = new Properties();
			propriedade.load(fs);
			LOGGER.info("Carregou arquivo de propriedades com exito!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Arquivo de propriedades nao encontrado! Verifcar se o arquivo esta no diretorio raiz!",
					e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Erro ao manipular o arquivo de propriedades!", e);
		}

	}

	/**
	 * Obtem o valor de uma chave configurada no arquivo de propriedade.
	 * 
	 * @since 04/09/2017
	 * @author Brunno Silva
	 * @param chave
	 *            do arquivo de propriedade
	 * @return Valor da chave, existente no arquivo de propriedade.
	 */
	public static String getProperty(String chave) {
		carregarPropiedades();
		return propriedade.getProperty(chave);
	}

}
