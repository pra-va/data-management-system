package utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class API {

	private static void post(String apiURL, String POST_PARAMS, String entity, String session_id) throws IOException {
		URL obj = new URL(apiURL);
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		postConnection.setRequestProperty("Cookie", "JSESSIONID=" + session_id);
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Content-Type", "application/json");
		postConnection.setDoOutput(true);
		OutputStream os = postConnection.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		int responseCode = postConnection.getResponseCode();
		// System.out.println("POST Response Code : " + responseCode);
		// System.out.println("POST Response Message : " +
		// postConnection.getResponseMessage());
		if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// System.out.println(response.toString());
		} else {
			// System.out.println(entity + " WAS NOT CREATED");
		}
	}

	public static void delete(String apiURL, String session_id) throws IOException {
		URL obj = new URL(apiURL);
		HttpURLConnection deleteConnection = (HttpURLConnection) obj.openConnection();
		deleteConnection.setRequestProperty("Cookie", "JSESSIONID=" + session_id);
		deleteConnection.setRequestMethod("DELETE");
		deleteConnection.connect();
		deleteConnection.disconnect();
		int responseCode = deleteConnection.getResponseCode();
		System.out.println("POST Response Code :  " + responseCode);
		System.out.println("POST Response Message : " + deleteConnection.getResponseMessage());
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(deleteConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// System.out.println(response.toString());
		} else {
			// System.out.println(entity + " WAS NOT CREATED");
		}
	}

//	public static String get(String apiURL, String session_id) throws IOException {
//		URL obj = new URL(apiURL);
//		HttpURLConnection getConnection = (HttpURLConnection) obj.openConnection();
//		getConnection.setRequestProperty("Cookie", "JSESSIONID=" + session_id);
//		getConnection.setRequestMethod("GET");
//		getConnection.connect();
//		getConnection.disconnect();
//		StringBuilder result = new StringBuilder();
//		InputStream in = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
//		  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//          String line;
//          while ((line = reader.readLine()) != null) {
//              result.append(line);
//          }
//
//	}

	public static void createUser(String groupList, String firstName, String lastName, String password, String username,
			String session_id) throws IOException {
		final String POST_PARAMS = "{\n" + "   \"groupList\": " + groupList + ",\n" + "    \"name\": \"" + firstName
				+ "\",\n" + "    \"password\": \"" + password + "\",\n" + "    \"surname\": \"" + lastName + "\",\n"
				+ "    \"username\": \"" + username + "\"" + "\n}";
		System.out.println(POST_PARAMS);
		String apiUrl = "http://akademijait.vtmc.lt:8180/dvs/api/createuser";
		String entity = "USER";
		post(apiUrl, POST_PARAMS, entity, session_id);
	}

	public static void createAdmin(String groupList, String firstName, String lastName, String password,
			String username, String session_id) throws IOException {
		final String POST_PARAMS = "{\n" + "   \"groupList\": " + groupList + ",\n" + "    \"name\": \"" + firstName
				+ "\",\n" + "    \"password\": \"" + password + "\",\n" + "    \"surname\": \"" + lastName + "\",\n"
				+ "    \"username\": \"" + username + "\"" + "\n}";
		System.out.println(POST_PARAMS);
		String apiUrl = "http://akademijait.vtmc.lt:8180/dvs/api/createadmin";
		String entity = "ADMIN";
		post(apiUrl, POST_PARAMS, entity, session_id);
	}

	public static void createGroup(String description, String docTypesToCreate, String docTypesToSign, String groupName,
			String userList, String session_id) throws IOException {
		final String POST_PARAMS = "{\n" + "    \"description\": \"" + description + "\",\n"
				+ "    \"docTypesToCreate\": " + docTypesToCreate + ",\n" + "    \"docTypesToSign\": " + docTypesToSign
				+ ",\n" + "    \"groupName\": \"" + groupName + "\",\n" + "    \"userList\": " + userList + "" + "\n}";
		System.out.println(POST_PARAMS);
		String apiUrl = "http://akademijait.vtmc.lt:8180/dvs/api/creategroup";
		String entity = "GROUP";
		post(apiUrl, POST_PARAMS, entity, session_id);
	}

	public static void createDocType(String groupsThatApprove, String groupsThatCreate, String docTypeName,
			String session_id) throws IOException {
		final String POST_PARAMS = "{\n" + "    \"approving\": " + groupsThatApprove + ",\n" + "    \"creating\": "
				+ groupsThatCreate + ",\n" + "    \"name\": \"" + docTypeName + "\"\n}";
		System.out.println(POST_PARAMS);
		String apiUrl = "http://akademijait.vtmc.lt:8180/dvs/api/doct/create";
		String entity = "DOCTYPE";
		post(apiUrl, POST_PARAMS, entity, session_id);
	}
//
//	public static void createDocument(String authorUsername, String description, String docType, String name)
//			throws IOException {
//		final String POST_DOCUMENT_PARAMS = "{\n" + "    \"authorUsername\": \"" + authorUsername + "\",\n"
//				+ "    \"description\": \"" + description + "\",\n" + "    \"docType\": \"" + docType + "\",\n"
//				+ "    \"name\": \"" + name + "\"" + "\n}";
//		System.out.println(POST_DOCUMENT_PARAMS);
//		URL obj = new URL("http://akademijait.vtmc.lt:8180/dvs/api/doc/create");
//		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
//		postConnection.setRequestMethod("POST");
//		postConnection.setRequestProperty("Content-Type", "application/json");
//		postConnection.setDoOutput(true);
//		OutputStream os = postConnection.getOutputStream();
//		os.write(POST_DOCUMENT_PARAMS.getBytes());
//		os.flush();
//		os.close();
//		int responseCode = postConnection.getResponseCode();
//		System.out.println("POST Response Code :  " + responseCode);
//		System.out.println("POST Response Message : " + postConnection.getResponseMessage());
//		String docID = "";
//		if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
//			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//			// print result
//			docID = response.toString();
//		} else {
//			System.out.println("DOCUMENT WAS NOT CREATED");
//		}
//
//		URL uploadFile = new URL("http://akademijait.vtmc.lt:8180/dvs/api/doc/upload/" + docID + "");
//		File file = new File("src/test/java/utilities/testFile.pdf");
//		HttpURLConnection postFileConnection = (HttpURLConnection) uploadFile.openConnection();
//		postFileConnection.setRequestMethod("POST");
//		postFileConnection.setRequestProperty("Content-Type", "application/pdf");
//		postFileConnection.setDoOutput(true);
//		BufferedOutputStream bos = new BufferedOutputStream(postFileConnection.getOutputStream());
//		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
//		int i;
//		// read byte by byte until end of stream
//		while ((i = bis.read()) > 0) {
//			bos.write(i);
//		}
//		bis.close();
//		bos.close();
//		int responseCode2 = postFileConnection.getResponseCode();
//		System.out.println("POST Response Code :  " + responseCode2);
//		System.out.println("POST Response Message : " + postFileConnection.getResponseMessage());
//
//		if (responseCode2 == HttpURLConnection.HTTP_CREATED) { // success
//			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//			// print result
//			System.out.println(response.toString());
//		} else {
//			System.out.println("FILE WAS NOT ATTACHED");
//		}
//	}

	public static void deleteUser(String userName, String session_id) throws IOException {
		String deleteUserApiURL = "http://akademijait.vtmc.lt:8180/dvs/api/delete/" + userName;
		delete(deleteUserApiURL, session_id);
	}

	public static void deleteGroup(String groupName, String session_id) throws IOException {
		String deleteGroupApiURL = "http://akademijait.vtmc.lt:8180/dvs/api/group/" + groupName + "/delete";
		delete(deleteGroupApiURL, session_id);
	}

	public static void deleteDoctype(String docTypeName, String session_id) throws IOException {
		String deleteDocTypeApiURL = "http://akademijait.vtmc.lt:8180/dvs/api/doct/delete/" + docTypeName;
		delete(deleteDocTypeApiURL, session_id);
	}

	public static void deleteFile(String fileID, String session_id) throws IOException {
		String deleteFileApiURL = "http://akademijait.vtmc.lt:8180/dvs/api/files/delete/" + fileID;
		delete(deleteFileApiURL, session_id);
	}

	// Document is not deleted when file is attached, use deleteFile method first
	public static void deleteDocument(String documentID, String session_id) throws IOException {
		String deleteDocumentApi = "http://akademijait.vtmc.lt:8180/dvs/api/doc/delete/" + documentID;
		delete(deleteDocumentApi, session_id);
	}

//	public static String getFileDetails(String documentID, String session_id) throws IOException {
//		String getFileDetailsApiURL = "http://akademijait.vtmc.lt:8180/dvs/api/files/info/docname/" + documentID;
//		get(getFileDetailsApiURL, session_id);
//		return response;

}
