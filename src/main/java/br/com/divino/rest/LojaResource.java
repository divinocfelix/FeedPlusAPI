package br.com.divino.rest;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.divino.factory.LojaSingleton;
import br.com.divino.model.Loja;

@Path("/loja")
public class LojaResource {
	
	private static Map<Long, Loja> mapaLojas;
	
	public LojaResource() {
		mapaLojas = LojaSingleton.getInstance().getMapaLojas();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLoja(@PathParam("id") Long id) {
		Loja loja = this.mapaLojas.get(id);
		return Response.status(200).entity(loja).type(MediaType.APPLICATION_JSON).build();
	}
	
}
