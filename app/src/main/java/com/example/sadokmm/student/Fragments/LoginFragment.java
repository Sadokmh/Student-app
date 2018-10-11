package com.example.sadokmm.student.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Activities.MainActivity;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private EditText pass,email;
    private TextView connexion,verifMdp,verifEmail;
    private AQuery aq;

    Bitmap img;

    public static User admin;


    public LoginFragment() {



    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);


        connexion=(TextView) view.findViewById(R.id.connexion);
        verifEmail=(TextView) view.findViewById(R.id.verifEmail);
        verifMdp=(TextView) view.findViewById(R.id.verifMdp);
        email=(EditText) view.findViewById(R.id.emailLogin);
        pass=(EditText) view.findViewById(R.id.passLogin);

        aq=new AQuery(getActivity().getApplicationContext());

        //try bitmap load aquery
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
         int w = 700, h = 480;

        img = Bitmap.createBitmap(w, h, conf);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                verifEmail.setText("");
                verifMdp.setText("");
                if (email.getText().toString().isEmpty() && pass.getText().toString().isEmpty()) {
                    verifEmail.setText("Veuiller saisir vos informations ");
                } else if (email.getText().toString().isEmpty()) {
                    verifEmail.setText("Veuillez saisir votre email ");
                } else if (email.getText().toString().isEmpty()) {
                    verifMdp.setText("Veuillez saisir votre mdp");
                } else {


                        chercherUserByEmail(email.getText().toString().toLowerCase());


                }

            }

        });




        return view;


    }


    public void chercherUserByEmail(String email) {


        String url= "http://192.168.2.127:8080/student/getuser/"+email;

        aq.ajax(url, JSONObject.class,this,"emailCallback");

    }


    public void emailCallback(String url , JSONObject jsonObject , AjaxStatus status){

        if (jsonObject == null ) {
            verifEmail.setText("Adresse non trouvée !");
        }

        else {

            try {
                String pas = jsonObject.getString("pass");
                if (!(pass.getText().toString().equals(pas))) {
                    verifMdp.setText("Mot de passe incorrecte");
                } else {
                    //Shared Preferences to save connectUser
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("userFile", Context.MODE_PRIVATE).edit();
                    editor.putString("connectUser", email.getText().toString());
                    editor.commit();

                    String id=jsonObject.getString("_id");
                    String nom=jsonObject.getString("nom");
                    String prenom=jsonObject.getString("prenom");
                    String filiere=jsonObject.getString("filiere");
                    String pass=jsonObject.getString("pass");



                    aq.ajax("http://192.168.2.127:8080/"+jsonObject.getString("img"),Bitmap.class,0,new AjaxCallback<Bitmap>(){
                        @Override
                        public void callback(String url, Bitmap object, AjaxStatus status) {
                            if(object != null)
                            {
                                img = object;
                                Toast.makeText(getActivity().getApplicationContext(),"image crée",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getActivity().getApplicationContext(),"pas d'image",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    int niveau = Integer.parseInt(jsonObject.getString("niveau"));
                    int groupe = Integer.parseInt(jsonObject.getString("groupe"));


                    Intent intent = new Intent(getContext(), MainActivity.class);
                    admin=new User(id,nom,prenom,email.getText().toString(),pass,img,filiere,groupe,niveau);
                    startActivity(intent);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }

    }

}
