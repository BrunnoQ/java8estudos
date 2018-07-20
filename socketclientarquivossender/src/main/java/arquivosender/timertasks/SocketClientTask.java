package arquivosender.timertasks;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

import arquivosender.sinais.dto.ConfiguracaoSocketDTO;
import arquivosender.util.ArquivoHandler;

/**
 * Classe de execução do processo SocketClient. schedulada as tarefas para executar
 * repetidademente e aciona todos os subprocessos de manipulação e recebimento
 * de arquivos.
 * 
 * @author Brunno Silva
 * @since 19/09/2017
 * @version 1.0
 */
public class SocketClientTask extends TimerTask {

	private static final Logger LOG = Logger.getLogger(SocketClientTask.class);
	private static List<ConfiguracaoSocketDTO> configuracaoList;
	private static Socket socketClient;
	private static boolean flagCarregarConfiguracao;	
	private static ExecutorService threadPool;

	
	/**
	 * Método construtor 
	 * 
	 * @param quantidadeThreads Quantidade de Threads do pool
	 * 
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	public SocketClientTask(final Integer quantidadeThreads) {
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
		SocketClientTask.configuracaoList = configuracao;
	}

	public static boolean isCarregarConfiguracao() {
		return flagCarregarConfiguracao;
	}

	public static void setCarregarConfiguracao(final boolean carregarConfiguracao) {
		SocketClientTask.flagCarregarConfiguracao = carregarConfiguracao;
	}
	
	public static Socket getClientSocket() {
		return socketClient;
	}
			
	public static ExecutorService getThreadPool() {
		return threadPool;
	}

	/**
	 * Realiza validacoes e aciona o Handler de arquivos.
	 * 
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	private void iniciarProcesso() {
		try {
			
			if (configuracaoList != null) {
				for (ConfiguracaoSocketDTO configuracaoSocket : configuracaoList) {			
					this.listarArquivosExistentesDiretorio(configuracaoSocket);
				}
			}
			
		} catch (SecurityException e) {
			LOG.fatal("Erro de seguranca, programa executou operacao nao permitida pelo S.O", e);
			fecharSocketClientThreadPool();
			
		} catch (IllegalArgumentException e) {
			LOG.fatal("Porta informada na inicializada do Socket fora do range permitido 0 e 65535", e);
			fecharSocketClientThreadPool();
			
		}
	}
	
	/**
	 * Inicia / Reinicia o Socket Client no Host / Porta configurados.
	 * @param host no qual o Socket Client ira consumir o serviço do Socket Server.
	 * @param porta na qual o Socket Client ira consumir o serviço do Socket Server.
	 * @param flagReiniciarSocket Informar true caso seja necessário reiniciar o Socket.
	 * @return Retorna uma nova instância de Socket.
	 * @throws IOException
	 * @throws SocketException
	 * 
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	private synchronized static Socket iniciarSocketClient(final String host, final int porta,
			                                               final boolean flagReiniciarSocket)
			throws IOException, SocketException {

		Socket socket;
		if (flagReiniciarSocket) {
			socketClient.close();
			socket = new Socket(host,porta);
		} else {
			socket = new Socket(host,porta);
		}
		return socket;

	}
		
	/**
	 * Encerra o programa da maneira correta. Deve ser utilizado somente em
	 * casos de falha. Encerrar o Socket e encerrar o POOL evita STUCK THREADS
	 * no servidor.
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	public static void fecharSocketClientThreadPool() {
		
		try {

			//Encerra o Socket
			if (getClientSocket() != null){
				getClientSocket().close();
			}
			
			//Encerra o pool
			if (getThreadPool() != null) {
				getThreadPool().shutdown();
				
				if(!getThreadPool().awaitTermination(10, TimeUnit.SECONDS)){
					getThreadPool().shutdownNow();
					LOG.warn("Encerrou o pool de threads com tarefas que nao foram executadas!");
				}
			}
					
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao fechar Socket Server", e);
			LOG.error("Programa abortado!");
						
		} finally{
			LOG.info("Fechou o Socket Client com sucesso!");
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
	
	/**
	 * Lista os arquivos existentes num diretório e os envia para o 
	 * método "Enviar Arquivo". 
	 * @author Brunno Silva
	 * @since 10/10/2017
	 * @version 1.0
	 * @param configuracaoSocket
	 */
	private void listarArquivosExistentesDiretorio(ConfiguracaoSocketDTO configuracaoSocket) {
		// TODO Auto-generated method stub
		try (Stream<Path> stream = Files.list(Paths.get(configuracaoSocket.getCaminhoDiretorio()))) {
			stream.forEach(SocketClientTask::enviarArquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Nao foi possivel listar os arquivos existentes no diretorio de origem.", e);
			LOG.error("Diretorio inexistente: "+configuracaoSocket.getCaminhoDiretorio(), e);
			//fecharSocketClientThreadPool();
		}
		
	}
	
	/**
	 * Envia o arquivo para a classe ArquivoHandler tratar.
	 * @param pathArquivo Caminho do arquivo
	 * @author Brunno Silva
	 * @since 19/09/2017
	 * @version 1.0
	 */
	private static synchronized void enviarArquivo(Path pathArquivo) {
		ArquivoHandler arquivoHandler;
		boolean isFileProcessSuccess = false;
		try {
			
			boolean reiniciarSocket = false;

			for (ConfiguracaoSocketDTO configuracaoSocket : configuracaoList) {

				socketClient = iniciarSocketClient(configuracaoSocket.getHost(), configuracaoSocket.getPorta(),
						reiniciarSocket);
				
				arquivoHandler = new ArquivoHandler(socketClient.getOutputStream(), pathArquivo);
				arquivoHandler.enviarArquivo();
				
				reiniciarSocket = true;
			}

			socketClient.close();
			isFileProcessSuccess = true;
						
		} catch(ConnectException e){
			LOG.error("Conexao rejeitada pelo Socket Server. [Arquivo] " + pathArquivo.toFile().getPath(), e);
			ArquivoHandler.moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());		
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao enviar arquivo para o Socket Server. [Arquivo] " + pathArquivo.toFile().getPath(), e);
			ArquivoHandler.moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());
			
		} finally {
			if(isFileProcessSuccess){
				pathArquivo.toFile().delete();//Deleta o arquivo apos enviar para todos os destinos
			}
		}
		
	}
}
