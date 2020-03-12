package lt.vtmc.statistics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lt.vtmc.docTypes.model.DocType;
import lt.vtmc.documents.Status;
import lt.vtmc.documents.dao.DocumentRepository;
import lt.vtmc.documents.model.Document;
import lt.vtmc.groups.model.Group;
import lt.vtmc.statistics.dto.StatisticsDocTypeDTO;
import lt.vtmc.statistics.dto.StatisticsUserDTO;
import lt.vtmc.user.dao.UserRepository;
import lt.vtmc.user.model.User;

@Service
public class StatService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DocumentRepository docRepo;

	public List<StatisticsDocTypeDTO> getDocTypeStatistics(String username, int startDate, int endDate) {
		// Finding user by received username
		User tmpUser = userRepository.findUserByUsername(username);

		// Finding users group list
		List<Group> tmpGroupList = tmpUser.getGroupList();

		// Finding DocTypes to approve
		List<DocType> tmpList = new ArrayList<DocType>();
		for (Group group : tmpGroupList) {
			tmpList.addAll(group.getDocTypesToApprove());
		}

		List<StatisticsDocTypeDTO> statToReturn = new ArrayList<StatisticsDocTypeDTO>();
		for (DocType dType : tmpList) {
			// Finding all documents for selected docType
			List<Document> tmpListDoc = new ArrayList<Document>();
			tmpListDoc.addAll(docRepo.findAllBydType(dType));

			// Creating temporary StatisticsDTO object with docType name
			StatisticsDocTypeDTO tmpDoc = new StatisticsDocTypeDTO();
			tmpDoc.setDocType(dType.getName());

			// Looping through every document of selected docType and filtering by status
			// and date frame
			for (Document document : tmpListDoc) {
				int dateSubmit = Integer.parseInt(document.getDateSubmit().toString().substring(0, 9).replace("-", ""));
				if (startDate <= dateSubmit & endDate >= dateSubmit) {
					if (document.getStatus() == Status.SUBMITTED) {
						tmpDoc.setNumberOfSubmitted((tmpDoc.getNumberOfSubmitted() + 1));
					}
					if (document.getStatus() == Status.ACCEPTED) {
						tmpDoc.setNumberOfAccepted((tmpDoc.getNumberOfAccepted() + 1));
					}
					if (document.getStatus() == Status.REJECTED) {
						tmpDoc.setNumberOfRejected((tmpDoc.getNumberOfRejected() + 1));
					}
					statToReturn.add(tmpDoc);
				}
			}
		}

		return statToReturn;

	}

	public List<StatisticsUserDTO> getUserStatistics(String username) {
		// Finding user by received username
		User tmpUser = userRepository.findUserByUsername(username);

		// Finding users group list
		List<Group> tmpGroupList = tmpUser.getGroupList();

		// Finding DocTypes to approve
		List<DocType> tmpList = new ArrayList<DocType>();

		for (Group group : tmpGroupList) {
			tmpList.addAll(group.getDocTypesToApprove());
		}

		List<Document> tmpListDoc = new ArrayList<Document>();

		for (DocType dType : tmpList) {
			// Finding all documents for selected docType
			tmpListDoc.addAll(docRepo.findAllBydType(dType));
		}

		Map<String, Integer> userMap = new HashMap<>();

		for (Document document : tmpListDoc) {
			Integer docCount = userMap.get(document.getAuthor().getUsername());
			if (docCount != null) {
				docCount++;
			} else {
				docCount = 1;
			}
			userMap.put(document.getAuthor().getUsername(), docCount);
		}

		List<StatisticsUserDTO> statToReturn = new ArrayList<StatisticsUserDTO>();

		for(Map.Entry<String, Integer> entry: userMap.entrySet()) {
			String usernameToReturn = entry.getKey();
			String name = userRepository.findUserByUsername(usernameToReturn).getName();
			String surname = userRepository.findUserByUsername(usernameToReturn).getSurname();
			int docNumber = entry.getValue();
			
			StatisticsUserDTO tmpUserToReturn = new StatisticsUserDTO();
			tmpUserToReturn.setUsername(usernameToReturn);
			tmpUserToReturn.setName(name);
			tmpUserToReturn.setSurname(surname);
			tmpUserToReturn.setDocNumber(docNumber);
			statToReturn.add(tmpUserToReturn);
						
		}
		
		return statToReturn;

	}
}
