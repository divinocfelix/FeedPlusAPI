package br.com.divino.factory;

import java.util.HashMap;
import java.util.Map;

import br.com.divino.model.Loja;

public class LojaSingleton {
	
	private static LojaSingleton instance; 
	private static Map<Long, Loja> mapaLojas;
	
	private LojaSingleton() {
		// TODO Auto-generated constructor stub
	}
	
	static {
		instance = instance != null ? instance : new LojaSingleton();
		
		mapaLojas = new HashMap<Long, Loja>();
		mapaLojas.put(1L, new Loja(1L, "Lojas Americanas", "https://pbs.twimg.com/profile_images/755034518520754176/EEBwUNXV_400x400.jpg"));
		mapaLojas.put(2L, new Loja(2L, "Submarino", "https://pbs.twimg.com/profile_images/937768180759572480/AMrh1U14_200x200.jpg"));
		mapaLojas.put(3L, new Loja(3L, "Pontofrio", "http://ligiagiacomini.com.br/wp-content/uploads/2017/12/ponto-frio-logo.png"));
		mapaLojas.put(4L, new Loja(4L, "Magazine Luiza", "http://www.perolapanfletos.com.br/wp-content/uploads/2017/07/magazine-500.png"));
		mapaLojas.put(5L, new Loja(5L, "Extra", "http://www.gpabr.com/wp-content/uploads/2016/08/imprensa_extra.jpg"));
	}
	
	public static LojaSingleton getInstance() {
		synchronized (instance) {
			instance = instance != null ? instance : new LojaSingleton();
		}
		 
		return instance;
	}
	
	public static Map<Long, Loja> getMapaLojas() {
		return mapaLojas;
	}
}
