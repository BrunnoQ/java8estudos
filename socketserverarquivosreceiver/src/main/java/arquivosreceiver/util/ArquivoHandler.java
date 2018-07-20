package arquivosreceiver.util;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import arquivosreceiver.util.ArquivoHandler;
import arquivosreceiver.dto.ConfiguracaoSocketDTO;
import arquivosreceiver.timertasks.SocketServerTask;

/**
 * Classe que manipula os arquivos recebidos pelo Socket.
 * @author Brunno Silva
 * @since 14/09/2017
 * @version 1.0
 */
public class ArquivoHandler implements Runnable {

	private final Socket socket;
	private List<ConfiguracaoSocketDTO> configuracoesTask;
	private static final Logger LOG = Logger.getLogger(ArquivoHandler.class);
	
	//Teste Unitários
	public ArquivoHandler(){
		socket = null;
	}
	
	public ArquivoHandler (final Socket socket, final List<ConfiguracaoSocketDTO> configuracoesTask){
		this.socket = socket;	
		this.configuracoesTask = configuracoesTask;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		manipularArquivo();
	}

	public List<ConfiguracaoSocketDTO> getConfiguracoesTask() {
		return configuracoesTask;
	}

	public void setConfiguracoesTask(final List<ConfiguracaoSocketDTO> configuracoesTask) {
		this.configuracoesTask = configuracoesTask;
	}

	public Socket getSocket() {
		return socket;
	}
	
	/**
	* Obtem o arquivo do canal do Socket e o salva nos diretórios
	* de destino configurados.
	* 
	* @author Brunno Silva
	* @since 14/09/2017
	* @version 1.0
	*/
	private boolean manipularArquivo() {

		boolean retorno = false;

		byte[] fileByte = null;
		long size = 0;
		int bytesToRead = 0;
		List<FileOutputStream> listFos = new ArrayList<FileOutputStream>();
		String fileName = "";
		FileOutputStream fos = null;
		
		try (DataInputStream dis = new DataInputStream(this.socket.getInputStream())) {

			fileByte = new byte[1024];// Define tamanho do buffer

			// 1- Obtem o nome do arquivo
			fileName = dis.readUTF();

			// 2- Tamanho da stream recebida
			size = dis.readLong();

			// 3- Define para cada destino um FileOutputStream
			for (ConfiguracaoSocketDTO configuracaoSocket : configuracoesTask) {
				try {
					fos = new FileOutputStream(configuracaoSocket.getCaminhoDiretorio().concat(fileName));
					listFos.add(fos);
					
				} catch (FileNotFoundException e) {
					LOG.warn("Arquivo ou diretorio nao encontrado! [Arquivo] destino desconsiderado . ".concat(fileName), e);
					//fos.close();
					continue;
				}
			}

			// 4- Varre o array de bytes e monta o arquivo
			while (size > 0 && (bytesToRead = dis.read(fileByte, 0, (int) Math.min(fileByte.length, size))) != -1) {
				// Para cada destino realiza a escrita.
				for (FileOutputStream fos2 : listFos) {
					fos2.write(fileByte, 0, bytesToRead);
				}
				size -= bytesToRead;
			}
			
			retorno = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao manipular arquivo no Socket Server [Arquivo]. ".concat(fileName), e);
			LOG.info("Estado atual da Thread: "+Thread.currentThread().getState());
			
			//SocketServerTask.fecharSocketServerThreadPool();
		} finally {
			// Encerra todos FileOutputStream
			try {

				for (FileOutputStream fos2 : listFos) {
					fos2.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.fatal("Erro ao fechar os FileOutputStreans.", e);
				SocketServerTask.fecharSocketServerThreadPool();
			}
		}

		return retorno;
	}
		
}
