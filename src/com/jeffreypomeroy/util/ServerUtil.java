package com.jeffreypomeroy.util;

import java.io.BufferedReader;

import org.json.JSONException;

import android.os.AsyncTask;

public class ServerUtil {

	private static final String THELOCALURLCMDPATHPREFIX = "http://192.168.0.59/index.php?job=getservercmds&operation=getservercmds&code=";
	private static final String THEURLCMDPATHPREFIX = "http://jeffreypomeroy.com/index.php?job=getservercmds&operation=getservercmds&code=";
	private static Object theRequest;

	public static void runServerStack(String theCode) throws RuntimeException {
		theRequest = new GetRequestAsync().execute(THEURLCMDPATHPREFIX
				+ theCode);
	}

	private static void doUpdate(String theReturnStr) throws JSONException {
		// printDialog("- touch outside of box to erase -", theData,
		// ByrdlandMain.this);
		String theLine, theCode, theParam1, theParam2;
		int theReturnAryLen, returnLp;

		String[] theReturnAry = theReturnStr.split("\n");
		theReturnAryLen = theReturnAry.length;

		for (returnLp = 0; returnLp < theReturnAryLen; returnLp++) {

			theLine = theReturnAry[returnLp];
			// String[] theLineAry = theLine.split(":");//
			// PatternSyntaxException if ?:
			GenUtil.doSplit(theLine, "?:");

			theCode = GenUtil.getTheField(0);
			theParam1 = GenUtil.getTheField(1);
			theParam2 = GenUtil.getTheField(2);// code: instbl, table:
												// byrdlandyoutubevideos,
												// json: events!!!, lp:
												// 7

			if ("chktbl".equalsIgnoreCase(theCode)) {
				
				if (!DbUtil.checkTable(theParam1)){
					DbUtil.createTable(theParam1);
				}

			} else if ("metatbl".equalsIgnoreCase(theCode)) {

				GenUtil.JsonLib.saveMeta(theParam1, theParam2);

			} else if ("clrtbl".equalsIgnoreCase(theCode)) {

				DbUtil.clearTable(theParam1);

			} else if ("instbl".equalsIgnoreCase(theCode)) {

				// - need to make this insert table
				
				GenUtil.JsonLib.getFromJson(theParam2);// theParam2 is
														// null for
														// byrdlandmembers
				int noItems = GenUtil.JsonLib.getLength(0);
				int retrieveLp;

				for (retrieveLp = 0; retrieveLp < noItems; retrieveLp++) {
					GenUtil.JsonLib.getRow(0, retrieveLp);// hangs on
															// this line
					String queryCols = GenUtil.JsonLib.getSqlColsStr(theParam1,
							0);
					String queryLine = "insert into " + theParam1 + " "
							+ queryCols;

					DbUtil.runUpdateQuery(queryLine);
				}

			} else if ("drptbl".equalsIgnoreCase(theCode)) {

				DbUtil.dropTable(theParam1);

			}
		}
	}

	// --- class ServerSync.GetRequestAsync

	private static class GetRequestAsync extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String returnStringFromServer) {
			// TODO Auto-generated method stub
			super.onPostExecute(returnStringFromServer);
			try {
				doUpdate(returnStringFromServer);
			} catch (JSONException e) {
				GenUtil.errorDisplay(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
