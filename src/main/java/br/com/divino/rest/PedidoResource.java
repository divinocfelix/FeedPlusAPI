package br.com.divino.rest;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.divino.factory.LojaSingleton;
import br.com.divino.model.Item;
import br.com.divino.model.Loja;
import br.com.divino.model.Pedido;
import br.com.divino.model.Usuario;

@Path("/pedido")
public class PedidoResource {
	
	private final String CHAVE_TOTAL_ITENS = "total.itens";
	private final String CHAVE_ITEM_DESCRICAO = "item.{0}.descricao";
	private final String CHAVE_ITEM_UNIDADE = "item.{0}.unidade";
	private final String CHAVE_ITEM_PRECO = "item.{0}.preco";
	private final String CHAVE_ITEM_QTDE = "item.{0}.qtde";
	private final String CHAVE_ITEM_FOTO = "item.{0}.foto";
	private final String CHAVE_ITEM_MARCA = "item.{0}.marca";
	
	private static Map<Long, Loja> mapaLojas;
	
	public PedidoResource() {
		mapaLojas = LojaSingleton.getInstance().getMapaLojas();
	}
	
	@Path("/buscarPorUsuario")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorUsuario(Usuario usuario) {
		Response response = null;
		
		try {
			Properties properties = this.retornaPropertis();
			List<Pedido> lista = this.montaPedidos(usuario, properties);
			
			GenericEntity<List<Pedido>> entity = new GenericEntity<List<Pedido>>(lista){};
			response = Response.ok().entity(entity).type(MediaType.APPLICATION_JSON).build();
		}catch(Exception e) {
			response = Response.status(500).type(MediaType.APPLICATION_JSON).build();
		}
		
		return response;
	}

	private List<Pedido> montaPedidos(Usuario usuario, Properties properties) {
		Map<Long, Pedido> mapaPedidos = new HashMap<Long, Pedido>();
		
		if(!properties.isEmpty()) {
			
			int totalItens = Integer.parseInt(properties.getProperty("total.itens"));
			int totalItemLido = 0;
			Long idItem = 0L;
			
			while(totalItemLido < totalItens) {
				for(Long i=0L; i < this.mapaLojas.size(); i++ ) {
					
					if(totalItemLido >= totalItens) {
						break;
					}
					
					Loja loja = this.mapaLojas.get(i+1);
					Pedido pedido = null;
					
					//Cria o pedido
					if(!mapaPedidos.containsKey(loja.getId())) {
						pedido = new Pedido();
						pedido.setId(i+1);
						pedido.setCodigo("000".concat(String.valueOf((i+1))));
						pedido.setLoja(loja);
						pedido.setUsuario(usuario);
						
						Calendar call = GregorianCalendar.getInstance();
						call.add(Calendar.DAY_OF_MONTH, -(i.intValue()+1));
						pedido.setData(call.getTime());
					}else {
						//Pega o pedido para atualizar
						pedido = mapaPedidos.get(loja.getId());
					}
					
					totalItemLido++;
					idItem++;
					
					Item item = this.montarItem(properties, idItem, totalItemLido);
					
					pedido.getItens().add(item);
					mapaPedidos.put(loja.getId(), pedido);
				}
			}
		}
		
		List<Pedido> lista = new ArrayList<Pedido>(mapaPedidos.values());
		BigDecimal total = new BigDecimal(0);
		
		for(Pedido p : lista) {
			for(Item it : p.getItens()) {
				total = total.add(new BigDecimal(it.getQuantidade()).multiply(it.getPreco()));
			}
			
			p.setTotal(total.setScale(2));
		}
		return lista;
	}

	private Properties retornaPropertis() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("/pedidos.properties");
		Properties properties = new Properties();
		properties.load(in);
		return properties;
	}
	
	private Item montarItem(Properties properties, Long idItem, int itemLido) {
		String descricao = properties.getProperty(MessageFormat.format(CHAVE_ITEM_DESCRICAO, itemLido));
		String foto = properties.getProperty(MessageFormat.format(CHAVE_ITEM_FOTO, itemLido));
		String marca = properties.getProperty(MessageFormat.format(CHAVE_ITEM_MARCA, itemLido));
		String unidade = properties.getProperty(MessageFormat.format(CHAVE_ITEM_UNIDADE, itemLido));
		BigDecimal preco = new BigDecimal(properties.getProperty(MessageFormat.format(CHAVE_ITEM_PRECO, itemLido)));
		Double qtde = Double.parseDouble(properties.getProperty(MessageFormat.format(CHAVE_ITEM_QTDE, itemLido)));
		
		Item item = new Item();
		item.setId(idItem);
		item.setCodigo("0000".concat(idItem.toString()));
		item.setDescricao(descricao);
		item.setFoto(foto);
		item.setMarca(marca);
		item.setPreco(preco);
		item.setQuantidade(qtde);
		item.setUnidade(unidade);
		
		return item;
	}
}
