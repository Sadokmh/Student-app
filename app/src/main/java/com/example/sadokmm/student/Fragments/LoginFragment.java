package com.example.sadokmm.student.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.example.sadokmm.student.Activities.MainActivity;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.myActivity;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class LoginFragment extends Fragment {

    private EditText pass,email;
    private TextView connexion,verifMdp,verifEmail,testPass;
    private AQuery aq;

    private String nom,id,prenom,mdp,imgUrl,filiere;
    int groupe,niveau;

    Bitmap img;


    ProgressDialog prgDialog;

    ImageView imm;


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
        imm=(ImageView)view.findViewById(R.id.imm);
        testPass=(TextView)view.findViewById(R.id.passTest);

        testPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin = new User("0","Mhiri","Sadok Mourad","Sadokmhiri@gmail.com","1223",getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.sadok),500),"lfig",4,3);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        aq=new AQuery(getActivity().getApplicationContext());


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


        String url= publicUrl + "getuser/"+email;

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
                    /*SharedPreferences.Editor editor = getActivity().getSharedPreferences("userFile", Context.MODE_PRIVATE).edit();
                    editor.putString("connectUser", email.getText().toString());
                    editor.commit();*/



                     id=jsonObject.getString("_id");
                     nom=jsonObject.getString("nom");
                     prenom=jsonObject.getString("prenom");
                     filiere=jsonObject.getString("filiere");
                     mdp=jsonObject.getString("pass");
                     imgUrl="http://192.168.43.196:8080/"+jsonObject.getString("img");


                     niveau = Integer.parseInt(jsonObject.getString("niveau"));
                     groupe = Integer.parseInt(jsonObject.getString("groupe"));

                     downloadImage(imgUrl);
                    //imm.setImageBitmap(img);




                    //Intent intent = new Intent(getContext(), MainActivity.class);

                    //admin=new User(id,nom,prenom,email.getText().toString(),mdp,img,filiere,groupe,niveau);

                    //startActivity(intent);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }

    }



    public void downloadImage(String imageUrl) {

        final Bitmap[] im = new Bitmap[1];

        try {

            prgDialog = new ProgressDialog(getActivity());
            prgDialog.setMessage("Connexion en cours ...");
            prgDialog.setIndeterminate(false);
            //prgDialog.setMax(100);
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.setCancelable(false);
            prgDialog.show();


            aq.ajax(imageUrl, Bitmap.class, new AjaxCallback<Bitmap>() {
                @Override
            public void callback(String url, Bitmap object, AjaxStatus status) {

                if (object != null) {
                    Toast.makeText(getActivity(), "image téléchargé", Toast.LENGTH_LONG).show();
                    im[0] = getResizedBitmap(object,500);
                    prgDialog.dismiss();

                    //créer l'admin et ouvrir l'application
                    admin=new User(id,nom,prenom,email.getText().toString(),mdp,im[0],filiere,groupe,niveau);



                    SharedPreferences.Editor editor=getActivity().getSharedPreferences("user",Context.MODE_PRIVATE).edit();
                    editor.putString("statut","true");
                    editor.putString("email",admin.getEmail());
                    editor.putString("prenom",admin.getPrenom());
                    editor.putString("nom" , admin.getNom());
                    editor.putString("filiere" , admin.getFiliere());
                    editor.putInt("groupe",admin.getGroupe());
                    editor.putInt("niveau",admin.getNiveau());
                    editor.commit();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                }
                else Toast.makeText(getActivity(), "image non téléchargé", Toast.LENGTH_LONG).show();
                }
            }).progress(prgDialog).show(prgDialog);

        }

        catch (Exception e) {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }


}
