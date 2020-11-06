package br.com.justworks.prestador.ServicoAki.Menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.Glide;
import br.com.justworks.prestador.ServicoAki.Enum.userEnum;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Activity.LoginActivity;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeuPerfilFragment extends Fragment {

    private TextView tv_logout, tv_name, tv_email, tv_authenticated, tv_phone;
    private CircleImageView imageView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meu_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarComponentes(view);

        carregarInfoUsuarios();

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseService.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseService.getFirebaseAuth();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private void carregarInfoUsuarios() {
        db.collection("users").document(FirebaseService.getFirebaseAuth().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString(userEnum.USER_NAME.getDisplayName());
                    String imageUrl = documentSnapshot.getString(userEnum.USER_IMAGE_URL.getDisplayName());
                    String email = documentSnapshot.getString(userEnum.USER_EMAIL.getDisplayName());
                    String phoneNumber = documentSnapshot.getString(userEnum.USER_PHONE.getDisplayName());
                    Boolean isAuthenticated = documentSnapshot.getBoolean(userEnum.USER_IS_AUTHENTICATED.getDisplayName());

                    tv_name.setText(name);
                    tv_email.setText(email);
                    tv_phone.setText(phoneNumber);
                    Glide.with(getContext()).load(imageUrl).into(imageView);
                    if(isAuthenticated){
                        tv_authenticated.setText("Usuário autenticado");
                    }else{
                        tv_authenticated.setText("Usuário não autenticado");
                        tv_authenticated.setTextColor(Color.RED);
                    }
                }
            }
        });
    }

    private void inicializarComponentes(View view) {
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);
        tv_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_email = (TextView) view.findViewById(R.id.tv_user_email);
        tv_authenticated = (TextView) view.findViewById(R.id.tv_user_authenticated);
        tv_phone = (TextView) view.findViewById(R.id.tv_user_phone);
        imageView = (CircleImageView) view.findViewById(R.id.profile_image);
    }


}