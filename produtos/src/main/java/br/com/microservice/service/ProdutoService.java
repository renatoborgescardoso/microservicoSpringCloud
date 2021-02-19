package br.com.microservice.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.microservice.data.vo.ProdutoVO;
import br.com.microservice.entity.Produto;
import br.com.microservice.exception.ResourceNotFoundException;
import br.com.microservice.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private ProdutoRepository produtoRepository;

	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public ProdutoVO save(ProdutoVO produtoVO) {
		return ProdutoVO.create(this.produtoRepository.save(Produto.create(produtoVO)));
	}

	public ProdutoVO update(ProdutoVO produtoVO) {
		final Optional<Produto> optional = this.produtoRepository.findById(produtoVO.getId());
		if (!optional.isPresent()) {
			new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", produtoVO.getId().toString()));
		}
		return ProdutoVO.create(this.produtoRepository.save(Produto.create(produtoVO)));
	}
	
	public void delete(Long id) {
		final Optional<Produto> optional = this.produtoRepository.findById(id);
		if (!optional.isPresent()) {
			new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", id));
		}
		this.produtoRepository.delete(optional.get());
	}
	
	public ProdutoVO findById(Long id) {
		var produto = this.produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", id.toString())));
		return ProdutoVO.create(produto);
	}
	
	public Page<ProdutoVO> findAll(Pageable pageable) {
		var page = this.produtoRepository.findAll(pageable);
		
		return page.map(this::convertToProdutoVO);
	}
	
	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.create(produto);
	}
	
}
