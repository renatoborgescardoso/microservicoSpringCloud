package br.com.microservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.modelmapper.ModelMapper;

import br.com.microservice.data.vo.ProdutoVO;

@Entity
public class Produto implements Serializable {

	private static final long serialVersionUID = -6511741101796394313L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "produto", nullable = false, length = 255)
	private String nome;

	@Column(name = "estoque", nullable = false, length = 10)
	private Integer estoque;

	@Column(name = "preco", nullable = false, length = 10)
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

	public static Produto create(ProdutoVO produtoVO) {
		return new ModelMapper().map(produtoVO, Produto.class);
	}
}
