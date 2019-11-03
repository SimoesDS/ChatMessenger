package Communication;

import java.io.IOException;

public interface IComunicacao {

  void enviarObject(Object obj) throws IOException;
  Object recebeObject() throws IOException;
  void closeConnection();
}
