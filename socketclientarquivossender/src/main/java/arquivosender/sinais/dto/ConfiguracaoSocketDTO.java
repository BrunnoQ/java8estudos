package arquivosender.sinais.dto;

import arquivosender.enuns.TipoProcessoEnum;

/**
 * Representa a configuração do Socket (server or client). 
 *  / 
 * 
 * @author Brunno Silva
 * @since 19/09/2017
 * @version 1.0
 */
public class ConfiguracaoSocketDTO {

	private long id;  //
	private String nomeMaquina; //
	private String nomeProcesso; //
	private TipoProcessoEnum tipoProceso;
	private String host; //
	private int porta; //
	private String caminhoDiretorio; //

	public String getNomeMaquina() {
		return nomeMaquina;
	}

	public void setNomeMaquina(final String nomeMaquina) {
		this.nomeMaquina = nomeMaquina;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getNomeProcesso() {
		return nomeProcesso;
	}

	public void setNomeProcesso(final String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}

	public TipoProcessoEnum getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(TipoProcessoEnum tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	public String getHost() {
		return host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(final int porta) {
		this.porta = porta;
	}

	public String getCaminhoDiretorio() {
		return caminhoDiretorio;
	}

	public void setCaminhoDiretorio(final String caminhoDiretorio) {
		this.caminhoDiretorio = caminhoDiretorio;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caminhoDiretorio == null) ? 0 : caminhoDiretorio.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nomeMaquina == null) ? 0 : nomeMaquina.hashCode());
		result = prime * result + porta;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfiguracaoSocketDTO other = (ConfiguracaoSocketDTO) obj;
		if (caminhoDiretorio == null) {
			if (other.caminhoDiretorio != null)
				return false;
		} else if (!caminhoDiretorio.equals(other.caminhoDiretorio))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (id != other.id)
			return false;
		if (nomeMaquina == null) {
			if (other.nomeMaquina != null)
				return false;
		} else if (!nomeMaquina.equals(other.nomeMaquina))
			return false;
		if (porta != other.porta)
			return false;
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String string = "Host do proceso : ".concat(this.nomeMaquina)
				.concat("\n Process Name: ").concat(this.nomeProcesso)
				.concat("\n Tipo Processo: ").concat(String.valueOf(this.tipoProceso.getTipoProcesso())
			    .concat("\n Porta: ").concat(String.valueOf(this.porta)))
				.concat("\n Diretorio de Arquivos de origem: ").concat(this.caminhoDiretorio)
				.concat("\n Host de destino: ").concat(this.host);
		
		return string;
	}
}
