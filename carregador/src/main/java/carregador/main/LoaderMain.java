package carregador.main;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import carregador.timertasks.LoaderTimerTask;
import carregador.util.ConfigUtil;

public class LoaderMain {

	private static final Logger LOG = Logger.getLogger(LoaderMain.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LOG.info("Loader Iniciado");
		new LoaderMain().iniciar();
	}
	
	/**
	 * Executa as etapas de execução do processo Loader.
	 * 
	 * @author Brunno - Brunno Silva
	 * @since 01/10/2017
	 */
	private void iniciar(){
		LoaderTimerTask loaderTimerTask = new LoaderTimerTask();
		//Schedula as tarefas para execucao repetida em um determinado intervalo de tempo.
		timerFactory(loaderTimerTask, "LoaderTimerTask",
				Long.valueOf(ConfigUtil.getProperty("timer.loaderTask")),
				Long.valueOf(ConfigUtil.getProperty("timer.loaderTaskdelay")));
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
