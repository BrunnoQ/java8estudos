package arquivosreceiver.timertasks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import arquivosreceiver.dto.ConfiguracaoSocketDTO;
import arquivosreceiver.util.ArquivoHandler;

/**
 * Classe de execução do processo SocketServer. schedulada as tarefas para executar
 * repetidademente e aciona todos os subprocessos de manipulação e recebimento
 * de arquivos.
 * 
 * @author Brunno Silva
 * @since 14/09/2017
 * @version 1.0
 */
public class SocketServerTask extends TimerTask {

	private static final Logger LOG = Logger.getLogger(SocketServerTask.class);
	private static List<ConfiguracaoSocketDTO> configuracaoList;
	private static ServerSocket serverSocket;
	private static boolean flagCarregarConfiguracao;	
	private static ExecutorService threadPool;
	
	/**
	 * Método construtor 
	 * 
	 * @param quantidadeThreads Quantidade de Threads do pool
	 * @param valor maximo em bytes do buffer do Socket
	 * 
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	public SocketServerTask(final Integer quantidadeThreads) {
		// TODO Auto-generated constructor stub
		threadPool = Executors.newFixedThreadPool(quantidadeThreads);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Inicia o Socket Server
		iniciarProcesso();
	}

	public static List<ConfiguracaoSocketDTO> getConfiguracao() {
		return configuracaoList;
	}

	public static void setConfiguracao(final List<ConfiguracaoSocketDTO> configuracao) {
		SocketServerTask.configuracaoList = configuracao;
	}

	public static boolean isCarregarConfiguracao() {
		return flagCarregarConfiguracao;
	}

	public static void setCarregarConfiguracao(final boolean carregarConfiguracao) {
		SocketServerTask.flagCarregarConfiguracao = carregarConfiguracao;
	}
	
	public static ServerSocket getServerSocket() {
		return serverSocket;
	}
			
	public static ExecutorService getThreadPool() {
		return threadPool;
	}
	
	/**
	 * Realiza validacoes e aciona o Handler de arquivos.
	 * 
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	private void iniciarProcesso() {
		try {
			// Validações de inializacao do Socket.
			if (validarInicializacaoSocket()) {
				serverSocket = iniciarSocket(configuracaoList.get(0).getPorta(),false);
				LOG.info("Iniciou o Socket Server");
				LOG.info("Estado atual da Thread: "+Thread.currentThread().getState());
				LOG.info("Configuracoes Socket Server: ");
				LOG.info("Quantidade de diretoris de destinos: "+configuracaoList.size());
				for(ConfiguracaoSocketDTO configuracaoSocket : configuracaoList){
					LOG.info(configuracaoSocket.toString());
				}
			} else if (validarRefreshParametrosConfiguracao()) {
				serverSocket = iniciarSocket(configuracaoList.get(0).getPorta(),true);
				LOG.info("Reiniciou Socket Server com novos parametros de configuracao!");
				LOG.info("Estado atual da Thread: "+Thread.currentThread().getState());
				LOG.info("Configuracoes Socket Server: ");
				LOG.info("Quantidade de diretorios de destino: "+configuracaoList.size());
				for(ConfiguracaoSocketDTO configuracaoSocket : configuracaoList){
					LOG.info(configuracaoSocket.toString());
				}
			}
			
			//Com threads paralelas não há garantias de que ConfiguracaoTask execute primeiro.
			if(serverSocket != null){
				//runnableMock();
				//Executa a manipulação dos arquivos em threads paralelas.
				threadPool.execute(new ArquivoHandler(serverSocket.accept(), getConfiguracao()));				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.fatal("Erro durante o processo de recebimento de arquivos no Socket Server", e);
			fecharSocketServerThreadPool();
			
		} catch (SecurityException e) {
			LOG.fatal("Erro de seguranca, programa executou operacao nao permitida pelo S.O", e);
			fecharSocketServerThreadPool();
						
		} catch (IllegalArgumentException e) {
			LOG.fatal("Porta informada na inicializada do Socket fora do range permitido 0 e 65535", e);
			fecharSocketServerThreadPool();
		}
	}

	/**
	 * Inicia / Reinicia o Socket na porta configurada.
	 * @param porta na qual o Socket Server ira subir.
	 * @param flagReiniciarSocket Informar true caso seja necessário reiniciar o Socket.
	 * @return Retorna uma nova instância de ServerSocket.
	 * @throws IOException
	 * @throws SocketException
	 * 
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	private synchronized ServerSocket iniciarSocket(final int porta, final boolean flagReiniciarSocket)
			throws IOException, SocketException {

		ServerSocket socket;
		if (flagReiniciarSocket) {
			serverSocket.close();//Fecha o Socket
			socket = new ServerSocket(porta);
		} else {
			socket = new ServerSocket(porta);
		}

		return socket;

	}
	
	/**
	 * Valida se o Socket necessita ser inicializado.
	 * @return True caso o Socket precise se inicializado.
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	private boolean validarInicializacaoSocket() {
		boolean resultado = false;
		if (serverSocket == null && configuracaoList != null && !configuracaoList.isEmpty()) {
			resultado = true;
		}
		return resultado;
	}

	/**
	 * Valida se o Socket necessita ser reinicializado, para carregar novas
	 * configurações.
	 * @return True caso o Socket precise ser reinicializado.
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	private boolean validarRefreshParametrosConfiguracao() {
		boolean resultado = false;
		if (flagCarregarConfiguracao && configuracaoList != null && !configuracaoList.isEmpty()
				&& serverSocket.isBound()) {
			flagCarregarConfiguracao = false;
			resultado = true;
		}
		return resultado;
	}
	
	/**
	 * Encerra o programa da maneira correta. Deve ser utilizado somente em
	 * casos de falha. Encerrar o Socket e encerrar o POOL evita STUCK THREADS
	 * no servidor.
	 * @author Brunno Silva
	 * @since 14/09/2017
	 * @version 1.0
	 */
	public static void fecharSocketServerThreadPool() {
		
		try {

			//Encerra o Socket
			if (getServerSocket() != null){
				getServerSocket().close();
			}
			
			//Encerra o pool
			if (getThreadPool() != null) {
				getThreadPool().shutdown();
				
				if(!getThreadPool().awaitTermination(10, TimeUnit.SECONDS)){
					getThreadPool().shutdownNow();
					LOG.warn("Encerrou o pool de threads com tarefas que nao foram executadas!");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao fechar Socket Server", e);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao fechar pool de Threads!", e);
			
		} finally{
			LOG.info("Fechou o Socket Server com sucesso!");
			System.exit(-1);
		}		
	}
	
	@SuppressWarnings("unused")
	/**
	 * Para testar o pool de threads
	 */
	private void runnableMock(){
		Runnable runnable = () -> {
		      System.out.println("Thread : " + Thread.currentThread().getName());
		      LOG.info("Teste");
		    };
		threadPool.execute(runnable);	
	}
	
}
