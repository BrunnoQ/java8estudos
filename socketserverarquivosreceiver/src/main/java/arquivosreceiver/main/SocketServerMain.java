package arquivosreceiver.main;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

import arquivosreceiver.timertasks.ConfiguracaoTask;
import arquivosreceiver.timertasks.SocketServerTask;
import arquivosreceiver.util.ConfigUtil;

/**
 * Classe principal de execução do Socket Server.
 * 
 * @author Brunno Silva
 * @since 04/09/2017
 * @version 1.0
 */
public class SocketServerMain {

	private static final Logger LOG = Logger.getLogger(SocketServerMain.class);

	public static void main(String args[]) {

		// TODO Auto-generated method stub
		LOG.info("Socket Server iniciado.");
		new SocketServerMain().iniciar();
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
		SocketServerTask socketServerTask = new SocketServerTask(
				Integer.valueOf(ConfigUtil.getProperty("socket.threadpool")));
		
		//Schedula as tarefas para execucao repetida em um determinado intervalo de tempo.
		timerFactory(configuracaoTask, "ConfiguracaoTask",
				Long.valueOf(ConfigUtil.getProperty("timer.configuracaoTaskdelay")),
				Long.valueOf(ConfigUtil.getProperty("timer.configuracaoTask")));
		timerFactory(socketServerTask, "SocketServerTask",
				Long.valueOf(ConfigUtil.getProperty("timer.socketServerTaskdelay")),
				Long.valueOf(ConfigUtil.getProperty("timer.socketServerTask")));
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
