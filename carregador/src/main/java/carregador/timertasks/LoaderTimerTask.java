package carregador.timertasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

import carregador.util.ArquivoHandler;
import carregador.util.ConfigUtil;

/**
 * Classe responsável por schedular a execucao da aplicacao.
 * 
 * @author Brunno - Brunno Silva
 * @since 16/10/2017
 * @version 1.0
 */
public class LoaderTimerTask extends TimerTask {

	private static final Logger LOG = Logger.getLogger(LoaderTimerTask.class);
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		iniciarProcesso();
	}
	
	private void iniciarProcesso() {
		List<String> origens = new ArrayList<String>();
		origens.add("loader.files.paths");
		origens.add("loader.badfiles");
		for (String origem : origens) {
			this.listarArquivosExistentesDiretorio(ConfigUtil.getProperty(origem));
		}
	}
	
	private void listarArquivosExistentesDiretorio(String caminho) {
		try (Stream<Path> stream = Files.list(Paths.get(caminho))) {
			stream.forEach(LoaderTimerTask::enviarArquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Nao foi possivel listar os arquivos existentes no diretorio de origem.", e);
		}
	}
		
	private static synchronized void enviarArquivo(Path pathArquivo) {
		ArquivoHandler arquivoHandler;
		arquivoHandler = new ArquivoHandler(pathArquivo);
		arquivoHandler.salvarArquivo();
	}
	

}
