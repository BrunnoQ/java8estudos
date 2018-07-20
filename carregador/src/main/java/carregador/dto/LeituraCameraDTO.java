package carregador.dto;

import java.time.LocalDateTime;

/**
 * Classe que representa a tabela kdfmkmrltr
 * @author Brunno Silva
 * @since 19/09/2017
 * @version 1.0
 *
 */
public class LeituraCameraDTO {

	
	private Long id; //
	private Integer codigoCamera; //
	private String placaVeiculo; //
	private LocalDateTime dataHoraSistema; //
	private LocalDateTime dataHoraMensagemCamera; //
	private LocalDateTime dataHoraCadastratamento; //
	private String codigoQualidadeInformacaoPlaca; //
	private String codigoEstadoLeituraCamera; //
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCodigoCamera() {
		return codigoCamera;
	}
	public void setCodigoCamera(Integer codigoCamera) {
		this.codigoCamera = codigoCamera;
	}
	public String getPlacaVeiculo() {
		return placaVeiculo;
	}
	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}
	public LocalDateTime getDataHoraSistema() {
		return dataHoraSistema;
	}
	public void setDataHoraSistema(LocalDateTime dataHoraSistema) {
		this.dataHoraSistema = dataHoraSistema;
	}
	public LocalDateTime getDataHoraMensagemCamera() {
		return dataHoraMensagemCamera;
	}
	public void setDataHoraMensagemCamera(LocalDateTime dataHoraMensagemCamera) {
		this.dataHoraMensagemCamera = dataHoraMensagemCamera;
	}
	public LocalDateTime getDataHoraCadastratamento() {
		return dataHoraCadastratamento;
	}
	public void setDataHoraCadastratamento(LocalDateTime dataHoraCadastratamento) {
		this.dataHoraCadastratamento = dataHoraCadastratamento;
	}
	public String getCodigoQualidadeInformacaoPlaca() {
		return codigoQualidadeInformacaoPlaca;
	}
	public void setCodigoQualidadeInformacaoPlaca(String codigoQualidadeInformacaoPlaca) {
		this.codigoQualidadeInformacaoPlaca = codigoQualidadeInformacaoPlaca;
	}
	public String getCodigoEstadoLeituraCamera() {
		return codigoEstadoLeituraCamera;
	}
	public void setCodigoEstadoLeituraCamera(String codigoEstadoLeituraCamera) {
		this.codigoEstadoLeituraCamera = codigoEstadoLeituraCamera;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCamera == null) ? 0 : codigoCamera.hashCode());
		result = prime * result + ((dataHoraSistema == null) ? 0 : dataHoraSistema.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((placaVeiculo == null) ? 0 : placaVeiculo.hashCode());
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
		LeituraCameraDTO other = (LeituraCameraDTO) obj;
		if (codigoCamera == null) {
			if (other.codigoCamera != null)
				return false;
		} else if (!codigoCamera.equals(other.codigoCamera))
			return false;
		if (dataHoraSistema == null) {
			if (other.dataHoraSistema != null)
				return false;
		} else if (!dataHoraSistema.equals(other.dataHoraSistema))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (placaVeiculo == null) {
			if (other.placaVeiculo != null)
				return false;
		} else if (!placaVeiculo.equals(other.placaVeiculo))
			return false;
		return true;
	}
}
