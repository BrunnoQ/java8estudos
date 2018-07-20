package arquivosreceiver.enuns;

/**
 * Representa as constantes do tipos de sockets existentes (1-Client 2-Server)
 * 
 * @author Brunno Silva
 * @since 04/09/2017
 * @version 1.0
 * 
 */
public enum TipoProcessoEnum {

	SERVER(1),CLIENT(2);

	private int tipoProcesso;

	private TipoProcessoEnum(int tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public int getTipoProcesso() {
		return tipoProcesso;
	}
}
