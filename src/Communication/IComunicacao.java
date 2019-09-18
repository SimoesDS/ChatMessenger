package Communication;

import java.io.IOException;

public interface IComunicacao {

  void enviarBytes(byte[] dadosIN) throws IOException;
  int recebeBytes(byte[] dadosOUT, int inicio, int tamanho) throws IOException;
  void enviarObject(Object obj) throws IOException;
  Object recebeObject() throws IOException;
}
