package com.example.e_campusguide;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;


public class mainmenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String input;
    private DrawerLayout mdrawerlayout;
    private Button b;
    private TextView email;
    private TextView name;
    private String id;
    private String username;
    private NavigationView nv;

    private ListView lv;
    private ArrayList<String> conversation;
    private EditText userinput;
    private TextView output;

    private CustomAdapter ca;


    private FloatingActionButton fab;
    private int flag;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private TextToSpeech tts;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);




        mdrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        b = (Button) findViewById(R.id.b);


        nv = (NavigationView) findViewById(R.id.nv1);
        nv.setNavigationItemSelectedListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        lv = (ListView) findViewById(R.id.list);


        conversation = new ArrayList<String>();
        userinput = (EditText) findViewById(R.id.userInput);
        ca = new CustomAdapter(this, conversation);
        lv.setAdapter(ca);



        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.ENGLISH);
                tts.setPitch(0.5f);
                tts.setSpeechRate(1.0f);

            }
        });



        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                  String s= lv.getItemAtPosition(position).toString();
                tts.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                return false;
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();



        fab.setImageResource(R.drawable.ic_mic_none_black_24dp);
        flag = 1;

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                send(view);


            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        conversation.clear();
    }

    public void btc(View view) {
        mdrawerlayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mdrawerlayout.closeDrawer(Gravity.LEFT, false);
        userinput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    fab.setImageResource(R.drawable.ic_send_black);          //Change image on floating btn
                    flag = 2;
                } else {
                    fab.setImageResource(R.drawable.ic_mic_none_black_24dp);
                    flag = 1;
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you Want To Exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        finish();

                        System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.chat) {

            mdrawerlayout.closeDrawers();
        }

        if (id == R.id.create) {
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        }


        if (id == R.id.signin) {

            Intent i = new Intent(this, SignIn.class);
            startActivity(i);

        }


        if (id == R.id.exit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you Want To Exit")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            finish();
                            System.exit(0);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
        }


        return false;
    }

    public void send(View view) {

        input = userinput.getText().toString();

        if (flag == 1) {
            speech();

        } else if (flag == 2) {
            RetrieveFeedTask task=new RetrieveFeedTask();
            task.execute(userinput.getText().toString());
            if (!TextUtils.isEmpty(input)) {
                userinput.setText("");
                conversation.add(input);
                ca.notifyDataSetChanged();
                //lv.setAdapter(ca);

            }
        }





    }


    private void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support speech input",
                    Toast.LENGTH_LONG).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    userinput.setText(result.get(0));


                }
                break;
            }

        }


    }


    public String GetText(String query) throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/query?v=20150910");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "Bearer b42ff61858e04b6da66164af7ab54ba8");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            JSONArray queryArray = new JSONArray();
            queryArray.put(query);
            jsonParam.put("query", queryArray);
//            jsonParam.put("name", "order a medium pizza");
            jsonParam.put("lang", "en");
            jsonParam.put("sessionId", "1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("karma", "after conversion is " + jsonParam.toString());
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d("karma", "json is " + jsonParam);

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();



            JSONObject object1 = new JSONObject(text);
            JSONObject object = object1.getJSONObject("result");
            JSONObject fulfillment = null;
            String speech = null;
//            if (object.has("fulfillment")) {
            fulfillment = object.getJSONObject("fulfillment");
//                if (fulfillment.has("speech")) {
            speech = fulfillment.optString("speech");
//                }
//            }


            Log.d("karma ", "response is " + text);
            return speech;

        } catch (Exception ex) {
            Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

        return null;
    }














    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            String s = null;
            try {

                s = GetText(voids[0]);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            conversation.add(s);
            ca.notifyDataSetChanged();



        }
    }




















}
//------>Adapter Class<------

class CustomAdapter extends BaseAdapter {


    Context c;
    ArrayList<String> data;
    //int count=1;

    boolean isMe=true;

    public CustomAdapter(Context context, ArrayList<String> conversation) {
        c = context;
        data = conversation;

    }


    @Override
    public int getCount() {
       return data.size();

    }


    @Override
    public Object getItem(int position) {
       return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        String chatMessage = (String) getItem(position);





        if (convertView == null && isMe  ) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.me_bubble, null);
            TextView tv=(TextView) convertView.findViewById(R.id.textMessage);
            tv.setText(chatMessage);
            isMe=false;
            //count=0;

        }

        else if(convertView == null && !isMe)
        {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bot_bubble, null);
            TextView tv=(TextView) convertView.findViewById(R.id.botMessage);
            tv.setText(chatMessage);
            isMe=true;
            //count=1;
        }





    return convertView;
    }

}





