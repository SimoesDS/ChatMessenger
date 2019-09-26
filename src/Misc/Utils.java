package Misc;

import Application.Core;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	static Usuario currUser;

	public static String[][] getPreviewData() {
		Object dbMessages[][] = (Object[][]) Core.getMessages();
		ArrayList<Object[]> messagesPrevil = new ArrayList<>();
		ArrayList<Integer> usersID = new ArrayList<>();
		
		for (int i = 0; i < dbMessages.length; i++) {
			int id = (int) dbMessages[i][2] == currUser.getId() ? (int) dbMessages[i][3] : (int) dbMessages[i][2];
			if(!usersID.contains(id)) {
				usersID.add(id);
				Object msg[] = new Object[] { (String) Misc.DbUtils.findUserById(id)[1], dbMessages[i][1], dbMessages[i][4] };
				messagesPrevil.add(msg);
				;
			}
		}		
		
		String messagesPrevilToString[][] = new String[messagesPrevil.size()][3];		
		for (int i = 0; i < messagesPrevil.size(); i++) {
			messagesPrevilToString[i][0] = (String) messagesPrevil.get(i)[0]; 
			messagesPrevilToString[i][1] = (String) messagesPrevil.get(i)[1];
			//messagesPrevilToString[i][2] = (String) messagesPrevil.get(i)[2]; TODO: nÃ£o estÃ¡ retorna a data por enquanto
		}
		
		
		return messagesPrevilToString;
	}

	public static String[] getPreviewMessages() {
		String auxArr[][] = getPreviewData();
		String returnTarget[] = new String[auxArr.length];

		for (int i = 0; i < auxArr.length; i++)
			returnTarget[i] = (auxArr[i][1].length() >= 52) ? auxArr[i][1].substring(0, 49) + "..." : auxArr[i][1];

		return returnTarget;
	}

	public static String[] getPreviewUsers() {
		String auxArr[][] = getPreviewData();
		String returnTarget[] = new String[auxArr.length];

		for (int i = 0; i < auxArr.length; i++)
			returnTarget[i] = auxArr[i][0];

		return returnTarget;
	}

	public static String[][] getEntireDialog(int targetId) {
		Object dialog[][] = Misc.DbUtils.getUserDialog(currUser.getId(), targetId);

		String returnTarget[][] = new String[dialog.length][3];

		for (int i = 0; i < dialog.length; i++) {
			returnTarget[i][0] = Integer.parseInt((String) dialog[i][2]) == targetId ? String.valueOf(targetId)
					: String.valueOf(currUser.getId());
			returnTarget[i][1] = (String) dialog[i][1];
			returnTarget[i][2] = (String) dialog[i][4];
		}

		String aux[];

		for (Object[] dialog1 : dialog) {
			for (int j = 0; j < dialog.length - 1; j++) {
				Long currTime = Long.parseLong(replaceAllString(returnTarget[j][2], "[-\\.\\s:]", ""));
				Long nextTime = Long.parseLong(replaceAllString(returnTarget[j + 1][2], "[-\\.\\s:]", ""));
				if (currTime > nextTime) {
					aux = returnTarget[j];
					returnTarget[j] = returnTarget[j + 1];
					returnTarget[j + 1] = aux;
				}
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
}
