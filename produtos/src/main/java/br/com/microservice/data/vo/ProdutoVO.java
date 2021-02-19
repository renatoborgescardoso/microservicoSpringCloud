package br.com.microservice.data.vo;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.microservice.entity.Produto;

@JsonPropertyOrder("{id, nome, estoque, preco}")
public class ProdutoVO extends RepresentationModel<ProdutoVO> implements Serializable {

	private static final long serialVersionUID = 3897078862273820283L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("nome")
	private String nome;

	@JsonProperty("estoque")
	private Integer estoque;

	@JsonProperty("preco")
	private Double preco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getEstoque() {
		return estoque;
	}

	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public static ProdutoVO create(Produto produto) {
		return new ModelMapper().map(produto, ProdutoVO.class);
	}
}
