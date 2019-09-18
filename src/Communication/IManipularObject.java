package Communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public final class IManipularObject{
	private IObject object = new DadosSerial();
	
	public interface IObject {
		Object convertByteToObject(byte[] byteObj);
		byte[] convertObjectToByte(Object obj);
	}
	
	public Object byteToObject(byte[] byteObj) {
		if(byteObj != null)
			return object.convertByteToObject(byteObj);
		else
			return null;
	}
	
	public byte[] objectToByte(Object obj){
		if(obj != null)
			return object.convertObjectToByte(obj);
		else 
			return null;
		
	}
	
	public static class DadosSerial implements IObject{

		@Override
		public Object convertByteToObject(byte[] byteObj){
			ByteArrayInputStream byteInpStr = new ByteArrayInputStream(byteObj);
			ObjectInputStream objInStr = null;
			Object obj = null;
			try {
				objInStr = new ObjectInputStream(byteInpStr);
				obj = objInStr.readObject();
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					objInStr.close();
				} catch (IOException e) {}
			}
			return obj;
		}

		@Override
		public byte[] convertObjectToByte(Object obj){
			ByteArrayOutputStream byteOutStr = new ByteArrayOutputStream();
			ObjectOutput objOut = null;
			byte[] byteObj = null;
			try {
				objOut = new ObjectOutputStream(byteOutStr);   
				objOut.writeObject(obj);
				objOut.flush();
				byteObj = byteOutStr.toByteArray();
			}catch(IOException e){
				System.out.println("Erro>>> " + e);
			}finally {
				try {
					byteOutStr.close();
				} catch (IOException ex){}
			}
			return byteObj;
		}		
	}
}