package arquivosender.main;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import arquivosender.timertasks.ConfiguracaoTask;
import arquivosender.timertasks.SocketClientTask;
import arquivosender.util.ConfigUtil;

/**
 * Classe principal de execução do Socket Client.
 * @author Brunno Silva
 * @since 19/09/2017
 */
public class SocketClientMain {

	private static final Logger LOG = Logger.getLogger(SocketClientMain.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LOG.info("Socket Client iniciado");
		new SocketClientMain().iniciar();
	}
	
	/**
	 * Executa as etapas de execução do Socket Server.
	 * 
	 * @author Brunno Silva
	 * @since 01/09/2017
	 */
	private void iniciar() {
		LOG.info("Setup inicial do Socket");
		ConfiguracaoTask configuracaoTask = new ConfiguracaoTask();
		SocketClientTask socketServerTask = new SocketClientTask(
				Integer.valueOf(ConfigUtil.getProperty("socket.threadpool")));
		
		//Schedula as tarefas para execucao repetida em um determinado intervalo de tempo.
		timerFactory(configuracaoTask, "ConfiguracaoTask",
				Long.valueOf(ConfigUtil.getProperty("timer.configuracaoTaskdelay")),
				Long.valueOf(ConfigUtil.getProperty("timer.configuracaoTask")));
		timerFactory(socketServerTask, "SocketClientTask",
				Long.valueOf(ConfigUtil.getProperty("timer.socketClientTaskdelay")),
				Long.valueOf(ConfigUtil.getProperty("timer.socketClientTask")));
	}

	/**
	 * Cria Timers para Schedular a execução dos tarefas.
	 * 
	 * @param tarefa
	 *            Tarefa a ser agendada para execução.
	 * @param nomeTarefa
	 *            Nome da tarefa que será executada.
	 * @param delay
	 *            em millesegundos antes da tarefa ser executada.
	 * @param tempo
	 *            em millesegundos para a execução da tarefa repedidamente.
	 */
	private void timerFactory(final TimerTask tarefa, final String nomeTarefa, final long delay, final long periodo) {
		Timer timer = new Timer(nomeTarefa);
		timer.scheduleAtFixedRate(tarefa, delay, periodo);
	}

}
