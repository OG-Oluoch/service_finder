//package com.whebtos.e_chiro.ui.home;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.whebtos.e_chiro.MainActivity;
//import com.whebtos.e_chiro.R;
//import com.whebtos.e_chiro.databinding.FragmentHomeBinding;
//import com.whebtos.e_chiro.ui.services.ElectriciansFragment;
//import com.whebtos.e_chiro.ui.services.MedicalFragment;
//import com.whebtos.e_chiro.ui.services.PlumbingFragment;
//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    private FragmentManager fragmentManager;
//
//
//
////    private Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        fragmentManager = getActivity().getSupportFragmentManager();
//
////        toolbar.inflateMenu(R.menu.main);
//
//        final LinearLayout medical_layout = binding.layoutMedical;
//        final LinearLayout plumber_layout = binding.layoutPlumbing;
//        final LinearLayout electrician_layout = binding.layoutElectrician;
//
//        medical_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openMedical();
//            }
//        });
//
//        plumber_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openPlumbers();
//            }
//        });
//
//        electrician_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openElectricians();
//            }
//        });
//
//
//        return root;
//    }
//
//    public void openMedical()
//    {
//
//
//        MedicalFragment medicalFragment = new MedicalFragment();
//        fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, medicalFragment, "Medical Practitioners")
//                .addToBackStack("Medical Practitioners")
//                .commit();
////        toolbar.setTitle("Medical Practitioners");
//        getActivity().setTitle("Medical Practitioners");
//    }
//
//    public void openPlumbers()
//    {
//        PlumbingFragment plumbingFragment = new PlumbingFragment();
//        fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container,plumbingFragment,"Plumbers")
//                .addToBackStack("Plumbers")
//                .commit();
//    }
//
//    public void openElectricians()
//    {
//        ElectriciansFragment electriciansFragment = new ElectriciansFragment();
//        fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container,electriciansFragment,"Plumbers")
//                .addToBackStack("Plumbers")
//                .commit();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}