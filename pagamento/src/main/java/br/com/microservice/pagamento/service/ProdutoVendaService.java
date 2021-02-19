package br.com.microservice.pagamento.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.microservice.pagamento.data.vo.ProdutoVendaVO;
import br.com.microservice.pagamento.entity.ProdutoVenda;
import br.com.microservice.pagamento.exception.ResourceNotFoundException;
import br.com.microservice.pagamento.repository.ProdutoVendaRepository;

@Service
public class ProdutoVendaService {

	private ProdutoVendaRepository produtoVendaRepository;

	@Autowired
	public ProdutoVendaService(ProdutoVendaRepository produtoVendaRepository) {
		this.produtoVendaRepository = produtoVendaRepository;
	}

	public ProdutoVendaVO save(ProdutoVendaVO produtoVendaVO) {
		return ProdutoVendaVO.create(this.produtoVendaRepository.save(ProdutoVenda.create(produtoVendaVO)));
	}

	public ProdutoVendaVO update(ProdutoVendaVO produtoVendaVO) {
		final Optional<ProdutoVenda> optional = this.produtoVendaRepository.findById(produtoVendaVO.getId());
		if (!optional.isPresent()) {
			new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", produtoVendaVO.getId().toString()));
		}
		return ProdutoVendaVO.create(this.produtoVendaRepository.save(ProdutoVenda.create(produtoVendaVO)));
	}
	
	public void delete(Long id) {
		final Optional<ProdutoVenda> optional = this.produtoVendaRepository.findById(id);
		if (!optional.isPresent()) {
			new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", id));
		}
		this.produtoVendaRepository.delete(optional.get());
	}
	
	public ProdutoVendaVO findById(Long id) {
		var produtoVenda = this.produtoVendaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", id.toString())));
		return ProdutoVendaVO.create(produtoVenda);
	}
	
	public Page<ProdutoVendaVO> findAll(Pageable pageable) {
		var page = this.produtoVendaRepository.findAll(pageable);
		
		return page.map(this::convertToProdutoVendaVO);
	}
	
	private ProdutoVendaVO convertToProdutoVendaVO(ProdutoVenda produtoVenda) {
		return ProdutoVendaVO.create(produtoVenda);
	}

}
