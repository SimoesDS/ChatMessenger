package Communication;

public interface ICommands {

  public static final int AUTHENTICATE = 0; 		// Código para fazer login
  public static final int AUTHENTICATED = 1; 		// Código login com sucesso
  public static final int UNAUTHENTICATE = 2; 	// Código de resposta do servidor, erro no login, Client deve enviar novamente os dados 
  public static final int UNREGISTERED = 4; 		// Código de resposta do servidor, usuario nao encontrado
  public static final int MESSAGE = 5; 			// Código indicando que há uma mensagem
  public static final int LOGGED = 6; 			// Código indicando que o usuario esta logado
  public static final int LIST_CONTACTS = 7; 		// Código requisitando a lista de contatos
  public static final int FAIL = 8; 		// Código de resposta do servidor, erro nao encontrado
  public static final int SUCCESS = 9; 		// Código de resposta do servidor, dados enviado com sucesso
  public static final int HANDLERAUTHENTICATE = 10; 		// Código de resposta do cliente, nao precisa autenticar
  public static final int LOGOUT = 11; 		// Faz o logout no sistema
  
}