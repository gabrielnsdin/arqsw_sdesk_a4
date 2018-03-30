package br.usjt.arqsw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.usjt.arqsw.entity.Chamado;
import br.usjt.arqsw.entity.Fila;
import br.usjt.arqsw.service.ChamadoService;
import br.usjt.arqsw.service.FilaService;

@RestController
public class ManterChamadosControllerRest {
	private FilaService filaService;
	private ChamadoService chamadoService;

	@Autowired
	public ManterChamadosControllerRest(FilaService fs, ChamadoService cs) {
		filaService = fs;
		chamadoService = cs;
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET , value="/index")
	public String inicio() {
		return "index";
	}

	private List<Fila> listarFilas() throws IOException{
			return filaService.listarFilas();
	}
	
	/**
	 * 
	 * @param model Acesso a request http
	 * @return JSP de Listar Chamados
	 */
	@RequestMapping(method=RequestMethod.GET , value="/fila")
	public List<Fila> listarFilasExibir() {
		List<Fila> lista = null;
		try {
			lista = listarFilas();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	@RequestMapping(method=RequestMethod.GET , value="/chamado/{id}")
	public @ResponseBody List<Chamado> listarChamados(@PathVariable("id") Long id){
		List<Chamado> lista = null;
		Fila fila = null;
		
		try {
			fila = new Fila();
			fila.setId(id.intValue());
			lista = chamadoService.listarChamados(fila);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value="/chamado")
	public ResponseEntity<Chamado> abrirChamado(@RequestBody Chamado chamado){
		try {
			chamadoService.novoChamado(chamado);
			return new ResponseEntity<Chamado>(chamado, HttpStatus.OK);
		}catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Chamado>(chamado, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.PUT, value="/chamado")
	public ResponseEntity<List<Chamado>> fecharChamado(@RequestBody Fila fila){
		List<Chamado> lista = null;
		try {
			lista = chamadoService.listarChamados(fila);
			chamadoService.fecharChamados(lista);
			lista = chamadoService.listarChamados(fila);
			return new ResponseEntity<List<Chamado>>(lista, HttpStatus.OK);
		}catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<List<Chamado>>(lista, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
