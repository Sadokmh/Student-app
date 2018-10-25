package com.example.sadokmm.student.Fragments;

import android.app.Dialog;
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
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.example.sadokmm.student.Activities.MainActivity;
import com.example.sadokmm.student.Objects.Emploi;
import com.example.sadokmm.student.Objects.Jour;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import static com.example.sadokmm.student.Activities.firstActivity.MY_SP_FILE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.sadokmm.student.Activities.firstActivity.EMPLOI_FILE;
import static com.example.sadokmm.student.Activities.firstActivity.SESSION;
import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.monEmploi;
import static com.example.sadokmm.student.Activities.firstActivity.myActivity;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class LoginFragment extends Fragment {

    private EditText pass,emailText;
    private TextView connexion,verifMdp,verifEmail,testPass;
    private AQuery aq;

    private String nom,id,prenom,mdp,imgUrl,filiere;
    int groupe,niveau;


    Dialog dialog;

    Bitmap img;


    ProgressDialog prgDialog;
    RequestQueue requestQueue ;

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
        emailText=(EditText) view.findViewById(R.id.emailLogin);
        pass=(EditText) view.findViewById(R.id.passLogin);
        imm=(ImageView)view.findViewById(R.id.imm);
        testPass=(TextView)view.findViewById(R.id.passTest);

        testPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin = new User("0","Mhiri","Sadok Mourad","Sadokmhiri@gmail.com","1223","lfig",4,3);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        aq=new AQuery(getActivity().getApplicationContext());

        requestQueue= Volley.newRequestQueue(getContext());


        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                verifEmail.setText("");
                verifMdp.setText("");
                if (emailText.getText().toString().isEmpty() && pass.getText().toString().isEmpty()) {
                    verifEmail.setText("Veuiller saisir vos informations ");
                } else if (emailText.getText().toString().isEmpty()) {
                    verifEmail.setText("Veuillez saisir votre email ");
                } else if (emailText.getText().toString().isEmpty()) {
                    verifMdp.setText("Veuillez saisir votre mdp");
                } else {


                        chercherUserByEmail(emailText.getText().toString().toLowerCase(),pass.getText().toString());


                }

            }

        });




        return view;


    }


    public void chercherUserByEmail(final String email , final String mdp) {

         dialog = new Dialog(getContext());
        dialog.setTitle("Connexion en cours");
        dialog.setCancelable(true);
        dialog.show();



        String url= publicUrl + "student/login/"+email+"/"+mdp;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {


                    id = "ee";
                    nom = jsonObject.getString("nom");
                    prenom = jsonObject.getString("prenom");
                    filiere = jsonObject.getString("filiere");
                    imgUrl = publicUrl + jsonObject.getString("img");
                    niveau = Integer.parseInt(jsonObject.getString("niveau"));
                    groupe = Integer.parseInt(jsonObject.getString("groupe"));

                    //créer l'admin et ouvrir l'application
                    admin = new User(id, nom, prenom, email, imgUrl, filiere, groupe, niveau);


                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(SESSION, MODE_PRIVATE).edit();
                    editor.putBoolean("statut", true);
                    editor.putString("email", admin.getEmail());
                    editor.putString("prenom", admin.getPrenom());
                    editor.putString("nom", admin.getNom());
                    editor.putString("filiere", admin.getFiliere());
                    editor.putString("img", admin.getImg());
                    editor.putInt("groupe", admin.getGroupe());
                    editor.putInt("niveau", admin.getNiveau());
                    editor.commit();

                    chargerMonEmploi();

                    dialog.dismiss();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("type","login");
                    emailText.getText().clear();
                    pass.getText().clear();

                    startActivity(intent);
                }
                catch (JSONException e) {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();


            }
        });

        requestQueue.add(jsonObjectRequest);


        //aq.ajax(url, JSONObject.class,this,"emailCallback");

    }




    public void chargerMonEmploi() {

        String filiere = admin.getFiliere();
        int niveau = admin.getNiveau();
        int groupe = admin.getGroupe();

        String url = publicUrl + "student/getemploi/"+filiere+"/"+niveau+"/"+groupe;
        //String url = publicUrl + "student/getemploi/"+filiere+"/"+niveau+"/"+groupe;
        Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Connexion en cours ...");
        prgDialog.setIndeterminate(false);
        //prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgDialog.setCancelable(false);
        prgDialog.show();


//        Snackbar.make(getCurrentFocus(),url,Snackbar.LENGTH_LONG).show();

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject myObject) {


                Toast.makeText(getContext(), "not null", Toast.LENGTH_LONG).show();

                try {

                    //JSONArray jsonArray = jsonObject.getJSONArray("data");
                    //JSONObject myObject = jsonArray.getJSONObject(0);


                    String id = myObject.getString("_id");
                    String maFiliere = myObject.getString("filiere");
                    int niveau = Integer.parseInt(myObject.getString("niveau"));
                    int groupe = Integer.parseInt(myObject.getString("groupe"));

                    monEmploi = new Emploi(id, maFiliere, niveau, groupe);

                    // Toast.makeText(getApplicationContext(), "sna3t groupe", Toast.LENGTH_LONG).show();

                    JSONArray joursArray = myObject.getJSONArray("jours");

                    //Toast.makeText(this, "sna3t groupejour", Toast.LENGTH_LONG).show();

                    String nomJour;

                    /*prgDialog = new ProgressDialog(getApplicationContext());
                    prgDialog.setMessage("Connexion en cours ...");
                    prgDialog.setIndeterminate(false);
                    //prgDialog.setMax(100);
                    prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    prgDialog.setCancelable(false);
                    prgDialog.show();*/

                    JSONArray seanceArray;
                    for (int i = 0; i < joursArray.length(); i++) {
                        nomJour = joursArray.getJSONObject(i).getString("nom");
                        Jour jj = new Jour(nomJour);
                        seanceArray = joursArray.getJSONObject(i).getJSONArray("seances");

                        for (int j = 0; j < seanceArray.length(); j++) {
                            JSONObject seance = seanceArray.getJSONObject(j);
                            String matiere = seance.getString("mat");
                            String enseignant = seance.getString("enseignant");
                            String salle = seance.getString("salle");
                            int numSeance = Integer.parseInt(seance.getString("numseance"));
                            String type = seance.getString("type");
                            String pqn= seance.getString("pq");
                            Boolean pq;
                            if (pqn.equals("false"))
                                pq = false ;
                            else pq=true;

                        /*if (seance.getString("parQuinzaine").equals("false"))
                            pq = false;
                        else
                            pq = true;*/
                            if (!(matiere.equals(""))) {
                                Seance s = new Seance(matiere, enseignant, salle, type, numSeance, pq);
                                jj.getListSeance().add(s);
                            };

                        }
                        monEmploi.getJours().add(jj);

                    }

                    //Enregistrer une copie local de l'emploi sur le téléphone
                    Gson gson = new Gson();
                    String monEmploiEnJson = gson.toJson(monEmploi);

                    SharedPreferences.Editor editor=getContext().getSharedPreferences(EMPLOI_FILE,MODE_PRIVATE).edit();
                    editor.putString("emploi",monEmploiEnJson);
                    editor.commit();

                    prgDialog.dismiss();



                    Toast.makeText(getContext(),monEmploiEnJson,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();

            }
        });

        requestQueue.add(jsonObjectRequest);

    }








}
