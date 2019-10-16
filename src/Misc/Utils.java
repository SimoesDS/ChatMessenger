package Misc;

import Application.Core;
import Server.DbConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	static Usuario currUser;

	public static ArrayList<Object[]> getPreviewData() {
		ArrayList<Usuario> usersArr = Core.getUsersName();
		ArrayList<Message> messages = Core.getAllMessages();
		ArrayList<Object[]> messagesPrevil = new ArrayList<>();

		if (messages.size() > 0) {
			for (int i = 0; i < usersArr.size(); i++) {
				int qtdMsgs = Core.getMessagesInvolvesTarget(usersArr.get(i).getId()).size();
				if (qtdMsgs > 0) {
					Message lastMessage = Core.getMessagesInvolvesTarget(usersArr.get(i).getId()).get(qtdMsgs - 1);
					messagesPrevil.add(new Object[] { usersArr.get(i).getNome(), lastMessage.getMessage(), usersArr.get(i).isOnline() });
				}
			}
		}

		return messagesPrevil;
	}

	public static String[][] getEntireDialog(int targetId) {

		Object dialog[][] = Misc.DbUtils.getUserDialog(currUser.getId(), targetId);

		String returnTarget[][] = new String[dialog.length][3];

		for (int i = 0; i < dialog.length; i++) {
			returnTarget[i][0] = (String) dialog[i][2];
			returnTarget[i][1] = (String) dialog[i][1];
			returnTarget[i][2] = (String) dialog[i][4];
		}

		/*
		 * if(messages.size() > 0) messages.forEach(msg -> { msg.get Long currTime =
		 * Long.parseLong(replaceAllString(returnTarget[2], "[-\\.\\s:]", "")); Long
		 * nextTime = Long.parseLong(replaceAllString(returnTarget[j + 1][2],
		 * "[-\\.\\s:]", "")); if (currTime > nextTime) { aux = returnTarget[j];
		 * returnTarget[j] = returnTarget[j + 1]; returnTarget[j + 1] = aux;
		 * 
		 * });
		 */

		String aux[];

		for (int j = 0; j < dialog.length - 1; j++) {
			Long currTime = Long.parseLong(replaceAllString(returnTarget[j][2], "[-\\.\\s:]", ""));
			Long nextTime = Long.parseLong(replaceAllString(returnTarget[j + 1][2], "[-\\.\\s:]", ""));
			if (currTime > nextTime) {
				aux = returnTarget[j];
				returnTarget[j] = returnTarget[j + 1];
				returnTarget[j + 1] = aux;
			}
		}

		return returnTarget;
	}

	public static void setUSerSession() {
		currUser = Core.getUserSession();
	}

	// SE NAO ESTIVER SENDO USADA AO FINAL DE TUDO, DEIXARÃ� DE EXISTIR
	public static String[] getRegexMatches(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		String returnTarget[] = new String[matcher.groupCount()];

		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++)
				returnTarget[i] = matcher.group(i);
		}
		return returnTarget;
	}

	public static String replaceString(String text, String occurrence, String entry) {
		return text.replace(occurrence, entry);
	}

	public static String replaceAllString(String text, String occurrences, String entry) {
		return text.replaceAll(occurrences, entry);
	}

	public static String[] insertStringBreak(String string) {
		String aux = "";
		String returnTarget[] = new String[2];
		int heightPlus = 22;

		Pattern p = Pattern.compile(".{20,30}(\\s)");
		Matcher m = p.matcher(string);
		while (m.find()) {
			aux += m.group() + "<br />";
			heightPlus += 13;
		}

		aux += string.replace(aux.replaceAll("<br />", ""), "") + "</html>";
		if (string.length() > 20 && m.groupCount() == 1)
			heightPlus += 9;

		returnTarget[0] = "<html>" + aux + "</html>";
		returnTarget[1] = String.valueOf(heightPlus);

		return returnTarget;
	}

	public static Object[][] getUserDialog(int userId, int targetId) {
//		ArrayList<Message> messages = Core.getMessages();
//
//		if (messages.size() > 0)
//			messages.forEach((msg) -> {
//				if((msg.getIdSender() == userId && msg.getIdReceiver() == targetId) ||
//						(msg.getIdSender() == targetId && msg.getIdReceiver() == userId))
//
//					
//					? msg.getIdReceiver() : msg.getIdSender();
//				if (!usersID.contains(id)) {
//					usersID.add(id);
//						usersArr.forEach((usr) -> {
//							if (usr.getId() == id)
//								messagesPrevil.add(new Object[] { usr.getNome(), msg.getMessage() });
//						});
//				}
//			});    

		return DbConnection.getData("messages", "*", "(message_owner = " + userId + " AND message_receiver = " + targetId
				+ ") OR (message_owner = " + targetId + " AND message_receiver = " + userId + ")");
	}
}
