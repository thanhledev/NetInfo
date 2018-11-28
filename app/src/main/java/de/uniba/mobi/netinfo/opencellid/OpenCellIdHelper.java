package de.uniba.mobi.netinfo.opencellid;

import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.uniba.mobi.netinfo.NetInfo;

public class OpenCellIdHelper {

    // public properties
    public static String apiToken = "ad64f825b45bb0";
    public static String apiServer = "https://eu1.unwiredlabs.com/v2/process.php";

    // instance property
    private static OpenCellIdHelper instance;

    // private properties

    // constructors
    private OpenCellIdHelper() {

    }

    public static OpenCellIdHelper getInstance() {
        if(instance == null) {
            synchronized (OpenCellIdHelper.class) {
                if(instance == null) {
                    instance = new OpenCellIdHelper();
                }
            }
        }

        return instance;
    }

    public String getJsonBody() {
        try {
            // create jsonObject from information of NetInfo
            NetInfo.getInstance().getCellInfo();

            JSONObject mainJSONObj = new JSONObject();
            mainJSONObj.put("token", apiToken);
            mainJSONObj.put("radio", NetInfo.mobilePhoneType);
            mainJSONObj.put("mcc", NetInfo.mobileCountryCode);
            mainJSONObj.put("mnc", NetInfo.mobileNetworkCode);
            mainJSONObj.put("address", 1);

            // cell array
            JSONArray cells = new JSONArray();
            JSONObject cell = new JSONObject();
            cell.put("lac", NetInfo.locationAreaCode);
            cell.put("cid", NetInfo.cellIdentifier);
            cells.put(cell);
            mainJSONObj.put("cells", cells);

            return mainJSONObj.toString();

        } catch (JSONException e) {
            return "";
        }
    }
}
