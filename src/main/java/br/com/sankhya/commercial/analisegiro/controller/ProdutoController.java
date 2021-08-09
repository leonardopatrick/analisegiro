package br.com.sankhya.commercial.analisegiro.controller;

import java.util.List;
import java.util.Optional;

import br.com.sankhya.commercial.analisegiro.elasticsearch.model.ProductElastic;
import br.com.sankhya.commercial.analisegiro.elasticsearch.repository.ProductRepositoryElastic;
import br.com.sankhya.commercial.analisegiro.model.Produto;
import br.com.sankhya.commercial.analisegiro.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoController {
	
	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	ProductRepositoryElastic produtoRepositoryElastic;

	@GetMapping("/products")
	@Operation(summary = "Get all products")
	public ResponseEntity listProduct() {
		
		List<Produto> products = produtoRepository.findAll();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@Operation(summary = "Get a products by its id")
	@GetMapping("/product/{id}")
	public ResponseEntity listProduct(@PathVariable(value = "id") long id, @AuthenticationPrincipal UserDetails auth) {
		Optional<Produto> product = produtoRepository.findById(id);

		if(product.isEmpty()){
			return new ResponseEntity<>(product, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	@PostMapping("/product")
	@Operation(summary = "Create a product [ROLE_ADMIN]")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity createProduct(@RequestBody Produto product) {
		produtoRepository.save(product);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@GetMapping("/elastic/products")
	@Operation(summary = "List products elastic search")
	public ResponseEntity listProductElastic() {

		Iterable<ProductElastic> products = produtoRepositoryElastic.findAll();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

}
