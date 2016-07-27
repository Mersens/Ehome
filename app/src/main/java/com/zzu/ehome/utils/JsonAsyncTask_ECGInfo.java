package com.zzu.ehome.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.zzu.ehome.application.Constants;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Dell on 2016/3/14.
 */
public class JsonAsyncTask_ECGInfo extends AsyncTask<Object, Integer, JSONObject> {
    private JsonAsyncTaskOnComplete	mySATCM					= null;
    private boolean					ShowProgressDialog		= false;
    private Context myContext				= null;
    private ProgressDialog myProgressDialog		= null;
    private String					ProgressDialogTitle		= "";
    private String					ProgressDialogMessage	= "努力加载中，请稍候....";


    public JsonAsyncTask_ECGInfo(Context context, boolean show,
                                 JsonAsyncTaskOnComplete method)
    {

        myContext = context;
        ShowProgressDialog = show;
        mySATCM = method;
    }


    @Override
    protected void onPreExecute()
    {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject result)
    {
        // TODO Auto-generated method stub
        if (myProgressDialog != null)
            myProgressDialog.dismiss();
        if (mySATCM != null&&result!=null)
        {
            mySATCM.processJsonObject(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected JSONObject doInBackground(Object... params)
    {
        String SOAP_NAMESPACE = params[0].toString();
        String SOAP_ACTION = params[1].toString();
        String SOAP_METHODNAME = params[2].toString();
        String SOAP_URL = params[3].toString();

        Map<String, Object> propertyMap = (Map<String, Object>) params[4];

        String response = null;
        JSONObject rsJson = null;
        try
        {
            SoapObject request = new SoapObject(SOAP_NAMESPACE,SOAP_METHODNAME);

            Element[] header = new Element[1];
            header[0] = new Element().createElement(SOAP_NAMESPACE, "Identify");

            Element username = new Element().createElement(SOAP_NAMESPACE,
                    "UserName");
            username.addChild(Node.TEXT, "weixin");
            header[0].addChild(Node.ELEMENT, username);
            Element pass = new Element().createElement(SOAP_NAMESPACE,
                    "PassWord");
            pass.addChild(Node.TEXT,"myw#weixin1001");
            header[0].addChild(Node.ELEMENT, pass);


            if(propertyMap!=null && !propertyMap.isEmpty())
            {
                for(String s:propertyMap.keySet())
                {
                    request.addProperty(s,propertyMap.get(s));
                }
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.headerOut=header;
            envelope.bodyOut=request;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL,120000);
                httpTransport.call(SOAP_ACTION,envelope);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            response  =result.toString();
            rsJson = new JSONObject(response);
        }
        catch (IllegalArgumentException ex2){
           ToastUtils.showMessage(myContext,"非法字符");
        }

        catch (Exception ex)
        {

            ex.printStackTrace();
        }

        return rsJson;
    }

    public static String convertStreamToString(java.io.InputStream is)
            throws java.io.UnsupportedEncodingException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                "utf-8"));

        StringBuilder sb = new StringBuilder();
        String line = null;

        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);// line+"/n"
            }
            is.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String convertInputStreamReaderToString(InputStreamReader is)
            throws java.io.UnsupportedEncodingException
    {
        BufferedReader reader = new BufferedReader(is);

        StringBuilder sb = new StringBuilder();
        String line = null;

        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);// line+"/n"
            }
            is.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
