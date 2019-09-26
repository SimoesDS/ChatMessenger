package Communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketComunicacao implements IComunicacao{
	
	private InputStream inpStr;
	private OutputStream outStr;
	private IManipularObject mObject;

	public SocketComunicacao(Socket socket) throws IOException {
		mObject = new IManipularObject();
		inpStr = socket.getInputStream();
		outStr = socket.getOutputStream();
	}
	
	@Override
	public void enviarBytes(byte[] dadosIN) throws IOException {
		outStr.write(dadosIN);
	}

	@Override
	public int recebeBytes(byte[] dadosOUT, int inicio, int tamanho)
			throws IOException {
		return inpStr.read(dadosOUT, inicio, tamanho);
		
	}
	@Override
	public void enviarObject(Object obj) throws IOException {
		enviarBytes(mObject.objectToByte(obj));
		
	}
	@Override
	synchronized public Object recebeObject() throws IOException {
		byte[] dadosOUT = new byte[4096];
		int tamanho = recebeBytes(dadosOUT, 0, dadosOUT.length);
		if(tamanho > 0)
			return mObject.byteToObject(dadosOUT); 
		return null;
		
	}

}
