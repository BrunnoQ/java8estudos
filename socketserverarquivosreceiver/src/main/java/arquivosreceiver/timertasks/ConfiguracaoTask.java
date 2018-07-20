package arquivosreceiver.timertasks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import arquivosreceiver.timertasks.ConfiguracaoTask;
import arquivosreceiver.dao.SocketDAO;
import arquivosreceiver.dto.ConfiguracaoSocketDTO;
import arquivosreceiver.enuns.TipoProcessoEnum;
import arquivosreceiver.util.ConexaoBancoDados;
import arquivosreceiver.util.ConfigUtil;

/**
 * Classe responsavel por carregar de tempos em tempos as configurações (Host,
 * IP, diretório e etc) do Socket.
 * 
 * @author Brunno Silva
 * @since 06/09/2017
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
		if (this.verificarMudancaConfiguracao(SocketServerTask.getConfiguracao(), configuracaoSocket)) {
			SocketServerTask.setCarregarConfiguracao(true);
			SocketServerTask.setConfiguracao(configuracaoSocket);
		}
	}

	/**
	 * Obtem os parametros de configuração do SocketServer.
	 * 
	 * @return Retorna as configurações do Socket obtidas do banco de dados.
	 * 
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	private List<ConfiguracaoSocketDTO> obterParametrosConfiguracao() {

		try (Connection conexaoBanco = ConexaoBancoDados.getInstance().obterConexao()){
			
			SocketDAO socketDAO = new SocketDAO(conexaoBanco);
			configuracaoSocket = socketDAO.obterConfiguracoesSocket(InetAddress.getLocalHost().getHostName(),
					ConfigUtil.getProperty("socket.processname"), TipoProcessoEnum.SERVER.getTipoProcesso());
			LOG.info("Obteve configuracoes no banco de dados com sucesso...");
			LOG.info("Estado atual da Thread: "+Thread.currentThread().getState());
			LOG.info("Configuracoes Socket Server: ");
			LOG.info("Quantidade de diretorios de destinos: "+configuracaoSocket.size());
			for(ConfiguracaoSocketDTO configuracaoSocket : configuracaoSocket){
				LOG.info(configuracaoSocket.toString());
			}
			
		} catch (ClassNotFoundException | SQLException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao obter Conexao com o banco de dados", erro);
			SocketServerTask.fecharSocketServerThreadPool();
						
		} catch (UnknownHostException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao obter o host do processo Socket", erro);
			SocketServerTask.fecharSocketServerThreadPool();
						
		} catch (DaoException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao executar consulta no banco de dados", erro);
			SocketServerTask.fecharSocketServerThreadPool();
						
		} catch (CryptographyException erro) {
			// TODO Auto-generated catch block
			LOG.fatal("[obterParametro] Erro ao executar descriptografar senha do banco", erro);
			SocketServerTask.fecharSocketServerThreadPool();
		}

		return configuracaoSocket;
	}
	
	/**
	 * Valida se há a necessidade de reiniciar o Socket Server, para recarregar
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
	 * @since 14/09/2017
	 * @version 1.0 
	 */
	private boolean verificarMudancaConfiguracao(List<ConfiguracaoSocketDTO> configuracoesAtuais,
			List<ConfiguracaoSocketDTO> configuracoesBanco) {

		boolean retorno = false;

		if (configuracoesBanco != null && configuracoesAtuais == null) {
			retorno = true; // Primeira Execução sempre deve carregar
							// parametros.
		} else {
			for (ConfiguracaoSocketDTO configuracaoAtual : configuracoesAtuais) {
				if (configuracoesBanco.indexOf(configuracaoAtual) < 0
						|| configuracoesBanco.size() > configuracoesAtuais.size()) {
					LOG.info("Parametro existente/modificado: ".concat(configuracaoAtual.toString()));
					LOG.info("Socket sera reiniciado!");
					retorno = true;
				}
			}

		}

		return retorno;
	}

}
