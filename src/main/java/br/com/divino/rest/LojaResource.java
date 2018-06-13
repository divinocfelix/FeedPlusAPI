package br.com.divino.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.divino.model.Loja;

@Path("/loja")
public class LojaResource {
	
	private static Map<Long, Loja> mapa = new HashMap<Long, Loja>();
	
	public LojaResource() {
		this.populaLojas();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLoja(@PathParam("id") Long id) {
		Loja loja = this.mapa.get(id);
		return Response.status(200).entity(loja).type(MediaType.APPLICATION_JSON).build();
	}
	
	private void populaLojas() {
		this.mapa.put(1L, new Loja(1L, "Lojas Americanas", "https://pbs.twimg.com/profile_images/755034518520754176/EEBwUNXV_400x400.jpg"));
		this.mapa.put(2L, new Loja(2L, "Submarino", "https://pbs.twimg.com/profile_images/937768180759572480/AMrh1U14_200x200.jpg"));
		this.mapa.put(3L, new Loja(3L, "Pontofrio", "http://ligiagiacomini.com.br/wp-content/uploads/2017/12/ponto-frio-logo.png"));
		this.mapa.put(4L, new Loja(4L, "Magazine Luiza", "http://www.perolapanfletos.com.br/wp-content/uploads/2017/07/magazine-500.png"));
		this.mapa.put(5L, new Loja(5L, "Extra", "http://www.gpabr.com/wp-content/uploads/2016/08/imprensa_extra.jpg"));
	}
}
