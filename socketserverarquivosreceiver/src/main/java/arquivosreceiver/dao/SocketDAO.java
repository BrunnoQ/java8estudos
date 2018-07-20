package arquivosreceiver.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

/**
 * Realiza comandandos SQL na tabela de configuração dos Sockets.
 * 
 * @author Brunno Silva
 * @since 04/09/2017
 * @version 1.0
 */
public class SocketDAO extends BaseDAO<ConfiguracaoSocketDTO> {

	private static final Logger LOG = Logger.getLogger(SocketDAO.class);

	/**
	 * Método construtor da classe
	 * 
	 * @param conexaoBanco
	 *            conexão com o banco de dados.
	 *            
	 * @author Brunno Silva
	 * @since 05/09/2017
	 * @version 1.0
	 */
	public SocketDAO(final Connection conexaoBanco) {
		super(conexaoBanco);
	}

	/**
	 * Converte o resultado obtido do banco de dados para um objeto da classe
	 * ConfiguracaoSocketDTO.
	 * 
	 * @param rs
	 *            ResultSet obtida após a realização de query.
	 * @return uma instância da classe ConfiguracaoSocketDTO.
	 * 
	 * @author Brunno Silva
	 * @since 05/09/2017 - 
	 * @version 1.0
	 */
	@Override
	protected ConfiguracaoSocketDTO getVO(final ResultSet rs) throws DaoException {
		// TODO Auto-generated method stub
		ConfiguracaoSocketDTO configuracaoSocket = new ConfiguracaoSocketDTO();
		
		try {
			configuracaoSocket.setId(rs.getLong("PCSDSTSEQNUM"));
			configuracaoSocket.setNomeMaquina(rs.getString("PCSHOSNOM"));
			configuracaoSocket.setNomeProcesso(rs.getString("PCSNOM"));
			configuracaoSocket.setTipoProceso(TipoProcessoEnum.SERVER);
			configuracaoSocket.setHost(InetAddress.getLocalHost().getHostName());//a fim de evitar a que o servico caia por erro no cadastro
			configuracaoSocket.setPorta(rs.getInt("PCSDSTPRTNUM"));
			configuracaoSocket.setCaminhoDiretorio(rs.getString("PCSDSTDRRCMHDES"));
			
		} catch (SQLException | UnknownHostException e) {
			// TODO Auto-generated catch block
			LOG.fatal("Error ao consultar parametro de configuracao ", e);
			throw new DaoException(e);
		}

		return configuracaoSocket;

	}

	/**
	 * Realiza uma query para buscar os parametros do Socket informados no banco
	 * de dados.
	 * <br><b>Cost Query: 4</b></br>
	 *  -  (1):
	 *    -  UNIQUE SCAN --> predicate processo.PCSHOSNOM = ?
	 *  -  (3):
	 *    -  FULL --> predicate processo.PCSSEQNUM = destino.PCSSEQNUM
	 * @param hostDoProcesso Nome da máquina onde o processo esta sendo executado.
	 * @param nomeProcesso   Nome do processo Socket.
	 * @param tipoProcesso   Tipo do processo (1- Server 2- Client)
	 * @return
	 * @throws DaoException
	 * 
	 * @author Brunno Silva
	 * @since 10/09/2017 - 
	 * @version 1.0
	 */
	public List<ConfiguracaoSocketDTO> obterConfiguracoesSocket(final String hostDoProcesso, final String nomeProcesso,
			final Integer tipoProcesso) throws DaoException {

		LOG.info("##########################");
		LOG.info("#Parametros de pesquisa: #");
		LOG.info("#hostDoProcesso: "+hostDoProcesso);
		LOG.info("#nomeProcesso: "+nomeProcesso);
		LOG.info("##########################");
		
		String sql = "";
		
		return selectList(sql, hostDoProcesso, nomeProcesso);	

	}

	/**
	 * Não implementado
	 */
	@Override
	public ConfiguracaoSocketDTO selectById(long id) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

	/**
	 * Não implementado
	 */
	@Override
	public void delete(ConfiguracaoSocketDTO object) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");

	}

	/**
	 * Não implementado
	 */
	@Override
	public void insert(ConfiguracaoSocketDTO object) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");

	}

	/**
	 * Não implementado
	 */
	@Override
	public void update(ConfiguracaoSocketDTO object) throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");

	}

	/**
	 * Não implementado
	 */
	@Override
	protected long getNextVal() throws DaoException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Método não implementado");
	}

}
