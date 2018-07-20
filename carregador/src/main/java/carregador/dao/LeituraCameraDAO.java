package carregador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import carregador.dto.LeituraCameraDTO;

/**
 * Classe que realiza comandos SQL na tabela kdfmkmrltr
 * @author BRunno
 * @since 19/09/2017
 * @version 1.0
 *
 */
public class LeituraCameraDAO extends BaseDAO<LeituraCameraDTO> {

	private static final Logger LOG = Logger.getLogger(LeituraCameraDAO.class);

	public LeituraCameraDAO(final Connection conexaoBanco){
		super(conexaoBanco);
	}
	
	@Override
	public LeituraCameraDTO selectById(long id) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

	@Override
	public void delete(LeituraCameraDTO object) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

	@Override
	public void insert(LeituraCameraDTO object) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

	@Override
	public void update(LeituraCameraDTO object) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

	@Override
	protected LeituraCameraDTO getVO(ResultSet rs) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

	@Override
	protected long getNextVal() throws DaoException {
		// TODO Auto-generated method stub
		Connection conexao = super.connection;
		long sequence = 0;
		String nextvalSql = "select kdfmkmrltr_seq.nextval from dual";
		
		try (Statement statement = conexao.createStatement();
			 ResultSet resultSet = statement.executeQuery(nextvalSql);) {
			
			if (resultSet.next()) {
				sequence = resultSet.getLong(1);			
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error("Erro ao realizar nextval no sequence da tabela kdfmkmrltr: ", e);
			throw new DaoException("Erro ao realizar nextval no sequence da tabela kdfmkmrltr: ", e);
		}

		return sequence;
	}
	
	/**
	 * Realiza um insertBatch, inserindo a lista de elementos
	 * informados no banco de dados de uma unica vez.
	 * @param leituraCameraDTOList
	 * @return TRUE caso a execucao tenha sido bem sucedida.
	 */
	public boolean insertBatch(final List<LeituraCameraDTO> leituraCameraDTOList){
		
		String insertSql = "";
		
		boolean flagOK = false;
		Connection conexao = super.connection;
		
		try (PreparedStatement preparedStatement = conexao.prepareStatement(insertSql);) {

			conexao.setAutoCommit(false);// Desabilita o autocommit

			for (LeituraCameraDTO leituraCameraDTO : leituraCameraDTOList) {
				preparedStatement.setLong(1, getNextVal());
				preparedStatement.setInt(2, leituraCameraDTO.getCodigoCamera());
				preparedStatement.setString(3, leituraCameraDTO.getPlacaVeiculo());
				preparedStatement.setTimestamp(4, Timestamp.from(
						leituraCameraDTO.getDataHoraSistema().atZone(ZoneId.of("America/Sao_Paulo")).toInstant()));
				preparedStatement.setTimestamp(5, Timestamp.from(
						leituraCameraDTO.getDataHoraMensagemCamera().atZone(ZoneId.of("America/Sao_Paulo")).toInstant()));
				preparedStatement.setTimestamp(6, Timestamp.from(
						leituraCameraDTO.getDataHoraCadastratamento().atZone(ZoneId.of("America/Sao_Paulo")).toInstant()));
				preparedStatement.setString(7, leituraCameraDTO.getCodigoQualidadeInformacaoPlaca());
				preparedStatement.setString(8, leituraCameraDTO.getCodigoEstadoLeituraCamera());
				preparedStatement.addBatch();// Adiciona o comando para ser processado em lote
			}
			
			int [] qntRegistrosAlt = preparedStatement.executeBatch();
			conexao.commit();
			flagOK = true;
			LOG.info("Numero de registros processados: "+qntRegistrosAlt.length);
			
		} catch (SQLException | DaoException e) {
			// TODO Auto-generated catch block
			try {
				conexao.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				LOG.error("Erro ao realizar rollback na tabela kdfmkmrltr: ", e);
			}
			LOG.error("Erro ao salvar registros na tabela kdfmkmrltr: ", e);
			flagOK = false;
		}
		
		return flagOK;	
		
	}//Fim do método insertBatch

}
