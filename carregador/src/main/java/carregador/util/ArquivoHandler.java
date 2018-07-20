package carregador.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import carregador.dao.LeituraCameraDAO;
import carregador.dto.LeituraCameraDTO;

public class ArquivoHandler {

	private Path pathArquivo;
	private static final Logger LOG = Logger.getLogger(ArquivoHandler.class);
	
	public ArquivoHandler(Path pathArquivo) {
		// TODO Auto-generated constructor stub
		this.pathArquivo = pathArquivo;
	}

	public void salvarArquivo() {
		// TODO Auto-generated method stub
		
		List<LeituraCameraDTO> leituraCameraDTOList = new ArrayList<LeituraCameraDTO>();
		LeituraCameraDAO leituraCameraDAO;
		boolean isInsertComSucesso = false;
		
		LOG.info("Iniciou processamento do arquivo: ".concat(pathArquivo.toFile().getName()));
		
		try (Stream<String> linhas = Files.lines(pathArquivo);
			 Connection conexao = ConexaoBancoDados.getInstance().obterConexao()) {

			Iterator<String> it = linhas.iterator();

			while (it.hasNext()) {
				LeituraCameraDTO leituraCameraDTO = this.converterLinhaToLeituraCameraDTO(it.next());
				leituraCameraDTOList.add(leituraCameraDTO);
			}
			
			leituraCameraDAO = new LeituraCameraDAO(conexao);
			
			//Caso ocorra erro move para o arquivo para reprocessamento.
			isInsertComSucesso = leituraCameraDAO.insertBatch(leituraCameraDTOList);
			if(!isInsertComSucesso){
				moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao realizar leitura do arquivo. [Arquivo]".concat(pathArquivo.toFile().getName()), e);
			moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());
		} catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e){
			LOG.error("Arquivo fora dos padroes esperados. Arquivo DESCARTADO: ".concat(pathArquivo.toFile().getName()), e);
			pathArquivo.toFile().delete();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LOG.fatal("Erro ao obter Conexao com o banco de dados, driver nao encontrado", e);
			System.exit(-1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao obter conexao com o banco de dados", e);
			moverArquivoParaReprocessamento(pathArquivo, pathArquivo.toFile().getName());
		} catch (CryptographyException e) {
			// TODO Auto-generated catch block
			LOG.fatal("Erro ao descriptografar senha do banco", e);
			System.exit(-1);
			
		} finally{
			if (isInsertComSucesso) {
				pathArquivo.toFile().delete();// Deleta o arquivo pos manipulacao
			}
		}

	}
	
	/**
	 * Converte uma linha do arquivo enviado pelo Gateway para um objeto do tipo
	 * LeituraCameraDTO. Formato da string esperada: <br>
	 * <b>CodigoCamera;DataHoraSistema;DataHoraMensagemCamera;CodigoQualidadeInformacaoPlaca;
	 * CodigoEstadoLeituraCamera;PlacaVeiculo</b></br>
	 * 
	 * @param linha
	 *            String contendo os dados separados por ";".
	 * @return Retorna uma instancia da classe LeituraCameraDTO.
	 * @author Brunno Silva
	 * @since 03/10/2017
	 * @version 1.0
	 */
	private LeituraCameraDTO converterLinhaToLeituraCameraDTO(String linha)
			throws NumberFormatException, DateTimeParseException {

		String[] leituraCamera = linha.split(";", 6);

		LeituraCameraDTO leituraCameraDTO = new LeituraCameraDTO();
		leituraCameraDTO.setCodigoCamera(Integer.valueOf(leituraCamera[0]));
		leituraCameraDTO.setDataHoraSistema(LocalDateTime.parse(leituraCamera[1]));
		leituraCameraDTO.setDataHoraMensagemCamera(LocalDateTime.parse(leituraCamera[2]));
		leituraCameraDTO.setDataHoraCadastratamento(LocalDateTime.now());
		leituraCameraDTO.setCodigoQualidadeInformacaoPlaca(leituraCamera[3]);
		leituraCameraDTO.setCodigoEstadoLeituraCamera(leituraCamera[4]);
		leituraCameraDTO.setPlacaVeiculo(leituraCamera[5]);

		return leituraCameraDTO;
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
			Files.move(pathArquivo, Paths.get(ConfigUtil.getProperty("loader.badfiles")
                                                        .concat("/").concat(fileName)),
					                                     StandardCopyOption.REPLACE_EXISTING);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao copiar arquivo para diretório de reprocessamento, arquivo descartado! [Arquivo] "
					.concat(pathArquivo.toFile().getName()), e);
			pathArquivo.toFile().delete();
		}
	}

}
