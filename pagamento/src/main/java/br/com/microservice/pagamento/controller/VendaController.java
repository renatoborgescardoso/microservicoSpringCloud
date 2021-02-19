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

import br.com.microservice.pagamento.data.vo.VendaVO;
import br.com.microservice.pagamento.service.VendaService;

@RestController
@RequestMapping(value = "/vendas")
public class VendaController {

	private VendaService vendaService;
	private PagedResourcesAssembler<VendaVO> pagedResourcesAssembler;

	@Autowired
	public VendaController(VendaService vendaService, PagedResourcesAssembler<VendaVO> pagedResourcesAssembler) {
		this.vendaService = vendaService;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public VendaVO save(@RequestBody VendaVO vendaVO) {
		VendaVO retornoVendaVO = this.vendaService.save(vendaVO);
		retornoVendaVO.add(linkTo(methodOn(VendaController.class).findById(retornoVendaVO.getId())).withSelfRel());
		return retornoVendaVO;
	}
	
	@PutMapping
	public VendaVO update(@RequestBody VendaVO vendaVO) {
		VendaVO retornoVendaVO = this.vendaService.update(vendaVO);
		retornoVendaVO.add(linkTo(methodOn(VendaController.class).findById(retornoVendaVO.getId())).withSelfRel());
		return retornoVendaVO;
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		this.vendaService.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/{id}")
	public VendaVO findById(@PathVariable("id") Long id) {
		VendaVO vendaVO =  this.vendaService.findById(id);
		vendaVO.add(linkTo(methodOn(VendaController.class).findById(id)).withSelfRel());
		return vendaVO;
	}
	
	@GetMapping
	public ResponseEntity<?> findAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "data"));
		
		Page<VendaVO> vendas = vendaService.findAll(pageable);
		vendas.stream().forEach(p -> 
			p.add(linkTo(methodOn(VendaController.class).findById(p.getId())).withSelfRel())
		);
		
		PagedModel<EntityModel<VendaVO>> pagedModel = pagedResourcesAssembler.toModel(vendas);
		return new ResponseEntity<>(pagedModel, HttpStatus.OK);
	}
	
	
}
