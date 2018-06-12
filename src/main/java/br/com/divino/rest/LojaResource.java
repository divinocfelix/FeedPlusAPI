package br.com.divino.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.divino.model.Loja;

@Path("/lojas")
public class LojaResource {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Loja getLoja(@PathParam("id") Long id) {
		return new Loja(1L, "teste", "http://teste.com.br");
	}
}
