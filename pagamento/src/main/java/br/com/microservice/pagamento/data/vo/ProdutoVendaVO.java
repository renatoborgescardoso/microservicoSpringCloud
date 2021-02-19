package br.com.microservice.pagamento.data.vo;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.microservice.pagamento.entity.ProdutoVenda;

public class ProdutoVendaVO extends RepresentationModel<ProdutoVendaVO> implements Serializable {

	private static final long serialVersionUID = -9045158277021927575L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("idProduto")
	private Long idProduto;

	@JsonProperty("quantidade")
	private int quantidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public static ProdutoVendaVO create(ProdutoVenda produtoVenda) {
		return new ModelMapper().map(produtoVenda, ProdutoVendaVO.class);
	}
}
