package arquivosender.timertasks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import arquivosender.enuns.TipoProcessoEnum;
import arquivosender.sinais.dao.SocketDAO;
import arquivosender.sinais.dto.ConfiguracaoSocketDTO;
import arquivosender.util.ConexaoBancoDados;
import arquivosender.util.ConfigUtil;

/**
 * Classe responsavel por carregar de tempos em tempos as configurações (Host,
 * IP, diretório e etc) do Socket.
 * 
 * @author Brunno Silva
 * @since 19/09/2017
 * @version 1.0
 */
public class ConfiguracaoTask extends TimerTask {

	private static final Logger LOG = Logger.getLogger(ConfiguracaoTask.class);

	private static List<ConfiguracaoSocketDTO> configuracaoSocket;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		// Obtem parametros do banco de dados.
		configuracaoSocket = this.obterParametrosConfiguracao();
		
		// Valida se algum parâmetro de configuração foi alterado
		if (this.verificarMudancaConfiguracao(SocketClientTask.getConfiguracao(), configuracaoSocket)) {
			SocketClientTask.setConfiguracao(configuracaoSocket);
		}
	}

	/**
	 * Obtem os parametros de configuração do SocketClient.
	 * 
	 * @return Retorna as configurações do Socket obtidas do banco de dados.
	 * 
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	private List<ConfiguracaoSocketDTO> obterParametrosConfiguracao() {

		try (Connection conexaoBanco = ConexaoBancoDados.getInstance().obterConexao();) {

			SocketDAO socketDAO = new SocketDAO(conexaoBanco);
			configuracaoSocket = socketDAO.obterConfiguracoesSocket(InetAddress.getLocalHost().getHostName(),
					ConfigUtil.getProperty("socket.processname"), TipoProcessoEnum.SERVER.getTipoProcesso());
			LOG.info("Obteve configuracoes no banco de dados com sucesso:");
			for(ConfiguracaoSocketDTO configuracaoSocket : configuracaoSocket){
				LOG.info(configuracaoSocket.toString());
			}

		} catch (ClassNotFoundException | SQLException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao obter Conexao com o banco de dados", erro);
			SocketClientTask.fecharSocketClientThreadPool();

		} catch (UnknownHostException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao obter o host do processo Socket", erro);
			SocketClientTask.fecharSocketClientThreadPool();

		} catch (DaoException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao executar consulta no banco de dados", erro);
			SocketClientTask.fecharSocketClientThreadPool();

		} catch (CryptographyException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao descriptografar senha do banco", erro);
			SocketClientTask.fecharSocketClientThreadPool();
		}

		return configuracaoSocket;
	}
	
	/**
	 * Valida se há a necessidade de reiniciar o Socket, para recarregar
	 * as configurações.
	 * 
	 * @param configuracoesAtuais
	 *            Configurações atuais do Socket.
	 * @param configuracoesBanco
	 *            Configurações obtidas do banco de dados.
	 * 
	 * @return True caso haja alterações das configurações existentes no banco
	 *         de dados.
	 *         
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	private boolean verificarMudancaConfiguracao(List<ConfiguracaoSocketDTO> configuracoesAtuais,
			List<ConfiguracaoSocketDTO> configuracoesBanco) {

		boolean retorno = false;
		
		if (configuracoesBanco != null && configuracoesAtuais == null) {
			retorno = true; // Primeira Execução sempre deve carregar parametros.
		} else {
			LOG.info("Configuracoes Atuais:");
			for (ConfiguracaoSocketDTO configuracaoAtual : configuracoesAtuais) {
				LOG.info(configuracaoAtual.toString());
				//Valida se alguma configuracao foi alterada, adicionada ou removida.
				if (configuracoesBanco.indexOf(configuracaoAtual) < 0
						|| configuracoesBanco.size() > configuracoesAtuais.size()) {
					LOG.info("Parametro existente/modificado: ".concat(configuracaoAtual.toString()));
					retorno = true;
				}
			}
		}

		return retorno;
	}

}
