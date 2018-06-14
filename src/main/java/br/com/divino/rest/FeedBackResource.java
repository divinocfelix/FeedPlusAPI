package br.com.divino.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.map.MultiKeyMap;

import br.com.divino.model.FeedBack;

@Path("/feedback")
public class FeedBackResource {
	
	private static Long idFeedBack = 0L;
	private static MultiKeyMap mapaFeedBack = new MultiKeyMap();
	
	@Path("/gravar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response gravar(Long clienteId, Long pedidoId, FeedBack feedBack) {
		this.idFeedBack++;
		feedBack.setId(this.idFeedBack);
		
		this.mapaFeedBack.put(clienteId, pedidoId, feedBack.getItem().getId(), feedBack);
		return Response.status(201).type(MediaType.APPLICATION_JSON).build();
	}
	
	@Path("/buscar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscar(Long clienteId, Long pedidoId, Long itemId) {
		FeedBack feedBack = (FeedBack) this.mapaFeedBack.get(clienteId, pedidoId, itemId);
		Response resposta = null;
		
		if(feedBack !=null) {
			resposta = Response.status(200).entity(feedBack).type(MediaType.APPLICATION_JSON).build();
		} else {
			Response.status(404).type(MediaType.APPLICATION_JSON).build();
		}
		
		return resposta;
	}
}
