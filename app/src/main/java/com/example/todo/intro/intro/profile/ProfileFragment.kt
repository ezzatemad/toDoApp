package com.example.todo.intro.intro.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.R
import com.example.todo.databinding.FragmentProfileBinding
import com.example.todo.intro.intro.database.getUserFromFirestore
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class profileFragment : Fragment() {

    lateinit var dataBinding: FragmentProfileBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = FragmentProfileBinding.inflate(inflater,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        auth = Firebase.auth
        getUserFromFireStoreDB(auth.currentUser?.uid!!)
    }

    fun getUserFromFireStoreDB(uId: String){
        getUserFromFirestore(uId,
           addOnSuccessListener = {

               dataBinding.tvProfileName.text = auth.currentUser?.displayName ?:"Ezzat"
        }, addOnFailureListener = {
                Log.e("TAG", "getUserFromFireStoreDB:${it.localizedMessage} ", )
        })
    }
}