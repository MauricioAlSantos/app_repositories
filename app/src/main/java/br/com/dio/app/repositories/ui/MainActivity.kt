package br.com.dio.app.repositories.ui

import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import br.com.dio.app.repositories.R
import br.com.dio.app.repositories.core.createDialog
import br.com.dio.app.repositories.core.createLoadingIndicator
import br.com.dio.app.repositories.core.createProgressDialog
import br.com.dio.app.repositories.core.hideSoftKeyboard
import br.com.dio.app.repositories.data.model.User
import br.com.dio.app.repositories.databinding.ActivityMainBinding
import br.com.dio.app.repositories.presentation.MainViewModel
import br.com.dio.app.repositories.presentation.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,SearchView.OnSuggestionListener {
    private val dialog by lazy {createProgressDialog()}

    private val viewModel by viewModel<MainViewModel>()

    private val usersViewModel  by viewModel<UsersViewModel>()

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val adapter by lazy {RepoListAdapter()}
    private lateinit var searchView:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.rvRepos.adapter=adapter
        viewModel.repos.observe(this){
            when(it){
                MainViewModel.State.Loading -> dialog.show()
                is MainViewModel.State.Error -> {
                    createDialog{
                        setMessage(it.error.message)
                    }.show()
                    dialog.dismiss()
                }
                is MainViewModel.State.Success -> {
                    dialog.dismiss()
                    if(it.list.isEmpty()){
                        Toast.makeText(this, "Sem resultados", Toast.LENGTH_SHORT).show()
                    }
                    adapter.submitList(it.list)
                }
            }
        }
        usersViewModel.users.observe(this){
            when(it){
                is UsersViewModel.State.Loading->{
                }
                is UsersViewModel.State.Error-> {println(it)
                }
                is  UsersViewModel.State.Success -> {
                    val cursor =getCursorAdapterFromList(it.list)
                    val from = arrayOf("login","avatar_url")
                    val to = intArrayOf(R.id.tv_user_name,R.id.iv_user)
                    if(searchView.suggestionsAdapter==null) {
                        val usersCursorAdapter= SimpleCursorAdapter(
                            this,
                            R.layout.item_user,
                            cursor,
                            from,
                            to,
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                        )
                        usersCursorAdapter.viewBinder=UserViewBinder()
                        searchView.suggestionsAdapter = usersCursorAdapter
                    }else{
                        searchView.suggestionsAdapter.changeCursor(cursor)
                    }

                }
            }
        }

    }
    private fun getCursorAdapterFromList(users: List<User>):Cursor{
        val cursor = MatrixCursor(arrayOf("_id", "login", "avatar_url"))
        for (user in users) {
            cursor.newRow()
                .add("_id", user.id)
                .add("login", user.login)
                .add("avatar_url", user.avatarURL)
        }
        return  cursor
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnSuggestionListener(this)
        searchView.showDividers=SearchView.SHOW_DIVIDER_MIDDLE
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            viewModel.getRepoList(query)
            hideKeyboard()
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            usersViewModel.getUsersList(newText)
        }

        return true
    }

    override fun onSuggestionSelect(position: Int): Boolean {
       TODO()
    }

    override fun onSuggestionClick(position: Int): Boolean {
        hideKeyboard()
        val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
        val query = cursor.getString(cursor.getColumnIndex("login"))

        if (query != null) {
            viewModel.getRepoList(query)
            hideKeyboard()
            return true
        }
        return false
    }
    private fun hideKeyboard(){
        binding.root.hideSoftKeyboard()
    }

}