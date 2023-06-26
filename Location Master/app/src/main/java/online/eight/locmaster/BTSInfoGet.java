package online.eight.locmaster;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BTSInfoGet {

    private Context context;
    private TelephonyManager telephonyManager;

    public BTSInfoGet(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public boolean hasPermission() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    public Map<String, String> getBTSDict() {
        if (!hasPermission()) {
            throw new RuntimeException("Permission is not granted!");
        }
        Map<String, String> bts = new HashMap<>();

        if (telephonyManager != null) {
//            if (Build.VERSION.SDK_INT >= 28) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                for (CellInfo cellInfo : telephonyManager.getAllCellInfo()) {

                    if (cellInfo instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                        String mcc = String.valueOf(cellInfoLte.getCellIdentity().getMcc());
                        String mnc = String.valueOf(cellInfoLte.getCellIdentity().getMnc());
                        String cellId = String.valueOf(cellInfoLte.getCellIdentity().getCi());
                        String tac = String.valueOf(cellInfoLte.getCellIdentity().getTac());

                        bts.put("MCC", mcc);
                        bts.put("MNC", mnc);
                        bts.put("CellID", cellId);
                        bts.put("TAC", tac);
                    }
                }
//            } else {
//                if (telephonyManager.getCellLocation() != null) {
//                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//
//                        if (telephonyManager.getCellLocation() instanceof GsmCellLocation) {
//                            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
//                            String mcc = String.valueOf(gsmCellLocation.getPsc());
//                            String mnc = String.valueOf(gsmCellLocation.getLac());
//                            String cellId = String.valueOf(gsmCellLocation.getCid());
//
//                            bts.put("MCC", mcc);
//                            bts.put("MNC", mnc);
//                            bts.put("CellID", cellId);
//                        }
//                }
//            }
        }

        return bts;
    }


    public String getBTSInfo() {
        if (!hasPermission()) {
            return "Permission not granted";
        }

        StringBuilder btsInfoBuilder = new StringBuilder();
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    for (CellInfo cellInfo : telephonyManager.getAllCellInfo()) {
                        if (cellInfo instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            String mcc = String.valueOf(cellInfoLte.getCellIdentity().getMccString());
                            String mnc = String.valueOf(cellInfoLte.getCellIdentity().getMncString());
                            String cellId = String.valueOf(cellInfoLte.getCellIdentity().getCi());
                            String tac = String.valueOf(cellInfoLte.getCellIdentity().getTac());

                            btsInfoBuilder.append("MCC: ").append(mcc).append(", ");
                            btsInfoBuilder.append("MNC: ").append(mnc).append(", ");
                            btsInfoBuilder.append("Cell ID: ").append(cellId).append(", ");
                            btsInfoBuilder.append("TAC: ").append(tac).append("\n");
                        }
                    }
            } else {
                if (telephonyManager.getCellLocation() != null) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                        if (telephonyManager.getCellLocation() instanceof GsmCellLocation) {
                            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                            String mcc = String.valueOf(gsmCellLocation.getPsc());
                            String mnc = String.valueOf(gsmCellLocation.getLac());
                            String cellId = String.valueOf(gsmCellLocation.getCid());

                            btsInfoBuilder.append("MCC: ").append(mcc).append(", ");
                            btsInfoBuilder.append("MNC: ").append(mnc).append(", ");
                            btsInfoBuilder.append("Cell ID: ").append(cellId).append("\n");
                        }
                }
            }
        }

        return btsInfoBuilder.toString();
    }
}
