package Communication;

public interface ICommands {

  public static final int AUTHENTICATE = 0; 		// Requisição para se autenticar
  public static final int AUTHENTICATED = 1; 		// Resposta login com sucesso 
  public static final int UNREGISTERED = 2; 		// Resposta do servidor, usuario nao encontrado
  public static final int MESSAGE = 3; 					// Resposta, há uma nova mensagem
  public static final int LOGGED = 4; 					// Resposta do servidor, usuario está logado
  public static final int LOGOUT = 5; 					// Requisiçõa, fazer logout no sistema
  public static final int STATUS = 6; 					// Resposta, atualiza status
}