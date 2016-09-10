

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;


public class CSVLoader {

	public CSVLoader() {
		
	}

	public void loadRequirements(InputStream input) {
		/*//Collection<IRequirement> result;
		try {
			//result = new LinkedList<IRequirement>();

			InputStreamReader fr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(fr);
			String req, name, text, classification;
			while ((req = br.readLine()) != null) {
				name = req.split(";")[0];
				text = req.split(";")[1];
				classification = req.split(";")[2];
				RequirementClassification tagT = RequirementClassification
						.valueOf(classification);

				result.add(ucmFactory.constructRequirement(name, text, tagT));
			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();

			//result = null;
		}
*/
		//return result;
	}

}
