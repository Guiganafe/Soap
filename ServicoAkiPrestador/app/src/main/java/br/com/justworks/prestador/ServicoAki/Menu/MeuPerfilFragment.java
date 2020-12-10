package br.com.justworks.prestador.ServicoAki.Menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import br.com.justworks.prestador.ServicoAki.Activity.EditarPerfil;
import br.com.justworks.prestador.ServicoAki.Activity.MeusServicos;
import br.com.justworks.prestador.ServicoAki.Base.UserBase;
import br.com.justworks.prestador.ServicoAki.Enum.userEnum;
import br.com.justworks.prestador.ServicoAki.Firebase.FirebaseService;
import br.com.justworks.prestador.ServicoAki.Activity.LoginActivity;
import br.com.justworks.prestador.ServicoAki.Model.User;
import br.com.justworks.prestador.ServicoAki.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeuPerfilFragment extends Fragment {

    private TextView tv_logout, tv_name, tv_email, tv_authenticated, tv_phone, tv_meusServicos, tv_meus_dados;
    private CircleImageView imageView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meu_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseService.getFirebaseAuth();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }

        inicializarComponentes(view);

        carregarInfoUsuarios();

        onClickController();
    }

    private void carregarDadosUsuario() {
        tv_name.setText(user.getName());
        tv_email.setText(user.getEmail());
        tv_phone.setText(user.getPhoneNumber());
        Glide.with(getContext()).load(user.getImageUrl()).into(imageView);
        if(user.getIsAuthenticated()){
            tv_authenticated.setText("Usuário autenticado");
        }else{
            tv_authenticated.setText("Usuário não autenticado");
            tv_authenticated.setTextColor(Color.RED);
        }
    }

    private void onClickController() {
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseService.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        tv_meusServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent meusServicos = new Intent(requireActivity(), MeusServicos.class);
                startActivity(meusServicos);
            }
        });

        tv_meus_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editarPerfil = new Intent(requireActivity(), EditarPerfil.class);
                startActivity(editarPerfil);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void carregarInfoUsuarios() {
        user = UserBase.getInstance().getUser();
        carregarDadosUsuario();
    }

    private void inicializarComponentes(View view) {
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);
        tv_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_email = (TextView) view.findViewById(R.id.tv_user_email);
        tv_authenticated = (TextView) view.findViewById(R.id.tv_user_authenticated);
        tv_phone = (TextView) view.findViewById(R.id.tv_user_phone);
        imageView = (CircleImageView) view.findViewById(R.id.profile_image);
        tv_meusServicos = (TextView) view.findViewById(R.id.tv_meusServicos);
        tv_meus_dados = (TextView) view.findViewById(R.id.tv_dados_pessoais);
    }


}