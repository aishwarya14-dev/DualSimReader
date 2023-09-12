package com.cogostech.simreader;

import static com.cogostech.simreader.MainActivity.getMainActContext;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.content.Context;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.List;

public class SimReader {
    protected ArrayList<SimInfo> simitems;
    private static SimReader simReader;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;
//    private String imsiSIM2;

    private String sim1_STATE;
    private String sim2_STATE;

    private static int sim1SubId;
    private static int sim2SubId;

    private static String phoneNo1;
    private static String phoneNo2;
    //    private String sim1_IMSI;
//    private String sim2_IMSI;
    // Service provider name (SPN)
    private String sim1_SPN;
    private String sim2_SPN;
    // Mobile country code (MCC)
    private String sim1_MCC;
    private String sim2_MCC;
    // Mobile network code (MNC)
    private String sim1_MNC;
    private String sim2_MNC;


    public static SimReader getSimInfo() {
        SimReader.simReader = null;
        SimReader.simReader = new SimReader();
        Context context = getMainActContext();
        TelephonyManager telMgr = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

        simReader.sim1_SPN = telMgr.getSimOperatorName();
        simReader.sim1_MCC = telMgr.getNetworkCountryIso();
        simReader.sim1_MNC = telMgr.getNetworkOperatorName();

        simReader.isSIM1Ready = telMgr.getSimState() == TelephonyManager.SIM_STATE_READY;
        simReader.isSIM2Ready = false;

        simReader.sim1_STATE = simState(telMgr.getSimState());

        try {
            simReader.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (simReader.isSIM2Ready) {
            try {
                SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


                List<SubscriptionInfo> localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                    for(int i=0;i<localList.size();i++)
                        System.out.println(localList.get(i).getNumber() + " " + localList.get(i).getSimSlotIndex());
                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(1);
                simReader.sim2_SPN = simInfo1.getDisplayName().toString();
                SimReader.sim2SubId = simInfo1.getSubscriptionId();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED ||  ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        SimReader.phoneNo2 = localSubscriptionManager.getPhoneNumber(sim2SubId);
                    } else {
                        SimReader.phoneNo2 = telMgr.getLine1Number();

                    }
                    System.out.println(phoneNo2);

                }
                System.out.println("!");
            }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        simReader.simitems = new ArrayList<SimInfo>();

        simReader.simitems.add(new SimInfo("SIM 1 state", simReader.sim1_STATE));
        simReader.simitems.add(new SimInfo("Service provider name (SPN)", simReader.sim1_SPN));
        simReader.simitems.add(new SimInfo("Mobile country code (MCC)", simReader.sim1_MCC));
        simReader.simitems.add(new SimInfo("Mobile operator name", simReader.sim1_MNC));

        if (simReader.isSIM2Ready) {
            simReader.simitems.add(new SimInfo(" ", " "));
            simReader.simitems.add(new SimInfo("SIM 2 state", simReader.sim2_STATE));
            simReader.simitems.add(new SimInfo("Service provider name (SPN)", simReader.sim2_SPN));
            simReader.simitems.add(new SimInfo("Mobile country code (MCC)", simReader.sim2_MCC));
            simReader.simitems.add(new SimInfo("Mobile operator name", simReader.sim2_MNC));


            //telInf.scitemsArr.add(new Sci("NC (Neighboring Cell ", telInf.sim1_NC.toString() ));

        }

        return simReader;


    }

    private static String simState(int simState) {
        switch (simState) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "ABSENT";
            case 2:
                return "REQUIRED";
            case 3:
                return "PUK_REQUIRED";
            case 4:
                return "NETWORK_LOCKED";
            case 5:
                return "READY";
            case 6:
                return "NOT_READY";
            case 7:
                return "PERM_DISABLED";
            case 8:
                return "CARD_IO_ERROR";
        }
        return "??? " + simState;
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) {
        boolean isReady = false;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimState.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                simReader.sim2_STATE = simState(simState);
                if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isReady;
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }
//
//    private boolean isDualSIM() {
//        return imsiSIM2 != null;
//    }


}
