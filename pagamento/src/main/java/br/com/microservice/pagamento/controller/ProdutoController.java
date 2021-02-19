package br.com.microservice.pagamento.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservice.pagamento.data.vo.ProdutoVO;
import br.com.microservice.pagamento.service.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoController {

	private ProdutoService produtoService;
	private PagedResourcesAssembler<ProdutoVO> pagedResourcesAssembler;

	@Autowired
	public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProdutoVO> pagedResourcesAssembler) {
		this.produtoService = produtoService;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProdutoVO save(@RequestBody ProdutoVO produtoVO) {
		ProdutoVO retornoProdutoVO = this.produtoService.save(produtoVO);
		retornoProdutoVO.add(linkTo(methodOn(ProdutoController.class).findById(retornoProdutoVO.getId())).withSelfRel());
		return retornoProdutoVO;
	}
	
	@PutMapping
	public ProdutoVO update(@RequestBody ProdutoVO produtoVO) {
		ProdutoVO retornoProdutoVO = this.produtoService.update(produtoVO);
		retornoProdutoVO.add(linkTo(methodOn(ProdutoController.class).findById(retornoProdutoVO.getId())).withSelfRel());
		return retornoProdutoVO;
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		this.produtoService.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/{id}")
	public ProdutoVO findById(@PathVariable("id") Long id) {
		ProdutoVO produtoVO =  this.produtoService.findById(id);
		produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
		return produtoVO;
	}
	
	@GetMapping
	public ResponseEntity<?> findAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<ProdutoVO> produtos = produtoService.findAll(pageable);
		produtos.stream().forEach(p -> 
			p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel())
		);
		
		PagedModel<EntityModel<ProdutoVO>> pagedModel = pagedResourcesAssembler.toModel(produtos);
		return new ResponseEntity<>(pagedModel, HttpStatus.OK);
	}
	
	
}
