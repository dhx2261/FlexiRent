package model;

import org.hsqldb.util.DatabaseManagerSwing;

public class RunDBGUI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DatabaseManagerSwing.main(new String[] {
				"--url","jdbc:hsqldb:file:database/FlexiRent","--noexit"
		});
	}

}
