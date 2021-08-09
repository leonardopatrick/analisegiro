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

}
