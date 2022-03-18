package myapp.com.jsonnt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import myapp.com.model.Contact;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<Contact> dscontact;
    ArrayAdapter<Contact> adaptercontact;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControl();
        addEvent();
    }

    private void addControl() {
        lv=findViewById(R.id.lvContact);
        dscontact=new ArrayList<>();
        adaptercontact=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,dscontact);
        lv.setAdapter(adaptercontact);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading!!");
        progressDialog.setMessage("Your infomation will be showed in a few second");

        ContactTask contactTask=new ContactTask();
        contactTask.execute();
    }

    private void addEvent() {
    }

    class ContactTask extends AsyncTask<Void,Void,ArrayList<Contact>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adaptercontact.clear();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            adaptercontact.addAll(contacts);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            ArrayList<Contact> ds=new ArrayList<>();
            try {
                URL u=new URL("https://www.w3schools.com/js/customers_mysql.php");
                HttpURLConnection connection=(HttpURLConnection) u.openConnection();
                InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

                StringBuilder builder=new StringBuilder();
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    builder.append(line);
                    line=bufferedReader.readLine();
                }
                JSONArray jsonArray=new JSONArray(builder.toString());
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Contact contact=new Contact();
                    if(jsonObject.has("Name"))
                        contact.setName(jsonObject.getString("Name"));
                    if(jsonObject.has("City"))
                       contact.setCity(jsonObject.getString("City"));
                    if(jsonObject.has("Country"))
                       contact.setCountry(jsonObject.getString("Country"));
                    dscontact.add(contact);
                }
             }
            catch (Exception e)
            {
                Log.e("Loi",e.toString());
            }
            return ds;
        }
    }
}