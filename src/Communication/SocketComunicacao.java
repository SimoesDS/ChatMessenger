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
	public void enviarObject(Object obj) throws IOException {
		outStr.write(mObject.objectToByte(obj));
		
	}
	@Override
	synchronized public Object recebeObject() throws IOException {
		byte[] dadosOUT = new byte[4096];
		int tamanho = inpStr.read(dadosOUT, 0, dadosOUT.length);
		if(tamanho > 0)
			return mObject.byteToObject(dadosOUT); 
		return null;
		
	}

	@Override
	public void closeConnection() {
		try {
			inpStr.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		try {
			outStr.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}		
	}

}
