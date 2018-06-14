package br.com.divino.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.divino.model.Usuario;

@Path("/usuario")
public class UsuarioResource {
	
	private final String EMAIL = "feedplusapi@gmail.com";
	private final String SENHA = "feed12345";
	private final String TITULO = "FeedPlus - Nova senha";
	private final String TEXTO = "Sua nova senha é: ";
	private final String SMTP_SERVER = "smtp.gmail.com";
	private final String SMTP_PORT = "587";
	private final String PROTOCOLO = "smtp";
		
	private static Map<String, Usuario> mapa = new HashMap<String, Usuario>();
	private Usuario admim;
	private Long id = 0L;
	
	public UsuarioResource() {
		this.populaUsuario();
	}
	
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Usuario usuario) {
		Response response;
		
		if(usuario.getEmail().trim().equals(this.admim.getEmail()) && 
			usuario.getSenha().trim().equals(this.admim.getSenha())) {
			response = this.criarResposta(MediaType.APPLICATION_JSON, 200, null);
		} else {
			response = this.criarResposta(MediaType.APPLICATION_JSON, 401, null);
		}
		
		return response;
	}
	
	@Path("/cadastrar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cadastrar(Usuario usuario) {
		Response response;
		
		if(this.campoValido(usuario.getEmail()) && 
		   this.campoValido(usuario.getSenha()) &&
		   this.campoValido(usuario.getNome()) && 
		   this.campoValido(usuario.getCpf())) {
			
			usuario.setId(this.getId());
			this.addUsuario(usuario);
			response = this.criarResposta(MediaType.APPLICATION_JSON, 201, null);
		} else {
			response = this.criarResposta(MediaType.APPLICATION_JSON, 400, null);
		}
		
		return response;
	}
	
	@Path("/emailValido")
	@POST
	public Response recuperarSenha(String email) {
		Response resposta = null;
		
		email = this.trataCampoEmail(email);
				
		if(this.mapa.containsKey(email)) {		
			resposta = Response.ok().build();
		} else {
			resposta = this.criarResposta(MediaType.APPLICATION_JSON, 404, null);
		}
		
		return resposta;
	}
	
	@POST
	@Path("/criarNovaSenha")
	public Response criarNovaSenha(String destino) {
		Response resposta = null;
		destino = this.trataCampoEmail(destino);
		
		if(this.mapa.containsKey(destino)) {
			try {
				String senha = this.gerarNovaSenha(5);
				Properties props = this.criarEmailProperties();
				Authenticator authenticator = this.criarAutentication();
				
				Session session = Session.getDefaultInstance(props, authenticator);
				session.setDebug(true);
				
				Message message = new MimeMessage(session);
				
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(destino));
				message.setFrom(new InternetAddress(EMAIL));
				message.setSubject(TITULO);
				message.setContent(TEXTO + senha, MediaType.TEXT_HTML);
				
				Transport transport = session.getTransport(PROTOCOLO);
				transport.connect(SMTP_SERVER, EMAIL, SENHA);
				message.saveChanges();
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
				
				resposta = Response.ok().build();
				
			} catch (Exception e) {
				resposta = this.criarResposta(MediaType.APPLICATION_JSON, 500, null);
			}
			
		} else {
			resposta = this.criarResposta(MediaType.APPLICATION_JSON, 404, null);
		}
		
		return resposta;
	}

	private Authenticator criarAutentication() {
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL, SENHA);
			}
		};
		return authenticator;
	}

	private Properties criarEmailProperties() {
		Properties props = new Properties();
		
		props.put("mail.transport.protocol", PROTOCOLO);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SMTP_SERVER);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.user", EMAIL);
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.port", "465");
		return props;
	}

	private String gerarNovaSenha(int tamanho) {
		return UUID.randomUUID().toString().substring(0, tamanho);
	}
	
	private Response criarResposta(String tipo, int status, Serializable entidade) {
		Response resposta = null;
		
		if(entidade!=null) {
			resposta = Response.status(status).type(tipo).entity(entidade).build();
		} else {
			resposta = Response.status(status).type(tipo).build();
		}
		
		return resposta;
	}
	
	private void populaUsuario(){
		Usuario user = new Usuario();
		user.setCpf("01234567890");
		user.setEmail("admin@teste.com");        
		user.setId(this.getId());
		user.setNome("admin");
		user.setSenha("12345");
		
		this.admim = user;
		this.addUsuario(user);
	}
	
	private boolean campoValido(String campo) {
		return campo!=null && !campo.trim().isEmpty() && campo.trim().length() > 0;
	}
	
	private Long getId() {
		return ++this.id;
	}
	
	private void addUsuario(Usuario user) {
		this.mapa.put(user.getEmail(), user);
	}
	
	private String trataCampoEmail(String email) {
		if(this.campoValido(email)) {
			email = email.replace("\"",""); 
		}
		
		return email;
	}
}
