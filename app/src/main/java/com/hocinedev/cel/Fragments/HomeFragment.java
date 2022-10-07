package com.hocinedev.cel.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hocinedev.cel.Adapters.HomeAdapter;
import com.hocinedev.cel.Constants;
import com.hocinedev.cel.InformationActivity;
import com.hocinedev.cel.UserObject;
import com.hocinedev.cel.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private final static String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    private HomeAdapter homeAdapter;
    private UserObject userObject;
    private RecyclerView recyclerView;

    private ArrayList<Map<String, Object>> arrayListMapBasket;
    private ArrayList<DocumentSnapshot> arrayListUsers;


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unit();
        setUpRecyclerView();
    }


    private void unit() {
        recyclerView = binding.recyclerView;
    }


    private void setUpRecyclerView() {

        FirebaseFirestore.getInstance().collection(Constants.USERS)
                .orderBy(Constants.SIGNUP_DATE)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                arrayListUsers = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    arrayListUsers.add(document);
                }
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                homeAdapter = new HomeAdapter(getContext(), arrayListUsers);
                recyclerView.setAdapter(homeAdapter);

                homeAdapter.setOnItemClickListener(position -> {
                    Intent intent = new Intent(getContext(), InformationActivity.class);
                    intent.putExtra(Constants.ID,arrayListUsers.get(position).getId());
                    startActivity(intent);
                    //Toast.makeText(getContext(), "الطالب: "+arrayListUsers.get(position).getString(Constants.FIRST_LAST_NAME), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

}