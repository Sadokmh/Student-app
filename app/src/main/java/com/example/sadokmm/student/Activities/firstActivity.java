package com.example.sadokmm.student.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Adapters.PageAdapterFirst;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class firstActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapterFirst pageAdapter;

    public static Activity myActivity;

    public static List<User> listUser = new ArrayList<>();

    public static User admin ;

    String nom,prenom,imgUrl,email,filiere,mdp,id;
    int niveau,groupe;

    ProgressDialog prgDialog;

    AQuery aq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        aq=new AQuery(this);


        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String verif = sp.getString("statut", "false");

        if (verif.equals("false")) {
            Toast.makeText(this,"Pas d'utilisateur connecté",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Bienvenue",Toast.LENGTH_LONG).show();


            if (!isNetworkAvailable()) {
                admin = new User("0", sp.getString("nom", "nom"), sp.getString("prenom", "prenom"), sp.getString("email", "email"), sp.getString("pass", "pass"), null, sp.getString("filiere", "filiere"), sp.getInt("niveau", 0), sp.getInt("groupe", 0));
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            else {
                chercherUserByEmail(sp.getString("email","email"));
            }
        }


        tabLayout=(TabLayout)findViewById(R.id.tabLayoutFirst);
        viewPager=(ViewPager) findViewById(R.id.viewPagerFirst);

        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));

        pageAdapter=new PageAdapterFirst(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

       // myActivity=this;





    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }


    public void chercherUserByEmail(String email) {


        String url= "http://192.168.2.127:8080/student/getuser/"+email;

        aq.ajax(url, JSONObject.class,this,"emailCallback");

    }

    public void emailCallback(String url , JSONObject jsonObject , AjaxStatus status){




            try {
                String pas = jsonObject.getString("pass");


                    //Shared Preferences to save connectUser
                    /*SharedPreferences.Editor editor = getActivity().getSharedPreferences("userFile", Context.MODE_PRIVATE).edit();
                    editor.putString("connectUser", email.getText().toString());
                    editor.commit();*/


                    email=jsonObject.getString("email");
                     id=jsonObject.getString("_id");
                 nom=jsonObject.getString("nom");
                 prenom=jsonObject.getString("prenom");
                 filiere=jsonObject.getString("filiere");
                 mdp=jsonObject.getString("pass");
                 imgUrl="http://192.168.2.127:8080/"+jsonObject.getString("img");


                     niveau = Integer.parseInt(jsonObject.getString("niveau"));
                     groupe = Integer.parseInt(jsonObject.getString("groupe"));

                    downloadImage(imgUrl);
                    //imm.setImageBitmap(img);




                    //Intent intent = new Intent(getContext(), MainActivity.class);

                    //admin=new User(id,nom,prenom,email.getText().toString(),mdp,img,filiere,groupe,niveau);

                    //startActivity(intent);
                }

            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }




    public void downloadImage(String imageUrl) {

        final Bitmap[] im = new Bitmap[1];

        try {


            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage("Chargement en cours ...");
            prgDialog.setIndeterminate(false);
            prgDialog.setMax(100);
            prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            prgDialog.setCancelable(false);
            prgDialog.show();


            aq.ajax(imageUrl, Bitmap.class, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap object, AjaxStatus status) {

                    if (object != null) {
                        Toast.makeText(getApplicationContext(), "image téléchargé", Toast.LENGTH_LONG).show();
                        im[0] = object;
                        prgDialog.dismiss();

                        //créer l'admin et ouvrir l'application
                        admin=new User(id,nom,prenom,email,mdp,im[0],filiere,groupe,niveau);





                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                    else Toast.makeText(getApplicationContext(), "image non téléchargé", Toast.LENGTH_LONG).show();
                }
            }).progress(prgDialog).show(prgDialog);

        }

        catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
