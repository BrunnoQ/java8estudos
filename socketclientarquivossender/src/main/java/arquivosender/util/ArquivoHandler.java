package arquivosender.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apache.log4j.Logger;

/**
 * Classe que manipula os arquivos recebidos pelo Socket.
 * @author Brunno Silva
 * @since 14/09/2017
 * @version 1.0
 */
public class ArquivoHandler implements Runnable {

	private final OutputStream socketOutputStream;
	private Path pathArquivo;
	private static final Logger LOG = Logger.getLogger(ArquivoHandler.class);
	
	//Teste Unitários
	public ArquivoHandler(){
		socketOutputStream = null;
	}
	
	public ArquivoHandler (final OutputStream socketOutputStream, final Path pathArquivo){
		this.socketOutputStream = socketOutputStream;	
		this.pathArquivo = pathArquivo;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		enviarArquivo();
	}
	
	public OutputStream getSocketOutputStream() {
		return socketOutputStream;
	}
	
	public Path getPathArquivo() {
		return pathArquivo;
	}

	public void setPathArquivo(Path pathArquivo) {
		this.pathArquivo = pathArquivo;
	}

	/**
	* Envia o arquivo para o Socket Server.
	* 
	* @author Brunno Silva
	* @since 14/09/2017
	* @version 1.0
	*/
	public void enviarArquivo() {

		File file = pathArquivo.toFile();

		try (FileInputStream fis = new FileInputStream(file);
			 BufferedInputStream bis = new BufferedInputStream(fis);
			 DataInputStream dis = new DataInputStream(bis);
			 DataOutputStream dos = new DataOutputStream(socketOutputStream);) {

			byte[] arquivoByte = new byte[(int) file.length()];

			dis.readFully(arquivoByte, 0, arquivoByte.length);

			dos.writeUTF(file.getName());
			dos.writeLong(arquivoByte.length);
			dos.write(arquivoByte, 0, arquivoByte.length);
			dos.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao enviar arquivo ao Socket Server: ".concat(pathArquivo.toFile().getName()), e);
			moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());

		} catch (Exception e) {
			LOG.error("Erro ao realizar envio de arquivo.", e);
			moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());
		}

	}
	
	/**
	 * Move o arquivo para o diretório de reprocessamento configurado no arquivo de
	 * propriedades.
	 * @param pathArquivo Arquivo a ser reprocessado.
	 * @param fileName Nome do arquivo a ser reprocessado.
	 * @author Brunno Silva
	 * @since 03/10/2017
	 * @version 1.0
	 */
	public static void moverArquivoParaReprocessamento(final Path pathArquivo, String fileName){
		
		try {
			Files.move(pathArquivo, Paths.get(ConfigUtil.getProperty("socketClient.badfiles")
                                                        .concat("/").concat(fileName)),
					                                     StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao copiar arquivo para diretório de reprocessamento, arquivo descartado! [Arquivo] "
					.concat(pathArquivo.toFile().getName()), e);
			//SocketClientTask.fecharSocketClientThreadPool();
		}
		
	}
	
}
