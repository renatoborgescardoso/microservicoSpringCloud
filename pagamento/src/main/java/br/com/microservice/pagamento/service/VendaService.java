package br.com.microservice.pagamento.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.microservice.pagamento.data.vo.VendaVO;
import br.com.microservice.pagamento.entity.ProdutoVenda;
import br.com.microservice.pagamento.entity.Venda;
import br.com.microservice.pagamento.exception.ResourceNotFoundException;
import br.com.microservice.pagamento.repository.ProdutoVendaRepository;
import br.com.microservice.pagamento.repository.VendaRepository;

@Service
public class VendaService {

	private VendaRepository vendaRepository;
	private ProdutoVendaRepository produtoVendaRepository;

	@Autowired
	public VendaService(VendaRepository vendaRepository, ProdutoVendaRepository produtoVendaRepository) {
		this.vendaRepository = vendaRepository;
		this.produtoVendaRepository = produtoVendaRepository;
	}

	public VendaVO save(VendaVO vendaVO) {
		Venda venda = this.vendaRepository.save(Venda.create(vendaVO));
		
		List<ProdutoVenda> produtoVendas = new ArrayList<>();
		vendaVO.getProdutos().forEach(p -> {
			ProdutoVenda produtoVenda = ProdutoVenda.create(p);
			produtoVenda.setVenda(venda);

			produtoVendas.add(this.produtoVendaRepository.save(produtoVenda));
		});
		venda.setProdutos(produtoVendas);
		return VendaVO.create(Venda.create(vendaVO));
	}

	public VendaVO update(VendaVO vendaVO) {
		final Optional<Venda> optional = this.vendaRepository.findById(vendaVO.getId());
		if (!optional.isPresent()) {
			new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", vendaVO.getId().toString()));
		}
		return VendaVO.create(this.vendaRepository.save(Venda.create(vendaVO)));
	}
	
	public void delete(Long id) {
		final Optional<Venda> optional = this.vendaRepository.findById(id);
		if (!optional.isPresent()) {
			new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", id));
		}
		this.vendaRepository.delete(optional.get());
	}
	
	public VendaVO findById(Long id) {
		var venda = this.vendaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format("Recurso não encontrado para o código {0}.", id.toString())));
		return VendaVO.create(venda);
	}
	
	public Page<VendaVO> findAll(Pageable pageable) {
		var page = this.vendaRepository.findAll(pageable);
		
		return page.map(this::convertToVendaVO);
	}
	
	private VendaVO convertToVendaVO(Venda venda) {
		return VendaVO.create(venda);
	}

}
