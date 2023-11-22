package br.com.tgid.cursomc.dto;

import java.io.Serializable;

import br.com.tgid.cursomc.domain.Produto;

public class ProdutoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private Double preco;

	public ProdutoDTO() {}
	
	public ProdutoDTO(Produto produto) {
		id = produto.getId();
		nome = produto.getNome();
		preco = produto.getPreco();
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Double getPreco() {
		return preco;
	}
}
