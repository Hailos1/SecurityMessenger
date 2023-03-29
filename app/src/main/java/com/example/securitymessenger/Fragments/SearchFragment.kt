package com.example.securitymessenger.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securitymessenger.Adapters.UserAdapter
import com.example.securitymessenger.Model.User
import com.example.securitymessenger.R
import com.example.securitymessenger.RestClientApi.SearchApi

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var userAdapter: UserAdapter? = null
    private var mUsers: List<User>? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        mUsers = ArrayList()
        recyclerView = view.findViewById(R.id.search_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        var searchEdit = view.findViewById<EditText>(R.id.search_edit)
        searchEdit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recieveUsers(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        return view
    }

    private fun recieveUsers(userid: String) {
        var api = SearchApi()
        api.findUsers(userid, this)
    }

    public fun writeUsers(list: List<User>){
        this.mUsers = list
        userAdapter = UserAdapter(requireContext(), mUsers!!, false)
        recyclerView!!.adapter = userAdapter
    }
}